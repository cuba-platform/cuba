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

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.EntityDeletedException;
import com.haulmont.cuba.core.global.EntityLoadInfo;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.exception.AccessDeniedHandler;
import com.haulmont.cuba.web.exception.NoSuchScreenHandler;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LinkHandler implements Serializable {

    private static final long serialVersionUID = 3067643567208120222L;

    private static Log log = LogFactory.getLog(LinkHandler.class);
    private App app;
    private Map<String, String> requestParams;

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

            WindowConfig windowConfig = AppContext.getBean(WindowConfig.class);
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
                EntityLoadInfo info = EntityLoadInfo.parse(itemStr);
                if (info == null) {
                    log.warn("Invalid item definition: " + itemStr);
                    return;
                }
                Entity entity = loadEntityInstance(info);
                if (entity != null) {
                    app.getWindowManager().openEditor(windowInfo,
                            entity,
                            WindowManager.OpenType.NEW_TAB,
                            getParamsMap());
                } else {
                    throw new EntityDeletedException();
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
            EntityLoadInfo info = EntityLoadInfo.parse(value);
            if (info != null) {
                Entity entity = loadEntityInstance(info);
                if (entity != null)
                    params.put(name, entity);
            } else if (Boolean.TRUE.toString().equals(value) || Boolean.FALSE.toString().equals(value)) {
                params.put(name, BooleanUtils.toBoolean(value));
            } else {
                params.put(name, value);
            }
        }
        return params;
    }

    private Entity loadEntityInstance(EntityLoadInfo info) {
        DataService ds = ServiceLocator.getDataService();
        LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
        if (info.getViewName() != null)
            ctx.setView(info.getViewName());
        Entity entity;
        try {
            entity = ds.load(ctx);
        } catch (Exception e) {
            log.warn("Unable to load item: " + info, e);
            return null;
        }
        return entity;
    }
}
