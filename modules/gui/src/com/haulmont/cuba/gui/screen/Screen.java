/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.screen;

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.events.TriggerOnce;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.sys.WindowImplementation;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.util.OperationResult;
import org.springframework.context.ApplicationListener;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Base class for all screen controllers.
 *
 * @see Window
 */
public abstract class Screen implements FrameOwner {

    private String id;

    private ScreenContext screenContext;

    private ScreenData screenData;

    private Window window;

    private Settings settings;

    private EventHub eventHub = new EventHub();

    private BeanLocator beanLocator;

    // Global event listeners
    private List<ApplicationListener> uiEventListeners;

    // Extensions state
    private Map<Class<?>, Object> extensions;

    protected BeanLocator getBeanLocator() {
        return beanLocator;
    }

    @Inject
    protected void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    protected EventHub getEventHub() {
        return eventHub;
    }

    protected Map<Class<?>, Object> getExtensions() {
        return extensions;
    }

    protected void setExtensions(Map<Class<?>, Object> extensions) {
        this.extensions = extensions;
    }

    public String getId() {
        return id;
    }

    /**
     * Sets id of the screen. Called by the framework during screen init to assign screen id.
     *
     * @param id screen id
     */
    protected void setId(String id) {
        this.id = id;
    }

    /**
     * Use {@link UiControllerUtils#getScreenContext(FrameOwner)} to obtain Screen services from external code.
     *
     * @return screen context
     */
    ScreenContext getScreenContext() {
        return screenContext;
    }

    void setScreenContext(ScreenContext screenContext) {
        this.screenContext = screenContext;
    }

    protected ScreenData getScreenData() {
        return screenData;
    }

    protected void setScreenData(ScreenData data) {
        this.screenData = data;
    }

    protected <E> void fireEvent(Class<E> eventType, E event) {
        eventHub.publish(eventType, event);
    }

    /**
     * @return screen UI component
     */
    public Window getWindow() {
        return window;
    }

    protected void setWindow(Window window) {
        checkNotNullArgument(window);

        if (this.window != null) {
            throw new IllegalStateException("Screen already has Window");
        }
        this.window = window;
    }

    protected List<ApplicationListener> getUiEventListeners() {
        return uiEventListeners;
    }

    protected void setUiEventListeners(List<ApplicationListener> listeners) {
        this.uiEventListeners = listeners;

        if (listeners != null && !listeners.isEmpty()) {
            ((WindowImplementation) this.window).initUiEventListeners();
        }
    }

    /**
     * Adds {@link InitEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addInitListener(Consumer<InitEvent> listener) {
        return eventHub.subscribe(InitEvent.class, listener);
    }

    /**
     * Adds {@link AfterInitEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addAfterInitListener(Consumer<AfterInitEvent> listener) {
        return eventHub.subscribe(AfterInitEvent.class, listener);
    }

    /**
     * Adds {@link BeforeCloseEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addBeforeCloseListener(Consumer<BeforeCloseEvent> listener) {
        return eventHub.subscribe(BeforeCloseEvent.class, listener);
    }

    /**
     * Adds {@link BeforeShowEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addBeforeShowListener(Consumer<BeforeShowEvent> listener) {
        return eventHub.subscribe(BeforeShowEvent.class, listener);
    }

    /**
     * Adds {@link AfterShowEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addAfterShowListener(Consumer<AfterShowEvent> listener) {
        return eventHub.subscribe(AfterShowEvent.class, listener);
    }

    /**
     * Adds {@link AfterCloseEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    public Subscription addAfterCloseListener(Consumer<AfterCloseEvent> listener) {
        return eventHub.subscribe(AfterCloseEvent.class, listener);
    }

    /**
     * Adds {@link AfterDetachEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addAfterDetachListener(Consumer<AfterDetachEvent> listener) {
        return eventHub.subscribe(AfterDetachEvent.class, listener);
    }

    /**
     * Adds {@link UrlParamsChangedEvent} listener.
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addUrlParamsChangeListener(Consumer<UrlParamsChangedEvent> listener) {
        return eventHub.subscribe(UrlParamsChangedEvent.class, listener);
    }

    /**
     * Shows this screen.
     *
     * @see Screens#show(Screen)
     */
    public Screen show() {
        getScreenContext().getScreens().show(this);
        return this;
    }

