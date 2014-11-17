package com.shafts.render;

import java.util.Vector;
    import javax.swing.JComponent;
 import javax.swing.table.TableModel;

 public interface TargetTableModel extends TableModel {
 /** 如果第rowIndex行可以展开返回true 
  * @param rowIndex 行序号
 * @return 如果此行可以展开返回true */
 boolean isRowExpandable(int rowIndex);

 /** 得到第rowIndex行对应的子组件 */
public JComponent getSubComponent(int rowIndex);
 }