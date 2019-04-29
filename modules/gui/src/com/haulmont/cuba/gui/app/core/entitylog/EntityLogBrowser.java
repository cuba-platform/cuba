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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.EntityLogService;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.HasUuid;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ReferenceToEntitySupport;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.app.core.entityinspector.EntityInspectorBrowse;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class EntityLogBrowser extends AbstractWindow {

    public interface Companion {
        void enableTextSelection(Table table);
    }

    protected static final String SELECT_ALL_CHECK_BOX = "selectAllCheckBox";

    @Inject
    protected Metadata metadata;

    @Inject
    protected EntityLogService logService;

    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Inject
    protected DynamicAttributes dynamicAttributes;

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
    protected LookupField<String> entityNameField;

    @Inject
    protected LookupField<String> filterEntityNameField;

    @Inject
    protected LookupField changeTypeField;

    @Inject
    protected CheckBox autoCheckBox;

    @Inject
    protected CheckBox manualCheckBox;

    @Named(SELECT_ALL_CHECK_BOX)
    protected CheckBox selectAllCheckBox;

    @Inject
    protected Table<LoggedEntity> loggedEntityTable;

    @Inject
    protected Table<EntityLogItem> entityLogTable;

    @Inject
    protected Table<EntityLogAttr> entityLogAttrTable;

    @Inject
    protected ScrollBoxLayout attributesBoxScroll;

    @Inject
    protected BoxLayout actionsPaneLayout;

    @Inject
    protected DateField fromDateField;

    @Inject
    protected DateField tillDateField;

    @Inject
    protected PickerField<Entity> instancePicker;

    @Inject
    protected LookupField userField;

    @Inject
    protected WindowConfig config;

    @Inject
    protected ThemeConstants themeConstants;

    protected TreeMap<String, String> entityMetaClassesMap;

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
        Map<String, Object> changeTypeMap = new LinkedHashMap<>();
        changeTypeMap.put(messages.getMessage(getClass(), "createField"), "C");
        changeTypeMap.put(messages.getMessage(getClass(), "modifyField"), "M");
        changeTypeMap.put(messages.getMessage(getClass(), "deleteField"), "D");
        changeTypeMap.put(messages.getMessage(getClass(), "restoreField"), "R");

        entityMetaClassesMap = getEntityMetaClasses();
        entityNameField.setOptionsMap(entityMetaClassesMap);
        changeTypeField.setOptionsMap(changeTypeMap);
        filterEntityNameField.setOptionsMap(entityMetaClassesMap);

        Action clearAction = instancePicker.getAction("clear");
        instancePicker.removeAction(clearAction);
        instancePicker.removeAction("lookup");

        addAction(new SaveAction());
        addAction(new CancelAction());
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
                    Lookup.Handler lookupWindowHandler = items -> {
                        if (!items.isEmpty()) {
                            Object item = items.iterator().next();
                            pickerField.setValue((Entity) item);
                            afterSelect(items);
                        }
                    };

                    if (config.hasWindow(currentWindowAlias)) {
                        lookupWindow.openLookup(
                                currentWindowAlias,
                                lookupWindowHandler,
                                lookupScreenOpenType,
                                lookupScreenParams != null ? lookupScreenParams : Collections.emptyMap()
                        );
                    } else {
                        lookupWindow.openLookup(EntityInspectorBrowse.SCREEN_NAME,
                                lookupWindowHandler,
                                WindowManager.OpenType.THIS_TAB,
                                ParamsMap.of("entity", metaClass.getName())
                        );
                    }

                    lookupWindow.addCloseListener(actionId ->
                            pickerField.focus());
                }
            }
        };

        instancePicker.addAction(lookupAction);
        instancePicker.addAction(clearAction);

        entityNameField.addValueChangeListener(e -> {
            if (entityNameField.isEditable())
                fillAttributes(e.getValue(), null, true);
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
                MetaClass metaClass = metadata.getSession().getClassNN(e.getValue());
                instancePicker.setMetaClass(metaClass);
            } else {
                instancePicker.setEnabled(false);
            }
            instancePicker.setValue(null);
        });
        selectAllCheckBox.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                enableAllCheckBoxes(e.getValue());
            }
        });

        entityLogTable.addGeneratedColumn("entityId", entity -> {
            if (entity.getObjectEntityId() != null) {
                return new Table.PlainTextCell(entity.getObjectEntityId().toString());
            }
            return null;
        }, Table.PlainTextCell.class);
    }

    public TreeMap<String, String> getEntityMetaClasses() {
        TreeMap<String, String> options = new TreeMap<>();
        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (metadata.getExtendedEntities().getExtendedClass(metaClass) == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
                String originalName = originalMetaClass.getName();
                Class javaClass = originalMetaClass.getJavaClass();
                if (metadata.getTools().hasCompositePrimaryKey(metaClass) && !HasUuid.class.isAssignableFrom(javaClass)) {
                    continue;
                }
                String caption = messages.getMessage(javaClass, javaClass.getSimpleName()) + " (" + originalName + ")";
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

    protected void fillAttributes(String metaClassName, LoggedEntity item, boolean editable) {
        clearAttributes();
        setSelectAllCheckBox(false);

        if (metaClassName != null) {
            MetaClass metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(
                    metadata.getClassNN(metaClassName));
            List<MetaProperty> metaProperties = new ArrayList<>(metaClass.getProperties());
            selectAllCheckBox.setEditable(editable);
            Set<LoggedAttribute> enabledAttr = null;
            if (item != null)
                enabledAttr = item.getAttributes();
            for (MetaProperty property : metaProperties) {
                if (allowLogProperty(property, null)) {
                    if (metadata.getTools().isEmbedded(property)) {
                        MetaClass embeddedMetaClass = property.getRange().asClass();
                        for (MetaProperty embeddedProperty : embeddedMetaClass.getProperties()) {
                            if (allowLogProperty(embeddedProperty, null)) {
                                addAttribute(enabledAttr,
                                        String.format("%s.%s", property.getName(), embeddedProperty.getName()), editable);
                            }
                        }
                    } else {
                        addAttribute(enabledAttr, property.getName(), editable);
                    }
                }
            }
            Collection<CategoryAttribute> attributes = dynamicAttributes.getAttributesForMetaClass(metaClass);
            if (attributes != null) {
                for (CategoryAttribute categoryAttribute : attributes) {
                    MetaPropertyPath propertyPath = DynamicAttributesUtils.getMetaPropertyPath(metaClass, categoryAttribute);
                    MetaProperty property = propertyPath.getMetaProperty();
                    if (allowLogProperty(property, categoryAttribute)) {
                        addAttribute(enabledAttr, property.getName(), editable);
                    }
                }
            }
        }
    }

    protected void addAttribute(Set<LoggedAttribute> enabledAttributes, String name, boolean editable) {
        CheckBox checkBox = factory.createComponent(CheckBox.class);
        if (enabledAttributes != null && isEntityHaveAttribute(name, enabledAttributes)) {
            checkBox.setValue(true);
        }
        checkBox.setId(name);
        checkBox.setCaption(name);
        checkBox.setEditable(editable);
        checkBox.addValueChangeListener(e -> checkAllCheckboxes());

        attributesBoxScroll.add(checkBox);
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
                    if (!checkBox.getValue()) {
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
        tillDateField.setValue(DateUtils.addDays(date, 1));
    }

    public void clearEntityLogTable() {
        userField.setValue(null);
        filterEntityNameField.setValue(null);
        changeTypeField.setValue(null);
        instancePicker.setValue(null);
        fromDateField.setValue(null);
        tillDateField.setValue(null);
    }

    public void search() {
        Entity entity = instancePicker.getValue();
        Map<String, Object> params = new HashMap<>();
        if (entity != null) {
            Object entityId = referenceToEntitySupport.getReferenceId(entity);
            if (entityId instanceof UUID) {
                params.put("entityId", entityId);
            } else if (entityId instanceof String) {
                params.put("stringEntityId", entityId);
            } else if (entityId instanceof Integer) {
                params.put("intEntityId", entityId);
            } else if (entityId instanceof Long) {
                params.put("longEntityId", entityId);
            }
        }
        if (entityLogDs instanceof CollectionDatasource.SupportsPaging) {
            ((CollectionDatasource.SupportsPaging) entityLogDs).setFirstResult(0);
        }
        entityLogDs.refresh(params);
    }

    public void clearAttributes() {
        for (Component c : attributesBoxScroll.getComponents())
            if (!c.getId().equals(SELECT_ALL_CHECK_BOX))
                attributesBoxScroll.remove(c);
    }

    public boolean isEntityHaveAttribute(String propertyName, Set<LoggedAttribute> enabledAttr) {
        if (enabledAttr != null && !systemAttrsList.contains(propertyName)) {
            for (LoggedAttribute logAttr : enabledAttr)
                if (logAttr.getName().equals(propertyName))
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
        entityNameField.focus();
    }

    public void reloadConfiguration() {
        logService.invalidateCache();
        showNotification(getMessage("changesApplied"), NotificationType.HUMANIZED);
    }

    public void modify() {
        enableControls();

        loggedEntityTable.setEnabled(false);
        cancelBtn.focus();
    }

    protected boolean allowLogProperty(MetaProperty metaProperty, CategoryAttribute categoryAttribute) {
        if (systemAttrsList.contains(metaProperty.getName())) {
            return false;
        }
        Range range = metaProperty.getRange();
        if (range.isClass() && metadata.getTools().hasCompositePrimaryKey(range.asClass()) &&
                !HasUuid.class.isAssignableFrom(range.asClass().getJavaClass())) {
            return false;
        }
        if (range.isClass() && range.getCardinality().isMany()) {
            return false;
        }
        if (categoryAttribute != null &&
                BooleanUtils.isTrue(categoryAttribute.getIsCollection())) {
            return false;
        }
        return true;
    }

    protected class SaveAction extends AbstractAction {

        public SaveAction() {
            super("save");
        }

        @Override
        public void actionPerform(Component component) {
            LoggedEntity selectedEntity = loggedEntityTable.getSelected().iterator().next();
            Set<LoggedAttribute> enabledAttributes = selectedEntity.getAttributes();
            for (Component c : attributesBoxScroll.getComponents()) {
                CheckBox currentCheckBox = (CheckBox) c;
                if (currentCheckBox.getId().equals(SELECT_ALL_CHECK_BOX))
                    continue;
                Boolean currentCheckBoxValue = currentCheckBox.getValue();
                if (currentCheckBoxValue && !isEntityHaveAttribute(currentCheckBox.getId(), enabledAttributes)) {   //add attribute if checked and not exist in table
                    LoggedAttribute newLoggedAttribute = metadata.create(LoggedAttribute.class);
                    newLoggedAttribute.setName(currentCheckBox.getId());
                    newLoggedAttribute.setEntity(selectedEntity);
                    loggedAttrDs.addItem(newLoggedAttribute);
                }
                if (!currentCheckBoxValue && isEntityHaveAttribute(currentCheckBox.getId(), enabledAttributes)) {  //remove attribute if unchecked and exist in table
                    LoggedAttribute removeAtr = getLoggedAttribute(currentCheckBox.getId(), enabledAttributes);
                    if (removeAtr != null)
                        loggedAttrDs.removeItem(removeAtr);
                }
            }
            getDsContext().commit();

            loggedEntityDs.refresh();
            disableControls();
            loggedEntityTable.setEnabled(true);
            loggedEntityTable.focus();

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
            loggedEntityTable.focus();
        }
    }
}