    /**
     * Request screen close with passed action.
     *
     * @param action close action
     * @return result of operation
     */
    public OperationResult close(CloseAction action) {
        BeforeCloseEvent beforeCloseEvent = new BeforeCloseEvent(this, action);
        fireEvent(BeforeCloseEvent.class, beforeCloseEvent);
        if (beforeCloseEvent.isClosePrevented()) {
            if (beforeCloseEvent.getCloseResult() != null) {
                return beforeCloseEvent.getCloseResult();
            }

            return OperationResult.fail();
        }

        // save settings right before removing
        if (isSaveSettingsOnClose(action)) {
            saveSettings();
        }

        screenContext.getScreens().remove(this);

        AfterCloseEvent afterCloseEvent = new AfterCloseEvent(this, action);
        fireEvent(AfterCloseEvent.class, afterCloseEvent);

        return OperationResult.success();
    }

    protected boolean isSaveSettingsOnClose(@SuppressWarnings("unused") CloseAction action) {
        Configuration configuration = beanLocator.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        return !clientConfig.getManualScreenSettingsSaving();
    }

    /**
     * @return screen settings
     */
    @Nullable
    protected Settings getSettings() {
        return settings;
    }

    /**
     * Saves screen settings.
     */
    protected void saveSettings() {
        if (settings != null) {
            ScreenSettings screenSettings = getBeanLocator().get(ScreenSettings.NAME);
            screenSettings.saveSettings(this, settings);
        }
    }

    /**
     * Applies screen settings to UI components.
     *
     * @param settings screen settings
     */
    protected void applySettings(Settings settings) {
        this.settings = settings;

        ScreenSettings screenSettings = getBeanLocator().get(ScreenSettings.NAME);
        screenSettings.applySettings(this, settings);
    }

    /**
     * Deletes screen settings associated with this screen.
     */
    protected void deleteSettings() {
        settings.delete();
    }

    /**
     * Event sent when the screen controller is created and dependency injection is completed. <br>
     * There are two ways to add event handler:
     * <br>
     * 1. Programmatically from the class constructor:
     * <pre>{@code
     *    addInitListener(event -> {
     *       // handle event here
     *    });
     * }</pre>
     * <p>
     * 2. Declaratively using method with {@link Subscribe} annotation: <br>
     * <pre>{@code
     *    @Subscribe
     *    protected void onInit(InitEvent event) {
     *       // handle event here
     *    }
     * }</pre>
     *
     * @see #addInitListener(Consumer)
     */
    @TriggerOnce
    public static class InitEvent extends EventObject {
        protected final ScreenOptions options;

        public InitEvent(Screen source, ScreenOptions options) {
            super(source);
            this.options = options;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        public ScreenOptions getOptions() {
            return options;
        }
    }

    /**
     * Event sent when the screen controller is created, dependency injection is completed, and all components have
     * completed their internal initialization procedures. Nested screen fragments (if any) have sent their
     * {@code InitEvent} and {@code AfterInitEvent}. <br>
     * There are two ways to add event handler:
     * <br>
     * 1. Programmatically from the class constructor:
     * <pre>{@code
     *    addAfterInitListener(event -> {
     *       // handle event here
     *    });
     * }</pre>
     * <p>
     * 2. Declaratively using method with {@link Subscribe} annotation: <br>
     * <pre>{@code
     *    @Subscribe
     *    protected void onAfterInit(AfterInitEvent event) {
     *       // handle event here
     *    }
     * }</pre>
     *
     * @see #addAfterInitListener(Consumer)
     */
    @TriggerOnce
    public static class AfterInitEvent extends EventObject {
        protected final ScreenOptions options;

        public AfterInitEvent(Screen source, ScreenOptions options) {
            super(source);
            this.options = options;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        public ScreenOptions getOptions() {
            return options;
        }
    }

    /**
     * Event sent right before the screen is shown by {@link Screens#show(Screen)} method. <br>
     * There are two ways to add an event handler:
     * <br>
     * 1. Programmatically:
     * <pre>{@code
     *    addBeforeShowListener(event -> {
     *       // handle event here
     *    });
     * }</pre>
     * <p>
     * 2. Declaratively using method with {@link Subscribe} annotation: <br>
     * <pre>{@code
     *    @Subscribe
     *    protected void onBeforeShow(BeforeShowEvent event) {
     *       // handle event here
     *    }
     * }</pre>
     *
     * @see #addBeforeShowListener(Consumer)
     */
    @TriggerOnce
    public static class BeforeShowEvent extends EventObject {
        public BeforeShowEvent(Screen source) {
            super(source);
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }
    }

