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
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenMode;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.Window;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MenuCommand {

    protected MenuItem item;

    protected MenuItemCommand command;

    public MenuCommand(MenuItem item) {
        this.item = item;

        createCommand();
    }

    protected void createCommand() {
        if (StringUtils.isNotEmpty(item.getScreen())) {
            command = new ScreenCommand(item.getScreen(), item.getDescriptor());
            return;
        }

        if (StringUtils.isNotEmpty(item.getRunnableClass())) {
            command = new RunnableClassCommand(item.getRunnableClass());
            return;
        }

        if (StringUtils.isNotEmpty(item.getBean())) {
            command = new BeanCommand(item.getBean(), item.getBeanMethod());
        }
    }

    public void execute() {
        StopWatch sw = new Log4JStopWatch("MenuItem." + item.getId());

        command.run();

        sw.stop();
    }

    public String getCommandDescription() {
        return command.getDescription();
    }

    protected interface MenuItemCommand extends Runnable {

        String getDescription();
    }

    protected class ScreenCommand implements MenuItemCommand {

        protected String screen;
        protected Element descriptor;

        protected ScreenCommand(String screen, Element descriptor) {
            this.screen = screen;
            this.descriptor = descriptor;
        }

        @Override
        public void run() {
            Map<String, Object> params = loadParams(descriptor);

            OpenType openType = OpenType.NEW_TAB;
            String openTypeStr = descriptor.attributeValue("openType");
            if (StringUtils.isNotEmpty(openTypeStr)) {
                openType = OpenType.valueOf(openTypeStr);
            }

            WindowManager wm = AppBeans.get(WindowManagerProvider.class).get();

            if (openType.getOpenMode() == OpenMode.DIALOG) {
                String resizable = descriptor.attributeValue("resizable");
                if (StringUtils.isNotEmpty(resizable)) {
                    openType = openType.resizable(Boolean.parseBoolean(resizable));
                }
            }

            WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo(screen);

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

                    entityItem = AppBeans.get(Metadata.class).create(metaClassName);
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

        @Override
        public String getDescription() {
            return String.format("Opening window: \"%s\"", screen);
        }

        protected Map<String, Object> loadParams(Element descriptor) {
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

            String caption = AppBeans.get(MenuConfig.class).getItemCaption(screen);
            WindowParams.CAPTION.set(params, caption);

            return params;
        }

        protected Entity loadEntityInstance(EntityLoadInfo info) {
            LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
            if (info.getViewName() != null) {
                ctx.setView(info.getViewName());
            }

            //noinspection unchecked
            return AppBeans.get(DataService.class).load(ctx);
        }
    }

    protected class BeanCommand implements MenuItemCommand {

        protected String bean;
        protected String beanMethod;

        protected BeanCommand(String bean, String beanMethod) {
            this.bean = bean;
            this.beanMethod = beanMethod;
        }

        @Override
        public void run() {
            Object beanInstance = AppBeans.get(bean);

            try {
                MethodUtils.invokeMethod(beanInstance, beanMethod, null);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getDescription() {
            return String.format("Calling bean method: %s#%s", bean, beanMethod);
        }
    }

    protected class RunnableClassCommand implements MenuItemCommand {

        protected String runnableClass;

        protected RunnableClassCommand(String runnableClass) {
            this.runnableClass = runnableClass;
        }

        @Override
        public void run() {
            Class<?> clazz = AppBeans.get(Scripting.class).loadClass(runnableClass);
            if (clazz == null) {
                throw new IllegalStateException(String.format("Can't load class: %s", runnableClass));
            }

            if (!Runnable.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException(String.format("%s is not instance of Runnable", runnableClass));
            }

            try {
                Object runnableInstance = clazz.newInstance();

                ((Runnable) runnableInstance).run();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DevelopmentException(String.format("Failed to get a new instance of %s", runnableClass));
            }
        }

        @Override
        public String getDescription() {
            return String.format("Running \"%s\"", runnableClass);
        }
    }
}