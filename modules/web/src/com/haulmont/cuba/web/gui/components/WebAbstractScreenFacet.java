/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.ScreenFacet;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;
import com.haulmont.cuba.gui.sys.UiControllerProperty;
import com.haulmont.cuba.gui.sys.UiControllerPropertyInjector;
import com.haulmont.cuba.web.gui.WebAbstractFacet;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public abstract class WebAbstractScreenFacet<S extends Screen> extends WebAbstractFacet
        implements ScreenFacet<S> {

    protected BeanLocator beanLocator;

    protected String screenId;
    protected Class<S> screenClass;

    protected Screens.LaunchMode launchMode = OpenMode.NEW_TAB;

    protected Supplier<ScreenOptions> optionsProvider;
    protected Collection<UiControllerProperty> properties;

    protected String actionId;
    protected String buttonId;

    protected S screen;

    @Override
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Override
    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    @Override
    public String getScreenId() {
        return screenId;
    }

    @Override
    public void setScreenClass(Class<S> screenClass) {
        this.screenClass = screenClass;
    }

    @Override
    public Class<S> getScreenClass() {
        return screenClass;
    }

    @Override
    public void setLaunchMode(Screens.LaunchMode launchMode) {
        this.launchMode = launchMode;
    }

    @Override
    public Screens.LaunchMode getLaunchMode() {
        return launchMode;
    }

    @Override
    public void setOptionsProvider(Supplier<ScreenOptions> optionsProvider) {
        this.optionsProvider = optionsProvider;
    }

    @Override
    public Supplier<ScreenOptions> getOptionsProvider() {
        return optionsProvider;
    }

    @Override
    public void setProperties(Collection<UiControllerProperty> properties) {
        this.properties = properties;
    }

    @Override
    public Collection<UiControllerProperty> getProperties() {
        return properties;
    }

    @Override
    public String getActionTarget() {
        return actionId;
    }

    @Override
    public void setActionTarget(String actionId) {
        this.actionId = actionId;
    }

    @Override
    public String getButtonTarget() {
        return buttonId;
    }

    @Override
    public void setButtonTarget(String buttonId) {
        this.buttonId = buttonId;
    }

    @Override
    public void setOwner(@Nullable Frame owner) {
        super.setOwner(owner);

        subscribe();
    }

    @Override
    public Subscription addAfterShowEventListener(Consumer<AfterShowEvent> listener) {
        return getEventHub().subscribe(AfterShowEvent.class, listener);
    }

    @Override
    public Subscription addAfterCloseEventListener(Consumer<AfterCloseEvent> listener) {
        return getEventHub().subscribe(AfterCloseEvent.class, listener);
    }

    protected void initScreenListeners(Screen screen) {
        screen.addAfterShowListener(this::fireAfterShowEvent);
        screen.addAfterCloseListener(this::fireAfterCloseEvent);
    }

    protected void fireAfterShowEvent(Screen.AfterShowEvent event) {
        AfterShowEvent afterShowEvent = new AfterShowEvent(this, event.getSource());
        publish(AfterShowEvent.class, afterShowEvent);
    }

    protected void fireAfterCloseEvent(Screen.AfterCloseEvent event) {
        AfterCloseEvent afterCloseEvent = new AfterCloseEvent(this, event.getSource());
        publish(AfterCloseEvent.class, afterCloseEvent);
    }

    protected void subscribe() {
        Frame owner = getOwner();
        if (owner == null) {
            throw new IllegalStateException("Notification is not attached to Frame");
        }

        if (isNotEmpty(actionId)
                && isNotEmpty(buttonId)) {
            throw new GuiDevelopmentException(
                    "Notification facet should have either action or button target", owner.getId());
        }

        if (isNotEmpty(actionId)) {
            subscribeOnAction(owner);
        } else if (isNotEmpty(buttonId)) {
            subscribeOnButton(owner);
        }
    }

    protected void subscribeOnAction(Frame owner) {
        Action action = WebComponentsHelper.findAction(owner, actionId);

        if (!(action instanceof BaseAction)) {
            throw new GuiDevelopmentException(
                    String.format("Unable to find Screen target action with id '%s'", actionId),
                    owner.getId());
        }

        ((BaseAction) action).addActionPerformedListener(e -> show());
    }

    protected void subscribeOnButton(Frame owner) {
        Component component = owner.getComponent(buttonId);

        if (!(component instanceof Button)) {
            throw new GuiDevelopmentException(
                    String.format("Unable to find Screen target button with id '%s'", buttonId),
                    owner.getId());
        }

        ((Button) component).addClickListener(e -> show());
    }

    protected void injectScreenProperties(S screen, Collection<UiControllerProperty> properties) {
        if (CollectionUtils.isNotEmpty(properties)) {
            if (beanLocator == null) {
                throw new IllegalStateException("Unable to inject properties. BeanLocator is null.");
            }

            UiControllerPropertyInjector injector =
                    beanLocator.getPrototype(UiControllerPropertyInjector.NAME,
                            screen, owner.getFrameOwner(), properties);

            injector.inject();
        }
    }

    protected ScreenOptions getScreenOptions() {
        return optionsProvider != null
                ? optionsProvider.get()
                : FrameOwner.NO_OPTIONS;
    }
}
