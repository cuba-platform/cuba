package com.haulmont.cuba.web.sys.linkhandling;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.NoSuchScreenException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.exception.AccessDeniedHandler;
import com.haulmont.cuba.gui.exception.EntityAccessExceptionHandler;
import com.haulmont.cuba.gui.exception.NoSuchScreenHandler;
import com.haulmont.cuba.web.App;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Component(ScreensLinkHandlerProcessor.NAME)
public class ScreensLinkHandlerProcessor implements LinkHandlerProcessor, Ordered {
    public static final String NAME = "cuba_ScreensLinkHandlerProcessor";

    @Inject
    private Logger log;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DataService dataService;

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected EntityAccessExceptionHandler entityAccessExceptionHandler;
    @Inject
    protected AccessDeniedHandler accessDeniedHandler;
    @Inject
    protected NoSuchScreenHandler noSuchScreenHandler;

    @Override
    public boolean canHandle(ExternalLinkContext linkContext) {
        return linkContext.getRequestParams().containsKey("screen");
    }

    @Override
    public void handle(ExternalLinkContext linkContext) {
        String screenName = linkContext.getRequestParams().get("screen");
        App app = linkContext.getApp();

        final WindowInfo windowInfo = windowConfig.getWindowInfo(screenName);
        if (windowInfo == null) {
            log.warn("WindowInfo not found for screen: {}", screenName);
            return;
        }

        try {
            openWindow(windowInfo, linkContext);
        } catch (EntityAccessException e) {
            entityAccessExceptionHandler.handle(e, app.getWindowManager());
        } catch (AccessDeniedException e) {
            accessDeniedHandler.handle(e, app.getWindowManager());
        } catch (NoSuchScreenException e) {
            noSuchScreenHandler.handle(e, app.getWindowManager());
        }
    }

    protected void openWindow(WindowInfo windowInfo, ExternalLinkContext linkContext) {
        Map<String, String> requestParams = linkContext.getRequestParams();
        App app = linkContext.getApp();

        String itemStr = requestParams.get("item");
        String openTypeParam = requestParams.get("openType");
        WindowManager.OpenType openType = WindowManager.OpenType.NEW_TAB;

        if (StringUtils.isNotEmpty(openTypeParam)) {
            try {
                openType = WindowManager.OpenType.valueOf(openTypeParam);
            } catch (IllegalArgumentException e) {
                log.warn("Unknown open type ({}) in request parameters", openTypeParam);
            }
        }

        if (itemStr == null) {
            app.getWindowManager().openWindow(windowInfo, openType, getParamsMap(requestParams));
        } else {
            EntityLoadInfo info = EntityLoadInfo.parse(itemStr);
            if (info == null) {
                log.warn("Invalid item definition: {}", itemStr);
            } else {
                Entity entity = loadEntityInstance(info);
                if (entity != null)
                    app.getWindowManager().openEditor(windowInfo, entity, openType, getParamsMap(requestParams));
                else
                    throw new EntityAccessException();
            }
        }
    }

    protected Map<String, Object> getParamsMap(Map<String, String> requestParams) {
        Map<String, Object> params = new HashMap<>();
        String paramsStr = requestParams.get("params");
        if (paramsStr == null)
            return params;

        String[] entries = paramsStr.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length != 2) {
                log.warn("Invalid parameter: {}", entry);
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

    protected Entity loadEntityInstance(EntityLoadInfo info) {
        if (info.isNewEntity()) {
            return metadata.create(info.getMetaClass());
        }

        @SuppressWarnings("unchecked")
        LoadContext<Entity> ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
        if (info.getViewName() != null)
            ctx.setView(info.getViewName());
        Entity entity;
        try {
            entity = dataService.load(ctx);
        } catch (Exception e) {
            log.warn("Unable to load item: {}", info, e);
            return null;
        }
        return entity;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 30;
    }
}
