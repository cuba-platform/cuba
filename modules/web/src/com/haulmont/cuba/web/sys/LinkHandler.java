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
import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.exception.AccessDeniedHandler;
import com.haulmont.cuba.web.exception.NoSuchScreenHandler;
import com.haulmont.chile.core.model.MetaClass;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LinkHandler {

    private Log log = LogFactory.getLog(LinkHandler.class);
    private App app;
    private Map<String, String> requestParams;

    private static Pattern INSTANCE_RE = Pattern.compile("\\w+\\$\\w+-\\w+-\\w+-\\w+-\\w+-\\w+");

    public LinkHandler(App app, Map<String, String> requestParams) {
        this.app = app;
        this.requestParams = requestParams;
    }

    public void handle() {
        try {
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
                        WindowManager.OpenType.NEW_TAB,
                        getParamsMap());
            } else {
                Entity entity = loadEntityInstance(itemStr);
                if (entity != null) {
                    app.getWindowManager().openEditor(windowInfo,
                            entity,
                            WindowManager.OpenType.NEW_TAB,
                            getParamsMap());
                }
            }
        } catch (AccessDeniedException e) {
            new AccessDeniedHandler().handle(e, app);
        } catch (NoSuchScreenException e) {
            new NoSuchScreenHandler().handle(e, app);
        }
    }

    private Map<String, Object> getParamsMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        String paramsStr = requestParams.get("params");
        if (paramsStr == null)
            return params;

        String[] entries = paramsStr.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length != 2) {
                log.warn("Invalid parameter: " + entry);
                return params;       
            }
            String name = parts[0];
            String value = parts[1];
            Matcher matcher = INSTANCE_RE.matcher(value);
            if (matcher.matches()) {
                Entity entity = loadEntityInstance(value);
                if (entity != null)
                    params.put(name, entity);
            } else {
                params.put(name, value);
            }
        }
        return params;
    }

    private Entity loadEntityInstance(String str) {
        int p = str.indexOf('-');
        if (p < 2) {
            log.warn("Invalid item description: " + str);
            return null;
        }
        String entityName = str.substring(0, p);
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityName);
        if (metaClass == null) {
            log.warn("No metaclass found for item: " + str);
            return null;
        }

        String viewName = null;
        if ("saneco$GenDoc".equals(entityName)) {
            viewName = "gen-doc-search";
        }

        String entityIdStr = str.substring(p + 1);
        UUID id;
        try {
            id = UUID.fromString(entityIdStr);
        } catch (Exception e) {
            log.warn("Invalid ID for item: " + str);
            return null;
        }

        DataService ds = ServiceLocator.getDataService();
        LoadContext ctx = new LoadContext(metaClass).setId(id);
        if (viewName != null)
            ctx.setView(viewName);
        Entity entity;
        try {
            entity = ds.load(ctx);
        } catch (Exception e) {
            log.warn("Unable to load item: " + str, e);
            return null;
        }
        return entity;
    }
}
