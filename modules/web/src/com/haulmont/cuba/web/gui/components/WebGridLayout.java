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
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.web.toolkit.ui.CubaGridLayout;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.MarginInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class WebGridLayout extends WebAbstractComponent<CubaGridLayout> implements GridLayout {

    protected List<Component> ownComponents = new ArrayList<>();
    protected LayoutEvents.LayoutClickListener layoutClickListener;
    protected Map<Component.ShortcutAction, ShortcutListener> shortcuts;

    public WebGridLayout() {
        component = new CubaGridLayout();
    }

    @Override
    public void add(Component childComponent) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        final com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(vComponent);
        component.setComponentAlignment(vComponent, WebWrapperUtils.toVaadinAlignment(childComponent.getAlignment()));

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                frame.registerComponent(childComponent);
            }
        }

        ownComponents.add(childComponent);

        childComponent.setParent(this);
    }

    @Override
    public float getColumnExpandRatio(int col) {
        return component.getColumnExpandRatio(col);
    }

    @Override
    public void setColumnExpandRatio(int col, float ratio) {
        component.setColumnExpandRatio(col, ratio);
    }

    @Override
    public float getRowExpandRatio(int row) {
        return component.getRowExpandRatio(row);
    }

    @Override
    public void setRowExpandRatio(int row, float ratio) {
        component.setRowExpandRatio(row, ratio);
    }

    @Override
    public void add(Component component, int col, int row) {
        add(component, col, row, col, row);
    }

    @Override
    public void add(Component childComponent, int col, int row, int col2, int row2) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        final com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(vComponent, col, row, col2, row2);
        component.setComponentAlignment(vComponent, WebWrapperUtils.toVaadinAlignment(childComponent.getAlignment()));

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                frame.registerComponent(childComponent);
            }
        }

        ownComponents.add(childComponent);

        childComponent.setParent(this);
    }

    @Override
    public int getRows() {
        return component.getRows();
    }

    @Override
    public void setRows(int rows) {
        component.setRows(rows);
    }

    @Override
    public int getColumns() {
        return component.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        component.setColumns(columns);
    }

    @Override
    public void remove(Component childComponent) {
        component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        component.removeAllComponents();

        Component[] components = ownComponents.toArray(new Component[ownComponents.size()]);
        ownComponents.clear();

        for (Component childComponent : components) {
            childComponent.setParent(null);
        }
    }

    @Override
    public void setFrame(Frame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (Component childComponent : ownComponents) {
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
    }

    @Override
    public Component getOwnComponent(String id) {
        Preconditions.checkNotNullArgument(id);

        return ownComponents.stream()
                .filter(component -> Objects.equals(id, component.getId()))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public Component getComponentNN(String id) {
        Component component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public void setMargin(boolean enable) {
        component.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        component.setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
    }

    @Override
    public void setSpacing(boolean enabled) {
        component.setSpacing(enabled);
    }

    @Override
    public void addLayoutClickListener(LayoutClickListener listener) {
        getEventRouter().addListener(LayoutClickListener.class, listener);

        if (layoutClickListener == null) {
            layoutClickListener = event -> {
                Component childComponent = findChildComponent(this, event.getChildComponent());
                MouseEventDetails mouseEventDetails = WebWrapperUtils.toMouseEventDetails(event);

                LayoutClickEvent layoutClickEvent = new LayoutClickEvent(this, childComponent, mouseEventDetails);

                getEventRouter().fireEvent(LayoutClickListener.class, LayoutClickListener::layoutClick, layoutClickEvent);
            };
            component.addLayoutClickListener(layoutClickListener);
        }
    }

    protected Component findChildComponent(GridLayout layout, com.vaadin.ui.Component clickedComponent) {
        for (Component component : layout.getComponents()) {
            if (WebComponentsHelper.getComposition(component) == clickedComponent) {
                return component;
            }
        }
        return null;
    }

    @Override
    public void removeLayoutClickListener(LayoutClickListener listener) {
        getEventRouter().removeListener(LayoutClickListener.class, listener);

        if (!getEventRouter().hasListeners(LayoutClickListener.class)) {
            component.removeLayoutClickListener(layoutClickListener);
            layoutClickListener = null;
        }
    }

    @Override
    public void addShortcutAction(ShortcutAction action) {
        KeyCombination keyCombination = action.getShortcutCombination();
        com.vaadin.event.ShortcutListener shortcut =
                new ContainerShortcutActionWrapper(action, this, keyCombination);
        component.addShortcutListener(shortcut);

        if (shortcuts == null) {
            shortcuts = new HashMap<>();
        }
        shortcuts.put(action, shortcut);
    }

    @Override
    public void removeShortcutAction(ShortcutAction action) {
        if (shortcuts != null) {
            component.removeShortcutListener(shortcuts.remove(action));

            if (shortcuts.isEmpty()) {
                shortcuts = null;
            }
        }
    }
}