/*
 * Copyright 2017 Henri Kerola / Vaadin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.addons.popupbutton;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;

import com.haulmont.cuba.web.widgets.client.addons.popupbutton.PopupButtonServerRpc;
import com.haulmont.cuba.web.widgets.client.addons.popupbutton.PopupButtonState;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;

import com.vaadin.util.ReflectTools;

/**
 * A {@link Button} with a popup. The popup can contain any Vaadin
 * {@link Component}s and @{link ComponentContainer}s.
 *
 * @author Henri Kerola / Vaadin
 */
// This class contains code from AbstractComponentContainer
public class PopupButton extends Button implements SingleComponentContainer {

    private static final long serialVersionUID = -3148268967211155218L;

    private Component component;

    // This is here for getter because in the state we store int bitmask only
    private Alignment direction;

    private PopupButtonServerRpc rpc = PopupButton.this::setPopupVisible;

    public PopupButton() {
        registerRpc(rpc);
        setDirection(Alignment.BOTTOM_RIGHT);
    }

    public PopupButton(String caption) {
        super(caption);
        registerRpc(rpc);
        setDirection(Alignment.BOTTOM_RIGHT);
    }

    @Override
    public Iterator<Component> iterator() {
        if (isPopupVisible() && getContent() != null) {
            return Collections.singletonList(component).iterator();
        } else {
            return Collections.<Component> emptyList().iterator();
        }
    }

    /**
     * Shows or hides popup.
     *
     * @param popupVisible
     *            if true, popup is set to visible, otherwise popup is hidden.
     */
    public void setPopupVisible(boolean popupVisible) {
        if (getState().popupVisible != popupVisible) {
            getState().popupVisible = popupVisible;

            if (component == null) {
                return;
            }

            if (popupVisible) {
                if (component.getParent() != null && component.getParent() != this) {
                    // If the component already has a parent, try to remove it
                    AbstractSingleComponentContainer
                            .removeFromParent(component);
                }
                component.setParent(this);
            } else {
                if (component.getParent() == this) {
                    component.setParent(null);
                }
            }
            fireEvent(new PopupVisibilityEvent(this));
            markAsDirty();
        }
    }

    /**
     * Checks if the popup is visible.
     *
     * @return true, if popup is visible, false otherwise.
     */
    public boolean isPopupVisible() {
        return getState().popupVisible;
    }

    /**
     * Set the content component of the popup.
     *
     * @param component
     *            the component to be displayed in the popup.
     * @deprecated Use {@link #setContent(Component)}  instead
     */
    @Deprecated
    public void setComponent(Component component) {
        setContent(component);
    }

    @Override
    public Component getContent() {
        return component;
    }

    @Override
    public void setContent(Component content) {
        component = content;
        markAsDirty();
    }

    /**
     * Gets popup's opening direction.
     */
    public Alignment getDirection() {
        return direction;
    }

    /**
     * Sets opening direction for the popup. At the moment only support values are
     * {@link Alignment#BOTTOM_LEFT}, {@link Alignment#BOTTOM_CENTER} and
     * {@link Alignment#BOTTOM_RIGHT}.
     *
     * Default is {@link Alignment#BOTTOM_RIGHT}.
     */
    public void setDirection(final Alignment direction) {
        if (direction == null) {
            throw new IllegalArgumentException("direction cannot be null");
        }

        this.direction = direction;
        getState().direction = direction.getBitMask();
    }

    /**
     * Is visibility of the popup toggled on a button click?
     *
     * @see #isClosePopupOnOutsideClick
     */
    public boolean isButtonClickTogglesPopupVisibility() {
        return getState().buttonClickTogglesPopupVisibility;
    }

    /**
     * If true, clicking the button toggles visibility of the popup:
     * a visible popup will be hidden, and an invisible popup will be shown.
     *
     * Default is true.
     *
     * @see #setClosePopupOnOutsideClick
     */
    public void setButtonClickTogglesPopupVisibility(boolean buttonClickTogglesPopupVisibility) {
        getState().buttonClickTogglesPopupVisibility = buttonClickTogglesPopupVisibility;
    }

    /**
     * Is a click outside the popup closing the popup or not?
     *
     * @see #isButtonClickTogglesPopupVisibility
     */
    public boolean isClosePopupOnOutsideClick() {
        return getState().closePopupOnOutsideClick;
    }

