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

package com.haulmont.cuba.desktop.gui.components;

import com.google.common.collect.Iterators;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakCollectionChangeListener;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.function.Consumer;

public class DesktopOptionsGroup extends DesktopAbstractOptionsField<JPanel> implements OptionsGroup {

    // Focus group transfer actions and corresponding keys.
    protected static final String ACTION_KEY_DOWN = "ACTION_KEY_DOWN";
    protected static final String ACTION_KEY_UP = "ACTION_KEY_UP";
    protected static final String ACTION_KEY_LEFT = "ACTION_KEY_LEFT";
    protected static final String ACTION_KEY_RIGHT = "ACTION_KEY_RIGHT";

    protected static final KeyStroke KEY_DOWN = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
    protected static final KeyStroke KEY_UP = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    protected static final KeyStroke KEY_RIGHT = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
    protected static final KeyStroke KEY_LEFT = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);

    private boolean multiselect;
    private boolean optionsInitialized;
    private Map<ValueWrapper, JToggleButton> items = new LinkedHashMap<>();
    private ButtonGroup buttonGroup;

    private Orientation orientation = Orientation.VERTICAL;
    private MigLayout layout;

    private boolean enabled = true;

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;

    public DesktopOptionsGroup() {
        layout = new MigLayout();
        impl = new JPanel(layout);
        updateLayout();
    }

    private void updateLayout() {
        layout.setLayoutConstraints(orientation == Orientation.VERTICAL ? "flowy" : "flowx");
    }

    @Override
    public boolean isMultiSelect() {
        return multiselect;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        if (this.multiselect != multiselect
                && (optionsDatasource != null || optionsList != null || optionsMap != null))
            throw new IllegalStateException("Set multiselect before initializing options");

        this.multiselect = multiselect;
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        if (optionsInitialized)
            return;

        super.setOptionsDatasource(datasource);
        if (optionsDatasource != null) {
            if (!optionsDatasource.getState().equals(Datasource.State.VALID)) {
                optionsDatasource.refresh();
            }
            for (Entity item : optionsDatasource.getItems()) {
                addItem(new EntityWrapper(item));
            }

            collectionChangeListener = e -> {
                Object value = getValue();

                removeAllItems();
                for (Object id : e.getDs().getItemIds()) {
                    addItem(new EntityWrapper(e.getDs().getItem(id)));
                }

                updateComponent(value);
                fireChangeListeners(getValue());

                impl.revalidate();
                impl.repaint();
            };
            optionsDatasource.addCollectionChangeListener(new WeakCollectionChangeListener(optionsDatasource, collectionChangeListener));

            if ((datasource!= null) && (datasource.getState() == Datasource.State.VALID)) {
                Entity newValue = datasource.getItem();
                updateComponent(newValue);
                fireChangeListeners(newValue);
            }
            optionsInitialized = true;
        }

        assignAutoDebugId();
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);

        assignAutoDebugId();
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + datasource.getId() + "_" + metaPropertyPath.toString();
        }
        if (optionsDatasource != null && StringUtils.isNotEmpty(optionsDatasource.getId())) {
            return getClass().getSimpleName() + optionsDatasource.getId();
        }

        return getClass().getSimpleName();
    }

    @Override
    public void setOptionsList(List optionsList) {
        if (optionsInitialized)
            return;

        super.setOptionsList(optionsList);
        if (optionsList != null) {
            for (Object obj : optionsList) {
                addItem(new ObjectWrapper(obj));
            }

            if ((datasource!= null) && (datasource.getState() == Datasource.State.VALID)) {
                Entity newValue = datasource.getItem();
                updateComponent(newValue);
                fireChangeListeners(newValue);
            }
            optionsInitialized = true;
        }
    }

    @Override
    public void setOptionsMap(Map<String, ?> map) {
        if (optionsInitialized)
            return;

        super.setOptionsMap(map);
        if (optionsMap != null) {
            for (String key : optionsMap.keySet()) {
                addItem(new MapKeyWrapper(key));
            }

            if ((datasource!= null) && (datasource.getState() == Datasource.State.VALID)) {
                Entity newValue = datasource.getItem();
                updateComponent(newValue);
                fireChangeListeners(newValue);
            }
            optionsInitialized = true;
        }
    }

    @Override
    public void setOptionsEnum(Class<? extends EnumClass> optionsEnum) {
        if (optionsInitialized)
            return;

        super.setOptionsEnum(optionsEnum);
        if (optionsEnum != null) {
            List options = Arrays.asList(optionsEnum.getEnumConstants());
            for (Object obj : options) {
                addItem(new ObjectWrapper(obj));
            }

            if ((datasource!= null) && (datasource.getState() == Datasource.State.VALID)) {
                Entity newValue = datasource.getItem();
                updateComponent(newValue);
                fireChangeListeners(newValue);
            }
            optionsInitialized = true;
        }
    }

    private void addItem(final ValueWrapper item) {
        JToggleButton button;
        if (multiselect) {
            button = new JCheckBox(item.toString());
        } else {
            if (buttonGroup == null)
                buttonGroup = new ButtonGroup();
            button = new JRadioButton(item.toString());
            buttonGroup.add(button);
        }
        button.setEnabled(enabled && editable);
        button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!multiselect) {
                            Object newValue = item.getValue();
                            if (!Objects.equals(newValue, prevValue)) {
                                updateInstance(newValue);
                                fireChangeListeners(newValue);
                            }
                            updateMissingValueState();
                        } else {
                            Set<Object> newValue = new LinkedHashSet<>();
                            for (Map.Entry<ValueWrapper, JToggleButton> item : items.entrySet()) {
                                if (item.getValue().isSelected()) {
                                    newValue.add(item.getKey().getValue());
                                }
                            }
                            if ((prevValue != null
                                    && !CollectionUtils.isEqualCollection(newValue, (Collection) prevValue))
                                || (prevValue == null)) {
                                updateInstance(newValue);
                                fireChangeListeners(newValue);
                            }
                            updateMissingValueState();
                        }
                    }
                }
        );
        addFocusGroupShortcuts(button);

        impl.add(button);
        items.put(item, button);
    }

    protected Optional<AbstractButton> nextTo(AbstractButton target) {
        Enumeration<AbstractButton> elements = buttonGroup.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton button = elements.nextElement();
            if (target.equals(button)) {
                if (elements.hasMoreElements()) {
                    return Optional.of(elements.nextElement());
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    protected Optional<AbstractButton> prevTo(AbstractButton target) {
        Enumeration<AbstractButton> elements = buttonGroup.getElements();
        AbstractButton button = null;
        while (elements.hasMoreElements()) {
            AbstractButton current = elements.nextElement();
            if (target.equals(current)) {
                return Optional.ofNullable(button);
            }
            button = current;
        }
        return Optional.empty();
    }

    protected AbstractAction asAction(Consumer<ActionEvent> consumer) {
        return new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                consumer.accept(e);
            }
        };
    }

    protected Consumer<ActionEvent> nextElementFocusAction(AbstractButton button) {
        return e -> nextTo(button).ifPresent(JComponent::requestFocusInWindow);
    }

    protected Consumer<ActionEvent> prevElementFocusAction(AbstractButton button) {
        return e -> prevTo(button).ifPresent(JComponent::requestFocusInWindow);
    }

    protected Consumer<ActionEvent> onlyWhenHorizontal(Consumer<ActionEvent> consumer) {
        return e -> {
            if (Orientation.HORIZONTAL == getOrientation()) {
                consumer.accept(e);
            }
        };
    }

    protected Consumer<ActionEvent> onlyWhenVertical(Consumer<ActionEvent> consumer) {
        return e -> {
            if (Orientation.VERTICAL == getOrientation()) {
                consumer.accept(e);
            }
        };
    }

    protected void addFocusGroupShortcuts(JToggleButton button) {
        InputMap inputs = button.getInputMap();
        inputs.put(KEY_DOWN, ACTION_KEY_DOWN);
        inputs.put(KEY_UP, ACTION_KEY_UP);
        inputs.put(KEY_LEFT, ACTION_KEY_LEFT);
        inputs.put(KEY_RIGHT, ACTION_KEY_RIGHT);

        ActionMap actions = button.getActionMap();
        actions.put(ACTION_KEY_DOWN, asAction(onlyWhenVertical(nextElementFocusAction(button))));
        actions.put(ACTION_KEY_UP, asAction(onlyWhenVertical(prevElementFocusAction(button))));
        actions.put(ACTION_KEY_RIGHT, asAction(onlyWhenHorizontal(nextElementFocusAction(button))));
        actions.put(ACTION_KEY_LEFT, asAction(onlyWhenHorizontal(prevElementFocusAction(button))));
    }

    @Override
    protected void updateInstance(Object newValue) {
        if (newValue instanceof Collection && multiselect && metaPropertyPath != null) {
            Class propertyType = metaPropertyPath.getMetaProperty().getJavaType();
            if (Set.class.isAssignableFrom(propertyType)) {
                newValue = new LinkedHashSet<>((Collection<?>) newValue);
            } else if (List.class.isAssignableFrom(propertyType)) {
                newValue = new ArrayList<>((Collection<?>) newValue);
            }
        }

        super.updateInstance(newValue);
    }

    @Override
    public void updateMissingValueState() {
        boolean state = required && getSelectedItem() == null;
        decorateMissingValue(impl, state);
    }

    private void refreshCaptions() {
        for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
            entry.getValue().setText(entry.getKey().toString());
        }
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        super.setCaptionMode(captionMode);

        refreshCaptions();
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        super.setCaptionProperty(captionProperty);

        refreshCaptions();
    }

    private void removeAllItems() {
        impl.removeAll();
        items.clear();
        buttonGroup = null;
    }

    @Override
    protected void updateComponent(Object value) {
        if (multiselect && value instanceof Collection) {
            Collection collection = (Collection) value;
            items.forEach((wrapper, button) ->
                    button.setSelected(collection.contains(wrapper.getValue())));
            updateMissingValueState();
        } else {
            super.updateComponent(value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        if (multiselect) {
            Set<Object> set = new LinkedHashSet<>();
            for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
                if (entry.getValue().isSelected()) {
                    set.add(entry.getKey().getValue());
                }
            }
            return (T) set;
        } else {
            return (T) wrapAsCollection(super.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T wrapAsCollection(Object o) {
        if (isMultiSelect()) {
            if (o != null) {
                return (T) Collections.singleton(o);
            } else {
                return (T) Collections.emptySet();
            }
        } else {
            return (T) o;
        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    protected Object getSelectedItem() {
        for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
            if (entry.getValue().isSelected())
                return entry.getKey();
        }
        return null;
    }

    @Override
    protected void setSelectedItem(Object item) {
        for (Map.Entry<ValueWrapper, JToggleButton> entry : items.entrySet()) {
            Object wrapperValue = entry.getKey().getValue();
            if (item != null && wrapperValue.equals(((ValueWrapper) item).getValue()))
                entry.getValue().setSelected(true);
            else
                entry.getValue().setSelected(false);
        }
        updateMissingValueState();
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        for (JToggleButton button : items.values()) {
            button.setEnabled(enabled && editable);
        }
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Orientation must not be null");
        }
        this.orientation = orientation;
        updateLayout();
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        for (JToggleButton button : items.values()) {
            button.setEnabled(enabled && editable && parentEnabled);
        }
    }

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        Object value = getValue();
        return (value instanceof Collection)
                ? (Collection) value
                : Collections.singleton(value);
    }
}