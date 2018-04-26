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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.SecuredActionsHolder;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.converters.StringToEntityConverter;
import com.haulmont.cuba.web.widgets.CubaPickerField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.AbstractField;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

public class WebPickerField<V extends Entity> extends WebAbstractField<CubaPickerField, V>
        implements PickerField<V>, SecuredActionsHolder {

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected MetaClass metaClass;

    protected List<Action> actions = new ArrayList<>();

    protected WebPickerFieldActionHandler actionHandler;

    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    public WebPickerField() {
        component = new Picker(this);

        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        component.setCaptionFormatter(new StringToEntityConverter() {
            @Override
            public String convertToPresentation(Entity value, Class<? extends String> targetType, Locale locale)
                    throws ConversionException {
                if (captionMode == CaptionMode.PROPERTY
                        && StringUtils.isNotEmpty(captionProperty)
                        && (value != null)) {

                    Object propertyValue = value.getValue(captionProperty);

                    MetaClass metaClass = metadata.getClassNN(value.getClass());
                    MetaProperty property = metaClass.getProperty(captionProperty);
                    return metadata.getTools().format(propertyValue, property);
                }

                return super.convertToPresentation(value, targetType, locale);
            }
        });

        attachListener(component);

        initActionHandler();
    }

    public WebPickerField(CubaPickerField component) {
        this.component = component;
        attachListener(component);
        initActionHandler();
    }

    @Override
    public MetaClass getMetaClass() {
        final Datasource ds = getDatasource();
        if (ds != null) {
            return getMetaProperty().getRange().asClass();
        } else {
            return metaClass;
        }
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        final Datasource ds = getDatasource();
        if (ds != null) {
            throw new IllegalStateException("Datasource is not null");
        }
        this.metaClass = metaClass;
    }

    @Override
    public void setValue(V value) {
        if (value != null) {
            if (getDatasource() == null && metaClass == null) {
                throw new IllegalStateException("Datasource or metaclass must be set for field");
            }

            Class fieldClass = getMetaClass().getJavaClass();
            Class<?> valueClass = value.getClass();
            //noinspection unchecked
            if (!fieldClass.isAssignableFrom(valueClass)) {
                throw new IllegalArgumentException(
                        String.format("Could not set value with class %s to field with class %s",
                                fieldClass.getCanonicalName(),
                                valueClass.getCanonicalName())
                );
            }
        }

        super.setValue(value);
    }

    @Override
    public LookupAction addLookupAction() {
        LookupAction action = LookupAction.create(this);
        addAction(action);
        return action;
    }

    @Override
    public ClearAction addClearAction() {
        ClearAction action = ClearAction.create(this);
        addAction(action);
        return action;
    }

    @Override
    public PickerField.OpenAction addOpenAction() {
        OpenAction action = OpenAction.create(this);
        addAction(action);
        return action;
    }

    // todo remove
    protected MetaPropertyPath getResolvedMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                .resolveMetaPropertyPath(metaClass, property);
        Preconditions.checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        return metaPropertyPath;
    }

    public void checkDatasourceProperty(Datasource datasource, String property) {
        Preconditions.checkNotNullArgument(datasource);
        Preconditions.checkNotNullArgument(property);

        MetaPropertyPath metaPropertyPath = getResolvedMetaPropertyPath(datasource.getMetaClass(), property);
        if (!metaPropertyPath.getRange().isClass()) {
            throw new DevelopmentException(String.format("property '%s.%s' should have Entity type", datasource.getMetaClass().getName(), property));
        }
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    @Override
    public void addAction(Action action) {
        int index = findActionById(actions, action.getId());
        if (index < 0) {
            index = actions.size();
        }

        addAction(action, index);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        int oldIndex = findActionById(actions, action.getId());
        if (oldIndex >= 0) {
            removeAction(actions.get(oldIndex));
            if (index > oldIndex) {
                index--;
            }
        }

        actions.add(index, action);
        actionHandler.addAction(action, index);

        PickerButton pButton = new PickerButton();
        pButton.setAction(action);
        // no captions for picker buttons
        Button vButton = (Button) pButton.getComponent();
        vButton.setCaption("");

        component.addButton(vButton, index);

        // apply Editable after action owner is set
        if (action instanceof StandardAction) {
            ((StandardAction) action).setEditable(isEditable());
        }

        if (StringUtils.isNotEmpty(getDebugId())) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            pButton.setDebugId(testIdManager.getTestId(getDebugId() + "_" + action.getId()));
        }

        actionsPermissions.apply(action);
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null) {
            String debugId = getDebugId();

            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            for (Action action : actions) {
                if (action.getOwner() != null && action.getOwner() instanceof WebButton) {
                    WebButton button = (WebButton) action.getOwner();
                    if (StringUtils.isEmpty(button.getDebugId())) {
                        button.setDebugId(testIdManager.getTestId(debugId + "_" + action.getId()));
                    }
                }
            }
        }
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (actions.remove(action)) {
            actionHandler.removeAction(action);
            //noinspection ConstantConditions
            if (action.getOwner() != null && action.getOwner() instanceof WebButton) {
                WebButton vButton = (WebButton) action.getOwner();
                vButton.setAction(null);
                Button button = (Button) vButton.getComponent();
                component.removeButton(button);
            }
        }
    }

    @Override
    public void removeAction(@Nullable String id) {
        Action action = getAction(id);
        if (action != null) {
            removeAction(action);
        }
    }

    @Override
    public void removeAllActions() {
        for (Action action : new ArrayList<>(actions)) {
            removeAction(action);
        }
    }

    @Override
    public void addFieldListener(FieldListener listener) {
        component.addFieldListener(listener::actionPerformed);
    }

    @Override
    public void setFieldEditable(boolean editable) {
        component.setFieldReadOnly(!editable);
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        for (Action action : actions) {
            if (Objects.equals(id, action.getId())) {
                return action;
            }
        }
        return null;
    }

    protected void initActionHandler() {
        actionHandler = new WebPickerFieldActionHandler(this);
        component.addActionHandler(actionHandler);
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        return Collections.singleton(getValue());
    }

    @Override
    public int getTabIndex() {
        return ((Component.Focusable) component.getField()).getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        ((Component.Focusable) component.getField()).setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    // todo use Vaadin button instead
    protected class PickerButton extends WebButton {
        public PickerButton() {
        }

        @Override
        protected void beforeActionPerformed() {
            WebPickerField.this.requestFocus();
        }
    }

    public static class Picker extends CubaPickerField {
        protected PickerField owner;

        public Picker(PickerField owner) {
            this.owner = owner;
        }

        public Picker(PickerField owner, AbstractField field) {
            super(field);
            this.owner = owner;
        }

        @Override
        public void setReadOnly(boolean readOnly) {
            super.setReadOnly(readOnly);

            for (Action action : owner.getActions()) {
                if (action instanceof StandardAction) {
                    ((StandardAction) action).setEditable(!readOnly);
                }
            }
        }
    }
}