package com.apn.filerenamer.ui;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class CustomModelListener implements TreeModelListener {
    public void treeNodesChanged(TreeModelEvent e) {
        DefaultMutableTreeNode node;
        node = (DefaultMutableTreeNode)
                (e.getTreePath().getLastPathComponent());

        /*
        * If the event lists children, then the changed
        * node is the child of the node we have already
        * gotten.  Otherwise, the changed node and the
        * specified node are the same.
        */
        try {
            int index = e.getChildIndices()[0];
            node = (DefaultMutableTreeNode)
                    (node.getChildAt(index));
        } catch (NullPointerException exc) {
            System.out.println("Error updating tree!"+exc.getMessage());
        }
    }


    public void treeNodesInserted(TreeModelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void treeStructureChanged(TreeModelEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
