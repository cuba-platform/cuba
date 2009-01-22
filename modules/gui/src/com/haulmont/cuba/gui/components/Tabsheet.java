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

public interface Tabsheet extends Component {
    Tab getTab();
    void setTab(Tab tab);
    void setTab(String name);

    List<Tab> getTabs();

    interface Tab {
        String getName();
        void setName(); 
    }
}
