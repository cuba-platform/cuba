/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 17:33:46
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

import java.io.Serializable;
import java.util.Collection;

public interface Tabsheet 
    extends
        Component, Component.BelongToFrame, Component.Expandable
{
    String NAME = "tabsheet";

    Tab addTab(String name, Component component);
    Tab addLazyTab(String name, Element descriptor, ComponentLoader loader);
    void removeTab(String name);

    Tab getTab();
    void setTab(Tab tab);
    void setTab(String name);

    Tab getTab(String name);

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

        // use this to override close behavior, default action is to just remove tab
        TabCloseHandler getCloseHandler();
        void setCloseHandler(TabCloseHandler tabCloseHandler);
    }

    interface TabChangeListener extends Serializable {
        void tabChanged(Tab newTab);
    }

    /*
     * Implement this to override default behavior when user clicks button to close tab
     */
    interface TabCloseHandler extends Serializable {
        void onTabClose(Tab tab);
    }
}
