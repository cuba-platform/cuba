/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebSplitPanel extends WebAbstractComponent<com.vaadin.ui.SplitPanel>
        implements SplitPanel, Component.HasSettings {

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new LinkedHashSet<>();

    protected boolean showHookButton = false;
    protected String defaultPosition = null;

    protected PositionUpdateListener positionListener;

    public WebSplitPanel() {
        component = new com.vaadin.ui.SplitPanel() {
            @Override
            public void changeVariables(Object source, Map<String, Object> variables) {
                int previousPosition = this.getSplitPosition();
                super.changeVariables(source, variables);
                int newPosition = this.getSplitPosition();
                if ((newPosition != previousPosition) && (positionListener != null)) {
                    positionListener.updatePosition(previousPosition, newPosition);
                }
            }

            @Override
            public void paintContent(PaintTarget target) throws PaintException {
                super.paintContent(target);

                target.addAttribute("useHookButton", showHookButton);
                if (defaultPosition != null) {
                    target.addAttribute("defaultPosition", defaultPosition);
                }
            }
        };
    }

    @Override
    public void add(Component childComponent) {
        final com.vaadin.ui.Component itmillComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(itmillComponent);

        if (childComponent.getId() != null) {
            componentByIds.put(childComponent.getId(), childComponent);
        }

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                frame.registerComponent(childComponent);
            }
        }

        ownComponents.add(childComponent);
    }

    @Override
    public void remove(Component childComponent) {
        component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);
    }

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (Component childComponent : ownComponents) {
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public void applySettings(Element element) {
        Element e = element.element("position");
        if (e != null) {
            String value = e.attributeValue("value");
            String unit = e.attributeValue("unit");
            if (!StringUtils.isBlank(value) && !StringUtils.isBlank(unit)) {
                try {
                    Integer posValue = Integer.valueOf(value);
                    Integer unitValue = Integer.valueOf(unit);

                    component.setSplitPosition(posValue, unitValue);
                } catch (NumberFormatException ex) {
                    LogFactory.getLog(WebSplitPanel.class).warn(
                            String.format("Unable to applySettings for position %s and unit %s", value, unit));
                }
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        Element e = element.element("position");
        if (e == null) {
            e = element.addElement("position");
        }
        e.addAttribute("value", String.valueOf(component.getSplitPosition()));
        e.addAttribute("unit", String.valueOf(component.getSplitPositionUnit()));
        return true;
    }

    public boolean isShowHookButton() {
        return showHookButton;
    }

    public void setShowHookButton(boolean showHookButton) {
        this.showHookButton = showHookButton;
    }

    public String getDefaultPosition() {
        return defaultPosition;
    }

    public void setDefaultPosition(String defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    @Override
    public int getOrientation() {
        return component.getOrientation();
    }

    @Override
    public void setOrientation(int orientation) {
        component.setOrientation(orientation);
    }

    @Override
    public void setSplitPosition(int pos) {
        component.setSplitPosition(pos);
    }

    @Override
    public void setLocked(boolean locked) {
        component.setLocked(locked);
    }

    @Override
    public boolean isLocked() {
        return component.isLocked();
    }

    @Override
    public void setPositionUpdateListener(PositionUpdateListener positionListener) {
        this.positionListener = positionListener;
    }

    @Override
    public PositionUpdateListener getPositionUpdateListener() {
        return positionListener;
    }
}