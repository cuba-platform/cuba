/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.entitylog;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.EntityLogService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.EntityLogItem;
import com.haulmont.cuba.security.entity.LoggedAttribute;
import com.haulmont.cuba.security.entity.LoggedEntity;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.time.DateUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * @author hasanov
 * @version $Id$
 */
public class EntityLogBrowser extends AbstractWindow {

    private static final String SELECT_ALL_CHECK_BOX = "selectAllCheckBox";

    private static final int DEFAULT_SHOW_ROWS = 50;

    @Inject
    protected Metadata metadata;

    @Inject
    protected EntityLogService logService;

    @Inject
    protected CollectionDatasource<EntityLogItem, UUID> entityLogDs;

    @Inject
    protected CollectionDatasource<LoggedEntity, UUID> loggedEntityDs;

    @Inject
    protected CollectionDatasource<LoggedAttribute, UUID> loggedAttrDs;

    @Inject
    protected CollectionDatasource<User, UUID> usersDs;

    @Inject
    protected ComponentsFactory factory;

    @Inject
    protected LookupField entityNameField;

    @Inject
    protected LookupField filterEntityNameField;

    @Inject
    protected LookupField changeTypeField;

    @Inject
    protected CheckBox autoCheckBox;

    @Inject
    protected CheckBox manualCheckBox;

    @Named(SELECT_ALL_CHECK_BOX)
    protected CheckBox selectAllCheckBox;

    @Inject
    protected Table loggedEntityTable;

    @Inject
    protected Table entityLogTable;

    @Inject
    protected GroupBoxLayout attributesBox;

    @Inject
    protected BoxLayout actionsPaneLayout;

    @Inject
    protected DateField fromDateField;

    @Inject
    protected DateField tillDateField;

    @Inject
    protected PickerField instancePicker;

    @Inject
    protected LookupField userField;

    @Inject
    protected WindowConfig config;

    private TextField showRowField;

    private TreeMap<String, Object> entityMetaClassesMap;

    private List<String> systemAttrsList;

    private boolean canSelectAllCheckboxGenerateEvents = true; // allow or not selectAllCheckBox to change values of other checkboxes

