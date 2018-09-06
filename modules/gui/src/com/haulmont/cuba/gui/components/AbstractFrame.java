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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base class for frame controllers.
 */
public class AbstractFrame extends ScreenFragment implements Frame, Frame.Wrapper, LegacyFrame {

    protected Frame frame;

    private Object _companion;
    private Component parent;
    private DsContext dsContext;

    @Inject
    protected Messages messages;
    @Inject
    private MessageBundle messageBundle;
    @Inject
    private WindowManager windowManager;

    public AbstractFrame() {
    }

    @Override
    protected void setFragment(Fragment fragment) {
        super.setFragment(fragment);

        this.frame = fragment;
    }

    @Override
    public WindowManager getWindowManager() {
        return windowManager;
    }

    @Override
    public Frame getWrappedFrame() {
        return frame;
    }

    /**
     * INTERNAL. Don't call from application code.
     */
    public void setWrappedFrame(Frame frame) {
        this.frame = frame;
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
    public void setEnabled(boolean enabled) {
        frame.setEnabled(enabled);
    }

    @Override
    public boolean isEnabledRecursive() {
        return frame.isEnabledRecursive();
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
    public void setHeight(String height) {
        frame.setHeight(height);
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return frame.getHeightSizeUnit();
    }

    @Override
    public float getWidth() {
        return frame.getWidth();
    }

    @Override
    public void setWidth(String width) {
        frame.setWidth(width);
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return frame.getWidthSizeUnit();
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
    public String getCaption() {
        return frame.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        frame.setCaption(caption);
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
    public String getDescription() {
        return frame.getDescription();
    }

    @Override
    public void setDescription(String description) {
        frame.setDescription(description);
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
    public FrameOwner getFrameOwner() {
        return this;
    }

    @Override
    public FrameContext getContext() {
        return frame.getContext();
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
    }

    @Override
    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
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
        String msgPack = getMessagesPack();
        if (StringUtils.isEmpty(msgPack)) {
            throw new DevelopmentException("MessagePack is not set");
        }

        return messages.getMessage(msgPack, key);
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
        String msgPack = getMessagesPack();
        if (StringUtils.isEmpty(msgPack)) {
            throw new DevelopmentException("MessagePack is not set");
        }

        return messages.formatMessage(msgPack, key, params);
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
    public boolean validate(List<Validatable> fields) {
        return frame.validate(fields);
    }

    @Override
    public boolean validateAll() {
        return frame.validateAll();
    }

    /**
     * Show validation errors alert. Can be overridden in subclasses.
     *
     * @param errors the list of validation errors. Caller fills it by errors found during the default validation.
     */
    public void showValidationErrors(ValidationErrors errors) {
        StringBuilder buffer = new StringBuilder();
        for (ValidationErrors.Item error : errors.getAll()) {
            buffer.append(error.description).append("\n");
        }

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        NotificationType notificationType = NotificationType.valueOf(clientConfig.getValidationNotificationType());
        showNotification(messages.getMainMessage("validationFail.caption"), buffer.toString(), notificationType);
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

    /**
     * INTERNAL. Don't call from application code.
     */
    public void setCompanion(Object companion) {
        this._companion = companion;
    }

    @Override
    public Frame getFrame() {
        return this.frame.getFrame();
    }

    @Override
    public void setFrame(Frame frame) {
        this.frame.setFrame(frame);
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
    public boolean getSpacing() {
        return frame.getSpacing();
    }

    @Override
    public void setSpacing(boolean enabled) {
        frame.setSpacing(enabled);
    }

    @Override
    public MarginInfo getMargin() {
        return frame.getMargin();
    }

    @Override
    public void setMargin(MarginInfo marginInfo) {
        frame.setMargin(marginInfo);
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
}