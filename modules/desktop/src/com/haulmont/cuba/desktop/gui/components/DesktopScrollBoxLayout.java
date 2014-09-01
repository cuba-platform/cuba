/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author budarov
 * @version $Id$
 */
public class DesktopScrollBoxLayout extends DesktopAbstractComponent<JScrollPane> implements ScrollBoxLayout, AutoExpanding {

    protected List<Component> components = new ArrayList<>();
    private Orientation orientation = Orientation.VERTICAL;
    private ScrollBarPolicy scrollBarPolicy = ScrollBarPolicy.VERTICAL;
    private DesktopAbstractBox content;

    protected boolean scheduledRepaint = false;

    public DesktopScrollBoxLayout() {
        impl = new JScrollPane();
        // by default it is turned off
        impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        impl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        impl.setBorder(null);

        content = new DesktopVBox();

        DesktopVBox contentPane = new DesktopVBox();
        contentPane.add(content);

        impl.setViewportView(DesktopComponentsHelper.getComposition(contentPane));

        applyScrollBarPolicy(scrollBarPolicy);

        // support tables with 100% width like in web
        impl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustViewPreferredSize();
            }
        });
    }

    private void adjustViewPreferredSize() {
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
        view.setMaximumSize(preferredSize);

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

            impl.setViewportView(DesktopComponentsHelper.getComposition(contentPane));

            applyScrollBarPolicy(scrollBarPolicy);
        }

        content.add(component);

        adjustViewPreferredSize();
    }

    @Override
    public void remove(Component component) {
        content.remove(component);
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return content.getOwnComponent(id);
    }

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        return content.getComponent(id);
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
        if (!ObjectUtils.equals(orientation, this.orientation)) {
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
    public void setMargin(boolean enable) {
        content.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        content.setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    @Override
    public void setSpacing(boolean enabled) {
        content.setSpacing(enabled);
    }
}