/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.core.entitylog;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.EntityLogService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.app.core.entityinspector.EntityInspectorBrowse;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.EntityLogItem;
import com.haulmont.cuba.security.entity.LoggedAttribute;
import com.haulmont.cuba.security.entity.LoggedEntity;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.time.DateUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class EntityLogBrowser extends AbstractWindow {

    public interface Companion {
        void enableTextSelection(Table table);
    }

    protected static final String SELECT_ALL_CHECK_BOX = "selectAllCheckBox";

    protected static final int DEFAULT_SHOW_ROWS = 50;

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
    protected Table entityLogAttrTable;

    @Inject
    protected ScrollBoxLayout attributesBoxScroll;

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

    @Inject
    protected ThemeConstants themeConstants;

    protected TextField showRowField;

    protected TreeMap<String, Object> entityMetaClassesMap;

    protected List<String> systemAttrsList;

    // allow or not selectAllCheckBox to change values of other checkboxes
    protected boolean canSelectAllCheckboxGenerateEvents = true;

    @Inject
    protected Button cancelBtn;

    @Override
    public void init(Map<String, Object> params) {
        WindowParams.DISABLE_AUTO_REFRESH.set(params, true);

        usersDs.refresh();
        loggedEntityDs.refresh();

        Companion companion = getCompanion();
        if (companion != null) {
            companion.enableTextSelection(entityLogTable);
            companion.enableTextSelection(entityLogAttrTable);
        }

        systemAttrsList = Arrays.asList("createTs", "createdBy", "updateTs", "updatedBy", "deleteTs", "deletedBy", "version", "id");
        Map<String, Object> changeTypeMap = new TreeMap<>();
        changeTypeMap.put(messages.getMessage(getClass(),"createField"), "C");
        changeTypeMap.put(messages.getMessage(getClass(),"modifyField"),"M");
        changeTypeMap.put(messages.getMessage(getClass(), "deleteField"), "D");

        entityMetaClassesMap = getEntityMetaClasses();
        entityNameField.setOptionsMap(entityMetaClassesMap);
        changeTypeField.setOptionsMap(changeTypeMap);
        filterEntityNameField.setOptionsMap(entityMetaClassesMap);

        Action clearAction = instancePicker.getAction("clear");
        instancePicker.removeAction(clearAction);
        instancePicker.removeAction("lookup");

        addAction(new SaveAction());
        addAction(new CancelAction());
        Label label1 = factory.createComponent(Label.class);
        label1.setValue(messages.getMessage(getClass(),"show"));
        label1.setAlignment(Alignment.MIDDLE_LEFT);
        Label label2 = factory.createComponent(Label.class);
        label2.setValue(messages.getMessage(getClass(),"rows"));
        label2.setAlignment(Alignment.MIDDLE_LEFT);
        ButtonsPanel panel = entityLogTable.getButtonsPanel();
        showRowField = factory.createComponent(TextField.class);
        showRowField.setWidth(themeConstants.get("cuba.gui.EntityLogBrowser.showRowField.width"));
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
                if (pickerField.isEditable()) {
                    String currentWindowAlias = lookupScreen;
                    if (currentWindowAlias == null) {
                        if (metaClass == null) {
                            throw new IllegalStateException("Please specify metaclass or property for PickerField");
                        }
                        currentWindowAlias = windowConfig.getLookupScreenId(metaClass);
                    }

                    Window lookupWindow = (Window) pickerField.getFrame();
                    Lookup.Handler lookupWindowHandler = new Lookup.Handler() {
                        @Override
                        public void handleLookup(Collection items) {
                            if (!items.isEmpty()) {
                                Object item = items.iterator().next();
                                pickerField.setValue(item);
                                afterSelect(items);
                            }
                        }
                    };

                    if (config.hasWindow(currentWindowAlias)) {
                        lookupWindow.openLookup(
                                currentWindowAlias,
                                lookupWindowHandler,
                                lookupScreenOpenType,
                                lookupScreenParams != null ? lookupScreenParams : Collections.<String, Object>emptyMap()
                        );
                    } else {
                        lookupWindow.openLookup(EntityInspectorBrowse.SCREEN_NAME,
                                lookupWindowHandler,
                                WindowManager.OpenType.THIS_TAB,
                                ParamsMap.of("entity", metaClass.getName())
                        );
                    }

                    lookupWindow.addCloseListener(actionId -> {
                        pickerField.requestFocus();
                    });
                }
            }
        };

        instancePicker.addAction(lookupAction);
        instancePicker.addAction(clearAction);

        entityNameField.addValueChangeListener(e -> {
            if (entityNameField.isEditable())
                fillAttributes((String) e.getValue(), null, true);
        });

        loggedEntityDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                fillAttributes(e.getItem().getName(), e.getItem(), false);
                checkAllCheckboxes();
            } else {
                setSelectAllCheckBox(false);
                clearAttributes();
            }
        });

        filterEntityNameField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                instancePicker.setEnabled(true);
                MetaClass metaClass = metadata.getSession().getClassNN(e.getValue().toString());
                instancePicker.setMetaClass(metaClass);
            } else {
                instancePicker.setEnabled(false);
            }
            instancePicker.setValue(null);
        });
        selectAllCheckBox.addValueChangeListener(e -> enableAllCheckBoxes((boolean) e.getValue()));
    }

    public TreeMap<String, Object> getEntityMetaClasses() {
        TreeMap<String, Object> options = new TreeMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                String originalName = originalMetaClass == null ? metaClass.getName() : originalMetaClass.getName();
                Class javaClass = metaClass.getJavaClass();
                String caption = messages.getMessage(javaClass, javaClass.getSimpleName()) + " (" + metaClass.getName() + ")";
                options.put(caption, originalName);
            }
        }
        return options;
    }

    protected void enableControls() {
        loggedEntityTable.setEnabled(false);
        entityNameField.setEditable(false);
        autoCheckBox.setEditable(true);
        manualCheckBox.setEditable(true);
        for (Component c : attributesBoxScroll.getComponents())
            ((CheckBox) c).setEditable(true);
        actionsPaneLayout.setVisible(true);
    }

    protected void disableControls() {
        entityNameField.setEditable(false);
        loggedEntityTable.setEnabled(true);
        autoCheckBox.setEditable(false);
        manualCheckBox.setEditable(false);
        for (Component c : attributesBoxScroll.getComponents())
            ((CheckBox) c).setEditable(false);
        actionsPaneLayout.setVisible(false);
    }

    protected void fillAttributes(String metaClassName, LoggedEntity item, boolean setEditableCheckboxes) {
        clearAttributes();
        setSelectAllCheckBox(false);

        if (metaClassName != null) {
            MetaClass metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(
                    metadata.getClassNN(metaClassName));
            Collection<MetaProperty> metaProperties = metaClass.getProperties();
            selectAllCheckBox.setEditable(setEditableCheckboxes);
            Set<LoggedAttribute> enabledAttr = null;
            if (item != null)
                enabledAttr = item.getAttributes();
            for (MetaProperty property : metaProperties) {
                if (!systemAttrsList.contains(property.getName())) {
                    CheckBox checkBox = factory.createComponent(CheckBox.class);
                    if ((enabledAttr != null) && isEntityHaveAtrribute(property.getName(), enabledAttr))
                        checkBox.setValue(true);
                    checkBox.setId(property.getName());
                    checkBox.setCaption(property.getName());
                    checkBox.setEditable(setEditableCheckboxes);
                    checkBox.addValueChangeListener(e -> checkAllCheckboxes());

                    attributesBoxScroll.add(checkBox);
                }
            }
        }
    }

    protected void enableAllCheckBoxes(boolean b) {
        if (canSelectAllCheckboxGenerateEvents) {
            for (Component box : attributesBoxScroll.getComponents())
                ((CheckBox) box).setValue(b);
        }
    }

    protected void checkAllCheckboxes() {
        CheckBox selectAllCheckBox = (CheckBox) attributesBoxScroll.getOwnComponent(SELECT_ALL_CHECK_BOX);
        if (selectAllCheckBox != null) {
            for (Component c : attributesBoxScroll.getComponents()) {
                if (!c.equals(selectAllCheckBox)) {
                    CheckBox checkBox = (CheckBox) c;
                    if (!((boolean) checkBox.getValue())) {
                        setSelectAllCheckBox(false);
                        return;
                    }
                }
            }
            if (attributesBoxScroll.getComponents().size() != 1)
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
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        Date date = timeSource.currentTimestamp();
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
        } catch (Exception e){
            showNotification(messages.getMessage(getClass(),"invalidNumber"),NotificationType.HUMANIZED);
            return;
        }

        entityLogDs.refresh();
        entityLogTable.refresh();
    }

    public void clearAttributes() {
        for (Component c : attributesBoxScroll.getComponents())
            if (!c.getId().equals(SELECT_ALL_CHECK_BOX))
                attributesBoxScroll.remove(c);
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
        LoggedEntity entity = metadata.create(LoggedEntity.class);
        entity.setAuto(false);
        entity.setManual(false);
        setSelectAllCheckBox(false);
        loggedEntityDs.addItem(entity);
        loggedEntityTable.setEditable(true);
        loggedEntityTable.setSelected(entity);

        enableControls();

        entityNameField.setEditable(true);
        entityNameField.requestFocus();
    }

    public void reloadConfiguration() {
        logService.invalidateCache();
        showNotification(getMessage("changesApplied"), NotificationType.HUMANIZED);
    }

    public void modify() {
        enableControls();

        loggedEntityTable.setEnabled(false);
        cancelBtn.requestFocus();
    }

    protected class SaveAction extends AbstractAction {

        public SaveAction() {
            super("save");
        }

        @Override
        public void actionPerform(Component component) {
            LoggedEntity selectedEntity = (LoggedEntity) loggedEntityTable.getSelected().iterator().next();
            Set<LoggedAttribute> enabledAttributes = selectedEntity.getAttributes();
            for (Component c : attributesBoxScroll.getComponents()) {
                CheckBox currentCheckBox = (CheckBox) c;
                if (currentCheckBox.getId().equals(SELECT_ALL_CHECK_BOX))
                    continue;
                Boolean currentCheckBoxValue = currentCheckBox.getValue();
                if (currentCheckBoxValue && !isEntityHaveAtrribute(currentCheckBox.getId(), enabledAttributes)) {   //add attribute if checked and not exist in table
                    LoggedAttribute newLoggedAttribute = metadata.create(LoggedAttribute.class);
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
            getDsContext().commit();

            loggedEntityDs.refresh();
            disableControls();
            loggedEntityTable.setEnabled(true);
            loggedEntityTable.requestFocus();

            logService.invalidateCache();
        }
    }

    protected class CancelAction extends AbstractAction {

        public CancelAction() {
            super("cancel");
        }

        @Override
        public void actionPerform(Component component) {
            loggedEntityDs.refresh();
            disableControls();
            loggedEntityTable.setEnabled(true);
            loggedEntityTable.requestFocus();
        }
    }
}