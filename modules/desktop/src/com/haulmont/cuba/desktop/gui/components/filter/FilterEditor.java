/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.entity.CategorizedEntity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;


import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static org.apache.commons.lang.BooleanUtils.isTrue;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class FilterEditor extends AbstractFilterEditor {

    private static final int NAME_COLUMN_WIDTH = 110;
    private static final int OPERATION_COLUMN_WIDTH = 130;
    private static final int PARAM_COLUMN_WIDTH = 130;
    private static final int HIDDEN_COLUMN_WIDTH = 75;
    private static final int DELETE_COLUMN_WIDTH = 38;

    private JPanel panel;
    private JPanel topPanel;
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private MigLayout layout;
    private JButton upBtn;
    private JButton downBtn;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JComboBox addSelect;
    private JTable table;
    private ConditionTableModel model;
    private JTextField nameField;
    private JCheckBox globalCb;
    private JCheckBox defaultCb;
    private JCheckBox applyDefaultCb;

    private JLabel globalCbLabel;
    private JLabel applyDefaultLabel;
    private JLabel defaultCbLabel;

    public FilterEditor(final DesktopFilter desktopFilter, FilterEntity filterEntity,
                        Element filterDescriptor, List<String> existingNames) {
        super(desktopFilter, filterEntity, filterDescriptor, existingNames);
    }

    @Override
    public void init() {
        LC lc = new LC();
        lc.insetsAll("0");
        layout = new MigLayout(lc);
        panel = new JPanel(layout);
        LC topLc = new LC();
        topLc.insetsAll("0");
        topLc.fillX();
        LC mainLc = new LC();
        mainLc.insetsAll("0");
        LC upDownLc = new LC();
        upDownLc.insetsAll("0");
        if (LayoutAdapter.isDebug()) {
            lc.debug(1000);
            topLc.debug(1000);
            mainLc.debug(1000);
            upDownLc.debug(1000);
        }
        topPanel = new JPanel(new MigLayout(topLc));
        bottomPanel = new JPanel(new MigLayout(topLc));
        mainPanel = new JPanel(new MigLayout(mainLc));
        panel.add(topPanel, new CC().cell(0, 0).growX());
        panel.add(mainPanel, new CC().cell(0, 1));
        panel.add(bottomPanel, new CC().cell(0, 2).growX());
        JPanel upDownPanel = new JPanel(new MigLayout(upDownLc));
        panel.add(upDownPanel, new CC().cell(1, 1));
        upBtn = new JButton(App.getInstance().getResources().getIcon("icons/up.png"));
        DesktopComponentsHelper.adjustSize(upBtn);
        upBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                AbstractCondition<Param> condition = ((ConditionTableModel) table.getModel()).getCondition(row);
                int index = conditions.indexOf(condition);
                if (index > 0) {
                    AbstractCondition next = conditions.get(index - 1);
                    conditions.set(index - 1, condition);
                    conditions.set(index, next);
                    table.setRowSelectionInterval(index, index);
                    TableColumnModel columnModel = table.getColumnModel();
                    for (int i = 1; i < 3; i++) {
                        columnModel.getColumn(i).getCellEditor().cancelCellEditing();
                    }
                    updateTable(index - 1);
                }
            }
        });
        upDownPanel.add(upBtn, new CC().wrap());

        downBtn = new JButton(App.getInstance().getResources().getIcon("icons/down.png"));
        DesktopComponentsHelper.adjustSize(downBtn);
        downBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    return;
                }
                AbstractCondition<Param> condition = ((ConditionTableModel) table.getModel()).getCondition(row);
                int index = conditions.indexOf(condition);
                int count = conditions.size();
                if (index < count - 1) {
                    AbstractCondition next = conditions.get(index + 1);
                    conditions.set(index + 1, condition);
                    conditions.set(index, next);
                    TableColumnModel columnModel = table.getColumnModel();
                    for (int i = 1; i < 3; i++) {
                        columnModel.getColumn(i).getCellEditor().cancelCellEditing();
                    }
                    updateTable(index + 1);
                }
            }
        });
        upDownPanel.add(downBtn);

        saveBtn = new JButton(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Ok"));
        saveBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
        DesktopComponentsHelper.adjustSize(saveBtn);

        JPanel buttonsPanel = new JPanel(new MigLayout());

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commit())
                    ((DesktopFilter) filter).editorCommitted();
            }
        });
        if (filterEntity.getCode() != null)
            saveBtn.setEnabled(false);
        buttonsPanel.add(saveBtn);

        cancelBtn = new JButton(MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Cancel"));
        cancelBtn.setIcon(App.getInstance().getResources().getIcon("icons/cancel.png"));
        DesktopComponentsHelper.adjustSize(cancelBtn);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DesktopFilter) filter).editorCancelled();
            }
        });
        buttonsPanel.add(cancelBtn);
        bottomPanel.add(buttonsPanel, new CC().growX().alignY("bottom"));
        JPanel checkBoxes = new JPanel(new MigLayout(new LC().wrapAfter(2)));
        bottomPanel.add(checkBoxes, new CC().alignX("right"));

        globalCbLabel = new JLabel(getMessage("FilterEditor.global"));
        globalCb = new JCheckBox();
        globalCb.setSelected(filterEntity.getUser() == null);
        globalCb.setEnabled(UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.filter.global"));

        checkBoxes.add(globalCbLabel, new CC().alignX("right"));
        checkBoxes.add(globalCb, new CC().alignX("right"));

        defaultCbLabel = new JLabel(getMessage("FilterEditor.isDefault"));
        defaultCb = new JCheckBox();
        defaultCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (defaultCb.isSelected()) {
                    applyDefaultCb.setEnabled(true);
                } else {
                    applyDefaultCb.setEnabled(false);
                    applyDefaultCb.setSelected(false);
                }
                if (filterEntity != null) {
                    filterEntity.setIsDefault(defaultCb.isSelected());

                }
            }
        });
        checkBoxes.add(defaultCbLabel);
        checkBoxes.add(defaultCb);


        applyDefaultLabel = new JLabel(getMessage("FilterEditor.applyDefault"));
        applyDefaultCb = new JCheckBox();
        applyDefaultCb.setEnabled(false);
        applyDefaultCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterEntity != null) {
                    filterEntity.setApplyDefault(applyDefaultCb.isSelected());
                }
            }
        });
        checkBoxes.add(applyDefaultLabel);
        checkBoxes.add(applyDefaultCb);

        JLabel label = new JLabel(getMessage("FilterEditor.nameLab"));

        JPanel namePanel = new JPanel(new MigLayout(new LC().insetsAll("0")));
        nameField = new JTextField();
        nameField.setText(filterEntity.getName());
        Dimension size = nameField.getSize();
        size.width = 200;
        nameField.setSize(size);
        nameField.setPreferredSize(size);
        namePanel.add(label);
        namePanel.add(nameField);

        JPanel addPanel = new JPanel(new MigLayout(new LC().insetsAll("0")));
        initAddSelect(addPanel);
        initTable(mainPanel);
        topPanel.add(namePanel, new CC().growX());
        topPanel.add(addPanel, new CC().alignX("right"));

        updateControls();
    }

    public JButton getSaveButton() {
        return saveBtn;
    }


    private void initAddSelect(JPanel panel) {
        JLabel label = new JLabel(getMessage("FilterEditor.addCondition"));
        panel.add(label, new CC().alignX("right"));

        addSelect = new ExtendedComboBox();

        addSelect.addItem(null);
        for (AbstractConditionDescriptor descriptor : descriptors) {
            addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(descriptor, descriptor.getLocCaption()));

        }
        if (UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions")) {
            ConditionCreator conditionCreator = new ConditionCreator(filterComponentName, datasource);
            addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(conditionCreator, conditionCreator.getLocCaption()));
        }

        if (CategorizedEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
            RuntimePropConditionCreator runtimePropCreator = new RuntimePropConditionCreator(filterComponentName, datasource);
            addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(runtimePropCreator, runtimePropCreator.getLocCaption()));
        }

        addSelect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED != e.getStateChange()) {
                    return;
                }
                if (addSelect.getSelectedItem() != null) {
                    addCondition(((ItemWrapper<AbstractConditionDescriptor>) addSelect.getSelectedItem()).getItem());
                    addSelect.setSelectedItem(null);
                }
            }
        });
        panel.add(addSelect, new CC().width("100:100:100"));
        Dimension size = addSelect.getSize();
        size.width = 100;
        addSelect.setSize(size);
        addSelect.setPreferredSize(size);
    }

    private void setComponentWidth(JComponent component, int width) {
        Dimension size = component.getSize();
        size.width = width;
        component.setSize(size);
        component.setPreferredSize(size);
    }

    private void initTable(JPanel panel) {
        model = new ConditionTableModel();
        table = new ConditionsTable();
        table.setModel(model);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);

        table.setRowHeight(DesktopComponentsHelper.FIELD_HEIGHT + 2);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(NAME_COLUMN_WIDTH);
        table.getColumnModel().getColumn(1).setPreferredWidth(OPERATION_COLUMN_WIDTH);
        table.getColumnModel().getColumn(2).setPreferredWidth(PARAM_COLUMN_WIDTH);
        table.getColumnModel().getColumn(3).setPreferredWidth(HIDDEN_COLUMN_WIDTH);
        table.getColumnModel().getColumn(4).setMaxWidth(DELETE_COLUMN_WIDTH);
        table.getColumnModel().getColumn(4).setPreferredWidth(DELETE_COLUMN_WIDTH);
        table.getColumnModel().getColumn(4).setMinWidth(DELETE_COLUMN_WIDTH);

        table.setCellSelectionEnabled(false);

        JScrollPane pane = new JScrollPane(table);

        OperationCell operationCell = new OperationCell();
        table.getColumnModel().getColumn(1).setCellRenderer(operationCell);
        table.getColumnModel().getColumn(1).setCellEditor(operationCell);

        ParamCell paramCell = new ParamCell();
        table.getColumnModel().getColumn(2).setCellRenderer(paramCell);
        table.getColumnModel().getColumn(2).setCellEditor(paramCell);

        DeleteCell deleteCell = new DeleteCell();
        table.getColumnModel().getColumn(4).setCellRenderer(deleteCell);
        table.getColumnModel().getColumn(4).setCellEditor(deleteCell);

        panel.add(pane, new CC().height("150:150:150").width("520:520:520"));

        this.panel.revalidate();
        this.panel.repaint();
        for (AbstractCondition<Param> condition : conditions) {
            model.addCondition(condition);
        }
        //todo add PopupMenu
    }

    private void addCondition(AbstractConditionDescriptor descriptor) {
        AbstractCondition condition = descriptor.createCondition();
        conditions.add(condition);
        model.addCondition(condition);
        table.revalidate();
        table.repaint();

        updateControls();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void deleteCondition(AbstractCondition condition) {
        conditions.remove(condition);
        ((ConditionTableModel) table.getModel()).removeCondition(condition);
        table.revalidate();
        table.repaint();

        updateControls();
    }

    private void updateControls() {
        if (filterEntity.getCode() == null)
            saveBtn.setEnabled(!conditions.isEmpty());
        else
            saveBtn.setEnabled(false);
        defaultCb.setVisible(filterEntity.getFolder() == null);
        defaultCbLabel.setVisible(defaultCb.isVisible());
        defaultCb.setSelected(isTrue(filterEntity.getIsDefault()));
        applyDefaultCb.setVisible(defaultCb.isVisible() && manualApplyRequired);
        applyDefaultLabel.setVisible(applyDefaultCb.isVisible());
        applyDefaultCb.setSelected(BooleanUtils.isTrue(filterEntity.getApplyDefault()));
    }

    private void updateTable(int index) {
        model.clear();
        for (AbstractCondition<Param> condition : conditions) {
            model.addCondition(condition);
        }
        table.setRowSelectionInterval(index, index);
        table.revalidate();
        table.repaint();

        updateControls();
    }

    @Override
    protected AbstractFilterParser createFilterParser(String xml, String messagesPack, String filterComponentName,
                                                      CollectionDatasource datasource) {
        return new FilterParser(xml, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractPropertyConditionDescriptor createPropertyConditionDescriptor(
            Element element, String messagesPack, String filterComponentName, CollectionDatasource datasource) {
        return new PropertyConditionDescriptor(element, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractPropertyConditionDescriptor createPropertyConditionDescriptor(
            String name, String caption, String messagesPack, String filterComponentName, CollectionDatasource datasource) {
        return new PropertyConditionDescriptor(name, caption, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractCustomConditionDescriptor createCustomConditionDescriptor(
            Element element, String messagesPack, String filterComponentName, CollectionDatasource datasource) {
        return new CustomConditionDescriptor(element, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractFilterParser createFilterParser(List<AbstractCondition> conditions, String messagesPack,
                                                      String filterComponentName, Datasource datasource) {
        return new FilterParser(conditions, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected String getName() {
        return nameField.getText();
    }

    @Override
    protected boolean isGlobal() {
        return globalCb.isSelected();
    }

    @Override
    protected void showNotification(String caption, String description) {
        App.getInstance().getWindowManager().showNotification
                (caption, description, IFrame.NotificationType.HUMANIZED);
    }

    private class ConditionTableModel extends AbstractTableModel {
        private String[] columnNames =
                {
                        getMessage("FilterEditor.column.name"),
                        getMessage("FilterEditor.column.op"),
                        getMessage("FilterEditor.column.param"),
                        getMessage("FilterEditor.column.hidden"),
                        getMessage("FilterEditor.column.control")
                };
        private ArrayList<AbstractCondition<Param>> items = new ArrayList<AbstractCondition<Param>>();

        @Override
        public int getRowCount() {
            return items.size();
        }

        public void clear() {
            int size = items.size();
            items.clear();
            fireTableRowsDeleted(0, size - 1);
        }

        @Override
        public int getColumnCount() {
            return 5;
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
                    c = JButton.class;
                    break;
            }
            return c;
        }

        @Override
        public Object getValueAt(final int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return items.get(rowIndex).getLocCaption();
                case 1:
                    return items.get(rowIndex);
                case 2:
                    return items.get(rowIndex);
                case 3:
                    return items.get(rowIndex).isHidden();
                case 4:
                    return items.get(rowIndex);
                default:
                    throw new RuntimeException();
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        public void addCondition(AbstractCondition<Param> condition) {
            items.add(condition);
            fireTableRowsInserted(items.size() - 1, items.size() - 1);
        }

        public void removeCondition(AbstractCondition<Param> condition) {
            int index = items.indexOf(condition);
            if (index != -1) {
                items.remove(condition);
                fireTableRowsDeleted(index, index);
            }
        }

        public AbstractCondition<Param> getCondition(int row) {
            if (items.size() < row) {
                return null;
            } else
                return items.get(row);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 3:
                    items.get(rowIndex).setHidden((Boolean) aValue);
                    break;
            }
        }

        public void fireConditionUpdated(AbstractCondition condition) {
            int row = items.indexOf(condition);
            if (row != -1) {
                fireTableRowsUpdated(row, row);
            }
        }
    }

    protected abstract class AbstractCell implements TableCellEditor, TableCellRenderer {

        protected EventListenerList listenerList = new EventListenerList();
        private ChangeEvent changeEvent;

        protected Map<Object, JComponent> components = new HashMap<Object, JComponent>();
        protected Map<Object, JPanel> wrappers = new HashMap<Object, JPanel>();

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        @Override
        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        protected void fireEditingCanceled() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    if (changeEvent == null)
                        changeEvent = new ChangeEvent(this);
                    ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
                }
            }
        }

        protected void fireEditingStopped() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    if (changeEvent == null)
                        changeEvent = new ChangeEvent(this);
                    ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
                }
            }
        }

        protected void setColors(JComponent component, boolean isSelected) {
            if (isSelected) {
                component.setBackground(new Color(table.getSelectionBackground().getRed(),
                        table.getSelectionBackground().getGreen(),
                        table.getSelectionBackground().getBlue(), 255));
            } else {
                component.setBackground(new Color(table.getBackground().getRed(),
                        table.getBackground().getGreen(),
                        table.getBackground().getBlue(), 255));
            }
        }

        protected abstract JComponent getComponent(Object value);

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JComponent component = components.get(value);
            JPanel wrapper;
            if (component == null) {
                component = getComponent(value);
                wrapper = wrapComponent(component);
                components.put(value, component);
                wrappers.put(value, wrapper);
            } else {
                wrapper = wrappers.get(value);
            }
            setColors(wrapper, true);
            return wrapper;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JComponent component = components.get(value);
            JPanel wrapper;
            if (component == null) {
                component = getComponent(value);
                wrapper = wrapComponent(component);
                components.put(value, component);
                wrappers.put(value, wrapper);
            } else {
                wrapper = wrappers.get(value);
            }
            setColors(wrapper, isSelected);
            return wrapper;
        }

        protected JPanel wrapComponent(JComponent component) {
            JPanel wrapper = new JPanel(new MigLayout("insets 0 0 0 0, align center"));
            wrapper.add(component);
            return wrapper;
        }

    }

    protected class OperationCell extends AbstractCell {

        protected JComponent getComponent(final Object value) {
            JComponent component = (JComponent) ((AbstractCondition<Param>) value).createOperationEditor().getImpl();
            setComponentWidth(component, OPERATION_COLUMN_WIDTH);
            if (component instanceof JComboBox) {
                ((JComboBox) component).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.fireConditionUpdated((AbstractCondition) value);
                    }
                });
            }
            return component;
        }
    }

    protected class ParamCell extends AbstractCell {

        protected JComponent getComponent(Object value) {
            JPanel component = new ParamEditor((AbstractCondition<Param>) value, false);
            setComponentWidth(component, PARAM_COLUMN_WIDTH);
            return component;
        }

        protected JPanel wrapComponent(JComponent component) {
            return (JPanel) component;
        }
    }

    protected class DeleteCell extends AbstractCell {

        protected JComponent getComponent(final Object value) {
            final JButton delBtn = new JButton(App.getInstance().getResources().getIcon("icons/tab-remove.png"));
            delBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Set<Map.Entry<Object, JComponent>> set = components.entrySet();
                    AbstractCondition condition = null;
                    for (Map.Entry<Object, JComponent> entry : set) {
                        if (entry.getValue().equals(delBtn)) {
                            condition = (AbstractCondition) entry.getKey();
                            deleteCondition(condition);
                            cancelCellEditing();
                            break;
                        }
                    }
                    if (condition != null) {
                        conditions.remove(condition);
                        wrappers.remove(condition);
                    }
                }
            });
            return delBtn;
        }
    }

    private class ConditionsTable extends JTable {
        public boolean isCellSelected(int row, int column) {
            return (row == getSelectedRow());
        }
    }
}
