/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalSplitPanel;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebSplitPanel extends WebAbstractComponent<AbstractSplitPanel> implements SplitPanel, Component.HasSettings {

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new LinkedHashSet<>();

    protected SplitPanel.PositionUpdateListener positionListener;

    protected int orientation;
    protected boolean settingsEnabled = true;

    @Override
    public void add(Component childComponent) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        if (component == null) {
            createComponentImpl();
        }
        if (getId() != null) {
            component.setCubaId(getId());
        } else {
            component.setCubaId(getAlternativeDebugId());
        }

        final com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(vComponent);

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

        childComponent.setParent(this);
    }

    protected void createComponentImpl() {
        if (orientation == SplitPanel.ORIENTATION_HORIZONTAL) {
            component = new CubaHorizontalSplitPanel() {
                @Override
                protected void onPositionUpdate(float previousPosition, float newPosition) {
                    super.onPositionUpdate(previousPosition, newPosition);

                    firePositionUpdateListener(previousPosition, newPosition);
                }
            };
        } else {
            component = new VerticalSplitPanel() {
                @Override
                protected void onPositionUpdate(float previousPosition, float newPosition) {
                    super.onPositionUpdate(previousPosition, newPosition);

                    firePositionUpdateListener(previousPosition, newPosition);
                }
            };
        }
    }

    protected void firePositionUpdateListener(float previousPosition, float newPosition) {
        if (positionListener != null) {
            positionListener.updatePosition(previousPosition, newPosition);
        }
    }

    @Override
    public void remove(Component childComponent) {
        component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        component.removeAllComponents();
        componentByIds.clear();

        List<Component> components = new ArrayList<>(ownComponents);
        ownComponents.clear();

        for (Component childComponent : components) {
            childComponent.setParent(null);
        }
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

    @Override
    public Component getOwnComponent(String id) {
        return componentByIds.get(id);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public Component getComponentNN(String id) {
        Component component = getComponent(id);
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
        if (!isSettingsEnabled()) {
            return;
        }

        Element e = element.element("position");
        if (e != null) {
            String value = e.attributeValue("value");
            String unit = e.attributeValue("unit");
            if (!StringUtils.isBlank(value) && !StringUtils.isBlank(unit)) {
                Sizeable.Unit convertedUnit;
                if (NumberUtils.isNumber(unit)) {
                    convertedUnit = convertLegacyUnit(Integer.parseInt(unit));
                } else {
                    convertedUnit = Sizeable.Unit.getUnitFromSymbol(unit);
                }
                component.setSplitPosition(Float.valueOf(value), convertedUnit);
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled()) {
            return false;
        }

        Element e = element.element("position");
        if (e == null) {
            e = element.addElement("position");
        }
        e.addAttribute("value", String.valueOf(component.getSplitPosition()));
        e.addAttribute("unit", String.valueOf(component.getSplitPositionUnit()));
        return true;
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }

    @Override
    public int getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(int orientation) {
        this.orientation = orientation;

        if (component == null) {
            createComponentImpl();
        }
    }

    @Override
    public void setSplitPosition(int pos) {
        component.setSplitPosition(pos);
    }

    @Override
    public void setSplitPosition(int pos, int unit) {
        if (unit == UNITS_PIXELS) {
            component.setSplitPosition(pos, Sizeable.Unit.PIXELS);
        } else if (unit == UNITS_PERCENTAGE) {
            component.setSplitPosition(pos, Sizeable.Unit.PERCENTAGE);
        } else {
            throw new IllegalArgumentException("Unsupported unit " + unit);
        }
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

    protected Sizeable.Unit convertLegacyUnit(int unit) {
        switch (unit) {
            case 0:
                return Sizeable.Unit.PIXELS;
            case 8:
                return Sizeable.Unit.PERCENTAGE;
            default:
                return Sizeable.Unit.PIXELS;
        }
    }
}