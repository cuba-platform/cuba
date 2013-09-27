/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.CategorizedEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.ExtendedComboBox;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.*;
import com.haulmont.cuba.gui.components.filter.addcondition.SelectionHandler;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.global.UserSession;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static org.apache.commons.lang.BooleanUtils.isTrue;

/**
 * @author devyatkin
 * @version $Id$
 */
public class FilterEditor extends AbstractFilterEditor {

    protected static final int NAME_COLUMN_WIDTH = 130;
    protected static final int OPERATION_COLUMN_WIDTH = 107;
    protected static final int PARAM_COLUMN_WIDTH = 150;
    protected static final int HIDDEN_COLUMN_WIDTH = 30;
    protected static final int REQUIRED_COLUMN_WIDTH = 30;
    protected static final int DELETE_COLUMN_WIDTH = 38;

    protected JPanel panel;
    protected JPanel topPanel;
    protected JPanel mainPanel;
    protected JPanel bottomPanel;
    protected MigLayout layout;
    protected JButton upBtn;
    protected JButton downBtn;
    protected JButton saveBtn;
    protected JButton cancelBtn;
    protected JComboBox addSelect;
    protected JTable table;
    protected ConditionsTableModel model;
    protected JTextField nameField;
    protected JCheckBox globalCb;
    protected JCheckBox defaultCb;
    protected JCheckBox applyDefaultCb;

    protected UserSession userSession = AppBeans.get(UserSessionSource.class).getUserSession();

    public FilterEditor(final DesktopFilter desktopFilter, FilterEntity filterEntity,
                        Element filterDescriptor, List<String> existingNames) {
        super(desktopFilter, filterEntity, filterDescriptor, existingNames);

    }

