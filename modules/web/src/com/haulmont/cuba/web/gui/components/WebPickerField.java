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
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.sys.TestIdManager;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.valueproviders.EntityNameValueProvider;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaPickerField;
import com.vaadin.data.ValueProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Resource;
import com.vaadin.shared.Registration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

public class WebPickerField<V extends Entity> extends WebV8AbstractField<CubaPickerField<V>, V, V>
        implements PickerField<V>, SecuredActionsHolder, InitializingBean, SupportsUserAction<V> {

    /* Beans */
    protected Metadata metadata;
    protected MetadataTools metadataTools;

    protected MetaClass metaClass;

    protected List<Action> actions = new ArrayList<>(4);
    protected Map<Action, CubaButton> actionButtons = new HashMap<>(4);
    protected Registration fieldListenerRegistration;

    protected ActionsPermissions actionsPermissions = new ActionsPermissions(this);
    protected WebPickerFieldActionHandler actionHandler;

    protected Consumer<PropertyChangeEvent> actionPropertyChangeListener = this::actionPropertyChanged;
    protected Function<? super V, String> optionCaptionProvider;

    public WebPickerField() {
        component = createComponent();

        attachValueChangeListener(this.component);
    }

    protected CubaPickerField<V> createComponent() {
        return new CubaPickerField<>();
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        actionHandler = new WebPickerFieldActionHandler(configuration);
        component.addActionHandler(actionHandler);
    }

    @Override
    public void setValueFromUser(V value) {
        setValueToPresentation(convertToPresentation(value));

        V oldValue = internalValue;
        this.internalValue = value;

        if (!fieldValueEquals(value, oldValue)) {
            ValueChangeEvent<V> event = new ValueChangeEvent<>(this, oldValue, value, true);
            publish(ValueChangeEvent.class, event);
        }
    }

    @Inject
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
    }

    protected void initComponent(CubaPickerField<V> component) {
        component.setTextFieldValueProvider(createTextFieldValueProvider());
    }

    protected ValueProvider<V, String> createTextFieldValueProvider() {
        return new EntityNameValueProvider<V>(metadataTools) {
            @Override
            public String apply(V entity) {
                if (optionCaptionProvider != null) {
                    return optionCaptionProvider.apply(entity);
                }

                return super.apply(entity);
            }
        };
    }

    @Override
    public MetaClass getMetaClass() {
        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaProperty metaProperty = ((EntityValueSource) valueSource).getMetaPropertyPath().getMetaProperty();
            return metaProperty.getRange().asClass();
        } else {
            return metaClass;
        }
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        ValueSource<V> valueSource = getValueSource();
        if (valueSource != null) {
            throw new IllegalStateException("ValueSource is not null");
        }
        this.metaClass = metaClass;
    }

    @Deprecated
    @Override
    public LookupAction addLookupAction() {
        LookupAction action = LookupAction.create(this);
        addAction(action);
        return action;
    }

    @Override
    @Deprecated
    public ClearAction addClearAction() {
        ClearAction action = ClearAction.create(this);
        addAction(action);
        return action;
    }

    @Deprecated
    @Override
    public PickerField.OpenAction addOpenAction() {
        OpenAction action = OpenAction.create(this);
        addAction(action);
        return action;
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider) {
        this.optionCaptionProvider = optionCaptionProvider;
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
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
        actionButtons.put(action, vButton);

        if (StringUtils.isNotEmpty(getDebugId())) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            // Set debug id
            vButton.setId(testIdManager.getTestId(getDebugId() + "_" + action.getId()));
        }

        if (action instanceof PickerFieldAction) {
            PickerFieldAction pickerFieldAction = (PickerFieldAction) action;
            pickerFieldAction.setPickerField(this);
            if (!isEditable()) {
                pickerFieldAction.editableChanged(this, isEditable());
            }
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

        AppUI ui = AppUI.getCurrent();
        if (ui != null && ui.isTestMode()) {
            button.setCubaId(action.getId());
        }

        action.addPropertyChangeListener(actionPropertyChangeListener);
        button.setClickHandler(event -> {
            this.focus();
            action.actionPerform(this);
        });
    }

    protected void setPickerButtonIcon(CubaButton button, String icon) {
        if (!StringUtils.isEmpty(icon)) {
            Resource iconResource = getIconResource(icon);
            button.setIcon(iconResource);
        } else {
            button.setIcon(null);
        }
    }

    protected void actionPropertyChanged(PropertyChangeEvent evt) {
        Action action = (Action) evt.getSource();
        CubaButton button = actionButtons.get(action);

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
        } else if (action instanceof PickerFieldAction
                && PickerFieldAction.PROP_EDITABLE.equals(evt.getPropertyName())) {
            button.setVisible(((PickerFieldAction) action).isEditable());
        }
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);
        if (id != null) {
            String debugId = getDebugId();

            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            for (Action action : actions) {
                CubaButton button = actionButtons.get(action);
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
                CubaButton button = actionButtons.remove(action);
                component.removeButton(button);

                action.removePropertyChangeListener(actionPropertyChangeListener);
            }

            if (action instanceof PickerFieldAction) {
                ((PickerFieldAction) action).setPickerField(null);
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

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addFieldValueChangeListener(Consumer<FieldValueChangeEvent<V>> listener) {
        if (fieldListenerRegistration == null) {
            fieldListenerRegistration = component.addFieldListener(this::onFieldValueChange);
        }

        return getEventHub().subscribe(FieldValueChangeEvent.class, (Consumer) listener);
    }

    protected void onFieldValueChange(CubaPickerField.FieldValueChangeEvent <V> e) {
        FieldValueChangeEvent<V> event = new FieldValueChangeEvent<>(this, e.getText(), e.getPrevValue());
        publish(FieldValueChangeEvent.class, event);
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
    public void setLookupSelectHandler(Consumer selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        V value = getValue();
        if (value == null) {
            return Collections.emptyList();
        }

        return Collections.singleton(value);
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
    protected void setEditableToComponent(boolean editable) {
        super.setEditableToComponent(editable);

        for (Action action : getActions()) {
            if (action instanceof PickerFieldAction) {
                ((PickerFieldAction) action).editableChanged(this, editable);
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

    public class WebPickerFieldActionHandler implements com.vaadin.event.Action.Handler {

        private int[] modifiers;

        private Map<ShortcutAction, Action> actionsMap = new HashMap<>(4);  // todo replace with custom class
        private List<com.vaadin.event.Action> shortcuts = new ArrayList<>(4);
        private List<ShortcutAction> orderedShortcuts = new ArrayList<>(4);

        protected List<Action> actionList = new ArrayList<>(4);

        public WebPickerFieldActionHandler(Configuration configuration) {
            ClientConfig config = configuration.getConfig(ClientConfig.class);
            String[] strModifiers = StringUtils.split(config.getPickerShortcutModifiers().toUpperCase(), "-");
            modifiers = new int[strModifiers.length];
            for (int i = 0; i < modifiers.length; i++) {
                modifiers[i] = KeyCombination.Modifier.valueOf(strModifiers[i]).getCode();
            }
        }

        @Override
        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            return shortcuts.toArray(new com.vaadin.event.Action[0]);
        }

        public void addAction(Action action, int index) {
            actionList.add(index, action);

            updateOrderedShortcuts();

            KeyCombination combination = action.getShortcutCombination();
            if (combination != null) {
                int key = combination.getKey().getCode();
                int[] modifiers = KeyCombination.Modifier.codes(combination.getModifiers());
                ShortcutAction providedShortcut = new ShortcutAction(action.getCaption(), key, modifiers);
                shortcuts.add(providedShortcut);
                actionsMap.put(providedShortcut, action);
            }
        }

        public void removeAction(Action action) {
            List<ShortcutAction> existActions = new ArrayList<>(4);
            for (Map.Entry<ShortcutAction, Action> entry : actionsMap.entrySet()) {
                if (entry.getValue().equals(action)) {
                    existActions.add(entry.getKey());
                }
            }
            shortcuts.removeAll(existActions);
            for (ShortcutAction shortcut : existActions) {
                actionsMap.remove(shortcut);
            }
            actionList.remove(action);

            updateOrderedShortcuts();
        }

        protected void updateOrderedShortcuts() {
            shortcuts.removeAll(orderedShortcuts);
            for (ShortcutAction orderedShortcut : orderedShortcuts) {
                actionsMap.remove(orderedShortcut);
            }

            for (int i = 0; i < actionList.size(); i++) {
                int keyCode = ShortcutAction.KeyCode.NUM1 + i;

                Action orderedAction = actionList.get(i);

                ShortcutAction orderedShortcut = new ShortcutAction(orderedAction.getCaption(), keyCode, modifiers);
                shortcuts.add(orderedShortcut);
                orderedShortcuts.add(orderedShortcut);
                actionsMap.put(orderedShortcut, orderedAction);
            }
        }

        @Override
        public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
            //noinspection RedundantCast
            Action pickerAction = actionsMap.get((ShortcutAction) action);
            if (pickerAction != null) {
                pickerAction.actionPerform(WebPickerField.this);
            }
        }
    }
}