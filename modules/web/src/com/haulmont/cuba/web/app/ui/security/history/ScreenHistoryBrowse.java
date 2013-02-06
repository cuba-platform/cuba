/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.app.ui.security.history;

import com.haulmont.cuba.gui.DialogParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import com.haulmont.cuba.web.app.LinkColumnHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.web.gui.components.ShowLinkAction;
import com.haulmont.cuba.web.sys.LinkHandler;

import java.util.*;

/**
 * @author novikov
 * @version $Id$
 */
public class ScreenHistoryBrowse extends AbstractWindow{

    protected Table historyTable;

    @Override
    public void init(Map<String, Object> params) {
        DialogParams dialogParams = getDialogParams();
        dialogParams.setHeight(480);
        dialogParams.setWidth(500);
        dialogParams.setResizable(false);
        historyTable = getComponent("historyTable");
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
//        LinkHandler linkHandler = new LinkHandler(AppUI.getInstance(), paramsScreen);
//        linkHandler.handle();
    }
}