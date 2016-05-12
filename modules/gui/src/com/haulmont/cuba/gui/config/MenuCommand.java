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
import com.haulmont.cuba.gui.WindowManager.OpenMode;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.Window;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class MenuCommand {

    private MenuItem item;
    private WindowInfo windowInfo;

    public MenuCommand(MenuItem item, WindowInfo windowInfo) {
        this.item = item;
        this.windowInfo = windowInfo;
    }

    public void execute() {
        StopWatch sw = new Log4JStopWatch("MenuItem." + windowInfo.getId());
        try {
            Element descriptor = item.getDescriptor();
            Map<String, Object> params = loadParams(descriptor);

            OpenType openType = OpenType.NEW_TAB;
            String openTypeStr = descriptor.attributeValue("openType");
            if (StringUtils.isNotEmpty(openTypeStr)) {
                openType = OpenType.valueOf(openTypeStr);
            }

            WindowManagerProvider wmProvider = AppBeans.get(WindowManagerProvider.NAME);
            WindowManager wm = wmProvider.get();

            if (openType.getOpenMode() == OpenMode.DIALOG) {
                String resizable = descriptor.attributeValue("resizable");
                if (StringUtils.isNotEmpty(resizable)) {
                    wm.getDialogParams().setResizable(Boolean.parseBoolean(resizable));
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
        } finally {
            sw.stop();
        }
    }

    private Map<String, Object> loadParams(Element descriptor) {
        Map<String, Object> params = new HashMap<>();
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