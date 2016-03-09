/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

import java.util.Collection;

/**
 * @author petunin
 */
public interface Accordion extends Component.Container, Component.BelongToFrame {
    String NAME = "accordion";

    Tab addTab(String name, Component component);
    Tab addLazyTab(String name, Element descriptor, ComponentLoader loader);

    /** Get current tab. May be null */
    Tab getTab();
    void setTab(Tab tab);
    void setTab(String name);

    Tab getTab(String name);
    Component getTabComponent(String name);

    Collection<Tab> getTabs();

    void addListener(TabChangeListener listener);
    void removeListener(TabChangeListener listener);

    interface Tab {
        String getName();
        void setName(String name);

        String getCaption();
        void setCaption(String caption);

        boolean isEnabled();
        void setEnabled(boolean enabled);

        boolean isVisible();
        void setVisible(boolean visible);

        /**
         * Set style for UI element that represents tab header.
         * @param styleName style
         */
        void setStyleName(String styleName);
        String getStyleName();
    }

    interface TabChangeListener {
        void tabChanged(Tab newTab);
    }
}