    /**
     * If true, clicking on outside the popup closes it. Note that
     * this doesn't affect clicking on the button itself.
     *
     * Default is true.
     *
     * @see #setButtonClickTogglesPopupVisibility
     */
    public void setClosePopupOnOutsideClick(boolean closePopupOnOutsideClick) {
        getState().closePopupOnOutsideClick = closePopupOnOutsideClick;
    }

    /**
     * Add a listener that is called whenever the visibility of the popup is
     * changed.
     *
     * @param listener
     *            the listener to add
     * @see PopupVisibilityListener
     * @see PopupVisibilityEvent
     * @see #removePopupVisibilityListener(PopupVisibilityListener)
     *
     */
    public Registration addPopupVisibilityListener(PopupVisibilityListener listener) {
        return addListener(PopupVisibilityEvent.class, listener,
                PopupVisibilityListener.popupVisibilityMethod);
    }

    /**
     * Removes a previously added listener, so that it no longer receives events
     * when the visibility of the popup changes.
     *
     * @param listener
     *            the listener to remove
     * @see PopupVisibilityListener
     * @see #addPopupVisibilityListener(PopupVisibilityListener)
     */
    @Deprecated
    public void removePopupVisibilityListener(PopupVisibilityListener listener) {
        removeListener(PopupVisibilityEvent.class, listener,
                PopupVisibilityListener.popupVisibilityMethod);
    }

    /**
     * This event is received by the PopupVisibilityListeners when the
     * visibility of the popup changes. You can get the new visibility directly
     * with {@link #isPopupVisible()}, or get the PopupButton that produced the
     * event with {@link #getPopupButton()}.
     *
     */
    public class PopupVisibilityEvent extends Event {

        private static final long serialVersionUID = 3170716121022820317L;

        public PopupVisibilityEvent(PopupButton source) {
            super(source);
        }

        /**
         * Get the PopupButton instance that is the source of this event.
         *
         * @return the source PopupButton
         */
        public PopupButton getPopupButton() {
            return (PopupButton) getSource();
        }

        /**
         * Returns the current visibility of the popup.
         *
         * @return true if the popup is visible
         */
        public boolean isPopupVisible() {
            return getPopupButton().isPopupVisible();
        }
    }

    /**
     * Defines a listener that can receive a PopupVisibilityEvent when the
     * visibility of the popup changes.
     *
     */
    @FunctionalInterface
    public interface PopupVisibilityListener extends Serializable {

        Method popupVisibilityMethod = ReflectTools.findMethod(
                PopupVisibilityListener.class, "popupVisibilityChange",
                PopupVisibilityEvent.class);

        /**
         * Pass to {@link PopupVisibilityEvent} to start listening
         * for popup visibility changes.
         *
         * @param event
         *            the event
         *
         * @see PopupVisibilityEvent
         * @see PopupButton#addPopupVisibilityListener(PopupVisibilityListener)
         */
        void popupVisibilityChange(PopupVisibilityEvent event);
    }

    @Override
    public int getComponentCount() {
        return (isPopupVisible() && getContent() != null ? 1 : 0);
    }

    @Override
    public PopupButtonState getState() {
        return (PopupButtonState) super.getState();
    }

    protected void setPopupPositionComponent(Component component) {
        getState().popupPositionConnector = component;
    }

    @Override
    public Registration addComponentAttachListener(ComponentAttachListener listener) {
        return addListener(ComponentAttachEvent.class, listener,
                ComponentAttachListener.attachMethod);
    }

    @Override
    @Deprecated
    public void removeComponentAttachListener(ComponentAttachListener listener) {
        removeListener(ComponentAttachEvent.class, listener,
                ComponentAttachListener.attachMethod);
    }

    @Override
    public Registration addComponentDetachListener(ComponentDetachListener listener) {
        return addListener(ComponentDetachEvent.class, listener,
                ComponentDetachListener.detachMethod);
    }

    @Override
    @Deprecated
    public void removeComponentDetachListener(ComponentDetachListener listener) {
        removeListener(ComponentDetachEvent.class, listener,
                ComponentDetachListener.detachMethod);
    }
}