    /**
     * Event sent after the screen is shown by {@link Screens#show(Screen)} method. <br>
     * There are two ways to add an event handler:
     * <br>
     * 1. Programmatically:
     * <pre>{@code
     *    addAfterShowListener(event -> {
     *       // handle event here
     *    });
     * }</pre>
     * <p>
     * 2. Declaratively using method with {@link Subscribe} annotation: <br>
     * <pre>{@code
     *    @Subscribe
     *    protected void onAfterShow(AfterShowEvent event) {
     *       // handle event here
     *    }
     * }</pre>
     *
     * @see #addAfterShowListener(Consumer)
     */
    @TriggerOnce
    public static class AfterShowEvent extends EventObject {
        public AfterShowEvent(Screen source) {
            super(source);
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }
    }

    /**
     * Event sent right before the screen is closed by its {@link #close(CloseAction)} method. <br>
     * Event handlers may prevent screen close using {@link #preventWindowClose()} method. <br>
     * There are two ways to add an event handler:
     * <br>
     * 1. Programmatically:
     * <pre>{@code
     *    addBeforeCloseListener(event -> {
     *       // handle event here
     *    });
     * }</pre>
     * <p>
     * 2. Declaratively using method with {@link Subscribe} annotation: <br>
     * <pre>{@code
     *    @Subscribe
     *    protected void onBeforeClose(BeforeCloseEvent event) {
     *       // handle event here
     *    }
     * }</pre>
     *
     * @see #addBeforeCloseListener(Consumer)
     */
    public static class BeforeCloseEvent extends EventObject {

        protected final CloseAction closeAction;
        protected boolean closePrevented = false;

        protected OperationResult closeResult;

        public BeforeCloseEvent(Screen source, CloseAction closeAction) {
            super(source);
            this.closeAction = closeAction;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        public Screen getScreen() {
            return (Screen) super.getSource();
        }

        public CloseAction getCloseAction() {
            return closeAction;
        }

        public void preventWindowClose() {
            this.closePrevented = true;
        }

        public void preventWindowClose(OperationResult closeResult) {
            this.closePrevented = true;
            this.closeResult = closeResult;
        }

        @Nullable
        public OperationResult getCloseResult() {
            return closeResult;
        }

        public boolean isClosePrevented() {
            return closePrevented;
        }
    }

    /**
     * Event sent after the screen is closed by its {@link #close(CloseAction)} method. <br>
     * Caller may add this listener after screen creation. <br>
     * There are two ways to add an event handler:
     * <br>
     * 1. Programmatically:
     * <pre>{@code
     *    addAfterCloseListener(event -> {
     *       // handle event here
     *    });
     * }</pre>
     * <p>
     * 2. Declaratively using method with {@link Subscribe} annotation: <br>
     * <pre>{@code
     *    @Subscribe
     *    protected void onAfterClose(AfterCloseEvent event) {
     *       // handle event here
     *    }
     * }</pre>
     * @see #addAfterCloseListener(Consumer)
     */
    public static class AfterCloseEvent extends EventObject {

        protected final CloseAction closeAction;

        public AfterCloseEvent(Screen source, CloseAction closeAction) {
            super(source);
            this.closeAction = closeAction;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        public Screen getScreen() {
            return (Screen) super.getSource();
        }

        public CloseAction getCloseAction() {
            return closeAction;
        }
    }

    /**
     * Event sent after the screen is removed from UI. This event can be used for cleaning up resources. <br>
     * There are two ways to add an event handler:
     * <br>
     * 1. Programmatically:
     * <pre>{@code
     *    addAfterDetachListener(event -> {
     *       // handle event here
     *    });
     * }</pre>
     * <p>
     * 2. Declaratively using method with {@link Subscribe} annotation: <br>
     * <pre>{@code
     *    @Subscribe
     *    protected void onAfterDetach(AfterDetachEvent event) {
     *       // handle event here
     *    }
     * }</pre>
     * @see #addAfterDetachListener(Consumer)
     */
    public static class AfterDetachEvent extends EventObject {

        public AfterDetachEvent(Screen source) {
            super(source);
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }
    }
}