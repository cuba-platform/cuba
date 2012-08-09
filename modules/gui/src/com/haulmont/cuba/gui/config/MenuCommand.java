/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.config;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityLoadInfo;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MenuCommand {

    private WindowManager windowManager;
    private MenuItem item;
    private WindowInfo windowInfo;

    public MenuCommand(WindowManager windowManager, MenuItem item, WindowInfo windowInfo) {
        this.windowManager = windowManager;
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

        if (openType == WindowManager.OpenType.DIALOG) {
            String resizable = descriptor.attributeValue("resizable");
            if (!StringUtils.isEmpty(resizable)) {
                windowManager.getDialogParams().setResizable(BooleanUtils.toBoolean(resizable));
            }
        }

        final String id = windowInfo.getId();
        if (id.endsWith(".create") || id.endsWith(".edit")) {
            Entity entityItem;
            if (params.containsKey("item")) {
                entityItem = (Entity) params.get("item");
            } else {
                final String[] strings = id.split("[.]");
                String metaClassName;
                if (strings.length == 2)
                    metaClassName = strings[0];
                else if (strings.length == 3)
                    metaClassName = strings[1];
                else
                    throw new UnsupportedOperationException();

                final Class javaClass = MetadataProvider.getReplacedClass(metaClassName);
                if (javaClass == null)
                    throw new IllegalStateException(String.format("Can't find metaClass %s", metaClassName));

                try {
                    entityItem = (Entity) javaClass.newInstance();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
            windowManager.openEditor(
                    windowInfo,
                    entityItem,
                    openType,
                    params
            );
        } else {
            windowManager.openWindow(
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
        DataService ds = ServiceLocator.getDataService();
        LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
        if (info.getViewName() != null)
            ctx.setView(info.getViewName());
        Entity entity = ds.load(ctx);
        return entity;
    }
}
