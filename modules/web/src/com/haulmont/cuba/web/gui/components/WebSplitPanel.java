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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.AttachNotifier;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.web.widgets.CubaDockableSplitPanel;
import com.haulmont.cuba.web.widgets.CubaHorizontalSplitPanel;
import com.haulmont.cuba.web.widgets.CubaVerticalSplitPanel;
import com.haulmont.cuba.web.widgets.client.split.SplitPanelDockMode;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebSplitPanel extends WebAbstractComponent<AbstractSplitPanel> implements SplitPanel {

    protected List<Component> ownComponents = new ArrayList<>(3);

    protected int orientation;
    protected boolean settingsEnabled = true;
    protected boolean settingsChanged = false;

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

        com.vaadin.ui.Component vComponent = childComponent.unwrapComposition(com.vaadin.ui.Component.class);

        component.addComponent(vComponent);

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                ((FrameImplementation) frame).registerComponent(childComponent);
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
            component = new CubaVerticalSplitPanel() {
                @Override
                public void setSplitPosition(float pos, Unit unit, boolean reverse) {
                    currentPosition = this.getSplitPosition();
                    inverse = this.isSplitPositionReversed();

                    super.setSplitPosition(pos, unit, reverse);
                }
            };
        }

        component.addSplitPositionChangeListener(this::fireSplitPositionChangeListener);
    }

    protected void fireSplitPositionChangeListener(AbstractSplitPanel.SplitPositionChangeEvent event) {
        SplitPositionChangeEvent cubaEvent = new SplitPositionChangeEvent(this, currentPosition,
                event.getSplitPosition(), event.isUserOriginated());
        publish(SplitPositionChangeEvent.class, cubaEvent);

        if (event.isUserOriginated()) {
            settingsChanged = true;
        }
    }

    @Override
    public void remove(Component childComponent) {
        checkNotNullArgument(childComponent);

        component.removeComponent(childComponent.unwrapComposition(com.vaadin.ui.Component.class));
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void removeAll() {
        component.removeAllComponents();

        Component[] components = ownComponents.toArray(new Component[0]);
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
        checkNotNullArgument(id);

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
    public Stream<Component> getOwnComponentsStream() {
        return ownComponents.stream();
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
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

        if (!settingsChanged) {
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
        setSplitPosition(pos, ComponentsHelper.convertToSizeUnit(unit));
    }

    @Override
    public void setSplitPosition(int pos, SizeUnit unit) {
        component.setSplitPosition(pos, WebWrapperUtils.toVaadinUnit(unit));
    }

    @Override
    public void setSplitPosition(int pos, int unit, boolean reversePosition) {
        setSplitPosition(pos, ComponentsHelper.convertToSizeUnit(unit), reversePosition);
    }

    @Override
    public void setSplitPosition(int pos, SizeUnit unit, boolean reversePosition) {
        component.setSplitPosition(pos, WebWrapperUtils.toVaadinUnit(unit), reversePosition);
    }

    @Override
    public float getSplitPosition() {
        return component.getSplitPosition();
    }

    @Override
    public int getSplitPositionUnit() {
        return ComponentsHelper.convertFromSizeUnit(getSplitPositionSizeUnit());
    }

    @Override
    public SizeUnit getSplitPositionSizeUnit() {
        return WebWrapperUtils.toSizeUnit(component.getSplitPositionUnit());
    }

    @Override
    public boolean isSplitPositionReversed() {
        return component.isSplitPositionReversed();
    }

    @Override
    public void setMinSplitPosition(int pos, int unit) {
        setMinSplitPosition(pos, ComponentsHelper.convertToSizeUnit(unit));
    }

    @Override
    public void setMinSplitPosition(int pos, SizeUnit unit) {
        component.setMinSplitPosition(pos, WebWrapperUtils.toVaadinUnit(unit));
    }

    @Override
    public float getMinSplitPosition() {
        return component.getMinSplitPosition();
    }

    @Override
    public SizeUnit getMinSplitPositionSizeUnit() {
        return WebWrapperUtils.toSizeUnit(component.getMinSplitPositionUnit());
    }

    @Override
    public void setMaxSplitPosition(int pos, int unit) {
        setMaxSplitPosition(pos, ComponentsHelper.convertToSizeUnit(unit));
    }

    @Override
    public void setMaxSplitPosition(int pos, SizeUnit unit) {
        component.setMaxSplitPosition(pos, WebWrapperUtils.toVaadinUnit(unit));
    }

    @Override
    public float getMaxSplitPosition() {
        return component.getMaxSplitPosition();
    }

    @Override
    public SizeUnit getMaxSplitPositionSizeUnit() {
        return WebWrapperUtils.toSizeUnit(component.getMaxSplitPositionUnit());
    }

    @Override
    public void setLocked(boolean locked) {
        component.setLocked(locked);
    }

    @Override
    public boolean isLocked() {
        return component.isLocked();
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
        ((CubaDockableSplitPanel) component).setDockable(dockable);
    }

    @Override
    public boolean isDockable() {
        return ((CubaDockableSplitPanel) component).isDockable();
    }

    @Override
    public void setDockMode(DockMode dockMode) {
        SplitPanelDockMode mode = SplitPanelDockMode.valueOf(dockMode.name());
        ((CubaDockableSplitPanel) component).setDockMode(mode);
    }

    @Override
    public DockMode getDockMode() {
        SplitPanelDockMode mode = ((CubaDockableSplitPanel) component).getDockMode();
        return DockMode.valueOf(mode.name());
    }

    @Override
    public Subscription addSplitPositionChangeListener(Consumer<SplitPositionChangeEvent> listener) {
        return getEventHub().subscribe(SplitPositionChangeEvent.class, listener);
    }

    @Override
    public void removeSplitPositionChangeListener(Consumer<SplitPositionChangeEvent> listener) {
        unsubscribe(SplitPositionChangeEvent.class, listener);
    }

    @Override
    public void attached() {
        super.attached();

        for (Component component : ownComponents) {
            ((AttachNotifier) component).attached();
        }
    }

    @Override
    public void detached() {
        super.detached();

        for (Component component : ownComponents) {
            ((AttachNotifier) component).detached();
        }
    }
}