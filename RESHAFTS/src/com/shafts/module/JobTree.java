/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shafts.module;

import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Little-Kitty
 * @date 2014-10-13 10:37:05
 */
public class JobTree {
    
    public void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children 
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
    public String getpath(String node, String path1, String path2, String path3){
        String path = null;
        switch (node) {
                        case "Local":
                            path = path1;
                            break;
                        case "Target Navigator":
                            path = path2;
                            break;
                        case "Hit Explorer":
                            path = path3;
                            break;
                    }
        return path;
    }
}