    @Override
    public void init(Map<String, Object> params) {
        WindowParams.DISABLE_AUTO_REFRESH.set(params, true);

        usersDs.refresh();
        loggedEntityDs.refresh();

        systemAttrsList = Arrays.asList("createTs", "createdBy", "updateTs", "updatedBy", "deleteTs", "deletedBy", "version", "id");
        Map<String, Object> changeTypeMap = new TreeMap<>();
        changeTypeMap.put(messages.getMessage(getClass(),"createField"), "C");
        changeTypeMap.put(messages.getMessage(getClass(),"modifyField"),"M");
        changeTypeMap.put(messages.getMessage(getClass(),"deleteField"), "D");
        entityMetaClassesMap = getEntityMetaClasses();
        entityNameField.setOptionsMap(entityMetaClassesMap);
        changeTypeField.setOptionsMap(changeTypeMap);
        filterEntityNameField.setOptionsMap(entityMetaClassesMap);
        Action clearAction = instancePicker.getAction("clear");
        instancePicker.removeAction(clearAction);
        instancePicker.removeAction(instancePicker.getAction("lookup"));
        addAction(new SaveAction());
        addAction(new CancelAction());
        Label label1 = factory.createComponent(Label.NAME);
        label1.setValue(messages.getMessage(getClass(),"show"));
        Label label2 = factory.createComponent(Label.NAME);
        label2.setValue(messages.getMessage(getClass(),"rows"));
        ButtonsPanel panel = entityLogTable.getButtonsPanel();
        showRowField = factory.createComponent(TextField.NAME);
        showRowField.setWidth("40px");
        showRowField.setValue(String.valueOf(DEFAULT_SHOW_ROWS));
        panel.add(label1);
        panel.add(showRowField);
        panel.add(label2);
        disableControls();
        setDateFieldTime();

        instancePicker.setEnabled(false);
        PickerField.LookupAction lookupAction = new PickerField.LookupAction(instancePicker) {
            @Override
            public void actionPerform(Component component) {
                final MetaClass metaClass = pickerField.getMetaClass();
                boolean openLookupWindowError = false;
                if (pickerField.isEditable()) {
                    String currentWindowAlias = lookupScreen;
                    if (currentWindowAlias == null) {
                        if (metaClass == null)
                            throw new IllegalStateException("Please specify metaclass or property for PickerField");
                        currentWindowAlias = metaClass.getName() + ".lookup";
                    }
                    try {
                        config.getWindowInfo(currentWindowAlias);
                    } catch (NoSuchScreenException ex1) {
                        openLookupWindowError = true;
                    } finally {
                        Window lookupWindow = pickerField.getFrame();
                        if (!openLookupWindowError) {
                            lookupWindow.openLookup(
                                    currentWindowAlias,
                                    new Window.Lookup.Handler() {
                                        @Override
                                        public void handleLookup(Collection items) {
                                            if (!items.isEmpty()) {
                                                Object item = items.iterator().next();
                                                pickerField.setValue(item);
                                                afterSelect(items);
                                            }
                                        }
                                    },
                                    lookupScreenOpenType,
                                    lookupScreenParams != null ? lookupScreenParams : Collections.<String, Object>emptyMap()
                            );
                        } else {
                            TreeMap<String, Object> treeMap = new TreeMap<>();
                            treeMap.put("entity", metaClass.getName());
                            lookupWindow.openLookup("entityInspector.browse",
                                    new Window.Lookup.Handler() {
                                        @Override
                                        public void handleLookup(Collection items) {
                                            if (!items.isEmpty()) {
                                                Object item = items.iterator().next();
                                                pickerField.setValue(item);
                                                afterSelect(items);
                                            }
                                        }
                                    },
                                    WindowManager.OpenType.THIS_TAB,
                                    treeMap
                            );
                        }
                    }
                }
            }

        };

        instancePicker.addAction(lookupAction);
        instancePicker.addAction(clearAction);
        entityNameField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (entityNameField.isEditable())
                    fillAttributes((String) value, null, true);
            }
        });
        loggedEntityDs.addListener(new CollectionDsListenerAdapter<LoggedEntity>() {
            @Override
            public void itemChanged(Datasource<LoggedEntity> ds, LoggedEntity prevItem, LoggedEntity item) {
                if (item != null) {
                    fillAttributes(item.getName(), item, false);
                    checkAllCheckboxes();
                }
            }
        });
        filterEntityNameField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                instancePicker.setValue(null);
                if (value != null) {
                    instancePicker.setEnabled(true);
                    MetaClass metaClass = metadata.getSession().getClassNN(value.toString());
                    instancePicker.setMetaClass(metaClass);
                } else
                    instancePicker.setEnabled(false);
            }
        });
    }

    public TreeMap<String, Object> getEntityMetaClasses() {
        TreeMap<String, Object> options = new TreeMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                String originalName = originalMetaClass == null ? metaClass.getName() : originalMetaClass.getName();
                options.put(metaClass.getName(), originalName);
            }
        }
        return options;
    }

    private void enableControls() {
        loggedEntityTable.setEnabled(false);
        entityNameField.setEditable(false);
        autoCheckBox.setEditable(true);
        manualCheckBox.setEditable(true);
        for (Component c : attributesBox.getComponents())
            ((CheckBox) c).setEditable(true);
        actionsPaneLayout.setVisible(true);
    }

    private void disableControls() {
        entityNameField.setEditable(false);
        loggedEntityTable.setEnabled(true);
        autoCheckBox.setEditable(false);
        manualCheckBox.setEditable(false);
        for (Component c : attributesBox.getComponents())
            ((CheckBox) c).setEditable(false);
        actionsPaneLayout.setVisible(false);
    }

    private void fillAttributes(String metaClassName, LoggedEntity item, boolean setEditableCheckboxes) {
        clearAttributes();
        if (metaClassName != null) {
            MetaClass metaClass = metadata.getClassNN(metaClassName);
            Collection<MetaProperty> metaProperties = metaClass.getProperties();
            selectAllCheckBox.setEditable(setEditableCheckboxes);
            Set<LoggedAttribute> enabledAttr = null;
            if (item != null)
                enabledAttr = item.getAttributes();
            for (MetaProperty property : metaProperties) {
                if (!systemAttrsList.contains(property.getName())) {
                    CheckBox checkBox = factory.createComponent(CheckBox.NAME);
                    if ((enabledAttr != null) && isEntityHaveAtrribute(property.getName(), enabledAttr))
                        checkBox.setValue(true);
                    checkBox.setId(property.getName());
                    checkBox.setCaption(property.getName());
                    checkBox.setEditable(setEditableCheckboxes);
                    checkBox.addListener(new ValueListener() {
                        @Override
                        public void valueChanged(Object source, String property, Object prevValue, Object value) {
                            checkAllCheckboxes();
                        }
                    });
                    attributesBox.add(checkBox);
                }
            }
            selectAllCheckBox.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, Object prevValue, Object value) {
                    enableAllCheckBoxes((boolean) value);
                }
            });
        }
    }

    private void enableAllCheckBoxes(boolean b) {
        if (canSelectAllCheckboxGenerateEvents) {
            for (Component box : attributesBox.getComponents())
                ((CheckBox) box).setValue(b);
        }
    }

    private void checkAllCheckboxes() {
        CheckBox selectAllCheckBox = attributesBox.getOwnComponent(SELECT_ALL_CHECK_BOX);
        if (selectAllCheckBox != null) {
            for (Component c : attributesBox.getComponents()) {
                if (!c.equals(selectAllCheckBox)) {
                    if (((CheckBox) c).getValue() == false) {
                        setSelectAllCheckBox(false);
                        return;
                    }
                }
            }
            if (attributesBox.getComponents().size() != 1)
                setSelectAllCheckBox(true);
        }
    }

    public void setSelectAllCheckBox(boolean value) {
        canSelectAllCheckboxGenerateEvents = false;
        boolean isEditable = selectAllCheckBox.isEditable();
        try {
            selectAllCheckBox.setEditable(true);
            selectAllCheckBox.setValue(value);
        } finally {
            canSelectAllCheckboxGenerateEvents = true;
            selectAllCheckBox.setEditable(isEditable);
        }
    }

    public void setDateFieldTime() {
        Date date = AppBeans.get(TimeSource.class).currentTimestamp();
        fromDateField.setValue(DateUtils.addDays(date, -1));
        tillDateField.setValue(DateUtils.addMinutes(date, 1));
    }

    public void clearEntityLogTable() {
        userField.setValue(null);
        filterEntityNameField.setValue(null);
        changeTypeField.setValue(null);
        instancePicker.setValue(null);
        fromDateField.setValue(null);
        tillDateField.setValue(null);
        showRowField.setValue(String.valueOf(DEFAULT_SHOW_ROWS));
    }

    public void search() {
        int maxRows;
        try {
            maxRows = Integer.parseInt(showRowField.getValue().toString());
            if (maxRows >= 0)
                entityLogDs.setMaxResults(maxRows);
            else
                throw new NumberFormatException();
        }
        catch (Exception e){
            showNotification(messages.getMessage(getClass(),"invalidNumber"),NotificationType.HUMANIZED);
            return;
        }

        entityLogDs.refresh();
        entityLogTable.refresh();
    }

    public void clearAttributes() {
        for (Component c : attributesBox.getComponents())
            if (!c.getId().equals(SELECT_ALL_CHECK_BOX))
                attributesBox.remove(c);
    }

    public boolean isEntityHaveAtrribute(String metaPropertyName, Set<LoggedAttribute> enabledAttr) {
        if ((enabledAttr != null) && !systemAttrsList.contains(metaPropertyName)) {
            for (LoggedAttribute logAtrr : enabledAttr)
                if (logAtrr.getName().equals(metaPropertyName))
                    return true;
        }
        return false;
    }

    public LoggedAttribute getLoggedAttribute(String name, Set<LoggedAttribute> enabledAttr) {
        for (LoggedAttribute atr : enabledAttr)
            if (atr.getName().equals(name))
                return atr;
        return null;
    }

    public void create() {
        LoggedEntity entity = new LoggedEntity();
        entity.setAuto(false);
        entity.setManual(false);
        setSelectAllCheckBox(false);
        loggedEntityDs.addItem(entity);
        loggedEntityTable.setSelected(entity);
        enableControls();
        entityNameField.setEditable(true);
    }

    public void reloadConfiguration() {
        logService.invalidateCache();
    }

    public void modify() {
        enableControls();
        loggedEntityTable.setEnabled(false);
    }

    private class SaveAction extends AbstractAction {

        protected SaveAction() {
            super("save");
        }

        public void actionPerform(Component component) {
            LoggedEntity selectedEntity = (LoggedEntity) loggedEntityTable.getSelected().iterator().next();
            Set<LoggedAttribute> enabledAttributes = selectedEntity.getAttributes();
            for (Component c : attributesBox.getComponents()) {
                CheckBox currentCheckBox = (CheckBox) c;
                if (currentCheckBox.getId().equals(SELECT_ALL_CHECK_BOX))
                    continue;
                Boolean currentCheckBoxValue = currentCheckBox.getValue();
                if (currentCheckBoxValue && !isEntityHaveAtrribute(currentCheckBox.getId(), enabledAttributes)) {   //add attribute if checked and not exist in table
                    LoggedAttribute newLoggedAttribute = new LoggedAttribute();
                    newLoggedAttribute.setName(currentCheckBox.getId());
                    newLoggedAttribute.setEntity(selectedEntity);
                    loggedAttrDs.addItem(newLoggedAttribute);
                }
                if (!currentCheckBoxValue && isEntityHaveAtrribute(currentCheckBox.getId(), enabledAttributes)) {  //remove attribute if unchecked and exist in table
                    LoggedAttribute removeAtr = getLoggedAttribute(currentCheckBox.getId(), enabledAttributes);
                    if (removeAtr != null)
                        loggedAttrDs.removeItem(removeAtr);
                }
            }
            if (loggedAttrDs.isModified())
                loggedAttrDs.commit();
            else
                loggedEntityDs.commit();
            loggedEntityDs.refresh();
            disableControls();
            loggedEntityTable.setEnabled(true);
        }
    }

    private class CancelAction extends AbstractAction {

        protected CancelAction() {
            super("cancel");
        }

        public void actionPerform(Component component) {
            loggedEntityDs.refresh();
            disableControls();
            loggedEntityTable.setEnabled(true);
        }
    }

}
