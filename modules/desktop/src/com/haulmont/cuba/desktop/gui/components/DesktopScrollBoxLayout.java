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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.MarginInfo;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DesktopScrollBoxLayout extends DesktopAbstractComponent<JScrollPane>
        implements ScrollBoxLayout, AutoExpanding, DesktopContainer {

    protected List<Component> components = new ArrayList<>();
    protected Orientation orientation = Orientation.VERTICAL;
    protected ScrollBarPolicy scrollBarPolicy = ScrollBarPolicy.VERTICAL;
    protected DesktopAbstractBox content;

    protected boolean scheduledRepaint = false;

    public DesktopScrollBoxLayout() {
        impl = new JScrollPane();
        // by default it is turned off
        impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        impl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        impl.setBorder(null);

        content = new DesktopVBox();

        DesktopVBox contentPane = new DesktopVBox();
        contentPane.setContainer(this);
        contentPane.add(content);

        impl.setViewportView(DesktopComponentsHelper.getComposition(contentPane));

        applyScrollBarPolicy(scrollBarPolicy);

        // support tables with 100% width like in web
        impl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustViewPreferredSize(true);
            }
        });

        setWidth("100%");
    }

    private void adjustViewPreferredSize() {
        adjustViewPreferredSize(false);
    }

    private void adjustViewPreferredSize(boolean onResize) {
        JComponent view = DesktopComponentsHelper.getComposition(content);
        Dimension minimumSize = view.getMinimumSize();
        Dimension preferredSize = null;
        switch (scrollBarPolicy) {
            case VERTICAL:
                preferredSize = new Dimension(impl.getViewport().getWidth(), minimumSize.height);
                break;

            case HORIZONTAL:
                preferredSize = new Dimension(minimumSize.width, impl.getViewport().getHeight());
                break;

            case NONE:
                preferredSize = new Dimension(impl.getViewport().getWidth(), impl.getViewport().getHeight());
                break;

            case BOTH:
                preferredSize = new Dimension(minimumSize.width, minimumSize.height);
                break;
        }
        view.setPreferredSize(preferredSize);

        if (onResize) {
            ScrollBarPolicy scrollBarPolicy = getScrollBarPolicy();

            if (scrollBarPolicy != ScrollBarPolicy.BOTH && preferredSize.width > 0 && preferredSize.height > 0) {
                JViewport viewport = impl.getViewport();
                if (viewport != null) {
                    //setting of the same preferred size has side-effects
                    if (!viewport.getViewSize().equals(preferredSize)) {
                        viewport.setViewSize(preferredSize);
                    }
                }
            }
        }

        requestRepaint();
    }

    protected void requestRepaint() {
        if (!scheduledRepaint) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JComponent view = DesktopComponentsHelper.getComposition(content);

                    view.revalidate();
                    view.repaint();

                    scheduledRepaint = false;
                }
            });

            scheduledRepaint = true;
        }
    }

    @Override
    public void add(Component component) {
        add(component, getOwnComponents().size());
    }

    @Override
    public void add(Component component, int index) {
        if (component.getParent() != null && component.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

        DesktopAbstractBox newContent = null;
        if (orientation == Orientation.VERTICAL && !(content instanceof DesktopVBox)) {
            newContent = new DesktopVBox();
        } else if (orientation == Orientation.HORIZONTAL && !(content instanceof DesktopHBox)) {
            newContent = new DesktopHBox();
        }

        if (newContent != null) {
            content = newContent;

            DesktopVBox contentPane = new DesktopVBox();
            contentPane.add(content);
            contentPane.setContainer(this);

            impl.setViewportView(DesktopComponentsHelper.getComposition(contentPane));

            applyScrollBarPolicy(scrollBarPolicy);
        }

        content.add(component, index);

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

        component.setParent(this);

        adjustViewPreferredSize();
    }

    @Override
    public int indexOf(Component child) {
        return Iterables.indexOf(components, c -> c == child);
    }

    @Nullable
    @Override
    public Component getComponent(int index) {
        return Iterables.get(components, index);
    }

    @Override
    public void remove(Component component) {
        components.remove(component);
        content.remove(component);

        if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
            ((DesktopAbstractComponent) component).setParentEnabled(true);
        }

        component.setParent(null);

        adjustViewPreferredSize();
    }

    @Override
    public void removeAll() {
        content.removeAll();

        List<Component> innerComponents = new ArrayList<>(components);
        components.clear();

        for (Component component : innerComponents) {
            if (component instanceof DesktopAbstractComponent && !isEnabledWithParent()) {
                ((DesktopAbstractComponent) component).setParentEnabled(true);
            }

            component.setParent(null);
        }

        adjustViewPreferredSize();
    }

    @Override
    public void setFrame(com.haulmont.cuba.gui.components.Frame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (Component childComponent : content.getOwnComponents()) {
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
    }

    @Override
    public Component getOwnComponent(String id) {
        return content.getOwnComponent(id);
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return content.getComponent(id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return content.getOwnComponents();
    }

    @Override
    public Collection<Component> getComponents() {
        return content.getComponents();
    }

    @Override
    public boolean expandsWidth() {
        return !(content instanceof AutoExpanding) || ((AutoExpanding) content).expandsWidth();
    }

    @Override
    public boolean expandsHeight() {
        return !(content instanceof AutoExpanding) || ((AutoExpanding) content).expandsHeight();
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (!Objects.equals(orientation, this.orientation)) {
            if (!components.isEmpty())
                throw new IllegalStateException("Unable to change scrollbox orientation after adding components to it");

            this.orientation = orientation;
        }
    }

    @Override
    public ScrollBarPolicy getScrollBarPolicy() {
        return scrollBarPolicy;
    }

    @Override
    public void setScrollBarPolicy(ScrollBarPolicy scrollBarPolicy) {
        if (this.scrollBarPolicy != scrollBarPolicy) {
            applyScrollBarPolicy(scrollBarPolicy);
        }
        this.scrollBarPolicy = scrollBarPolicy;
    }

    private void applyScrollBarPolicy(ScrollBarPolicy scrollBarPolicy) {
        switch (scrollBarPolicy) {
            case BOTH:
                impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                impl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

                content.setWidth("-1px");
                content.setHeight("-1px");
                break;

            case HORIZONTAL:
                impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                impl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

                content.setWidth("-1px");
                content.setHeight("100%");
                break;

            case VERTICAL:
                impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                impl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

                content.setWidth("100%");
                content.setHeight("-1px");
                break;

            case NONE:
                impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                impl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

                content.setWidth("100%");
                content.setHeight("100%");
                break;
        }
    }

    @Override
    public void setMargin(MarginInfo marginInfo) {
        content.setMargin(marginInfo);
    }

    @Override
    public MarginInfo getMargin() {
        return content.getMargin();
    }

    @Override
    public void setSpacing(boolean enabled) {
        content.setSpacing(enabled);
    }

    @Override
    public boolean getSpacing() {
        return content.getSpacing();
    }

    @Override
    public void updateComponent(Component child) {
        adjustViewPreferredSize();
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        boolean resultEnabled = isEnabledWithParent();
        for (Component component : components) {
            if (component instanceof DesktopAbstractComponent) {
                ((DesktopAbstractComponent) component).setParentEnabled(resultEnabled);
            }
        }
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
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
    public void addShortcutAction(ShortcutAction action) {
        // do nothing
    }

    @Override
    public void removeShortcutAction(ShortcutAction action) {
        // do nothing
    }
}