/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:15:07
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import java.util.Collection;

public interface DsContext {
    WindowContext getWindowContext();
    void setWindowContext(WindowContext context);

    DataService getDataService();

    <T extends Datasource> T get(String name);
    Collection<Datasource> getAll();

    boolean isModified();

    void refresh();
    void commit();

    void regirterDependency(Datasource ds, Datasource dependFrom, String property);
}
