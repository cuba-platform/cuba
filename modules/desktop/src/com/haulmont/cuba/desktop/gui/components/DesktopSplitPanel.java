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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SplitPanel;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.synth.SynthSplitPaneUI;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DesktopSplitPanel extends DesktopAbstractComponent<JSplitPane> implements SplitPanel, Component.HasSettings {

    protected boolean applyNewPosition = true;
    protected int position = 50;

    protected boolean positionChanged = false;

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new LinkedHashSet<>();
    protected boolean settingsEnabled = true;
    protected boolean locked = false;

    public DesktopSplitPanel() {
        impl = new JSplitPane() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                if (applyNewPosition) {
                    double ratio = position / 100.0;

                    impl.setDividerLocation(ratio);
                    impl.setResizeWeight(ratio);
                    applyNewPosition = false;
                }
            }
        };

        // default orientation as web split
        impl.setOrientation(JSplitPane.VERTICAL_SPLIT);

        impl.setUI(new SynthSplitPaneUI() {
                       @Override
                       protected void dragDividerTo(int location) {
                           super.dragDividerTo(location);

                           // user touched split divider
                           positionChanged = true;
                       }
                   });

        impl.setLeftComponent(new JPanel());
        impl.setRightComponent(new JPanel());

        impl.getLeftComponent().setMinimumSize(new Dimension());
        impl.getRightComponent().setMinimumSize(new Dimension());
    }

    @Override
    public int getOrientation() {
        return impl.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? ORIENTATION_HORIZONTAL : ORIENTATION_VERTICAL;
    }

    @Override
    public void setOrientation(int orientation) {
        impl.setOrientation(orientation == ORIENTATION_HORIZONTAL ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT);
    }

    @Override
    public void setSplitPosition(int pos) {
        if (pos < 0 || pos > 100) {
            throw new IllegalArgumentException("Split position must be between 0 and 100");
        }

        this.position = pos;
        this.applyNewPosition = true;

        impl.revalidate();
        impl.repaint();
    }

    @Override
    public void setSplitPosition(int pos, int unit) {
        if (unit != UNITS_PERCENTAGE) {
            // not supported
            return;
        }

        setSplitPosition(pos);
    }

    @Override
    public void setSplitPosition(int pos, int unit, boolean reversePosition) {
        if (unit != UNITS_PERCENTAGE) {
            // not supported
            return;
        }

        setSplitPosition(pos);
    }

    @Override
    public void setMaxSplitPosition(int pos, int unit) {
        if (unit != UNITS_PERCENTAGE) {
            // not supported
            return;
        }

        setMaxSplitPosition(pos, UNITS_PIXELS);
    }

    @Override
    public void setMinSplitPosition(int pos, int unit) {
        if (unit != UNITS_PERCENTAGE) {
            // not supported
            return;
        }

        setMaxSplitPosition(pos, UNITS_PIXELS);
    }

    @Override
    public boolean isSplitPositionReversed() {
        return isSplitPositionReversed();
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;

        BasicSplitPaneDivider divider = ((BasicSplitPaneUI) impl.getUI()).getDivider();
        if (locked) {
            divider.setDividerSize(0);
        } else {
            divider.setDividerSize(10);
        }

        impl.revalidate();
        impl.repaint();
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setPositionUpdateListener(PositionUpdateListener positionListener) {
        // not supported
    }

    @Override
    public PositionUpdateListener getPositionUpdateListener() {
        // not supported
        return null;
    }

    @Override
    public void add(Component component) {
        JComponent jComponent = DesktopComponentsHelper.getComposition(component);
        jComponent.setMinimumSize(new Dimension());
        if (ownComponents.isEmpty()) {
            impl.setLeftComponent(jComponent);
        } else {
            impl.setRightComponent(jComponent);
        }

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }

        if (frame != null) {
            if (component instanceof BelongToFrame
                    && ((BelongToFrame) component).getFrame() == null) {
                ((BelongToFrame) component).setFrame(frame);
            } else {
                frame.registerComponent(component);
            }
        }

        if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
            ((DesktopAbstractComponent) component).setParentEnabled(false);
        }

        ownComponents.add(component);
    }

    @Override
    public void remove(Component component) {
        JComponent jComponent = DesktopComponentsHelper.getComposition(component);
        impl.remove(jComponent);

        if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
            ((DesktopAbstractComponent) component).setParentEnabled(true);
        }

        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    @Override
    public void removeAll() {
        impl.removeAll();
        componentByIds.clear();

        List<Component> components = new ArrayList<>(ownComponents);
        ownComponents.clear();

        for (Component component : components) {
            if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
                ((DesktopAbstractComponent) component).setParentEnabled(true);
            }

            component.setParent(null);
        }
    }

    @Override
    public void setFrame(com.haulmont.cuba.gui.components.Frame frame) {
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
    public void applySettings(Element element) {
        if (!isSettingsEnabled()) {
            return;
        }

        Element e = element.element("position");
        if (e != null) {
            String value = e.attributeValue("value");
            if (!StringUtils.isBlank(value)) {
                // ignore defaults
                this.applyNewPosition = false;
                this.positionChanged = true;

                impl.setDividerLocation(Integer.parseInt(value));
                impl.setResizeWeight(position / 100.0);
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled()) {
            return false;
        }

        if (!positionChanged) {
            return false; // most probably user didn't change the divider location
        }

        int location = impl.getUI().getDividerLocation(impl);
        Element e = element.element("position");
        if (e == null) {
            e = element.addElement("position");
        }
        e.addAttribute("value", String.valueOf(location));
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
    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            super.setEnabled(enabled);
        }
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        boolean resultEnabled = isEnabledWithParent();
        for (Component component : ownComponents) {
            if (component instanceof DesktopAbstractComponent) {
                ((DesktopAbstractComponent) component).setParentEnabled(resultEnabled);
            }
        }
    }
}