    @Override
    public void init() {
        LC lc = new LC();
        lc.insets("0 0 5 0");
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
                Node<AbstractCondition> node = model.getNode(table.getSelectedRow());
                model.moveUp(node);
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
                Node<AbstractCondition> node = model.getNode(table.getSelectedRow());
                model.moveDown(node);
            }
        });
        upDownPanel.add(downBtn);

        saveBtn = new JButton(messages.getMainMessage("actions.Ok"));
        saveBtn.setIcon(App.getInstance().getResources().getIcon("icons/ok.png"));
        DesktopComponentsHelper.adjustSize(saveBtn);

        JPanel buttonsPanel = new JPanel(new MigLayout(new LC().insetsAll("0").alignX("right")));

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commit())
                    ((DesktopFilter) filter).editorCommitted();
            }
        });
        if (filterEntity.getFolder() == null && filterEntity.getCode() != null)
            saveBtn.setEnabled(false);
        buttonsPanel.add(saveBtn);

        cancelBtn = new JButton(messages.getMainMessage("actions.Cancel"));
        cancelBtn.setIcon(App.getInstance().getResources().getIcon("icons/cancel.png"));
        DesktopComponentsHelper.adjustSize(cancelBtn);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DesktopFilter) filter).editorCancelled();
            }
        });
        buttonsPanel.add(cancelBtn);

        JPanel checkBoxes = new JPanel(new MigLayout());

        bottomPanel.add(checkBoxes, new CC().alignX("left"));
        bottomPanel.add(buttonsPanel, new CC().growX().alignY("bottom"));

        globalCb = new JCheckBox(getMessage("FilterEditor.global"));
        globalCb.setSelected(filterEntity.getUser() == null);
        globalCb.setEnabled(userSession.isSpecificPermitted("cuba.gui.filter.global"));

        checkBoxes.add(globalCb);

        defaultCb = new JCheckBox(getMessage("FilterEditor.isDefault"));
        defaultCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateApplyDefaultCb();
            }
        });
        checkBoxes.add(defaultCb);

        applyDefaultCb = new JCheckBox(getMessage("FilterEditor.applyDefault"));
        applyDefaultCb.setEnabled(false);
        applyDefaultCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterEntity != null) {
                    filterEntity.setApplyDefault(applyDefaultCb.isSelected());
                }
            }
        });
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

        if (ConfigProvider.getConfig(ClientConfig.class).getGenericFilterTreeConditionSelect()) {
            initAddDialog(addPanel);
        } else {
            initAddSelect(addPanel);
        }

        initTable(mainPanel);
        topPanel.add(namePanel, new CC().growX());
        topPanel.add(addPanel, new CC().alignX("right"));

        updateControls();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                nameField.requestFocus();
            }
        });
    }

    public JButton getSaveButton() {
        return saveBtn;
    }

    protected void updateApplyDefaultCb(){
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

    protected void initAddDialog(JPanel panel) {
        JButton button = new JButton(getMessage("FilterEditor.addCondition"));
        panel.add(button, new CC().alignX("right"));

        button.addActionListener(new AddConditionActionListener());
    }

    protected void initAddSelect(JPanel panel) {
        JLabel label = new JLabel(getMessage("FilterEditor.addCondition"));
        panel.add(label, new CC().alignX("right"));

        addSelect = new ExtendedComboBox();

        addSelect.addItem(null);
        for (AbstractConditionDescriptor descriptor : descriptors) {
            addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(descriptor, descriptor.getLocCaption()));

        }

        if (CategorizedEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
            RuntimePropConditionCreator runtimePropCreator = new RuntimePropConditionCreator(filterComponentName, datasource);
            addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(runtimePropCreator, runtimePropCreator.getLocCaption()));
        }

        GroupCreator andGroupCreator = new GroupCreator(GroupType.AND, filterComponentName, datasource);
        addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(andGroupCreator, andGroupCreator.getLocCaption()));

        GroupCreator orGroupCreator = new GroupCreator(GroupType.OR, filterComponentName, datasource);
        addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(orGroupCreator, orGroupCreator.getLocCaption()));

        if (userSession.isSpecificPermitted("cuba.gui.filter.customConditions")) {
            ConditionCreator conditionCreator = new ConditionCreator(filterComponentName, datasource);
            addSelect.addItem(new ItemWrapper<AbstractConditionDescriptor>(conditionCreator, conditionCreator.getLocCaption()));
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

        JButton button = new JButton(getMessage("FilterEditor.addMoreConditions"));
        panel.add(button, new CC().alignX("right"));

        button.addActionListener(new AddConditionActionListener());
    }

    protected void setComponentWidth(JComponent component, int width) {
        Dimension size = component.getSize();
        size.width = width;
        component.setSize(size);
        component.setPreferredSize(size);
    }

    protected void initTable(JPanel panel) {
        model = new ConditionsTableModel(conditions);
        table = new ConditionsTable();
        table.setModel(model);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        DesktopComponentsHelper.correctTableFocusTraversal(table);

        table.setRowHeight(DesktopComponentsHelper.FIELD_HEIGHT + 2);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(NAME_COLUMN_WIDTH);
        table.getColumnModel().getColumn(1).setPreferredWidth(OPERATION_COLUMN_WIDTH);
        table.getColumnModel().getColumn(2).setPreferredWidth(PARAM_COLUMN_WIDTH);
        table.getColumnModel().getColumn(3).setPreferredWidth(HIDDEN_COLUMN_WIDTH);
        table.getColumnModel().getColumn(4).setPreferredWidth(REQUIRED_COLUMN_WIDTH);
        table.getColumnModel().getColumn(5).setMaxWidth(DELETE_COLUMN_WIDTH);
        table.getColumnModel().getColumn(5).setPreferredWidth(DELETE_COLUMN_WIDTH);
        table.getColumnModel().getColumn(5).setMinWidth(DELETE_COLUMN_WIDTH);

        table.setCellSelectionEnabled(false);

        JScrollPane pane = new JScrollPane(table);

        OperationCell operationCell = new OperationCell();
        table.getColumnModel().getColumn(1).setCellRenderer(operationCell);
        table.getColumnModel().getColumn(1).setCellEditor(operationCell);

        ParamCell paramCell = new ParamCell();
        table.getColumnModel().getColumn(2).setCellRenderer(paramCell);
        table.getColumnModel().getColumn(2).setCellEditor(paramCell);

        DeleteCell deleteCell = new DeleteCell();
        table.getColumnModel().getColumn(5).setCellRenderer(deleteCell);
        table.getColumnModel().getColumn(5).setCellEditor(deleteCell);

        panel.add(pane, new CC().height("150:150:150").width("520:520:520"));

        this.panel.revalidate();
        this.panel.repaint();
        //todo add PopupMenu
    }

    protected void addCondition(AbstractConditionDescriptor descriptor) {
        AbstractCondition condition = descriptor.createCondition();
        Node<AbstractCondition> node = new Node<AbstractCondition>(condition);

        Node<AbstractCondition> parentNode = null;
        if (table.getSelectedRow() > -1) {
            parentNode = model.getNode(table.getSelectedRow());
            if (parentNode.getData().isGroup()) {
                parentNode.addChild(node);
            }
        }

        model.addNode(node);

        if (node.getData().isGroup()) {
            // Select the added node if it is a group
            int row = model.getRow(node);
            table.setRowSelectionInterval(row, row);
        } else if (parentNode != null) {
            // Otherwise select a parent node if it is a group
            int row = model.getRow(parentNode);
            table.setRowSelectionInterval(row, row);
        }

        table.revalidate();
        table.repaint();

        updateControls();

        AbstractOperationEditor editor = condition.getOperationEditor();
        if (editor == null)
            editor = condition.createOperationEditor();
        if (editor instanceof HasAction && descriptor.isShowImmediately()) {
            ((HasAction) editor).doAction(panel);
        }

        table.requestFocus();
    }

    public JPanel getPanel() {
        return panel;
    }

    protected void deleteCondition(AbstractCondition condition) {
        model.removeCondition(condition);
        updateControls();
    }

    protected void updateControls() {
        if (filterEntity.getFolder() != null || filterEntity.getCode() == null)
            saveBtn.setEnabled(!conditions.getRootNodes().isEmpty());
        else
            saveBtn.setEnabled(false);
        defaultCb.setVisible(filterEntity.getFolder() == null);
        defaultCb.setSelected(isTrue(filterEntity.getIsDefault()));
        updateApplyDefaultCb();
        applyDefaultCb.setVisible(defaultCb.isVisible() && manualApplyRequired);
        applyDefaultCb.setSelected(BooleanUtils.isTrue(filterEntity.getApplyDefault()));
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
    protected AbstractFilterParser createFilterParser(ConditionsTree conditions, String messagesPack,
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
        DesktopComponentsHelper.getTopLevelFrame(this.getPanel()).getWindowManager().showNotification
                (caption, description, IFrame.NotificationType.HUMANIZED);
    }

    protected abstract class AbstractCell implements TableCellEditor, TableCellRenderer {

        protected EventListenerList listenerList = new EventListenerList();
        protected ChangeEvent changeEvent;

        protected Map<Object, JComponent> components = new HashMap<Object, JComponent>();
        protected Map<Object, JPanel> wrappers = new HashMap<Object, JPanel>();

        protected JComponent activeComponent;

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
            if (activeComponent != null) {
                // post specific event for handle focus lost
                FocusEvent focusEvent = new FocusEvent(activeComponent, FocusEvent.FOCUS_LOST, false, table);
                activeComponent.dispatchEvent(focusEvent);
            }
            return "";
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
            activeComponent = wrapper;
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
            AbstractOperationEditor editor = ((AbstractCondition<Param>) value).getOperationEditor();
            if (editor == null) {
                editor = ((AbstractCondition<Param>) value).createOperationEditor();
            }
            JComponent component = (JComponent) editor.getImpl();
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
            final JButton delBtn = new JButton(App.getInstance().getResources().getIcon("icons/item-remove.png"));
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
                        wrappers.remove(condition);
                    }
                }
            });
            return delBtn;
        }
    }

    protected class ConditionsTable extends JXTable {
        public boolean isCellSelected(int row, int column) {
            return (row == getSelectedRow());
        }
    }

    protected class AddConditionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddConditionDlg dlg = new AddConditionDlg(metaClass,
                    descriptors,
                    new AddConditionDlg.DescriptorBuilder(messagesPack, filterComponentName, datasource),
                    new SelectionHandler() {
                        @Override
                        public void select(AbstractConditionDescriptor descriptor) {
                            addCondition(descriptor);
                        }
                    },
                    getPanel());
            DesktopComponentsHelper.getTopLevelFrame(getPanel()).deactivate(null);
            dlg.setVisible(true);
        }
    }
}
