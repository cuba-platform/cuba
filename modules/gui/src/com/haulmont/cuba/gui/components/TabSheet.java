/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

import java.util.Collection;

/**
 * @author abramov
 * @version $Id$
 */
public interface TabSheet extends Component, Component.BelongToFrame {

    String NAME = "tabSheet";

    Tab addTab(String name, Component component);
    Tab addLazyTab(String name, Element descriptor, ComponentLoader loader);
    void removeTab(String name);

    void removeAllTabs();

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

        boolean isClosable(); // false by default
        void setClosable(boolean closable);

        boolean isDetachable(); //false by default
        void setDetachable(boolean detachable);

        // use this to override close behavior, default action is to just remove tab
        TabCloseHandler getCloseHandler();
        void setCloseHandler(TabCloseHandler tabCloseHandler);

        /**
         * Set style for UI element that represents tab header caption.
         * @param styleName style styleName
         * @deprecated Use {@link #setStyleName} instead
         */
        @Deprecated
        void setCaptionStyleName(String styleName);

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

    /*
     * Implement this to override default behavior when user clicks button to close tab
     */
    interface TabCloseHandler {
        void onTabClose(Tab tab);
    }
}