/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;
import com.haulmont.cuba.gui.components.filter.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import org.apache.commons.lang.mutable.MutableInt;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * {@link ConditionsTree} adapter for the desktop-client table component.
 *
 * @author krivopustov
 * @version $Id$
*/
class ConditionsTableModel extends AbstractTableModel {

    private String[] columnNames = new String[6];

    private ConditionsTree conditions;

    public ConditionsTableModel(ConditionsTree conditions) {
        this.conditions = conditions;

        Messages msgs = AppBeans.get(Messages.NAME, Messages.class);
        columnNames[0] = msgs.getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.column.name");
        columnNames[1] = msgs.getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.column.op");
        columnNames[2] = msgs.getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.column.param");
        columnNames[3] = msgs.getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.column.hidden");
        columnNames[4] = msgs.getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.column.required");
        columnNames[5] = msgs.getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.column.control");
    }

    @Override
    public int getRowCount() {
        return conditions.toConditionsList().size();
    }

    public void clear() {
        int size = conditions.toConditionsList().size();
        conditions.getRootNodes().clear();
        fireTableRowsDeleted(0, size - 1);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int col) {
        Class c = null;

        switch (col) {
            case 0:
                c = String.class;
                break;
            case 1:
                c = AbstractOperationEditor.class;
                break;
            case 2:
                c = ParamEditor.class;
                break;
            case 3:
                c = Boolean.class;
                break;
            case 4:
                c = Boolean.class;
                break;
            case 5:
                c = JButton.class;
                break;
        }
        return c;
    }

    @Override
    public Object getValueAt(final int rowIndex, int columnIndex) {
        Pair<Node<AbstractCondition>, Integer> nodeWithLevel = getNodeWithLevel(rowIndex);
        AbstractCondition condition = nodeWithLevel.getFirst().getData();
        int level = nodeWithLevel.getSecond();
        switch (columnIndex) {
            case 0:
                StringBuilder sb = new StringBuilder(condition.getLocCaption());
                for (int i = 0; i < level; i++) {
                    sb.insert(0, "    ");
                }
                return sb.toString();
            case 1:
                return condition;
            case 2:
                return condition;
            case 3:
                return condition.isHidden();
            case 4:
                return condition.isRequired();
            case 5:
                return condition;
            default:
                throw new IllegalArgumentException("Invalid column index: " + columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        Pair<Node<AbstractCondition>, Integer> nodeWithLevel = getNodeWithLevel(rowIndex);
        AbstractCondition condition = nodeWithLevel.getFirst().getData();
        return !(condition instanceof GroupCondition && columnIndex == 4) && !(columnIndex == 0);
    }

    public void addNode(Node<AbstractCondition> node) {
        if (node.getParent() == null)
            conditions.getRootNodes().add(node);
        int size = conditions.toList().size();
        fireTableRowsInserted(size - 1, size - 1);
    }

    public void removeCondition(AbstractCondition condition) {
        Node<AbstractCondition> node = conditions.getNode(condition);
        if (node == null)
            return;

        int idx = conditions.toList().indexOf(node);

        if (node.getParent() == null) {
            conditions.getRootNodes().remove(node);
        } else {
            node.getParent().getChildren().remove(node);
        }

        fireTableRowsDeleted(idx, idx);
    }

    public Node<AbstractCondition> getNode(int row) {
        return getNodeWithLevel(row).getFirst();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Pair<Node<AbstractCondition>, Integer> nodeWithLevel = getNodeWithLevel(rowIndex);
        AbstractCondition condition = nodeWithLevel.getFirst().getData();
        switch (columnIndex) {
            case 3:
                condition.setHidden((Boolean) aValue);
                break;

            case 4:
                condition.setRequired((Boolean) aValue);
                break;
        }
    }

    public void fireConditionUpdated(AbstractCondition condition) {
        Node<AbstractCondition> node = conditions.getNode(condition);
        if (node == null)
            return;

        int idx = conditions.toList().indexOf(node);
        fireTableRowsUpdated(idx, idx);
    }

    @Nonnull
    private Pair<Node<AbstractCondition>, Integer> getNodeWithLevel(int plainIdx) {
        int level = 0;
        MutableInt count = new MutableInt(0);
        Pair<Node<AbstractCondition>, Integer> result = recursiveGetNodeWithLevel(plainIdx, conditions.getRootNodes(), count, level);
        if (result == null)
            throw new IllegalArgumentException("Node with index " + plainIdx + " not found");
        else
            return result;
    }

    private Pair<Node<AbstractCondition>, Integer> recursiveGetNodeWithLevel(
            int plainIdx, List<Node<AbstractCondition>> nodes, MutableInt count, int level)
    {
        Pair<Node<AbstractCondition>, Integer> result;
        for (Node<AbstractCondition> node : nodes) {
            if (count.intValue() == plainIdx) {
                return new Pair<>(node, level);
            } else {
                count.increment();
                if (!node.getChildren().isEmpty()) {
                    result = recursiveGetNodeWithLevel(plainIdx, node.getChildren(), count, level + 1);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public void moveUp(Node<AbstractCondition> node) {
        List<Node<AbstractCondition>> siblings = node.getParent() == null ?
                conditions.getRootNodes() : node.getParent().getChildren();

        int idx = siblings.indexOf(node);
        if (idx > 0) {
            Node<AbstractCondition> prev = siblings.get(idx - 1);
            siblings.set(idx - 1, node);
            siblings.set(idx, prev);
            fireTableDataChanged();
        }
    }

    public void moveDown(Node<AbstractCondition> node) {
        List<Node<AbstractCondition>> siblings = node.getParent() == null ?
                conditions.getRootNodes() : node.getParent().getChildren();

        int idx = siblings.indexOf(node);
        if (idx < siblings.size() - 1) {
            Node<AbstractCondition> next = siblings.get(idx + 1);
            siblings.set(idx + 1, node);
            siblings.set(idx, next);
            fireTableDataChanged();
        }
    }

    public int getRow(Node<AbstractCondition> node) {
        return conditions.toList().indexOf(node);
    }
}