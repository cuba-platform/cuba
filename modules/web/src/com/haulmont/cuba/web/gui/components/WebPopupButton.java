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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.widgets.CubaPopupButton;
import com.haulmont.cuba.web.widgets.CubaPopupButtonLayout;
import com.vaadin.ui.Button;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.beans.PropertyChangeListener;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

public class WebPopupButton extends WebAbstractComponent<CubaPopupButton>
        implements PopupButton, Component.SecuredActionsHolder {

    protected final static String CONTEXT_MENU_BUTTON_STYLENAME = "c-cm-button";

    protected Component popupComponent;
    protected com.vaadin.ui.Component vPopupComponent;
    protected CubaPopupButtonLayout vActionsContainer;

    protected boolean showActionIcons;

    protected List<Action> actionOrder = new ArrayList<>(3);
    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    public WebPopupButton() {
        component = new CubaPopupButton() {
            @Override
            public void setPopupVisible(boolean popupVisible) {
                if (vPopupComponent == vActionsContainer
                        && popupVisible && !hasVisibleActions()) {
                    return;
                }

                super.setPopupVisible(popupVisible);
            }
        };

        component.addPopupVisibilityListener(event ->
                getEventRouter().fireEvent(PopupVisibilityListener.class,
                        PopupVisibilityListener::popupVisibilityChange,
                        new PopupVisibilityEvent(this))
        );

        this.vActionsContainer = createActionsContainer();
        this.vPopupComponent = vActionsContainer;
        component.setContent(vPopupComponent);

        component.setDescription(null);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        showActionIcons = clientConfig.getShowIconsForPopupMenuActions();
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
    public void addPopupVisibilityListener(PopupVisibilityListener listener) {
        getEventRouter().addListener(PopupVisibilityListener.class, listener);
    }

    @Override
    public void removePopupVisibilityListener(PopupVisibilityListener listener) {
        getEventRouter().removeListener(PopupVisibilityListener.class, listener);
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

        actionsPermissions.apply(action);
    }

    protected void updateActionsIcons() {
        for (Action action : actionOrder) {
            for (ActionOwner actionOwner : action.getOwners()) {
                if (actionOwner instanceof PopupButtonActionButton) {
                    PopupButtonActionButton button = (PopupButtonActionButton) actionOwner;

                    if (showActionIcons) {
                        button.setIcon(action.getIcon());
                    } else {
                        button.setIcon(null);
                    }
                }
            }
        }
    }

    protected Button createActionButton(Action action) {
        WebButton button = new PopupButtonActionButton() {
            @Override
            protected void beforeActionPerformed() {
                WebPopupButton.this.requestFocus();
            }
        };
        button.setAction(new PopupActionWrapper(action));

        button.setIcon(this.isShowActionIcons() ? action.getIcon() : null);


        Button vButton = (Button) button.getComposition();
        vButton.setSizeFull();
        vButton.setStyleName(CONTEXT_MENU_BUTTON_STYLENAME);

        AppUI ui = AppUI.getCurrent();

        if (ui.isTestMode()) {
            String debugId = getDebugId();
            if (debugId != null) {
                button.setDebugId(ui.getTestIdManager().getTestId(debugId + "_" + action.getId()));
            }
            button.setId(action.getId());
        }
        return vButton;
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            for (Action action : getActions()) {
                WebButton button = (WebButton) action.getOwner();
                if (StringUtils.isEmpty(button.getDebugId())) {
                    button.setDebugId(testIdManager.getTestId(id + "_" + action.getId()));
                }
            }
        }
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (actionOrder.remove(action)) {
            //noinspection ConstantConditions
            for (ActionOwner owner : new LinkedList<>(action.getOwners())) {
                if (owner instanceof PopupButtonActionButton) {
                    owner.setAction(null);
                    Button vButton = (Button) WebComponentsHelper.unwrap((PopupButtonActionButton) owner);
                    vActionsContainer.removeComponent(vButton);
                }
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

    protected class PopupActionWrapper implements Action {

        protected Action action;

        public PopupActionWrapper(Action action) {
            this.action = action;
        }

        protected Action getAction() {
            return action;
        }

        @Override
        public void actionPerform(Component component) {
            if (isAutoClose()) {
                WebPopupButton.this.component.setPopupVisible(false);
            }

            action.actionPerform(component);
        }

        @Override
        public void addOwner(ActionOwner actionOwner) {
            action.addOwner(actionOwner);
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            action.addPropertyChangeListener(listener);
        }

        @Override
        public String getCaption() {
            return action.getCaption();
        }

        @Override
        public String getIcon() {
            return action.getIcon();
        }

        @Override
        public String getId() {
            return action.getId();
        }

        @Override
        public ActionOwner getOwner() {
            return action.getOwner();
        }

        @Override
        public Collection<ActionOwner> getOwners() {
            return action.getOwners();
        }

        @Override
        public boolean isEnabled() {
            return action.isEnabled();
        }

        @Override
        public boolean isVisible() {
            return action.isVisible();
        }

        @Override
        public void removeOwner(ActionOwner actionOwner) {
            action.removeOwner(actionOwner);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            action.removePropertyChangeListener(listener);
        }

        @Override
        public void setCaption(String caption) {
            action.setCaption(caption);
        }

        @Override
        public String getDescription() {
            return action.getDescription();
        }

        @Override
        public void setDescription(String description) {
            action.setDescription(description);
        }

        @Override
        public KeyCombination getShortcutCombination() {
            return null;
        }

        @Override
        public void setShortcutCombination(KeyCombination shortcut) {
        }

        @Override
        public void setShortcut(String shortcut) {
        }

        @Override
        public void setEnabled(boolean enabled) {
            action.setEnabled(enabled);
        }

        @Override
        public void setIcon(String icon) {
            action.setIcon(icon);
        }

        @Override
        public void setIconFromSet(Icons.Icon icon) {
            String iconName = AppBeans.get(Icons.class)
                    .get(icon);
            setIcon(iconName);
        }

        @Override
        public void setVisible(boolean visible) {
            action.setVisible(visible);
        }

        @Override
        public void refreshState() {
            action.refreshState();
        }
    }

    protected static class PopupButtonActionButton extends WebButton {

    }
}