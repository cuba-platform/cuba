/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.config;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.EntityLoadInfo;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.Window;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class MenuCommand {

    private MenuItem item;
    private WindowInfo windowInfo;

    public MenuCommand(MenuItem item, WindowInfo windowInfo) {
        this.item = item;
        this.windowInfo = windowInfo;
    }

    public void execute() {
        Element descriptor = item.getDescriptor();
        Map<String, Object> params = loadParams(descriptor);

        WindowManager.OpenType openType = WindowManager.OpenType.NEW_TAB;
        String openTypeStr = descriptor.attributeValue("openType");
        if (openTypeStr != null) {
            openType = WindowManager.OpenType.valueOf(openTypeStr);
        }

        WindowManagerProvider wmProvider = AppBeans.get(WindowManagerProvider.NAME);
        WindowManager wm = wmProvider.get();

        if (openType == WindowManager.OpenType.DIALOG) {
            String resizable = descriptor.attributeValue("resizable");
            if (!StringUtils.isEmpty(resizable)) {
                wm.getDialogParams().setResizable(BooleanUtils.toBoolean(resizable));
            }
        }

        final String id = windowInfo.getId();
        if (id.endsWith(Window.CREATE_WINDOW_SUFFIX) || id.endsWith(Window.EDITOR_WINDOW_SUFFIX)) {
            Entity entityItem;
            if (params.containsKey("item")) {
                entityItem = (Entity) params.get("item");
            } else {
                final String[] strings = id.split("[.]");
                String metaClassName;
                if (strings.length == 2) {
                    metaClassName = strings[0];
                } else if (strings.length == 3) {
                    metaClassName = strings[1];
                } else {
                    throw new UnsupportedOperationException();
                }

                Metadata metadata = AppBeans.get(Metadata.NAME);
                entityItem = metadata.create(metaClassName);
            }
            wm.openEditor(
                    windowInfo,
                    entityItem,
                    openType,
                    params
            );
        } else {
            wm.openWindow(
                    windowInfo,
                    openType,
                    params
            );
        }
    }

    private Map<String, Object> loadParams(Element descriptor) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Element element : Dom4j.elements(descriptor, "param")) {
            String value = element.attributeValue("value");
            EntityLoadInfo info = EntityLoadInfo.parse(value);
            if (info == null) {
                if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                    Boolean booleanValue = Boolean.valueOf(value);
                    params.put(element.attributeValue("name"), booleanValue);
                } else {
                    if (value.startsWith("${") && value.endsWith("}")) {
                        String property = AppContext.getProperty(value.substring(2, value.length() - 1));
                        if (!StringUtils.isEmpty(property))
                            value = property;
                    }
                    params.put(element.attributeValue("name"), value);
                }
            } else {
                params.put(element.attributeValue("name"), loadEntityInstance(info));
            }
        }

        String caption = MenuConfig.getMenuItemCaption(item.getId());
        WindowParams.CAPTION.set(params, caption);

        return params;
    }

    private Entity loadEntityInstance(EntityLoadInfo info) {
        DataService ds = AppBeans.get(DataService.NAME);
        LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
        if (info.getViewName() != null)
            ctx.setView(info.getViewName());
        Entity entity = ds.load(ctx);
        return entity;
    }
}