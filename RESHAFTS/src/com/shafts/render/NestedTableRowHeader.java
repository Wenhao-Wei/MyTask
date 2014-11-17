package com.shafts.render;

import java.awt.*;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;

 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.event.TableModelEvent;
 import javax.swing.event.TableModelListener;


 /**  表格的列头,显示行号并处理选择行的鼠标操作 */
class NestedTableRowHeader extends JPanel 
 implements MouseListener, TableModelListener {

 private TargetTable nestedTable = null;

 public NestedTableRowHeader(TargetTable nestedTable) {
 this.nestedTable = nestedTable;

 //setBackground(Color.white);
 addMouseListener(this);
 nestedTable.getModel().addTableModelListener(this);
 }

 public Dimension getPreferredSize() {
 return new Dimension(16, nestedTable.getHeight());
 }

 public void paintComponent(Graphics g) {
 super.paintComponent(g);

 Rectangle clip = g.getClipBounds();
 Point upperLeft = clip.getLocation();
 Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
 int rMin = nestedTable.rowAtPoint(upperLeft);
 int rMax = nestedTable.rowAtPoint(lowerRight);
 if (rMin == -1) {
 rMin = 0;
 }
 if (rMax == -1) {
 rMax = nestedTable.getRowCount()-1;
 }

 Rectangle rect = nestedTable.getCellRect(rMin, 0, false);
 int rowCount = nestedTable.getRowCount();
 for (int i = rMin; i <= rMax; i++) {
 boolean isAHoldplaceRow = nestedTable.isAHoldPlaceRow(i);
 int rowHeight = nestedTable.getRowHeight(i);

 g.setColor(Color.gray);
 if (isAHoldplaceRow == false) {
 g.drawRect(3, rect.y + rowHeight/2 - 5, 8, 8);
 if (i+1 > rowCount || !nestedTable.isAHoldPlaceRow(i+1)) { //未展开的行
g.setColor(Color.black);
 g.drawLine(5, rect.y + rowHeight/2 - 1, 9, rect.y + rowHeight/2 - 1);
 g.drawLine(7, rect.y + rowHeight/2 - 3, 7, rect.y + rowHeight/2 + 1);
 } else {  //展开的行
g.setColor(Color.black);
 g.drawLine(5, rect.y + rowHeight/2 - 1, 9, rect.y + rowHeight/2 - 1);
 g.setColor(Color.gray);
 g.drawLine(7, rect.y + rowHeight/2 + 4, 7, rect.y + rowHeight-1);
 }
 } else {
 g.setColor(Color.gray);
 g.drawLine(7, rect.y, 7, rect.y + 8);
 g.drawLine(7, rect.y + 8, 15, rect.y + 8);
 }
 rect.y += rowHeight;
 }
 }

 public void mouseClicked(MouseEvent e) {
 }

 public void mousePressed(MouseEvent e) {
 int row = nestedTable.rowAtPoint(e.getPoint());
 if (row != -1 && !nestedTable.isAHoldPlaceRow(row)) {
 int rowCount = nestedTable.getRowCount();
 if (row+1 > rowCount || !nestedTable.isAHoldPlaceRow(row+1)) {  //未展开的行
int r = nestedTable.convertRowIndex(row);
 JComponent comp = nestedTable.model.getSubComponent(r);
 nestedTable.addComponentAfterRow(row, comp);
 } else {
 nestedTable.removeComponentAfterRow(row);
 }
 }
 }

 public void mouseReleased(MouseEvent e) {
 }

 public void mouseEntered(MouseEvent e) {
 }

 public void mouseExited(MouseEvent e) {
 }

 //实现TableModelListener接口
public void tableChanged(TableModelEvent e) {
 invalidate();
 repaint();
 }
 }