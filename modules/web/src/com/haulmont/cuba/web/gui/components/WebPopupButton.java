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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.sys.TestIdManager;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.haulmont.cuba.web.widgets.CubaPopupButton;
import com.haulmont.cuba.web.widgets.CubaPopupButtonLayout;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

public class WebPopupButton extends WebAbstractComponent<CubaPopupButton> implements PopupButton, SecuredActionsHolder {

    protected final static String CONTEXT_MENU_BUTTON_STYLENAME = "c-cm-button";

    protected Component popupComponent;
    protected com.vaadin.ui.Component vPopupComponent;
    protected CubaPopupButtonLayout vActionsContainer;

    protected boolean showActionIcons;

    protected List<Action> actionOrder = new ArrayList<>(4);
    protected Map<Action, Button> actionButtons = new HashMap<>(4);

    protected ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    protected Registration popupVisibilityListenerRegistration;
    protected Consumer<PropertyChangeEvent> actionPropertyChangeListener = this::actionPropertyChanged;

    public WebPopupButton() {
        component = createComponent();
        // do not show empty tooltip
        component.setDescription(null);

        this.vActionsContainer = createActionsContainer();
        this.vPopupComponent = vActionsContainer;
        component.setContent(vPopupComponent);
    }

    @Inject
    public void setThemeConstantsManager(ThemeConstantsManager themeConstantsManager) {
        ThemeConstants theme = themeConstantsManager.getConstants();
        this.showActionIcons = theme.getBoolean("cuba.gui.showIconsForPopupMenuActions", false);
    }

    protected CubaPopupButton createComponent() {
        return new PopupMenuButton();
    }

    protected CubaPopupButtonLayout createActionsContainer() {
        return new CubaPopupButtonLayout();
    }

