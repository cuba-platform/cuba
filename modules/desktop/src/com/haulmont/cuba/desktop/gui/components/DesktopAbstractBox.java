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

import com.google.common.collect.Iterables;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.desktop.gui.data.DesktopContainerHelper;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigLayoutHelper;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.MarginInfo;
import com.haulmont.cuba.gui.components.SizeUnit;
import net.miginfocom.layout.CC;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.*;

public abstract class DesktopAbstractBox
        extends DesktopAbstractComponent<JPanel>
        implements com.haulmont.cuba.gui.components.BoxLayout, DesktopContainer {

    protected BoxLayoutAdapter layoutAdapter;

    protected Collection<Component> ownComponents = new LinkedHashSet<>();
    protected Map<String, Component> componentByIds = new HashMap<>();

    protected Component expandedComponent;
    protected Map<Component, ComponentCaption> captions = new HashMap<>();
    protected Map<Component, Pair<JPanel, BoxLayoutAdapter>> wrappers = new HashMap<>();

    protected boolean scheduledRepaint = false;

    public DesktopAbstractBox() {
        impl = new JPanel();
        impl.setFocusable(false);
        assignClassDebugProperty(impl);
        layoutAdapter = BoxLayoutAdapter.create(impl);
    }

    @Override
    public void add(Component component) {
        add(component, ownComponents.size());
    }

    @Override
    public void add(Component component, int index) {
        if (component.getParent() != null && component.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        if (ownComponents.contains(component)) {
            int existingIndex = new ArrayList<>(ownComponents).indexOf(component);
            if (index > existingIndex) {
                index--;
            }

            remove(component);
        }

        JComponent composition = DesktopComponentsHelper.getComposition(component);
        boolean hasExternalCaption = DesktopContainerHelper.hasExternalCaption(component);
        if (hasExternalCaption
                || DesktopContainerHelper.hasExternalContextHelp(component)) {
            ComponentCaption caption  = new ComponentCaption(component);
            captions.put(component, caption);

            JPanel wrapper = new LayoutSlot();
            BoxLayoutAdapter adapter = BoxLayoutAdapter.create(wrapper);
            adapter.setExpandLayout(true);
            adapter.setSpacing(false);
            adapter.setMargin(false);
            wrapper.add(composition);

            if (hasExternalCaption) {
                adapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
                wrapper.add(caption, 0);
            } else {
                wrapper.add(caption, new CC().alignY("top"));
            }

            impl.add(wrapper, layoutAdapter.getConstraints(component), index);
            wrappers.put(component, new Pair<>(wrapper, adapter));
        } else {
            impl.add(composition, layoutAdapter.getConstraints(component), index);
        }

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }

        if (frame != null) {
            if (component instanceof BelongToFrame
                    && ((BelongToFrame) component).getFrame() == null) {
                ((BelongToFrame) component).setFrame(frame);
            } else {
                attachToFrame(component);
            }
        }

        if (index == ownComponents.size()) {
            ownComponents.add(component);
        } else {
            List<Component> componentsTempList = new ArrayList<>(ownComponents);
            componentsTempList.add(index, component);

            ownComponents.clear();
            ownComponents.addAll(componentsTempList);
        }

        DesktopContainerHelper.assignContainer(component, this);

        if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
            ((DesktopAbstractComponent) component).setParentEnabled(false);
        }

        component.setParent(this);

        requestContainerUpdate();

        requestRepaint();
    }

    @Override
    public int indexOf(Component child) {
        return Iterables.indexOf(ownComponents, c -> c == child);
    }

    protected void attachToFrame(Component component) {
        frame.registerComponent(component);
    }

    @Override
    public void remove(Component component) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        if (wrappers.containsKey(component)) {
            impl.remove(wrappers.get(component).getFirst());
            wrappers.remove(component);
        } else {
            impl.remove(composition);
        }
        if (captions.containsKey(component)) {
            impl.remove(captions.get(component));
            captions.remove(component);
        }

        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);

        DesktopContainerHelper.assignContainer(component, null);
        if (expandedComponent == component) {
            expandedComponent = null;
        }

        if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
            ((DesktopAbstractComponent) component).setParentEnabled(true);
        }

        component.setParent(null);

        requestContainerUpdate();

        requestRepaint();
    }

    @Override
    public void removeAll() {
        wrappers.clear();
        impl.removeAll();
        componentByIds.clear();
        captions.clear();

        List<Component> components = new ArrayList<>(ownComponents);
        ownComponents.clear();

        for (Component component : components) {
            if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
                ((DesktopAbstractComponent) component).setParentEnabled(true);
            }

            if (expandedComponent == component) {
                expandedComponent = null;
            }

            component.setParent(null);

            DesktopContainerHelper.assignContainer(component, null);
        }

        requestRepaint();
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

    protected void requestRepaint() {
        if (!scheduledRepaint) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (expandedComponent != null) {
                        updateComponentInternal(expandedComponent);
                    }

                    impl.revalidate();
                    impl.repaint();

                    scheduledRepaint = false;
                }
            });

            scheduledRepaint = true;
        }
    }

    @Override
    public void updateComponent(Component child) {
        updateComponentInternal(child);

        requestRepaint();

        requestContainerUpdate();
    }

    protected void updateComponentInternal(Component child) {
        boolean componentReAdded = false;
        if (DesktopContainerHelper.mayHaveExternalCaption(child)
                || DesktopContainerHelper.mayHaveExternalContextHelp(child)) {
            if (captions.containsKey(child)
                    && !DesktopContainerHelper.hasExternalCaption(child)
                    && !DesktopContainerHelper.hasExternalContextHelp(child)) {
                reAddChild(child);
                componentReAdded = true;
            } else if (!captions.containsKey(child)
                    && (DesktopContainerHelper.hasExternalCaption(child)
                    || DesktopContainerHelper.hasExternalContextHelp(child))) {
                reAddChild(child);
                componentReAdded = true;
            } else if (captions.containsKey(child)) {
                ComponentCaption caption = captions.get(child);
                caption.update();
                BoxLayoutAdapter adapterForCaption = layoutAdapter;
                if (wrappers.containsKey(child)) {
                    adapterForCaption = wrappers.get(child).getSecond();
                }
                adapterForCaption.updateConstraints(caption, adapterForCaption.getCaptionConstraints(child));
            }
        }

        if (!componentReAdded) {
            JComponent composition;
            if (wrappers.containsKey(child)) {
                composition = wrappers.get(child).getFirst();
                CC constraints = MigLayoutHelper.getConstraints(child);
                if (child.getHeight() == -1.0) {
                    MigLayoutHelper.applyHeight(constraints, -1, SizeUnit.PIXELS, false);
                } else {
                    MigLayoutHelper.applyHeight(constraints, 100, SizeUnit.PERCENTAGE, false);
                }
                if (child.getWidth() == -1.0) {
                    MigLayoutHelper.applyWidth(constraints, -1, SizeUnit.PIXELS, false);
                } else {
                    MigLayoutHelper.applyWidth(constraints, 100, SizeUnit.PERCENTAGE, false);
                }
                BoxLayoutAdapter adapter = wrappers.get(child).getSecond();
                adapter.updateConstraints(DesktopComponentsHelper.getComposition(child), constraints);
            } else {
                composition = DesktopComponentsHelper.getComposition(child);
            }
            layoutAdapter.updateConstraints(composition, layoutAdapter.getConstraints(child));
        }
    }

    protected void reAddChild(Component child) {
        int index = indexOf(child);

        boolean expanded = expandedComponent == child;
        remove(child);
        add(child, index);

        if (expanded) {
            expand(child);
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

    @Nullable
    @Override
    public Component getComponent(int index) {
        return Iterables.get(ownComponents, index);
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
    public void expand(Component component, String height, String width) {
        if (expandedComponent != null
                && expandedComponent instanceof DesktopComponent) {
            ((DesktopComponent) expandedComponent).setExpanded(false);
        }

        if (layoutAdapter.getFlowDirection() == BoxLayoutAdapter.FlowDirection.Y) {
            if (StringUtils.isEmpty(height) || AUTO_SIZE.equals(height) || height.endsWith("%")) {
                component.setHeight("100%");
            }
        } else if (layoutAdapter.getFlowDirection() == BoxLayoutAdapter.FlowDirection.X) {
            if (StringUtils.isEmpty(width) || AUTO_SIZE.equals(width) || width.endsWith("%")) {
                component.setWidth("100%");
            }
        }

        JComponent expandingChild = DesktopComponentsHelper.getComposition(component);

        Pair<JPanel, BoxLayoutAdapter> wrapperInfo = wrappers.get(component);
        if (wrapperInfo != null) {
            expandingChild = wrapperInfo.getFirst();
        }

        layoutAdapter.expand(expandingChild, height, width);

        if (component instanceof DesktopComponent) {
            ((DesktopComponent) component).setExpanded(true);
        }

        expandedComponent = component;
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public void resetExpanded() {
        expandedComponent = null;
        layoutAdapter.resetExpanded();

        requestRepaint();
    }

    @Override
    public boolean isExpanded(Component component) {
        return expandedComponent == component;
    }

    @Override
    public void setMargin(MarginInfo marginInfo) {
        layoutAdapter.setMargin(marginInfo);
    }

    @Override
    public MarginInfo getMargin() {
        return layoutAdapter.getMargin();
    }

    @Override
    public void setSpacing(boolean enabled) {
        layoutAdapter.setSpacing(enabled);
    }

    @Override
    public boolean getSpacing() {
        return layoutAdapter.getSpacing();
    }

    @Override
    public void setExpanded(boolean expanded) {
        layoutAdapter.setExpandLayout(expanded);
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

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
    }

    @Override
    public void addLayoutClickListener(LayoutClickListener listener) {
    }

    @Override
    public void removeLayoutClickListener(LayoutClickListener listener) {
    }

    @Override
    public void addShortcutAction(ShortcutAction action) {
        // do nothing
    }

    @Override
    public void removeShortcutAction(ShortcutAction action) {
        // do nothing
    }
}