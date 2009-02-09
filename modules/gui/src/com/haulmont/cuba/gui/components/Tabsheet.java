/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 17:33:46
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import java.util.List;
import java.util.Collection;

public interface Tabsheet 
    extends
        Component, Component.BelongToWindow
{
    Tab addTab(String name, Component component);
    void removeTab(String name);

    Tab getTab();
    void setTab(Tab tab);
    void setTab(String name);

    Collection<Tab> getTabs();

    interface Tab {
        String getName();
        void setName(String name);

        String getCaption();
        void setCaption(String caption);
    }
}
