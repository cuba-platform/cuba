/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.06.2009 12:04:31
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.DataServiceRemote;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.web.App;
import com.haulmont.chile.core.model.MetaClass;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LinkHandler {

    private Log log = LogFactory.getLog(LinkHandler.class);
    private App app;
    private Map<String, String> requestParams;

    public LinkHandler(App app, Map<String, String> requestParams) {
        this.app = app;
        this.requestParams = requestParams;
    }

    public void handle() {
        String screenName = requestParams.get("screen");
        if (screenName == null) {
            log.warn("ScreenId not found in request parameters");
            return;
        }

        WindowConfig windowConfig = AppConfig.getInstance().getWindowConfig();
        WindowInfo windowInfo = windowConfig.getWindowInfo(screenName);
        if (windowInfo == null) {
            log.warn("WindowInfo not found for screen: " + screenName);
            return;
        }

        String itemStr = requestParams.get("item");
        if (itemStr == null) {
            app.getWindowManager().openWindow(windowInfo,
                    com.haulmont.cuba.gui.WindowManager.OpenType.NEW_TAB);
        } else {
            int p = itemStr.indexOf('-');
            if (p < 2) {
                log.warn("Invalid item description: " + itemStr);
                return;
            }
            String entityName = itemStr.substring(0, p);
            MetaClass metaClass = MetadataProvider.getSession().getClass(entityName);
            if (metaClass == null) {
                log.warn("No metaclass found for item: " + itemStr);
                return;
            }

            String entityIdStr = itemStr.substring(p + 1);
            UUID id;
            try {
                id = UUID.fromString(entityIdStr);
            } catch (Exception e) {
                log.warn("Invalid ID for item: " + itemStr);
                return;
            }

            DataService ds = ServiceLocator.getDataService();
            DataServiceRemote.LoadContext ctx = new DataService.LoadContext(metaClass).setId(id);
            Entity entity = null;
            try {
                entity = ds.load(ctx);
            } catch (Exception e) {
                log.warn("Unable to load item: " + itemStr, e);
                return;
            }

            app.getWindowManager().openEditor(windowInfo,
                    entity,
                    com.haulmont.cuba.gui.WindowManager.OpenType.NEW_TAB);
        }
    }
}
