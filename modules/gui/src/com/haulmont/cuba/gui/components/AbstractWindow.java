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
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.DialogOptions;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.security.ActionsPermissions;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.screen.events.AfterShowEvent;
import com.haulmont.cuba.gui.screen.events.CloseTriggeredEvent;
import com.haulmont.cuba.gui.screen.events.InitEvent;
import com.haulmont.cuba.gui.settings.Settings;
import org.dom4j.Element;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base class for simple screen controllers.
 */
public class AbstractWindow extends Screen implements Window, LegacyFrame, Component.HasXmlDescriptor, Window.Wrapper,
        SecuredActionsHolder {

    public static final String UNKNOWN_CLOSE_ACTION_ID = "unknown";

    protected Frame frame;
    private Object _companion;

    private Component parent;
    private List<ApplicationListener> uiEventListeners;

    private DsContext dsContext;

    @Inject
    protected Messages messages;
    @Inject
    private MessageBundle messageBundle;

    public AbstractWindow() {
    }

    @Override
    protected void setWindow(Window window) {
        super.setWindow(window);

        this.frame = window;
    }

    @Override
    public WindowManager getWindowManager() {
        return frame.getWindowManager();
    }

    @Override
    public Frame getWrappedFrame() {
        return frame;
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @Subscribe
    protected void init(InitEvent initEvent) {
        Map<String, Object> params = Collections.emptyMap();
        ScreenOptions options = initEvent.getOptions();
        if (options instanceof MapScreenOptions) {
            params = ((MapScreenOptions) options).getParams();
        }

        init(params);
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @Subscribe
    protected void afterShow(AfterShowEvent event) {
        ready();
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @Subscribe
    protected void onCloseTriggered(CloseTriggeredEvent event) {
        String actionId = event.getCloseAction() instanceof StandardCloseAction ?
            ((StandardCloseAction) event.getCloseAction()).getActionId() : UNKNOWN_CLOSE_ACTION_ID;

        if (!preClose(actionId)) {
            event.preventWindowClose();
        }
    }

    /**
     * Called by the framework after creation of all components and before showing the screen.
     * <br> Override this method and put initialization logic here.
     *
     * @param params parameters passed from caller's code, usually from
     *               {@link #openWindow(String, WindowManager.OpenType)} and similar methods, or set in
     *               {@code screens.xml} for this registered screen
     */
    public void init(Map<String, Object> params) {
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public Component getParent() {
        return parent;
    }

    @Override
    public void setParent(Component parent) {
        this.parent = parent;
    }

    @Override
    public boolean isEnabled() {
        return frame.isEnabled();
    }

    @Override
    public boolean isEnabledRecursive() {
        return frame.isEnabledRecursive();
    }

    @Override
    public void setEnabled(boolean enabled) {
        frame.setEnabled(enabled);
    }

    @Override
    public boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    @Override
    public boolean isVisibleRecursive() {
        return frame.isVisibleRecursive();
    }

    @Override
    public float getHeight() {
        return frame.getHeight();
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return frame.getHeightSizeUnit();
    }

    @Override
    public void setHeight(String height) {
        frame.setHeight(height);
    }

    @Override
    public float getWidth() {
        return frame.getWidth();
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return frame.getWidthSizeUnit();
    }

    @Override
    public void setWidth(String width) {
        frame.setWidth(width);
    }

    @Override
    public Alignment getAlignment() {
        return frame.getAlignment();
    }

    @Override
    public void setAlignment(Alignment alignment) {
        frame.setAlignment(alignment);
    }

    @Override
    public boolean isResponsive() {
        return frame.isResponsive();
    }

    @Override
    public void setResponsive(boolean responsive) {
        frame.setResponsive(responsive);
    }

    @Override
    public String getIcon() {
        return frame.getIcon();
    }

    @Override
    public void setIcon(String icon) {
        frame.setIcon(icon);
    }

    @Override
    public void setIconFromSet(Icons.Icon icon) {
        frame.setIconFromSet(icon);
    }

    @Override
    public void add(Component component) {
        frame.add(component);
    }

    @Override
    public void remove(Component component) {
        frame.remove(component);
    }

    @Override
    public void removeAll() {
        frame.removeAll();
    }

    @Override
    public Component getOwnComponent(String id) {
        return frame.getOwnComponent(id);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return frame.getComponent(id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return frame.getOwnComponents();
    }

    @Override
    public Collection<Component> getComponents() {
        return frame.getComponents();
    }

    @Override
    public Object getComponent() {
        return frame;
    }

    @Override
    public Object getComposition() {
        return frame;
    }

    @Override
    public void expand(Component component, String height, String width) {
        frame.expand(component, height, width);
    }

    @Override
    public void expand(Component component) {
        frame.expand(component);
    }

    @Override
    public void resetExpanded() {
        frame.resetExpanded();
    }

    @Override
    public boolean isExpanded(Component component) {
        return frame.isExpanded(component);
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return ExpandDirection.VERTICAL;
    }

    @Override
    public String getMessagesPack() {
        return messageBundle.getMessagesPack();
    }

    @Override
    public void setMessagesPack(String name) {
        messageBundle.setMessagesPack(name);
    }

    /**
     * Get localized message from the message pack associated with this frame or window.
     *
     * @param key message key
     * @return localized message
     * @see Messages#getMessage(String, String)
     */
    protected String getMessage(String key) {
        return messageBundle.getMessage(key);
    }

    /**
     * Get localized message from the message pack associated with this frame or window, and use it as a format
     * string for parameters provided.
     *
     * @param key    message key
     * @param params parameter values
     * @return formatted string or the key in case of IllegalFormatException
     * @see Messages#formatMessage(String, String, Object...)
     */
    protected String formatMessage(String key, Object... params) {
        return messageBundle.formatMessage(key, params);
    }

    @Override
    public boolean isValid() {
        return frame.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        frame.validate();
    }

    @Override
    public void add(Component childComponent, int index) {
        frame.add(childComponent, index);
    }

    @Override
    public int indexOf(Component component) {
        return frame.indexOf(component);
    }

    @Nullable
    @Override
    public Component getComponent(int index) {
        return frame.getComponent(index);
    }

    /**
     * @return a companion implementation, specific for the current client type
     */
    @Nullable
    public <T> T getCompanion() {
        //noinspection unchecked
        return (T) _companion;
    }

    /** INTERNAL. Don't call from application code. */
    public void setCompanion(Object companion) {
        this._companion = companion;
    }

    /** INTERNAL. Don't call from application code. */
    @Nullable
    public List<ApplicationListener> getUiEventListeners() {
        return uiEventListeners;
    }

    /** INTERNAL. Don't call from application code. */
    public void setUiEventListeners(List<ApplicationListener> uiEventListeners) {
        this.uiEventListeners = uiEventListeners;
    }

    @Override
    public Frame getFrame() {
        return this.frame.getFrame();
    }

    @Override
    public void setFrame(Frame frame) {
        // do nothing
    }

    @Override
    public String getStyleName() {
        return frame.getStyleName();
    }

    @Override
    public void setStyleName(String styleName) {
        frame.setStyleName(styleName);
    }

    @Override
    public void addStyleName(String styleName) {
        frame.addStyleName(styleName);
    }

    @Override
    public void removeStyleName(String styleName) {
        frame.removeStyleName(styleName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrap(Class<X> internalComponentClass) {
        if (getComponent() instanceof Component.Wrapper) {
            return (X) ((Component.Wrapper) frame).getComponent();
        }

        return (X) frame;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrapComposition(Class<X> internalCompositionClass) {
        if (getComposition() instanceof Component.Wrapper) {
            return (X) ((Component.Wrapper) frame).getComposition();
        }

        return (X) frame;
    }

    @Override
    public void setSpacing(boolean enabled) {
        frame.setSpacing(enabled);
    }

    @Override
    public boolean getSpacing() {
        return frame.getSpacing();
    }

    @Override
    public void setMargin(MarginInfo marginInfo) {
        frame.setMargin(marginInfo);
    }

    @Override
    public MarginInfo getMargin() {
        return frame.getMargin();
    }

    @Override
    public void addAction(Action action) {
        frame.addAction(action);
    }

    @Override
    public void addAction(Action action, int index) {
        frame.addAction(action, index);
    }

    @Override
    public void removeAction(@Nullable Action action) {
        frame.removeAction(action);
    }

    @Override
    public void removeAction(@Nullable String id) {
        frame.removeAction(id);
    }

    @Override
    public void removeAllActions() {
        frame.removeAllActions();
    }

    @Override
    public Collection<Action> getActions() {
        return frame.getActions();
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        return frame.getAction(id);
    }

    @Override
    public Element getXmlDescriptor() {
        return ((HasXmlDescriptor) frame).getXmlDescriptor();
    }

    @Override
    public void setXmlDescriptor(Element element) {
        ((HasXmlDescriptor) frame).setXmlDescriptor(element);
    }

    @Override
    public void setCloseable(boolean closeable) {
        ((Window)this.frame).setCloseable(closeable);
    }

    @Override
    public boolean isCloseable() {
        return ((Window)this.frame).isCloseable();
    }

    @Override
    public Screen getFrameOwner() {
        return this;
    }

    @Override
    public WindowContext getContext() {
        return (WindowContext) frame.getContext();
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
    }

    @Override
    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    /**
     * @return screen caption which is set in XML or via {@link #setCaption(String)}
     */
    @Override
    public String getCaption() {
        return frame.getCaption();
    }

    /**
     * Set the screen caption. If called in {@link #init(java.util.Map)}, overrides the value from XML.
     * @param caption   caption
     */
    @Override
    public void setCaption(String caption) {
        frame.setCaption(caption);
    }

    /**
     * Screen description is used by the framework to show some specified information, e.g. current filter or folder
     * name. We don't recommend to use it in application code.
     */
    @Override
    public String getDescription() {
        return frame.getDescription();
    }

    /**
     * Screen description is used by the framework to show some specified information, e.g. current filter or folder
     * name. We don't recommend to use it in application code.
     */
    @Override
    public void setDescription(String description) {
        frame.setDescription(description);
    }

    /** INTERNAL. Don't call from application code. */
    @Override
    public Window getWrappedWindow() {
        return (Window) frame;
    }

    /**
     * This method is called when the screen is opened to restore settings saved in the database for the current user.
     * <p>You can override it to restore custom settings.
     * <p>For example:
     * <pre>
     * public void applySettings(Settings settings) {
     *     super.applySettings(settings);
     *     String visible = settings.get(hintBox.getId()).attributeValue("visible");
     *     if (visible != null)
     *         hintBox.setVisible(Boolean.valueOf(visible));
     * }
     * </pre>
     * @param settings settings object loaded from the database for the current user
     */
    @Override
    public void applySettings(Settings settings) {
        super.applySettings(settings);
    }

    /**
     * This method is called when the screen is closed to save the screen settings to the database.
     */
    @Override
    public void saveSettings() {
        super.saveSettings();
    }

    @Override
    public void deleteSettings() {
        super.deleteSettings();
    }

    @Override
    public Settings getSettings() {
        return super.getSettings();
    }

    @Override
    public void setFocusComponent(String componentId) {
        ((Window) frame).setFocusComponent(componentId);
    }

    @Override
    public String getFocusComponent() {
        return ((Window) frame).getFocusComponent();
    }

    @Override
    public void addTimer(Timer timer) {
        ((Window) frame).addTimer(timer);
    }

    @Override
    public Timer getTimer(String id) {
        return ((Window) frame).getTimer(id);
    }

    @Override
    public boolean validate(List<Validatable> fields) {
        return frame.validate(fields);
    }

    @Override
    public DialogOptions getDialogOptions() {
        return ((Window) frame).getDialogOptions();
    }

    /**
     * Whether automatic applying of attribute access rules enabled. If you don't want to apply attribute access
     * rules to a screen, override this method and return false.
     */
    public boolean isAttributeAccessControlEnabled() {
        return true;
    }

    /**
     * Hook to be implemented in subclasses. <br>
     * Called by the framework after the screen is fully initialized and opened. <br>
     * Override this method and put custom initialization logic here.
     */
    public void ready() {
    }

    /**
     * Hook to be implemented in subclasses. Called by {@link #validateAll()} at the end of standard validation.
     * @param errors the list of validation errors. Caller fills it by errors found during the default validation.
     * Overridden method should add into it errors found by custom validation.
     */
    protected void postValidate(ValidationErrors errors) {
    }

    /**
     * Hook to be implemented in subclasses. Called by the framework before closing the screen.
     *
     * @param actionId a string that is passed to one of {@link #close} methods by calling code to identify itself.
     *                 Can be an {@link Action} ID, or a constant like {@link #COMMIT_ACTION_ID} or
     *                 {@link #CLOSE_ACTION_ID}.
     * @return true to proceed with closing, false to interrupt and leave the screen open
     */
    protected boolean preClose(String actionId) {
        return true;
    }

    protected void validateAdditionalRules(ValidationErrors errors) {
    }

    /**
     * Check validity by invoking validators on all components which support them
     * and show validation result notification. This method also calls {@link #postValidate(ValidationErrors)} hook to
     * support additional validation.
     * <p>You should override this method in subclasses ONLY if you want to completely replace the validation process,
     * otherwise use {@link #postValidate(ValidationErrors)}.
     *
     * @return true if the validation was successful, false if there were any problems
     */
    @Override
    public boolean validateAll() {
        ValidationErrors errors = getValidationErrors();

        validateAdditionalRules(errors);

        return handleValidationErrors(errors);
    }

    protected boolean handleValidationErrors(ValidationErrors errors) {
        postValidate(errors);

        if (errors.isEmpty())
            return true;

        showValidationErrors(errors);

        focusProblemComponent(errors);

        return false;
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        if (frame instanceof SecuredActionsHolder) {
            return ((SecuredActionsHolder) frame).getActionsPermissions();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public ContentSwitchMode getContentSwitchMode() {
        return ((Window) frame).getContentSwitchMode();
    }

    @Override
    public void setContentSwitchMode(ContentSwitchMode mode) {
        ((Window) frame).setContentSwitchMode(mode);
    }

    @Override
    public void addBeforeCloseWithShortcutListener(BeforeCloseWithShortcutListener listener) {
        ((Window) frame).addBeforeCloseWithShortcutListener(listener);
    }

    @Override
    public void removeBeforeCloseWithShortcutListener(BeforeCloseWithShortcutListener listener) {
        ((Window) frame).removeBeforeCloseWithShortcutListener(listener);
    }

    @Override
    public void addBeforeCloseWithCloseButtonListener(BeforeCloseWithCloseButtonListener listener) {
        ((Window) frame).addBeforeCloseWithCloseButtonListener(listener);
    }

    @Override
    public void removeBeforeCloseWithCloseButtonListener(BeforeCloseWithCloseButtonListener listener) {
        ((Window) frame).removeBeforeCloseWithCloseButtonListener(listener);
    }
}