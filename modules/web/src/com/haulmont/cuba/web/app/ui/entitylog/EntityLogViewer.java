/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 19.05.2009 16:53:54
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.entitylog;

import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.security.entity.LoggedEntity;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityLogViewer extends AbstractWindow {

    public EntityLogViewer(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        final CollectionDatasource datasource = getDsContext().get("events");
        datasource.refresh();

        ((LookupField) getComponent("filter.object")).setOptionsMap(getTargets());

        Table table = getComponent("events");
        TableActionsHelper helper = new TableActionsHelper(this, table);
        helper.createFilterApplyAction("filter.apply");
        helper.createFilterClearAction("filter.clear", "filter-pane");

        Date date = TimeProvider.currentTimestamp();
        ((DateField) getComponent("filter.createdFrom")).setValue(DateUtils.addDays(date, -1));
        ((DateField) getComponent("filter.createdTo")).setValue(DateUtils.addDays(date, 1));
    }

    protected Map<String, Object> getTargets() {
        Map<String, Object> result = new HashMap<String, Object>();
        for (LoggedEntity le : loadEntities()) {
            String name = le.getName();
            String message = MessageProvider.getMessage(name.substring(0, name.lastIndexOf(".")),
                    name.substring(name.lastIndexOf(".") + 1, name.length()));
            result.put(message, name);
        }
        return result;
    }

    protected List<LoggedEntity> loadEntities() {
        DataService service = getDsContext().getDataService();
        LoadContext lc = new LoadContext(LoggedEntity.class);
        lc.setQuery(new LoadContext.Query("select e from sec$LoggedEntity e"));
        lc.setView(new View(LoggedEntity.class).addProperty("name"));
        return service.loadList(lc);
    }
}
