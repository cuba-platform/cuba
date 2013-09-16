/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.app.ui.security.history;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.LinkColumnHelper;
import com.haulmont.cuba.web.gui.components.ShowLinkAction;
import com.haulmont.cuba.web.sys.LinkHandler;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Novikov
 * @version $Id$
 */
public class ScreenHistoryBrowse extends AbstractWindow {

    @Inject
    protected Table historyTable;

    @Inject
    protected Configuration configuration;

    @Override
    public void init(Map<String, Object> params) {
        DialogParams dialogParams = getDialogParams();
        dialogParams.setHeight(480);
        dialogParams.setWidth(500);
        dialogParams.setResizable(false);

        LinkColumnHelper.initColumn(historyTable, "caption",
                new LinkColumnHelper.Handler() {
                    public void onClick(Entity entity) {
                        close("windowClose");
                        openUrl(entity);
                    }
                }
        );
        historyTable.addAction(new ShowLinkAction(historyTable.getDatasource(), new ShowLinkAction.Handler() {
            public String makeLink(Entity entity) {
                return entity != null ? ((ScreenHistoryEntity) entity).getUrl() : "";
            }
        }));
    }

    private void openUrl(Entity entity) {
        ScreenHistoryEntity screenHistoryEntity = (ScreenHistoryEntity) entity;
        Map<String, String> paramsScreen = new HashMap<>();
        String url = screenHistoryEntity.getUrl();
        url = url.substring(url.indexOf("\u003f") + 1);
        paramsScreen.put("local", "true");
        String[] params =  url.split("&");
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            paramsScreen.put(name, value);
        }

        List<String> actions = configuration.getConfig(WebConfig.class).getLinkHandlerActions();
        LinkHandler linkHandler = AppBeans.getPrototype(LinkHandler.NAME,
                App.getInstance(),
                actions.isEmpty() ? "open" : actions.get(0),
                paramsScreen);
        linkHandler.handle();
    }
}