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

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.sys.BeanLocatorAware;
import com.haulmont.cuba.gui.Screens.LaunchMode;
import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioDelegate;
import com.haulmont.cuba.gui.meta.StudioFacet;
import com.haulmont.cuba.gui.meta.StudioProperties;
import com.haulmont.cuba.gui.meta.StudioProperty;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;
import com.haulmont.cuba.gui.sys.UiControllerProperty;

import java.util.Collection;
import java.util.EventObject;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Prepares and shows screens.
 */
@StudioFacet(
        caption = "Screen",
        description = "Prepares and shows screens",
        defaultProperty = "screenId"
)
@StudioProperties(
        properties = {
                @StudioProperty(name = "id", required = true)
        }
)
public interface ScreenFacet<S extends Screen> extends Facet, BeanLocatorAware {

    /**
     * Sets the id of screen to open.
     *
     * @param screenId screen id
     */
    @StudioProperty(type = PropertyType.STRING)
    void setScreenId(String screenId);

    /**
     * @return screen id
     */
    String getScreenId();

    /**
     * Sets class of screen to open.
     *
     * @param screenClass screen class
     */
    @StudioProperty(type = PropertyType.JAVA_CLASS_NAME)
    void setScreenClass(Class<S> screenClass);

    /**
     * @return class of screen to open
     */
    Class<S> getScreenClass();

    /**
     * Sets screen {@link LaunchMode}.
     *
     * @param launchMode launch mode
     */
    void setLaunchMode(LaunchMode launchMode);

    /**
     * @return screen {@link LaunchMode}
     */
    LaunchMode getLaunchMode();

    /**
     * Sets the given {@code Supplier} as screen options provider.
     *
     * @param optionsProvider screen options provider
     */
    @StudioDelegate
    void setOptionsProvider(Supplier<ScreenOptions> optionsProvider);

    /**
     * @return {@link ScreenOptions} provider
     */
    Supplier<ScreenOptions> getOptionsProvider();

    /**
     * Sets properties that will be injected into opened screen via public setters.
     *
     * @param properties screen properties
     */
    void setProperties(Collection<UiControllerProperty> properties);

    /**
     * @return properties that will be injected into opened screen via public setters.
     */
    Collection<UiControllerProperty> getProperties();

    /**
     * @return id of action that triggers screen
     */
    String getActionTarget();

    /**
     * Sets that screen should be shown when action with id {@code actionId} is performed.
     *
     * @param actionId action id
     */
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    void setActionTarget(String actionId);

    /**
     * @return id of button that triggers screen
     */
    String getButtonTarget();

    /**
     * Sets that screen should be shown when button with id {@code actionId} is clicked.
     *
     * @param buttonId button id
     */
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    void setButtonTarget(String buttonId);

    /**
     * @return new screen instance
     */
    S create();

    /**
     * Shows and returns screen.
     */
    S show();

    /**
     * Adds the given {@code Consumer} as screen after show event listener.
     *
     * @param listener listener
     * @return after show event subscription
     */
    Subscription addAfterShowEventListener(Consumer<AfterShowEvent> listener);

    /**
     * Adds the given {@code Consumer} as screen after close event listener.
     *
     * @param listener listener
     * @return after close event subscription
     */
    Subscription addAfterCloseEventListener(Consumer<AfterCloseEvent> listener);

    /**
     * Event that is fired after screen show.
     */
    class AfterShowEvent extends EventObject {

        protected Screen screen;

        public AfterShowEvent(ScreenFacet source, Screen screen) {
            super(source);
            this.screen = screen;
        }

        @Override
        public ScreenFacet getSource() {
            return (ScreenFacet) super.getSource();
        }

        public Screen getScreen() {
            return screen;
        }
    }

    /**
     * Event that is fired when screen is closed.
     */
    class AfterCloseEvent extends EventObject {

        protected Screen screen;

        public AfterCloseEvent(ScreenFacet source, Screen screen) {
            super(source);
            this.screen = screen;
        }

        @Override
        public ScreenFacet getSource() {
            return (ScreenFacet) super.getSource();
        }

        public Screen getScreen() {
            return screen;
        }
    }
}
