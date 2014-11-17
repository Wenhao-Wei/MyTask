/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.render;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.*;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-2 10:17:41
 */
public class TreeRender extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded,
                leaf, row, hasFocus);
        //DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;   //---此处为userObject 
        //if (node.) {
          // setBackground(new Color(0,51,51));
            //setForeground(new Color(0,51,51));
   //setTextSelectionColor(new Color(0,204,204));
   setTextNonSelectionColor(new Color(0,255,255));
    setBackgroundSelectionColor(null);
    // setForegroundSelectionColor(new Color(0,102,102));
    setBackgroundNonSelectionColor(new Color(0,51,51));
       // } else {
         //   c.setBackground(new Color(0, 102, 102));
       // }
        return this;
    }
;
}
