/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:18:21
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.xml.ParametersHelper;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class DsContextImpl implements DsContext {
    private Context context;
    private DataService dataservice;

    private Map<String, Datasource> datasourceMap =
            new HashMap<String, Datasource>();

    private Map<String, Collection<Datasource>> contextListeners =
            new HashMap<String, Collection<Datasource>>();

    public DsContextImpl(DataService dataservice) {
        this.dataservice = dataservice;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        for (Map.Entry<String, Collection<Datasource>> entry : contextListeners.entrySet()) {
            final String property = entry.getKey();
            final Object value = context.getValue(property);

            if (value != null) {
                final Collection<Datasource> datasources = entry.getValue();
                for (Datasource datasource : datasources) {
                    datasource.setItem(value);
                }
            }
        }

        context.addValueListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                for (Map.Entry<String, Collection<Datasource>> entry : contextListeners.entrySet()) {
                    if (entry.getKey().equals(property)) {
                        final Collection<Datasource> datasources = entry.getValue();
                        for (Datasource datasource : datasources) {
                            datasource.setItem(value);
                        }
                    }
                }
            }
        });
    }

    public DataService getDataService() {
        return dataservice;
    }

    public <T extends Datasource> T get(String id) {
        return (T) datasourceMap.get(id);
    }

    public Collection<Datasource> getAll() {
        return datasourceMap.values();
    }

    public void register(Datasource datasource) {
        datasourceMap.put(datasource.getId(), datasource);
    }

    public void registerListener(ParametersHelper.ParameterInfo item, Datasource datasource) {
        if (ParametersHelper.ParameterInfo.Type.CONTEXT.equals(item.getType())) {
            Collection<Datasource> collection = contextListeners.get(item.getPath());
            if (collection == null) {
                collection = new ArrayList<Datasource>();
                contextListeners.put(item.getPath(), collection);
            }
            collection.add(datasource);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
