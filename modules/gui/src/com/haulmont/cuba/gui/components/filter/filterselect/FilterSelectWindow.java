/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.filterselect;

import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.security.entity.FilterEntity;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Window is used for selecting a (@code FilterEntity}
 * 
 * @author gorbunkov
 * @version $Id$
 */
public class FilterSelectWindow extends AbstractWindow {

    @Inject
    protected CollectionDatasource<FilterEntity, UUID> filterEntitiesDs;

    @Inject
    protected ThemeConstantsManager themeConstantsManager;

    @Inject
    protected Table filterEntitiesTable;

    protected List<FilterEntity> filterEntities;

    @SuppressWarnings("unchecked")
    @Override
    public void init(Map<String, Object> params) {
        ThemeConstants theme = themeConstantsManager.getConstants();
        getDialogParams()
                .setHeight(Integer.valueOf(theme.get("cuba.gui.filterSelect.dialog.height")))
                .setWidth(Integer.valueOf(theme.get("cuba.gui.filterSelect.dialog.width")))
                .setResizable(true);

        filterEntities = (List<FilterEntity>) params.get("filterEntities");
        for (FilterEntity filterEntity : filterEntities) {
            filterEntitiesDs.includeItem(filterEntity);
        }
        filterEntitiesDs.refresh();

        filterEntitiesTable.setItemClickAction(new AbstractAction("selectByDblClk") {
            @Override
            public void actionPerform(Component component) {
                select();
            }
        });
    }

    public void select() {
        FilterEntity item = filterEntitiesDs.getItem();
        if (item == null) {
            showNotification(getMessage("FilterSelect.selectFilterEntity"), NotificationType.WARNING);
            return;
        } else {
            close(COMMIT_ACTION_ID);
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }

    public FilterEntity getFilterEntity() {
        return filterEntitiesDs.getItem();
    }
}
