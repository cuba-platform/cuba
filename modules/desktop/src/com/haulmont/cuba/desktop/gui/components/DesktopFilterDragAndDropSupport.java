/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.gui.data.TreeModelAdapter;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

/**
 * Class provides support for Drag and Drop functionality in desktop implementation
 * of generic filter editor
 *
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean
public class DesktopFilterDragAndDropSupport {

    private Logger log = LoggerFactory.getLogger(DesktopFilterDragAndDropSupport.class);

    public void initDragAndDrop(Tree tree, ConditionsTree conditions) {
        JTree dTree = (JTree) DesktopComponentsHelper.unwrap(tree);
        dTree.setDragEnabled(true);
        dTree.setDropMode(DropMode.ON_OR_INSERT);
        dTree.setTransferHandler(new TreeTransferHandler(tree, conditions));
    }

    protected class TreeTransferHandler extends TransferHandler {

        private ConditionsTree conditions;
        private Tree tree;

        public TreeTransferHandler(Tree tree, ConditionsTree conditions) {
            this.tree = tree;
            this.conditions = conditions;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            DropInfo dropInfo = getDropInfo(support);
            AbstractCondition condition = dropInfo.getCondition();
            int dropIndex = dropInfo.getDropIndex();
            if (dropIndex == -1) {
                return condition.isGroup();
            } else {
                return true;
            }
        }

        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JTree tree = (JTree) c;
            Object lastSelectedPathComponent = tree.getLastSelectedPathComponent();
            Entity entity = ((TreeModelAdapter.Node) lastSelectedPathComponent).getEntity();
            return new ConditionTransferable((AbstractCondition) entity);
        }

        @Override
        public boolean importData(TransferSupport support) {
            DropInfo dropInfo = getDropInfo(support);
            int dropIndex = dropInfo.getDropIndex();
            AbstractCondition targetCondition = dropInfo.getCondition();

            AbstractCondition sourceCondition;

            Transferable transferable = support.getTransferable();
            try {
                sourceCondition = (AbstractCondition) transferable.getTransferData(support.getDataFlavors()[0]);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
                return false;
            }

            Node<AbstractCondition> sourceNode = conditions.getNode(sourceCondition);
            Node<AbstractCondition> targetNode = targetCondition == null ? null : conditions.getNode(targetCondition);

            boolean moveToTheSameParent = Objects.equals(sourceNode.getParent(), targetNode);

            if (dropIndex == -1) { //insert right into the node
                if (sourceNode.getParent() == null) {
                    conditions.getRootNodes().remove(sourceNode);
                } else {
                    sourceNode.getParent().getChildren().remove(sourceNode);
                }
                targetNode.addChild(sourceNode);
            } else { //insert before or after other node
                int sourceNodeIndex;
                if (sourceNode.getParent() == null) {
                    sourceNodeIndex = conditions.getRootNodes().indexOf(sourceNode);
                    conditions.getRootNodes().remove(sourceNode);
                } else {
                    sourceNodeIndex = sourceNode.getParent().getChildren().indexOf(sourceNode);
                    sourceNode.getParent().getChildren().remove(sourceNode);
                }

                //decrease drop position index if dragging from top to bottom inside the same parent node
                if (moveToTheSameParent && (sourceNodeIndex < dropIndex))
                    dropIndex--;

                if (targetNode != null) {
                    targetNode.insertChildAt(dropIndex, sourceNode);
                } else {
                    sourceNode.parent = null;
                    conditions.getRootNodes().add(dropIndex, sourceNode);
                }
            }
            return true;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            refreshConditionsDs();
        }

        protected void refreshConditionsDs() {
            tree.getDatasource().refresh(Collections.<String, Object>singletonMap("conditions", conditions));
            tree.expandTree();
        }

        protected DropInfo getDropInfo(TransferSupport support) {
            JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
            Object lastPathComponent = dropLocation.getPath().getLastPathComponent();
            if (lastPathComponent instanceof TreeModelAdapter.Node) {
                TreeModelAdapter.Node targetTreeNode = (TreeModelAdapter.Node) lastPathComponent;
                AbstractCondition condition = (AbstractCondition) targetTreeNode.getEntity();
                return new DropInfo(condition, dropLocation.getChildIndex());
            } else {
                //if lastPathComponent is a String then a drop was on one of root nodes
                return new DropInfo(null, dropLocation.getChildIndex());
            }
        }

        /**
         * Class holds an information about drop position in a tree.
         */
        protected class DropInfo {
            private AbstractCondition condition;
            private int dropIndex;

            public DropInfo(AbstractCondition condition, int dropIndex) {
                this.condition = condition;
                this.dropIndex = dropIndex;
            }

            /**
             * @return a group condition if inserting node inside a group
             * or null if inserting node to the top level of tree
             */
            public AbstractCondition getCondition() {
                return condition;
            }

            /**
             * @return drop position relatively to the parent node or -1 if inserting
             * right into the node
             */
            public int getDropIndex() {
                return dropIndex;
            }
        }
    }


    /**
     * Class provides a transferable {@link com.haulmont.cuba.gui.components.filter.condition.AbstractCondition}
     * for Drag And Drop operation
     */
    protected class ConditionTransferable implements Transferable {

        private AbstractCondition condition;

        public ConditionTransferable(AbstractCondition condition) {
            this.condition = condition;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=\"" +
                    AbstractCondition.class.getName() +
                    "\"";

            DataFlavor[] dataFlavors = new DataFlavor[1];
            try {
                dataFlavors[0] = new DataFlavor(mimeType);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            return dataFlavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return true;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            return condition;
        }
    }
}
