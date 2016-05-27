/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.app.ui.security.history;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.gui.app.LinkColumnHelper;
import com.haulmont.cuba.web.gui.components.ShowLinkAction;
import com.haulmont.cuba.web.sys.LinkHandler;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenHistoryBrowse extends AbstractWindow {

    @Inject
    protected Table historyTable;

    @Inject
    protected Configuration configuration;

    @Inject
    protected ThemeConstants themeConstants;

    @Override
    public void init(Map<String, Object> params) {
        getDialogOptions()
                .setHeight(themeConstants.getInt("cuba.web.ScreenHistoryBrowse.height"))
                .setWidth(themeConstants.getInt("cuba.web.ScreenHistoryBrowse.width"))
                .setResizable(false);

        LinkColumnHelper.initColumn(historyTable, "caption",
                new LinkColumnHelper.Handler() {
                    @Override
                    public void onClick(Entity entity) {
                        close("windowClose");
                        openUrl(entity);
                    }
                }
        );
        historyTable.addAction(new ShowLinkAction(historyTable.getDatasource(), new ShowLinkAction.Handler() {
            @Override
            public String makeLink(Entity entity) {
                return entity != null ? ((ScreenHistoryEntity) entity).getUrl() : "";
            }
        }));
    }

    protected void openUrl(Entity entity) {
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