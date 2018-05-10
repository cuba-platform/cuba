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

import com.haulmont.bali.events.EventPublisher;
import com.haulmont.bali.events.EventRouter;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class WebAbstractComponent<T extends com.vaadin.ui.Component>
        extends EventPublisher
        implements Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame, Component.HasIcon,
                   Component.HasCaption {

    @Deprecated
    public static final List<Sizeable.Unit> UNIT_SYMBOLS = Collections.unmodifiableList(Arrays.asList(
            Sizeable.Unit.PIXELS, Sizeable.Unit.POINTS, Sizeable.Unit.PICAS,
            Sizeable.Unit.EM, Sizeable.Unit.EX, Sizeable.Unit.MM,
            Sizeable.Unit.CM, Sizeable.Unit.INCH, Sizeable.Unit.PERCENTAGE));

    public static final String ICON_STYLE = "icon";

    protected String id;
    protected T component;

    protected Element element;
    protected Frame frame;
    protected Component parent;

    protected Alignment alignment = Alignment.TOP_LEFT;
    protected String icon;

    // todo remove
    private EventRouter eventRouter = null;

    /**
     * Use EventRouter for listeners instead of fields with listeners List.
     *
     * @see EventRouter
     */
    protected EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    @Override
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void setFrame(Frame frame) {
        this.frame = frame;
        if (frame != null) {
            frame.registerComponent(this);
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

    /**
     * @return id that is suitable for auto debug id
     */
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }

        return getClass().getSimpleName();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        if (!Objects.equals(this.id, id)) {
            if (frame != null) {
                frame.unregisterComponent(this);
            }

            this.id = id;
            if (this.component != null && AppUI.getCurrent().isTestMode()) {
                this.component.setCubaId(id);
            }

            if (frame != null) {
                frame.registerComponent(this);
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
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        ((AbstractComponent) component).setDescription(description);
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (!StringUtils.isEmpty(icon)) {
            Resource iconResource = AppBeans.get(IconResolver.class)
                    .getIconResource(this.icon);
            getComposition().setIcon(iconResource);
            getComposition().addStyleName(ICON_STYLE);
        } else {
            getComposition().setIcon(null);
            getComposition().removeStyleName(ICON_STYLE);
        }
    }

    @Override
    public void setIconFromSet(Icons.Icon icon) {
        String iconName = AppBeans.get(Icons.class)
                .get(icon);
        setIcon(iconName);
    }

    /**
     * @return component enabled property
     */
    public boolean getComponentEnabledFlag() {
        return getComposition().isEnabled();
    }

    /**
     * @return component visible property
     */
    public boolean getComponentVisibleFlag() {
        return getComposition().isVisible();
    }

    @Override
    public void requestFocus() {
        if (component instanceof com.vaadin.ui.Component.Focusable) {
            ((com.vaadin.ui.Component.Focusable) component).focus();
        }
    }

    @Override
    public float getHeight() {
        return getComposition().getHeight();
    }

    @Override
    public int getHeightUnits() {
        return ComponentsHelper.convertFromSizeUnit(getHeightSizeUnit());
    }

    @Override
    public SizeUnit getHeightSizeUnit() {
        return WebWrapperUtils.toSizeUnit(getComposition().getHeightUnits());
    }

    @Override
    public void setHeight(String height) {
        getComposition().setHeight(height);
    }

    @Override
    public float getWidth() {
        return getComposition().getWidth();
    }

    @Override
    public int getWidthUnits() {
        return ComponentsHelper.convertFromSizeUnit(getWidthSizeUnit());
    }

    @Override
    public SizeUnit getWidthSizeUnit() {
        return WebWrapperUtils.toSizeUnit(getComposition().getWidthUnits());
    }

    @Override
    public void setWidth(String width) {
        getComposition().setWidth(width);
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
    public com.vaadin.ui.Component getComponent() {
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