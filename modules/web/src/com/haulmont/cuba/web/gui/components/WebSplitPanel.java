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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.web.widgets.CubaHorizontalSplitPanel;
import com.haulmont.cuba.web.widgets.client.split.SplitPanelDockMode;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.*;

public class WebSplitPanel extends WebAbstractComponent<AbstractSplitPanel> implements SplitPanel {

    protected List<Component> ownComponents = new ArrayList<>(3);

    protected SplitPanel.PositionUpdateListener positionListener;

    protected int orientation;
    protected boolean settingsEnabled = true;

    protected float currentPosition = 0;
    protected boolean inverse = false;

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
                public void setSplitPosition(float pos, Unit unit, boolean reverse) {
                    currentPosition = this.getSplitPosition();
                    inverse = this.isSplitPositionReversed();

                    super.setSplitPosition(pos, unit, reverse);
                }
            };
        } else {
            component = new VerticalSplitPanel() {
                @Override
                public void setSplitPosition(float pos, Unit unit, boolean reverse) {
                    currentPosition = this.getSplitPosition();

                    super.setSplitPosition(pos, unit, reverse);
                }
            };
        }

        component.addSplitPositionChangeListener(this::fireSplitPositionChangeListener);
    }

    protected void fireSplitPositionChangeListener(AbstractSplitPanel.SplitPositionChangeEvent event) {
        if (positionListener != null) {
            positionListener.updatePosition(currentPosition, event.getSplitPosition());
        }

        SplitPositionChangeEvent cubaEvent = new SplitPositionChangeEvent(this, currentPosition, event.getSplitPosition());
        getEventRouter().fireEvent(SplitPositionChangeListener.class,
                SplitPositionChangeListener::onSplitPositionChanged,
                cubaEvent);
    }

    @Override
    public void remove(Component childComponent) {
        component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        component.removeAllComponents();

        Component[] components = ownComponents.toArray(new Component[ownComponents.size()]);
        ownComponents.clear();

        for (Component childComponent : components) {
            childComponent.setParent(null);
        }
    }

    @Override
    public void setFrame(Frame frame) {
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
        Preconditions.checkNotNullArgument(id);

        return ownComponents.stream()
                .filter(component -> Objects.equals(id, component.getId()))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
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
                Unit convertedUnit;
                if (NumberUtils.isNumber(unit)) {
                    convertedUnit = convertLegacyUnit(Integer.parseInt(unit));
                } else {
                    convertedUnit = Unit.getUnitFromSymbol(unit);
                }
                component.setSplitPosition(Float.parseFloat(value), convertedUnit, component.isSplitPositionReversed());
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
            component.setSplitPosition(pos, Unit.PIXELS);
        } else if (unit == UNITS_PERCENTAGE) {
            component.setSplitPosition(pos, Unit.PERCENTAGE);
        } else {
            throw new IllegalArgumentException("Unsupported unit " + unit);
        }
    }

    @Override
    public void setSplitPosition(int pos, int unit, boolean reversePosition) {
        if (unit == UNITS_PIXELS) {
            component.setSplitPosition(pos, Unit.PIXELS, reversePosition);
        } else if (unit == UNITS_PERCENTAGE) {
            component.setSplitPosition(pos, Unit.PERCENTAGE, reversePosition);
        } else {
            throw new IllegalArgumentException("Unsupported unit " + unit);
        }
    }

    @Override
    public float getSplitPosition() {
        return component.getSplitPosition();
    }

    @Override
    public int getSplitPositionUnit() {
        if (component.getSplitPositionUnit() == Unit.PIXELS) {
            return UNITS_PIXELS;
        } else if (component.getSplitPositionUnit() == Unit.PERCENTAGE) {
            return UNITS_PERCENTAGE;
        } else {
            throw new IllegalArgumentException("Component has unsupported split position unit " + component.getSplitPositionUnit());
        }
    }

    @Override
    public boolean isSplitPositionReversed() {
        return component.isSplitPositionReversed();
    }

    @Override
    public void setMinSplitPosition(int pos, int unit) {
        if (unit == UNITS_PIXELS) {
            component.setMinSplitPosition(pos, Unit.PIXELS);
        } else if (unit == UNITS_PERCENTAGE) {
            component.setMinSplitPosition(pos, Unit.PERCENTAGE);
        } else {
            throw new IllegalArgumentException("Unsupported unit " + unit);
        }
    }

    @Override
    public void setMaxSplitPosition(int pos, int unit) {
        if (unit == UNITS_PIXELS) {
            component.setMaxSplitPosition(pos, Unit.PIXELS);
        } else if (unit == UNITS_PERCENTAGE) {
            component.setMaxSplitPosition(pos, Unit.PERCENTAGE);
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

    @Override
    public void addSplitPositionChangeListener(SplitPositionChangeListener listener) {
        getEventRouter().addListener(SplitPositionChangeListener.class, listener);
    }

    @Override
    public void removeSplitPositionChangeListener(SplitPositionChangeListener listener) {
        getEventRouter().removeListener(SplitPositionChangeListener.class, listener);
    }

    protected Unit convertLegacyUnit(int unit) {
        switch (unit) {
            case 0:
                return Unit.PIXELS;
            case 8:
                return Unit.PERCENTAGE;
            default:
                return Unit.PIXELS;
        }
    }

    @Override
    public void setDockable(boolean dockable) {
        if (orientation == SplitPanel.ORIENTATION_VERTICAL) {
            throw new IllegalStateException("Docking is not available for the vertically oriented SplitPanel.");
        }
        ((CubaHorizontalSplitPanel) component).setDockable(dockable);
    }

    @Override
    public boolean isDockable() {
        if (orientation == SplitPanel.ORIENTATION_VERTICAL) {
            return false;
        }
        return ((CubaHorizontalSplitPanel) component).isDockable();
    }

    @Override
    public void setDockMode(DockMode dockMode) {
        if (orientation == SplitPanel.ORIENTATION_VERTICAL) {
            throw new IllegalStateException("Docking is not available for the vertically oriented SplitPanel.");
        }
        SplitPanelDockMode mode = SplitPanelDockMode.valueOf(dockMode.name());
        ((CubaHorizontalSplitPanel) component).setDockMode(mode);
    }

    @Override
    public DockMode getDockMode() {
        if (orientation == SplitPanel.ORIENTATION_VERTICAL) {
            return null;
        }
        SplitPanelDockMode mode = ((CubaHorizontalSplitPanel) component).getDockMode();
        return DockMode.valueOf(mode.name());
    }
}