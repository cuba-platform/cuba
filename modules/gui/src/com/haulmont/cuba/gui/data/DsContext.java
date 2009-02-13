/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:15:07
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;

import java.util.Collection;

public interface DsContext {
    Context getContext();
    void setContext(Context context);

    DataService getDataService();

    <T extends Datasource> T get(String name);
    Collection<Datasource> getAll();

    boolean isModified();

    void refresh();
    void commit();

    void regirterDependency(Datasource ds, Datasource dependFrom);
}
