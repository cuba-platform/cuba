/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 17:33:46
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import org.dom4j.Element;

import java.io.Serializable;
import java.util.Collection;

import com.haulmont.cuba.gui.xml.layout.ComponentLoader;

public interface Tabsheet 
    extends
        Component, Component.BelongToFrame, Component.Expandable
{
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
    }

    interface TabChangeListener extends Serializable {
        void tabChanged(Tab newTab);
    }
}
