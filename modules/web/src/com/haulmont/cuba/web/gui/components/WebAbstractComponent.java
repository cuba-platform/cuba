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

import com.haulmont.bali.events.EventHub;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.HasContextHelp;
import com.haulmont.cuba.gui.components.HasDebugId;
import com.haulmont.cuba.gui.components.HasHtmlCaption;
import com.haulmont.cuba.gui.components.HasHtmlDescription;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.UserError;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Layout;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import javax.inject.Inject;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class WebAbstractComponent<T extends com.vaadin.ui.Component>
        implements Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame, Component.HasIcon,
                   Component.HasCaption, HasDebugId, HasContextHelp, HasHtmlCaption, HasHtmlDescription {

    public static final String ICON_STYLE = "icon";

    protected String id;
    protected T component;

    protected Element element;
    protected Frame frame;
    protected Component parent;

    protected Alignment alignment = Alignment.TOP_LEFT;
    protected String icon;

    protected boolean descriptionAsHtml = false;

    protected Consumer<ContextHelpIconClickEvent> contextHelpIconClickHandler;
    protected Registration contextHelpIconClickListener;

    protected BeanLocator beanLocator;

    // private, lazily initialized
    private EventHub eventHub = null;

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    protected EventHub getEventHub() {
        if (eventHub == null) {
            eventHub = new EventHub();
        }
        return eventHub;
    }

    protected <E> void publish(Class<E> eventType, E event) {
        if (eventHub != null) {
            eventHub.publish(eventType, event);
        }
    }

    protected boolean hasSubscriptions(Class<?> eventClass) {
        return eventHub != null && eventHub.hasSubscriptions(eventClass);
    }

    protected <E> boolean unsubscribe(Class<E> eventType, Consumer<E> listener) {
        if (eventHub != null) {
            return eventHub.unsubscribe(eventType, listener);
        }
        return false;
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void setFrame(Frame frame) {
        this.frame = frame;

        if (frame instanceof FrameImplementation) {
            ((FrameImplementation) frame).registerComponent(this);
        }
    }

    @Override
    public boolean isResponsive() {
        com.vaadin.ui.Component composition = getComposition();
        if (composition instanceof AbstractComponent) {
            return ((AbstractComponent) composition).isResponsive();
        }
        return false;
    }

    @Override
    public void setResponsive(boolean responsive) {
        com.vaadin.ui.Component composition = getComposition();

        if (composition instanceof AbstractComponent) {
            ((AbstractComponent) composition).setResponsive(true);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        if (!Objects.equals(this.id, id)) {
            if (frame != null) {
                ((FrameImplementation) frame).unregisterComponent(this);
            }

            this.id = id;

            AppUI ui = AppUI.getCurrent();
            if (ui != null) {
                if (this.component != null && ui.isTestMode()) {
                    this.component.setCubaId(id);
                }
            }

            if (frame != null) {
                ((FrameImplementation) frame).registerComponent(this);
            }
        }
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
    public String getDebugId() {
        return component.getId();
    }

    @Override
    public void setDebugId(String id) {
        component.setId(id);
    }

    @Override
    public String getStyleName() {
        return getComposition().getStyleName();
    }

    @Override
    public void setStyleName(String name) {
        getComposition().setStyleName(name);
    }

    @Override
    public void addStyleName(String styleName) {
        getComposition().addStyleName(styleName);
    }

    @Override
    public void removeStyleName(String styleName) {
        getComposition().removeStyleName(styleName);
    }

    @Override
    public boolean isEnabled() {
        return getComposition().isEnabled();
    }

    @Override
    public boolean isEnabledRecursive() {
        return WebComponentsHelper.isComponentEnabled(getComposition());
    }

    @Override
    public void setEnabled(boolean enabled) {
        getComposition().setEnabled(enabled);
    }

    @Override
    public boolean isVisible() {
        return getComposition().isVisible();
    }

    @Override
    public boolean isVisibleRecursive() {
        return WebComponentsHelper.isComponentVisible(getComposition());
    }

    @Override
    public void setVisible(boolean visible) {
        getComposition().setVisible(visible);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public boolean isCaptionAsHtml() {
        return ((AbstractComponent) component).isCaptionAsHtml();
    }

    @Override
    public void setCaptionAsHtml(boolean captionAsHtml) {
        ((AbstractComponent) component).setCaptionAsHtml(captionAsHtml);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        ((AbstractComponent) component).setDescription(description, descriptionAsHtml
                ? com.vaadin.shared.ui.ContentMode.HTML
                : com.vaadin.shared.ui.ContentMode.PREFORMATTED);
    }

    @Override
    public boolean isDescriptionAsHtml() {
        return descriptionAsHtml;
    }

    @Override
    public void setDescriptionAsHtml(boolean descriptionAsHtml) {
        if (this.descriptionAsHtml != descriptionAsHtml) {
            this.descriptionAsHtml = descriptionAsHtml;
            // Trigger component changes
            setDescription(getDescription());
        }
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;

        if (StringUtils.isNotEmpty(icon)) {
            Resource iconResource = getIconResource(icon);
            getComposition().setIcon(iconResource);
            getComposition().addStyleName(ICON_STYLE);
        } else {
            getComposition().setIcon(null);
            getComposition().removeStyleName(ICON_STYLE);
        }
    }

    @Override
    public void setIconFromSet(Icons.Icon icon) {
        String iconName = getIconName(icon);
        setIcon(iconName);
    }

    protected Resource getIconResource(String icon) {
        return beanLocator.get(IconResolver.class).getIconResource(icon);
    }

    protected String getIconName(Icons.Icon icon) {
        return beanLocator.get(Icons.class).get(icon);
    }

    @Override
    public float getHeight() {
        return getComposition().getHeight();
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return WebWrapperUtils.toSizeUnit(getComposition().getHeightUnits());
    }

    @Override
    public void setHeight(String height) {
        // do not try to parse string if constant passed
        if (Component.AUTO_SIZE.equals(height)) {
            getComposition().setHeight(-1, Sizeable.Unit.PIXELS);
        } else if (Component.FULL_SIZE.equals(height)) {
            getComposition().setHeight(100, Sizeable.Unit.PERCENTAGE);
        } else {
            getComposition().setHeight(height);
        }
    }

    @Override
    public float getWidth() {
        return getComposition().getWidth();
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return WebWrapperUtils.toSizeUnit(getComposition().getWidthUnits());
    }

    @Override
    public void setWidth(String width) {
        // do not try to parse string if constant passed
        if (Component.AUTO_SIZE.equals(width)) {
            getComposition().setWidth(-1, Sizeable.Unit.PIXELS);
        } else if (Component.FULL_SIZE.equals(width)) {
            getComposition().setWidth(100, Sizeable.Unit.PERCENTAGE);
        } else {
            getComposition().setWidth(width);
        }
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;

        if (getComposition().getParent() != null) {
            com.vaadin.ui.Component component = this.getComposition().getParent();
            if (component instanceof Layout.AlignmentHandler) {
                ((Layout.AlignmentHandler) component).setComponentAlignment(this.getComposition(),
                        WebWrapperUtils.toVaadinAlignment(alignment));
            }
        }
    }

    @Override
    public T getComponent() {
        return component;
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return component;
    }

    @Override
    public Element getXmlDescriptor() {
        return element;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        this.element = element;
    }

    @Override
    public String getContextHelpText() {
        return ((AbstractComponent) getComposition()).getContextHelpText();
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        ((AbstractComponent) getComposition()).setContextHelpText(contextHelpText);
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return ((AbstractComponent) getComposition()).isContextHelpTextHtmlEnabled();
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        ((AbstractComponent) getComposition()).setContextHelpTextHtmlEnabled(enabled);
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return contextHelpIconClickHandler;
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        if (!Objects.equals(this.contextHelpIconClickHandler, handler)) {
            this.contextHelpIconClickHandler = handler;

            if (handler == null) {
                contextHelpIconClickListener.remove();
                contextHelpIconClickListener = null;
            } else if (contextHelpIconClickListener == null) {
                contextHelpIconClickListener = ((AbstractComponent) getComposition())
                        .addContextHelpIconClickListener(this::onContextHelpIconClick);
            }
        }
    }

    protected void onContextHelpIconClick(@SuppressWarnings("unused") com.vaadin.ui.Component.HasContextHelp.ContextHelpIconClickEvent e) {
        if (contextHelpIconClickHandler != null) {
            ContextHelpIconClickEvent event = new ContextHelpIconClickEvent(this);
            contextHelpIconClickHandler.accept(event);
        }
    }

    @Override
    public <X> X unwrap(Class<X> internalComponentClass) {
        return internalComponentClass.cast (getComponent());
    }

    @Override
    public <X> X unwrapComposition(Class<X> internalCompositionClass) {
        return internalCompositionClass.cast(getComposition());
    }

    protected boolean hasValidationError() {
        if (getComposition() instanceof AbstractComponent) {
            AbstractComponent composition = (AbstractComponent) getComposition();
            return composition.getComponentError() instanceof UserError;
        }
        return false;
    }

    protected void setValidationError(String errorMessage) {
        if (getComposition() instanceof AbstractComponent) {
            AbstractComponent composition = (AbstractComponent) getComposition();
            if (errorMessage == null) {
                composition.setComponentError(null);
            } else {
                composition.setComponentError(new UserError(errorMessage));
            }
        }
    }
}