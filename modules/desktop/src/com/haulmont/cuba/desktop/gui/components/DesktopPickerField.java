/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.Picker;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;
import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopPickerField extends DesktopAbstractField<Picker>
        implements PickerField, Component.SecuredActionsHolder {

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected Datasource<Entity> datasource;
    protected MetaClass metaClass;

    protected Object prevValue;
    protected String prevTextValue;

    protected boolean editable = true;

    protected java.util.List<Action> actionsOrder = new LinkedList<>();
    protected java.util.Set<DesktopButton> buttons = new HashSet<>();

    protected int modifiersMask;
    protected Map<Action, List<KeyStroke>> keyStrokesMap = new HashMap<>();

    protected String caption;
    protected boolean updatingInstance;

    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    public DesktopPickerField() {
        impl = new Picker();
        initModifiersMask();
    }

    public DesktopPickerField(Picker picker) {
        impl = picker;
        initModifiersMask();
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
    public MetaClass getMetaClass() {
        Datasource ds = getDatasource();
        if (ds != null) {
            return metaProperty.getRange().asClass();
        } else {
            return metaClass;
        }
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        Datasource ds = getDatasource();
        if (ds != null)
            throw new IllegalStateException("Datasource is not null");
        this.metaClass = metaClass;
    }

    @Override
    public LookupAction addLookupAction() {
        LookupAction action = new LookupAction(this);
        addAction(action);
        return action;
    }

    @Override
    public ClearAction addClearAction() {
        ClearAction action = new ClearAction(this);
        addAction(action);
        return action;
    }

    @Override
    public OpenAction addOpenAction() {
        OpenAction action = new OpenAction(this);
        addAction(action);
        return action;
    }

    @Override
    public void addFieldListener(final FieldListener listener) {
        final JTextField field = (JTextField) impl.getEditor();
        field.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                fireFieldListener(listener, field.getText());
            }
        });

        field.addKeyListener(new KeyAdapter() {
            protected static final int ENTER_CODE = 10;

            @Override
            public void keyPressed(KeyEvent e) {
                if (ENTER_CODE == e.getKeyCode()) {
                    fireFieldListener(listener, field.getText());
                }
            }
        });
    }

    protected void fireFieldListener(FieldListener listener, String fieldText) {
        if (!(ObjectUtils.equals(prevTextValue, fieldText))) {
            prevTextValue = fieldText;
            listener.actionPerformed(fieldText, getValue());
        }
    }

    protected void initModifiersMask() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig config = configuration.getConfig(ClientConfig.class);
        String[] strModifiers = StringUtils.split(config.getPickerShortcutModifiers().toUpperCase(), "-");

        for (String strModifier : strModifiers) {
            KeyCombination.Modifier modifier = KeyCombination.Modifier.valueOf(strModifier);
            modifiersMask = modifiersMask | DesktopComponentsHelper.convertModifier(modifier);
        }
    }

    @Override
    public void setFieldEditable(boolean editable) {
        if (isEditable())
            ((JTextField) impl.getEditor()).setEditable(editable);
    }

    @Override
    public <T> T getValue() {
        if ((datasource != null) && (metaPropertyPath != null) && (datasource.getState() == Datasource.State.VALID)) {
            return datasource.getItem().getValue(metaProperty.getName());
        } else {
            return (T) prevValue;
        }
    }

    @Override
    public void setValue(Object value) {
        if (datasource == null && metaClass == null) {
            throw new IllegalStateException("Datasource or metaclass must be set for field");
        }

        if (value != null) {
            Class fieldClass = getMetaClass().getJavaClass();
            Class<?> valueClass = value.getClass();
            if (!fieldClass.isAssignableFrom(valueClass)) {
                throw new IllegalArgumentException(
                        String.format("Could not set value with class %s to field with class %s",
                                fieldClass.getCanonicalName(),
                                valueClass.getCanonicalName())
                );
            }
        }

        if (!ObjectUtils.equals(prevValue, value)) {
            updateInstance(value);
            updateComponent(value);
            fireChangeListeners(value);
        } else {
            updateComponent(prevValue);
        }
    }

    private void updateInstance(Object value) {
        if (updatingInstance)
            return;

        if (ObjectUtils.equals(prevValue, value))
            return;

        updatingInstance = true;
        try {
            if (datasource != null && metaProperty != null && datasource.getState() == Datasource.State.VALID &&
                    datasource.getItem() != null)
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
        } finally {
            updatingInstance = false;
        }
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    public void checkDatasourceProperty(Datasource datasource, String property){
        Preconditions.checkNotNullArgument(datasource);
        Preconditions.checkNotNullArgument(property);

        MetaPropertyPath metaPropertyPath = getResolvedMetaPropertyPath(datasource.getMetaClass(), property);
        if (!metaPropertyPath.getRange().isClass()) {
            throw new DevelopmentException(String.format("property '%s.%s' should have Entity type",  datasource.getMetaClass().getName(), property));
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        checkDatasourceProperty(datasource, property);
        this.datasource = datasource;

        if (datasource == null) {
            setValue(null);
            return;
        }

        resolveMetaPropertyPath(datasource.getMetaClass(), property);

        //noinspection unchecked
        datasource.addItemChangeListener(e -> {
            if (updatingInstance) {
                return;
            }

            Object value = InstanceUtils.getValueEx(e.getItem(), metaPropertyPath.getPath());
            updateComponent(value);
            fireChangeListeners(value);
        });

        //noinspection unchecked
        datasource.addItemPropertyChangeListener(e -> {
            if (updatingInstance) {
                return;
            }

            if (e.getProperty().equals(metaProperty.getName())) {
                updateComponent(e.getValue());
                fireChangeListeners(e.getValue());
            }
        });

        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            updateComponent(newValue);
            fireChangeListeners(newValue);
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }

        handleFilteredAttributes(this.datasource, metaProperty, this);
        this.datasource.addItemChangeListener(e -> handleFilteredAttributes(this.datasource, metaProperty, this));
    }

    protected void fireChangeListeners(Object newValue) {
        if (!ObjectUtils.equals(prevValue, newValue)) {
            Object oldValue = prevValue;

            prevValue = newValue;

            fireValueChanged(oldValue, newValue);
        }
    }

    protected void updateComponent(Object value) {
        String text;

        if (value == null) {
            text = "";
        } else {
            if (value instanceof Instance) {
                if (captionMode.equals(CaptionMode.ITEM)) {
                    text = ((Instance) value).getInstanceName();
                } else {
                    Object propertyValue = ((Instance)value).getValue(captionProperty);
                    MetaClass valueClass = metadata.getClassNN(value.getClass());
                    MetaProperty property = valueClass.getProperty(captionProperty);

                    text = metadataTools.format(propertyValue, property);
                }
            } else {
                text = value.toString();
            }
        }

        impl.setValue(text);
        prevTextValue = text;
        updateMissingValueState();
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        if (!ObjectUtils.equals(this.caption, caption)) {
            this.caption = caption;

            requestContainerUpdate();
        }
    }

    @Override
    public String getDescription() {
        return impl.getEditor().getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.getEditor().setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl.getEditor());
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        for (Action action : actionsOrder) {
            if (action instanceof StandardAction) {
                ((StandardAction) action).setEditable(isEditable());
            }
        }
        if (!editable && impl.getEditor() instanceof JTextComponent) {
            JTextComponent editor = (JTextComponent) impl.getEditor();
            editor.setEditable(false);
        }
        updateMissingValueState();
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        boolean resultEnabled = isEnabledWithParent();
        for (DesktopButton button : buttons) {
            button.setParentEnabled(resultEnabled);
        }

        if (impl.getEditor() instanceof JTextComponent) {
            JTextComponent editor = (JTextComponent) impl.getEditor();
            editor.setFocusable(resultEnabled);
        }
    }

    @Override
    public void addAction(Action action) {
        int index = findActionById(actionsOrder, action.getId());
        if (index < 0) {
            index = actionsOrder.size();
        }

        addAction(action, index);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        int oldIndex = findActionById(actionsOrder, action.getId());
        if (oldIndex >= 0) {
            removeAction(actionsOrder.get(oldIndex));
            if (index > oldIndex) {
                index--;
            }
        }

        actionsOrder.add(index, action);

        DesktopButton dButton = new DesktopButton();
        dButton.setParentEnabled(isEnabledWithParent());
        dButton.setShouldBeFocused(false);
        dButton.setAction(action);
        dButton.getImpl().setFocusable(false);
        dButton.getImpl().setText("");

        impl.addButton(dButton.getImpl(), index);
        buttons.add(dButton);

        // apply Editable after action owner is set
        if (action instanceof StandardAction) {
            ((StandardAction) action).setEditable(isEditable());
        }

        updateOrderedShortcuts();

        ActionMap actionMap = getImpl().getInputField().getActionMap();
        actionMap.put(action.getId(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerform(dButton);
            }
        });

        if (action.getShortcut() != null) {
            InputMap inputMap = getImpl().getInputField().getInputMap(JComponent.WHEN_FOCUSED);

            KeyStroke shortcutKeyStroke = DesktopComponentsHelper.convertKeyCombination(action.getShortcut());
            inputMap.put(shortcutKeyStroke, action.getId());
        }

        actionsPermissions.apply(action);
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (action != null) {
            if (actionsOrder.remove(action)) {
                if (action.getOwner() != null && action.getOwner() instanceof DesktopButton) {
                    JButton button = ((DesktopButton) action.getOwner()).getImpl();
                    impl.removeButton(button);
                }

                InputMap inputMap = getImpl().getInputField().getInputMap(JComponent.WHEN_FOCUSED);
                ActionMap actionMap = getImpl().getInputField().getActionMap();
                List<KeyStroke> keyStrokes = keyStrokesMap.get(action);
                if (keyStrokes != null) {
                    for (KeyStroke keyStroke : keyStrokes) {
                        inputMap.remove(keyStroke);
                    }
                    actionMap.remove(action.getId());
                }

                updateOrderedShortcuts();
            }
        }
    }

    protected void updateOrderedShortcuts() {
        InputMap inputMap = getImpl().getInputField().getInputMap(JComponent.WHEN_FOCUSED);
        for (int i = 0; i < 9; i++) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_1 + i, modifiersMask, false);
            inputMap.remove(keyStroke);
        }

        int index = 0;
        for (Action action : actionsOrder) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_1 + index, modifiersMask, false);
            List<KeyStroke> keyStrokes = new LinkedList<>();
            keyStrokes.add(keyStroke);
            keyStrokesMap.put(action, keyStrokes);

            inputMap.put(keyStroke, action.getId());

            index++;
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
        for (Action action : new ArrayList<>(actionsOrder)) {
            removeAction(action);
        }
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public Action getActionNN(String id) {
        Action action = getAction(id);
        if (action == null) {
            throw new IllegalStateException("Unable to find action with id " + id);
        }
        return action;
    }

    @Override
    public void updateMissingValueState() {
        if (!(impl.getEditor() instanceof JTextComponent)) {
            return;
        }
        JTextComponent editor = (JTextComponent) impl.getEditor();
        boolean value = required && editable && StringUtils.isBlank(editor.getText());

        decorateMissingValue(impl.getEditor(), value);
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }
}
