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

import com.google.common.base.Strings;
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
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.valueproviders.EntityNameValueProvider;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaPickerField;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

public class WebPickerField<V extends Entity> extends WebV8AbstractField<CubaPickerField<V>, V, V>
        implements PickerField<V>, SecuredActionsHolder, InitializingBean {

    /* Beans */
    protected Metadata metadata;
    protected MetadataTools metadataTools;
    protected IconResolver iconResolver;

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected MetaClass metaClass;

    protected List<Action> actions = new ArrayList<>();
    protected Map<String, CubaButton> actionButtons = new HashMap<>();
    protected Map<String, PropertyChangeListener> actionPropertyChangeListeners = new HashMap<>();

    protected WebPickerFieldActionHandler actionHandler;

    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    public WebPickerField() {
        component = createComponent();

        attachValueChangeListener(this.component);
    }

    protected CubaPickerField<V> createComponent() {
        return new CubaPickerField<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initComponent(component);
        initActionHandler(component);
    }

    protected void initComponent(CubaPickerField<V> component) {
        component.setTextFieldValueProvider(createTextFieldValueProvider());
    }

    protected ValueProvider<V, String> createTextFieldValueProvider() {
        return new EntityNameValueProvider<V>(metadataTools) {
            @Override
            public String apply(V entity) {
                if (captionMode == CaptionMode.PROPERTY
                        && StringUtils.isNotEmpty(captionProperty)
                        && (entity != null)) {

                    Object propertyValue = entity.getValue(captionProperty);

                    MetaClass metaClass = metadata.getClassNN(entity.getClass());
                    MetaProperty property = metaClass.getProperty(captionProperty);
                    return metadata.getTools().format(propertyValue, property);
                }

                return super.apply(entity);
            }
        };
    }

    protected void initActionHandler(CubaPickerField<V> component) {
        actionHandler = new WebPickerFieldActionHandler(this);
        component.addActionHandler(actionHandler);
    }

    @Inject
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Inject
    public void setIconResolver(IconResolver iconResolver) {
        this.iconResolver = iconResolver;
    }

    @Override
    public MetaClass getMetaClass() {
        final ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            return ((EntityValueSource) valueSource).getMetaPropertyPath().getMetaProperty().getRange().asClass();
        } else {
            return metaClass;
        }
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        final ValueSource<V> valueSource = getValueSource();
        if (valueSource != null) {
            throw new IllegalStateException("ValueSource is not null");
        }
        this.metaClass = metaClass;
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

    // todo remove
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

        CubaButton vButton = new CubaButton();
        setPickerButtonAction(vButton, action);

        component.addButton(vButton, index);
        actionButtons.put(action.getId(), vButton);

        // apply Editable after action owner is set
        if (action instanceof StandardAction) {
            ((StandardAction) action).setEditable(isEditable());
        }

        if (StringUtils.isNotEmpty(getDebugId())) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            // Set debug id
            vButton.setId(testIdManager.getTestId(getDebugId() + "_" + action.getId()));
        }

        actionsPermissions.apply(action);
    }

    protected void setPickerButtonAction(CubaButton button, Action action) {
        String description = action.getDescription();
        if (description == null && action.getShortcutCombination() != null) {
            description = action.getShortcutCombination().format();
        }
        if (description != null) {
            button.setDescription(description);
        }

        button.setEnabled(action.isEnabled());
        button.setVisible(action.isVisible());

        if (action.getIcon() != null) {
            setPickerButtonIcon(button, action.getIcon());
        }

        PropertyChangeListener actionPropertyChangeListener = createActionPropertyChangeListener(button, action);
        action.addPropertyChangeListener(actionPropertyChangeListener);
        actionPropertyChangeListeners.put(action.getId(), actionPropertyChangeListener);

        button.setClickHandler(createPickerButtonClickHandler(action));
    }

    protected void setPickerButtonIcon(CubaButton button, String icon) {
        if (!StringUtils.isEmpty(icon)) {
            Resource iconResource = iconResolver.getIconResource(icon);
            button.setIcon(iconResource);
            button.addStyleName(ICON_STYLE);
        } else {
            button.setIcon(null);
            button.removeStyleName(ICON_STYLE);
        }
    }

    protected PropertyChangeListener createActionPropertyChangeListener(CubaButton button, Action action) {
        return evt -> {
            if (Action.PROP_ICON.equals(evt.getPropertyName())) {
                setPickerButtonIcon(button, action.getIcon());
            } else if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                button.setCaption(action.getCaption());
            } else if (Action.PROP_DESCRIPTION.equals(evt.getPropertyName())) {
                button.setDescription(action.getDescription());
            } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                button.setEnabled(action.isEnabled());
            } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                button.setVisible(action.isVisible());
            } else if (action instanceof PickerField.StandardAction
                    && PickerField.StandardAction.PROP_EDITABLE.equals(evt.getPropertyName())) {
                button.setVisible(((StandardAction) action).isEditable());
            }
        };
    }

    protected Consumer<MouseEventDetails> createPickerButtonClickHandler(Action action) {
        return event -> {
            WebPickerField.this.focus();
            action.actionPerform(null);
        };
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);
        if (id != null) {
            String debugId = getDebugId();

            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            for (Action action : actions) {
                CubaButton button = actionButtons.get(action.getId());
                if (button != null && Strings.isNullOrEmpty(button.getId())) {
                    button.setId(testIdManager.getTestId(debugId + "_" + action.getId()));
                }
            }
        }
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (actions.remove(action)) {
            actionHandler.removeAction(action);

            if (action != null) {
                CubaButton button = actionButtons.remove(action.getId());
                component.removeButton(button);

                PropertyChangeListener listener = actionPropertyChangeListeners.remove(action.getId());
                action.removePropertyChangeListener(listener);
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
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);

        for (Action action : getActions()) {
            if (action instanceof StandardAction) {
                ((StandardAction) action).setEditable(editable);
            }
        }
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
}