    protected boolean hasVisibleActions() {
        for (Action action : actionOrder) {
            if (action.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Subscription addPopupVisibilityListener(Consumer<PopupVisibilityEvent> listener) {
        getEventHub().subscribe(PopupVisibilityEvent.class, listener);

        if (popupVisibilityListenerRegistration == null) {
            popupVisibilityListenerRegistration = component.addPopupVisibilityListener(e ->
                    publish(PopupVisibilityEvent.class, new PopupVisibilityEvent(this))
            );
        }
        return () -> removePopupVisibilityListener(listener);
    }

    @Override
    public void removePopupVisibilityListener(Consumer<PopupVisibilityEvent> listener) {
        unsubscribe(PopupVisibilityEvent.class, listener);

        if (!hasSubscriptions(PopupVisibilityEvent.class)) {
            popupVisibilityListenerRegistration.remove();
        }
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
    public boolean isPopupVisible() {
        return component.isPopupVisible();
    }

    @Override
    public void setPopupVisible(boolean popupVisible) {
        component.setPopupVisible(popupVisible);
    }

    @Override
    public void setMenuWidth(String width) {
        vPopupComponent.setWidth(width);
    }

    @Override
    public float getMenuWidth() {
        return vPopupComponent.getWidth();
    }

    @Override
    public int getMenuWidthUnits() {
        return ComponentsHelper.convertFromSizeUnit(getMenuWidthSizeUnit());
    }

    @Override
    public SizeUnit getMenuWidthSizeUnit() {
        return WebWrapperUtils.toSizeUnit(vPopupComponent.getWidthUnits());
    }

    @Override
    public boolean isAutoClose() {
        return component.isAutoClose();
    }

    @Override
    public void setShowActionIcons(boolean showActionIcons) {
        if (this.showActionIcons != showActionIcons) {
            this.showActionIcons = showActionIcons;
            updateActionsIcons();
        }
    }

    @Override
    public boolean isShowActionIcons() {
        return this.showActionIcons;
    }

    @Override
    public boolean isTogglePopupVisibilityOnClick() {
        return component.isButtonClickTogglesPopupVisibility();
    }

    @Override
    public void setTogglePopupVisibilityOnClick(boolean togglePopupVisibilityOnClick) {
        component.setButtonClickTogglesPopupVisibility(togglePopupVisibilityOnClick);
    }

    @Override
    public PopupOpenDirection getPopupOpenDirection() {
        return WebWrapperUtils.toPopupOpenDirection(component.getDirection());
    }

    @Override
    public void setPopupOpenDirection(PopupOpenDirection direction) {
        component.setDirection(WebWrapperUtils.toVaadinAlignment(direction));
    }

    @Override
    public boolean isClosePopupOnOutsideClick() {
        return component.isClosePopupOnOutsideClick();
    }

    @Override
    public void setClosePopupOnOutsideClick(boolean closePopupOnOutsideClick) {
        component.setClosePopupOnOutsideClick(closePopupOnOutsideClick);
    }

    @Override
    public void setPopupContent(Component popupContent) {
        this.popupComponent = popupContent;

        if (popupContent != null) {
            this.vPopupComponent = popupComponent.unwrapComposition(com.vaadin.ui.Component.class);
        } else {
            this.vPopupComponent = vActionsContainer;
        }
        this.component.setContent(vPopupComponent);
    }

    @Override
    public Component getPopupContent() {
        return popupComponent;
    }

    @Override
    public void setAutoClose(boolean autoClose) {
        component.setAutoClose(autoClose);
    }

    @Override
    public void addAction(Action action) {
        int index = findActionById(actionOrder, action.getId());
        if (index < 0) {
            index = actionOrder.size();
        }

        addAction(action, index);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        int oldIndex = findActionById(actionOrder, action.getId());
        if (oldIndex >= 0) {
            removeAction(actionOrder.get(oldIndex));
            if (index > oldIndex) {
                index--;
            }
        }

        Button vButton = createActionButton(action);

        vActionsContainer.addComponent(vButton, index);
        component.markAsDirty();
        actionOrder.add(index, action);
        actionButtons.put(action, vButton);

        actionsPermissions.apply(action);
    }

    protected void updateActionsIcons() {
        for (Map.Entry<Action, Button> entry : actionButtons.entrySet()) {
            if (showActionIcons) {
                setPopupButtonIcon(entry.getValue(), entry.getKey().getIcon());
            } else {
                setPopupButtonIcon(entry.getValue(), null);
            }
        }
    }

    protected CubaButton createActionButton(Action action) {
        CubaButton button = new CubaButton();

        button.setWidth(100, Sizeable.Unit.PERCENTAGE);
        button.setPrimaryStyleName(CONTEXT_MENU_BUTTON_STYLENAME);

        setPopupButtonAction(button, action);

        AppUI ui = AppUI.getCurrent();
        if (ui != null) {
            if (ui.isTestMode()) {
                button.setCubaId(action.getId());
            }

            if (ui.isPerformanceTestMode()) {
                String debugId = getDebugId();
                if (debugId != null) {
                    TestIdManager testIdManager = ui.getTestIdManager();
                    button.setId(testIdManager.getTestId(debugId + "_" + action.getId()));
                }
            }
        }

        return button;
    }

    protected void setPopupButtonAction(CubaButton button, Action action) {
        button.setCaption(action.getCaption());

        String description = action.getDescription();
        if (description == null && action.getShortcutCombination() != null) {
            description = action.getShortcutCombination().format();
        }
        if (description != null) {
            button.setDescription(description);
        }

        button.setEnabled(action.isEnabled());
        button.setVisible(action.isVisible());

        if (showActionIcons) {
            setPopupButtonIcon(button, action.getIcon());
        } else {
            setPopupButtonIcon(button, null);
        }

        action.addPropertyChangeListener(actionPropertyChangeListener);
        button.setClickHandler(mouseEventDetails -> {
            this.focus();

            if (isAutoClose()) {
                this.component.setPopupVisible(false);
            }

            action.actionPerform(this);
        });
    }

    protected void setPopupButtonIcon(Button button, String icon) {
        if (!StringUtils.isEmpty(icon)) {
            Resource iconResource = getIconResource(icon);
            button.setIcon(iconResource);
        } else {
            button.setIcon(null);
        }
    }

    protected void actionPropertyChanged(PropertyChangeEvent evt) {
        Action action = (Action) evt.getSource();
        Button button = actionButtons.get(action);

        if (Action.PROP_ICON.equals(evt.getPropertyName())) {
            if (showActionIcons) {
                setPopupButtonIcon(button, action.getIcon());
            } else {
                setPopupButtonIcon(button, null);
            }
        } else if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
            button.setCaption(action.getCaption());
        } else if (Action.PROP_DESCRIPTION.equals(evt.getPropertyName())) {
            button.setDescription(action.getDescription());
        } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
            button.setEnabled(action.isEnabled());
        } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
            button.setVisible(action.isVisible());
        }
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        AppUI ui = AppUI.getCurrent();
        if (id != null && ui != null && ui.isPerformanceTestMode()) {
            TestIdManager testIdManager = ui.getTestIdManager();

            for (Map.Entry<Action, Button> entry : actionButtons.entrySet()) {
                Button button = entry.getValue();
                Action action = entry.getKey();

                button.setId(testIdManager.getTestId(id + "_" + action.getId()));
            }
        }
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (action != null && actionOrder.remove(action)) {
            action.removePropertyChangeListener(actionPropertyChangeListener);
            Button button = actionButtons.remove(action);

            if (button != null) {
                vActionsContainer.removeComponent(button);
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
        for (Action action : new ArrayList<>(actionOrder)) {
            removeAction(action);
        }
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        for (Action action : actionOrder) {
            if (id.equals(action.getId())) {
                return action;
            }
        }
        return null;
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionOrder);
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }

    protected class PopupMenuButton extends CubaPopupButton {
        @Override
        public void setPopupVisible(boolean popupVisible) {
            if (vPopupComponent == vActionsContainer
                    && popupVisible && !hasVisibleActions()) {
                // do not show empty menu
                return;
            }

            super.setPopupVisible(popupVisible);
        }
    }
}