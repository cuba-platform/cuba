/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.app.ui.entitylog;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.FilterApplyAction;
import com.haulmont.cuba.gui.components.actions.FilterClearAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.security.entity.LoggedEntity;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tulupov
 * @version $Id$
 */
public class EntityLogViewer extends AbstractWindow {

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        ((LookupField) getComponent("filter.object")).setOptionsMap(getTargets());

        Table table = getComponent("events");
        table.addAction(new FilterApplyAction(table));
        table.addAction(new FilterClearAction(table, "filter.filter-pane"));

        Date date = AppBeans.get(TimeSource.class).currentTimestamp();
        ((DateField) getComponent("filter.createdFrom")).setValue(DateUtils.addDays(date, -1));
        ((DateField) getComponent("filter.createdTo")).setValue(DateUtils.addDays(date, 1));

        getDsContext().get("values").addListener(new CollectionDsListenerAdapter<Entity>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                getDsContext().get("valuesDs").refresh();
            }
        });
    }

    protected Map<String, Object> getTargets() {
        Map<String, Object> result = new HashMap<>();
        for (LoggedEntity le : loadEntities()) {
            String name = le.getName();
            String message = messages.getMessage(name.substring(0, name.lastIndexOf(".")),
                    name.substring(name.lastIndexOf(".") + 1, name.length()));
            result.put(message, name);
        }
        return result;
    }

    protected List<LoggedEntity> loadEntities() {
        DataSupplier supplier = getDsContext().getDataService();
        LoadContext lc = new LoadContext(LoggedEntity.class);
        lc.setQuery(new LoadContext.Query("select e from sec$LoggedEntity e"));
        lc.setView(new View(LoggedEntity.class).addProperty("name"));
        return supplier.loadList(lc);
    }
}
