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
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.sys.ScreenHistorySupport;
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
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onInit(InitEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addInitListener(Consumer<InitEvent> listener) {
        return eventHub.subscribe(InitEvent.class, listener);
    }

    /**
     * Adds {@link AfterInitEvent} listener.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onAfterInit(AfterInitEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addAfterInitListener(Consumer<AfterInitEvent> listener) {
        return eventHub.subscribe(AfterInitEvent.class, listener);
    }

    /**
     * Adds {@link BeforeCloseEvent} listener.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onBeforeClose(BeforeCloseEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addBeforeCloseListener(Consumer<BeforeCloseEvent> listener) {
        return eventHub.subscribe(BeforeCloseEvent.class, listener);
    }

    /**
     * Adds {@link BeforeShowEvent} listener.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onBeforeShow(BeforeShowEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addBeforeShowListener(Consumer<BeforeShowEvent> listener) {
        return eventHub.subscribe(BeforeShowEvent.class, listener);
    }

    /**
     * Adds {@link AfterShowEvent} listener.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onAfterShow(AfterShowEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    public Subscription addAfterShowListener(Consumer<AfterShowEvent> listener) {
        return eventHub.subscribe(AfterShowEvent.class, listener);
    }

    /**
     * Adds {@link AfterCloseEvent} listener.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onAfterClose(AfterCloseEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    public Subscription addAfterCloseListener(Consumer<AfterCloseEvent> listener) {
        return eventHub.subscribe(AfterCloseEvent.class, listener);
    }

    /**
     * Adds {@link AfterDetachEvent} listener.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onAfterDetach(AfterDetachEvent event) {
     *       // handle event here
     *    }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    protected Subscription addAfterDetachListener(Consumer<AfterDetachEvent> listener) {
        return eventHub.subscribe(AfterDetachEvent.class, listener);
    }

    /**
     * Adds {@link UrlParamsChangedEvent} listener.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe
     *    protected void onUrlParamsChanged(UrlParamsChangedEvent event) {
     *       // handle event here
     *    }
     * </pre>
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
     * Requests closing of the screen caused by the given action.
     *
     * @param action close action which is propagated to {@link BeforeCloseEvent} and {@link AfterCloseEvent}
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

        if (isSaveScreenHistoryOnClose(action)) {
            ScreenHistorySupport screenHistorySupport = beanLocator.get(ScreenHistorySupport.NAME);
            screenHistorySupport.saveScreenHistory(this);
        }

        screenContext.getScreens().remove(this);

        AfterCloseEvent afterCloseEvent = new AfterCloseEvent(this, action);
        fireEvent(AfterCloseEvent.class, afterCloseEvent);

        return OperationResult.success();
    }

    /**
     * Closes the screen with {@link #WINDOW_CLOSE_ACTION} action.
     *
     * @return result of close request
     */
    public OperationResult closeWithDefaultAction() {
        return close(WINDOW_CLOSE_ACTION);
    }

    /**
     * Requests closing of the screen with the given {@code outcome}.
     *
     * @param outcome {@link StandardOutcome}
     * @return result of operation
     */
    public OperationResult close(StandardOutcome outcome) {
        return close(outcome.getCloseAction());
    }

    /**
     * @param action close action
     * @return true if the screen should be registered in UI history
     */
    protected boolean isSaveScreenHistoryOnClose(@SuppressWarnings("unused") CloseAction action) {
        return true;
    }

    /**
     * @param action close action
     * @return true if UI settings should be saved
     */
    protected boolean isSaveSettingsOnClose(@SuppressWarnings("unused") CloseAction action) {
        Configuration configuration = beanLocator.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        return !clientConfig.getManualScreenSettingsSaving();
    }

    /**
     * @return true if screen can be opened multiple times from a navigation menu
     */
    protected boolean isMultipleOpen() {
        WindowInfo windowInfo = UiControllerUtils.getScreenContext(this).getWindowInfo();
        if (windowInfo.getDescriptor() != null) {
            String multipleOpenAttr = windowInfo.getDescriptor().attributeValue("multipleOpen");

            if (multipleOpenAttr != null) {
                return Boolean.parseBoolean(multipleOpenAttr);
            }
        }

        MultipleOpen annotation = this.getClass().getAnnotation(MultipleOpen.class);
        if (annotation != null) {
            // default is false
            return annotation.value();
        }

        return false;
    }

    /**
     * Compares this screen with an already opened screen.
     *
     * @param openedScreen already opened screen
     * @return true if screens are the same
     */
    protected boolean isSameScreen(Screen openedScreen) {
        return this.getClass() == openedScreen.getClass()
                && this.getId().equals(openedScreen.getId());
    }

    /**
     * @return screen settings
     */
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
     * Applies screen settings to data components.
     *
     * @param settings screen settings
     */
    protected void applyDataLoadingSettings(Settings settings) {
        this.settings = settings;

        ScreenSettings screenSettings = getBeanLocator().get(ScreenSettings.NAME);
        screenSettings.applyDataLoadingSettings(this, settings);
    }

    /**
     * Deletes screen settings associated with this screen.
     */
    protected void deleteSettings() {
        settings.delete();
    }

    /**
     * Event sent when the screen controller and all its declaratively defined components are created, and dependency
     * injection is completed. Nested fragments are not initialized yet. Some visual components are not fully
     * initialized, for example buttons are not linked with actions.
     * <p>
     * In this event listener, you can create visual and data components, for example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onInit(InitEvent event) {
     *         Label&lt;String&gt; label = uiComponents.create(Label.TYPE_STRING);
     *         label.setValue("Hello World");
     *         getWindow().add(label);
     *     }
     * </pre>
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
     * Event sent when the screen controller and all its declaratively defined components are created, dependency
     * injection is completed, and all components have completed their internal initialization procedures.
     * Nested screen fragments (if any) have sent their {@code InitEvent} and {@code AfterInitEvent}.
     * <p>
     * In this event listener, you can create visual and data components and perform additional initialization
     * if it depends on initialized nested fragments.
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
     * Event sent right before the screen is shown, i.e. it is not added to the application UI yet.
     * Security restrictions are applied to UI components. Saved component settings are not yet applied to UI components.
     * Data is not loaded yet for screens annotated with {@code @LoadDataBeforeShow}.
     * <p>
     * In this event listener, you can load data, check permissions and modify UI components. For example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onBeforeShow(BeforeShowEvent event) {
     *         customersDl.load();
     *     }
     * </pre>
     *
     * You can abort the process of opening the screen by throwing an exception. Note that if you have created
     * notifications in this listener before the exception, they will still be shown even though the screen will not.
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
     * Event sent right after the screen is shown, i.e. when it is added to the application UI.
     * Saved component settings are applied to UI components.
     * <p>
     * In this event listener, you can show notifications, dialogs or other screens. For example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onAfterShow(AfterShowEvent event) {
     *         notifications.create().withCaption("Just opened").show();
     *     }
     * </pre>
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
     * Event sent right before the screen is closed by its {@link #close(CloseAction)} method. The screen is still
     * displayed and fully functional. Component settings are not saved yet.
     * <p>
     * In this event listener, you can check any conditions and prevent screen closing using the
     * {@link #preventWindowClose()} method of the event, for example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onBeforeClose(BeforeCloseEvent event) {
     *         if (Strings.isNullOrEmpty(textField.getValue())) {
     *             notifications.create().withCaption("Input required").show();
     *             event.preventWindowClose();
     *         }
     *     }
     * </pre>
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

        /**
         * @return action passed to the {@link #close(CloseAction)} method of the screen.
         */
        public CloseAction getCloseAction() {
            return closeAction;
        }

        /**
         * Prevents closing of the screen.
         */
        public void preventWindowClose() {
            this.closePrevented = true;
        }

        /**
         * Prevents closing of the screen.
         *
         * @param closeResult result object returned from the {@link #close(CloseAction)} method
         */
        public void preventWindowClose(OperationResult closeResult) {
            this.closePrevented = true;
            this.closeResult = closeResult;
        }

        /**
         * @return result passed to the {@link #preventWindowClose(OperationResult)} method
         */
        @Nullable
        public OperationResult getCloseResult() {
            return closeResult;
        }

        /**
         * @return whether the closing was prevented by invoking {@link #preventWindowClose()} method
         */
        public boolean isClosePrevented() {
            return closePrevented;
        }

        /**
         * Checks that screen was closed with the given {@code outcome}.
         */
        public boolean closedWith(StandardOutcome outcome) {
            return outcome.getCloseAction().equals(closeAction);
        }
    }

    /**
     * Event sent after the screen is closed by its {@link #close(CloseAction)} method and after {@link AfterDetachEvent}.
     * Component settings are saved.
     * <p>
     * In this event listener, you can show notifications or dialogs after closing the screen, for example:
     * <pre>
     *     &#64;Subscribe
     *     protected void onAfterClose(AfterCloseEvent event) {
     *         notifications.create().withCaption("Just closed").show();
     *     }
     * </pre>
     *
     * @see #addAfterCloseListener(Consumer)
     */
    @TriggerOnce
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

        /**
         * @return action passed to the {@link #close(CloseAction)} method of the screen.
         */
        public CloseAction getCloseAction() {
            return closeAction;
        }

        /**
         * Checks that screen was closed with the given {@code outcome}.
         */
        public boolean closedWith(StandardOutcome outcome) {
            return outcome.getCloseAction().equals(closeAction);
        }
    }

    /**
     * Event sent after the screen is removed from the application UI when it is closed by the user or when the user logs out.
     * <p>
     * This event listener can be used for releasing resources acquired by the screen.
     * Note that this event is not sent on HTTP session expiration.
     *
     * @see #addAfterDetachListener(Consumer)
     */
    @TriggerOnce
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