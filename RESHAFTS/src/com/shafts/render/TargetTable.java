package com.shafts.render;


 import java.awt.Component;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.Rectangle;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;

 import javax.swing.JComponent;
 import javax.swing.JScrollPane;
 import javax.swing.JTable;
 import javax.swing.JViewport;
 import javax.swing.table.AbstractTableModel;
 import javax.swing.table.TableModel;

 public class TargetTable extends JTable {
 protected NestedTableRowHeader rowHeader = null;

 private int[] holdPlaceRowIndexes = new int[0];

 /** 插入的组件到组件所占据的占位行的行号(Integer型)的映射表 */
private Map<Component, Integer> compRowIndexMap = new HashMap<Component, Integer>();

 protected TargetTableModel model;

 public TargetTable(TargetTableModel model) {
 setModel(new InternalTableModel(model));

 this.model = model;
 this.rowHeader = new NestedTableRowHeader(this);
 }

 @Override
 protected void configureEnclosingScrollPane()
 {
 super.configureEnclosingScrollPane();

 Container p = getParent();
     if (p instanceof JViewport) {
       Container gp = p.getParent();
       if (gp instanceof JScrollPane) {
           JScrollPane scrollPane = (JScrollPane)gp;
           // Make certain we are the viewPort's view and not, for
           // example, the rowHeaderView of the scrollPane -
          // an implementor of fixed columns might do this.
           JViewport viewport = scrollPane.getViewport();
           if (viewport == null || viewport.getView() != this) {
               return;
           }
           scrollPane.setRowHeaderView(rowHeader);
       }
     }
 }

 /** 判断指定的行是不是一个用来占位的行
 * @param row 行号
 * @return 如果是一个占位行返回true
  */
 public boolean isAHoldPlaceRow(int row) {
 return Arrays.binarySearch(holdPlaceRowIndexes, row) >= 0;
 }

 /** 添加一个占位行
 * @param rowIndex 占位行的行号
 * @param rowHeight 占位行的高度 */
public void addHoldPlaceRow(int rowIndex, int rowHeight) {
 int temp[] = new int[holdPlaceRowIndexes.length + 1];
 int insertPos = temp.length - 1;
 boolean f = false;            
 for (int i = 0; i < holdPlaceRowIndexes.length; i++) {
 if (holdPlaceRowIndexes[i] < rowIndex) {
 temp[i] = holdPlaceRowIndexes[i];
 }
 else {
 temp[i+1] = holdPlaceRowIndexes[i] + 1;
 if (f == false) {
 insertPos = i;
 f = true;
 }
 }             
 }
 temp[insertPos] = rowIndex;

 this.holdPlaceRowIndexes = temp;
 ((InternalTableModel)getModel()).fireTableRowsInserted(rowIndex, rowIndex);
 setRowHeight(rowIndex, rowHeight);
 }

 /** 删除指定的占位行
 * @param rowIndex 占位行的行号 */
public void removeHoldPlaceRow(int rowIndex) {
 int temp[] = new int[holdPlaceRowIndexes.length -1];
 for (int i = 0; i < temp.length; i++) {
 if (holdPlaceRowIndexes[i] < rowIndex) {
 temp[i] = holdPlaceRowIndexes[i];
 }
 else {
 temp[i] = holdPlaceRowIndexes[i+1] - 1;
 }
 }

 this.holdPlaceRowIndexes = temp;
 ((InternalTableModel)getModel()).fireTableRowsDeleted(rowIndex, rowIndex);
 }

 /** 得到指定的行号在原始表格模型中对应的行号
 * @param rowIndex
  * @return
  */
 protected int convertRowIndex(int rowIndex) {
 int row = Arrays.binarySearch(holdPlaceRowIndexes, rowIndex);
 if (row == -1)
 return rowIndex;
 return rowIndex + row + 1;
 }

 public void addComponentAfterRow(int row, JComponent comp) {
 int nextRow = row + 1;
 if (isAHoldPlaceRow(nextRow)) {
 throw new IllegalStateException();
 }
 Dimension d = comp.getPreferredSize();
 addHoldPlaceRow(nextRow, d.height);
 compRowIndexMap.put(comp, new Integer(nextRow));

 Rectangle cellRect = getCellRect(nextRow, 0, false);
 int compCount = getComponentCount();
 Rectangle rect = new Rectangle();
 Dimension spacing = getIntercellSpacing();
 for (int i = 0; i < compCount; i++) {
 Component c = getComponent(i);
 Integer rowIndex = (Integer) compRowIndexMap.get(c);
 if (rowIndex != null && rowIndex.intValue() > nextRow) {
 c.getBounds(rect);
 rect.y += cellRect.height + spacing.height;
 c.setBounds(rect); 
 compRowIndexMap.put(c, new Integer(rowIndex.intValue() + 1));
 }
 }
 comp.setBounds(0, cellRect.y, getWidth(), cellRect.height);
 add(comp);
 }

 public void removeComponentAfterRow(int row) {
 int nextRow = row + 1;
 if (!isAHoldPlaceRow(nextRow)) {
 throw new IllegalStateException();
 }

 int rowHeight = getRowHeight(nextRow);
 removeHoldPlaceRow(nextRow);

 Rectangle rect = new Rectangle();
 Iterator<Component> iter = compRowIndexMap.keySet().iterator();
 while (iter.hasNext()) {
 Component comp = iter.next();
 Integer rowIndex = compRowIndexMap.get(comp);
 if (rowIndex.intValue() == nextRow) {
 remove(comp);
 iter.remove();
 }
 else if (rowIndex.intValue() > nextRow) {
 compRowIndexMap.put(comp, new Integer(rowIndex.intValue() - 1));
 comp.getBounds(rect);
 rect.y -= rowHeight;
 comp.setBounds(rect);
 }
 }
 }

 class InternalTableModel extends AbstractTableModel implements TableModel {
 TableModel originModel = null;

 public InternalTableModel(TableModel originModel) {
 this.originModel = originModel;
 }

 public Class<?> getColumnClass(int columnIndex) {
 return originModel.getColumnClass(columnIndex);
 }

 public int getColumnCount() {
 return originModel.getColumnCount();
 }

 public String getColumnName(int columnIndex) {
 return originModel.getColumnName(columnIndex);
 }

 public int getRowCount() {
 return originModel.getRowCount() + holdPlaceRowIndexes.length;
 }

 public Object getValueAt(int rowIndex, int columnIndex) {
 if (isAHoldPlaceRow(rowIndex))
 return null;
 rowIndex = convertRowIndex(rowIndex);
 return originModel.getValueAt(rowIndex, columnIndex);
 }

 public boolean isCellEditable(int rowIndex, int columnIndex) {
 if (isAHoldPlaceRow(rowIndex))
 return false;
 rowIndex = convertRowIndex(rowIndex);
 return originModel.isCellEditable(rowIndex, columnIndex);
 }

 public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
 if (isAHoldPlaceRow(rowIndex))
 return;
 rowIndex = convertRowIndex(rowIndex);
 originModel.setValueAt(aValue, rowIndex, columnIndex);
 }
 }
 }