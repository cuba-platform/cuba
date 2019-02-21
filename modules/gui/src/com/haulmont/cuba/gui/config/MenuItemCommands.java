/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.config;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.BeanLocatorAware;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.logging.UserActionsLogger;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.sys.UiControllerPropertyInjector;
import com.haulmont.cuba.gui.sys.UiControllerProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

@Component("cuba_MenuItemCommands")
public class MenuItemCommands {

    private static final org.slf4j.Logger userActionsLog = LoggerFactory.getLogger(UserActionsLogger.class);

    @Inject
    protected DataService dataService;
    @Inject
    protected MenuConfig menuConfig;
    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected Scripting scripting;
    @Inject
    protected Metadata metadata;

    @Inject
    protected BeanLocator beanLocator;

    /**
     * Create menu command.
     *
     * @param item menu item
     * @return command
     */
    public MenuItemCommand create(FrameOwner origin, MenuItem item) {
        Map<String, Object> params = loadParams(item.getDescriptor(), item.getScreen());
        List<UiControllerProperty> properties = loadProperties(item.getDescriptor());

        if (StringUtils.isNotEmpty(item.getScreen())) {
            return new ScreenCommand(origin, item, item.getScreen(), item.getDescriptor(), params, properties);
        }

        if (StringUtils.isNotEmpty(item.getRunnableClass())) {
            return new RunnableClassCommand(origin, item, item.getRunnableClass(), params);
        }

        if (StringUtils.isNotEmpty(item.getBean())) {
            return new BeanCommand(origin, item, item.getBean(), item.getBeanMethod(), params);
        }

        return null;
    }

    protected Map<String, Object> loadParams(Element descriptor, String screen) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

        for (Element element : descriptor.elements("param")) {
            String value = element.attributeValue("value");
            EntityLoadInfo info = EntityLoadInfo.parse(value);
            if (info == null) {
                if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                    Boolean booleanValue = Boolean.valueOf(value);
                    builder.put(element.attributeValue("name"), booleanValue);
                } else {
                    if (value.startsWith("${") && value.endsWith("}")) {
                        String property = AppContext.getProperty(value.substring(2, value.length() - 1));
                        if (!StringUtils.isEmpty(property)) {
                            value = property;
                        }
                    }
                    builder.put(element.attributeValue("name"), value);
                }
            } else {
                builder.put(element.attributeValue("name"), loadEntityInstance(info));
            }
        }

        if (StringUtils.isNotEmpty(screen)) {
            WindowInfo windowInfo = windowConfig.getWindowInfo(screen);
            // caption is passed only for legacy screens
            if (windowInfo.getDescriptor() != null) {
                String caption = menuConfig.getItemCaption(screen);

                builder.put(WindowParams.CAPTION.name(), caption);
            }
        }

        return builder.build();
    }

    protected List<UiControllerProperty> loadProperties(Element element) {
        Element propsEl = element.element("properties");
        if (propsEl == null) {
            return Collections.emptyList();
        }

        List<Element> propElements = propsEl.elements("property");
        if (propElements.isEmpty()) {
            return Collections.emptyList();
        }

        List<UiControllerProperty> properties = new ArrayList<>(propElements.size());

        for (Element property : propElements) {
            String name = property.attributeValue("name");
            if (StringUtils.isEmpty(name)) {
                throw new IllegalStateException("Screen property cannot have empty name");
            }

            String value = property.attributeValue("value");
            if (StringUtils.isEmpty(value)) {
                throw new IllegalStateException("Screen property cannot have empty value");
            }

            properties.add(new UiControllerProperty(name, value, UiControllerProperty.Type.VALUE));
        }

        return properties;
    }

    protected Entity loadEntityInstance(EntityLoadInfo info) {
        LoadContext ctx = new LoadContext(info.getMetaClass()).setId(info.getId());
        if (info.getViewName() != null) {
            ctx.setView(info.getViewName());
        }

        //noinspection unchecked
        return dataService.load(ctx);
    }

    protected StopWatch createStopWatch(MenuItem item) {
        return new Slf4JStopWatch("MenuItem." + item.getId(), LoggerFactory.getLogger(UIPerformanceLogger.class));
    }

    protected class ScreenCommand implements MenuItemCommand {
        protected FrameOwner origin;
        protected MenuItem item;

        protected String screen;
        protected Element descriptor;
        protected Map<String, Object> params;
        protected List<UiControllerProperty> properties;

        protected ScreenCommand(FrameOwner origin, MenuItem item,
                                String screen, Element descriptor, Map<String, Object> params, List<UiControllerProperty> properties) {
            this.origin = origin;
            this.item = item;
            this.screen = screen;
            this.descriptor = descriptor;
            this.params = new HashMap<>(params); // copy map values only for compatibility with legacy screens
            this.properties = properties;
        }

        @Override
        public void run() {
            userActionsLog.trace("Menu item {} triggered", item.getId());

            StopWatch sw = createStopWatch(item);

            WindowManager.OpenType openType = WindowManager.OpenType.NEW_TAB;
            String openTypeStr = descriptor.attributeValue("openType");
            if (StringUtils.isNotEmpty(openTypeStr)) {
                openType = WindowManager.OpenType.valueOf(openTypeStr);
            }

            if (openType.getOpenMode() == OpenMode.DIALOG) {
                String resizable = descriptor.attributeValue("resizable");
                if (StringUtils.isNotEmpty(resizable)) {
                    openType = openType.resizable(Boolean.parseBoolean(resizable));
                }
            }

            String screenId = this.screen;

            Screens screens = getScreenContext(origin).getScreens();

            if (screenId.endsWith(Window.CREATE_WINDOW_SUFFIX)
                    || screenId.endsWith(Window.EDITOR_WINDOW_SUFFIX)) {
                // only for legacy screens

                Entity entityItem;
                if (params.containsKey("item")) {
                    entityItem = (Entity) params.get("item");
                } else {
                    String[] strings = screenId.split("[.]");
                    String metaClassName;
                    if (strings.length == 2) {
                        metaClassName = strings[0];
                    } else if (strings.length == 3) {
                        metaClassName = strings[1];
                    } else {
                        throw new UnsupportedOperationException("Incorrect screen parameters in menu item " + item.getId());
                    }

                    entityItem = metadata.create(metaClassName);
                }

                WindowInfo windowInfo = windowConfig.getWindowInfo(this.screen);
                ((WindowManager) screens).openEditor(windowInfo, entityItem, openType, params);

            } else {
                Screen screen = screens.create(screenId, openType.getOpenMode(), new MapScreenOptions(params));

                // inject declarative properties
                UiControllerPropertyInjector propertyInjector = beanLocator.getPrototype(UiControllerPropertyInjector.NAME,
                        screen, properties);
                propertyInjector.inject();

                screens.showFromNavigation(screen);
            }

            sw.stop();
        }

        @Override
        public String getDescription() {
            return String.format("Opening window: \"%s\"", screen);
        }
    }

    protected class BeanCommand implements MenuItemCommand {

        protected FrameOwner origin;
        protected MenuItem item;

        protected String bean;
        protected String beanMethod;
        protected Map<String, Object> params;

        protected BeanCommand(FrameOwner origin, MenuItem item,
                              String bean, String beanMethod, Map<String, Object> params) {
            this.origin = origin;
            this.item = item;
            this.bean = bean;
            this.beanMethod = beanMethod;
            this.params = params;
        }

        @Override
        public void run() {
            userActionsLog.trace("Menu item {} triggered", item.getId());

            StopWatch sw = createStopWatch(item);

            Object beanInstance = beanLocator.get(bean);
            try {
                Method methodWithParams = MethodUtils.getAccessibleMethod(beanInstance.getClass(), beanMethod, Map.class);
                if (methodWithParams != null) {
                    methodWithParams.invoke(beanInstance, params);
                    return;
                }

                Method methodWithScreen = MethodUtils.getAccessibleMethod(beanInstance.getClass(), beanMethod, FrameOwner.class);
                if (methodWithScreen != null) {
                    methodWithScreen.invoke(beanInstance, origin);
                    return;
                }

                MethodUtils.invokeMethod(beanInstance, beanMethod, (Object[]) null);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Unable to execute bean method", e);
            }

            sw.stop();
        }

        @Override
        public String getDescription() {
            return String.format("Calling bean method: %s#%s", bean, beanMethod);
        }
    }

    protected class RunnableClassCommand implements MenuItemCommand {

        protected FrameOwner origin;
        protected MenuItem item;

        protected String runnableClass;
        protected Map<String, Object> params;

        protected RunnableClassCommand(FrameOwner origin, MenuItem item,
                                       String runnableClass, Map<String, Object> params) {
            this.origin = origin;
            this.item = item;
            this.runnableClass = runnableClass;
            this.params = params;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            userActionsLog.trace("Menu item {} triggered", item.getId());

            StopWatch sw = createStopWatch(item);

            Class<?> clazz = scripting.loadClass(runnableClass);
            if (clazz == null) {
                throw new IllegalStateException(String.format("Can't load class: %s", runnableClass));
            }

            if (!Runnable.class.isAssignableFrom(clazz)
                    && !Consumer.class.isAssignableFrom(clazz)
                    && !MenuItemRunnable.class.isAssignableFrom(clazz)) {

                throw new IllegalStateException(
                        String.format("Class \"%s\" must implement Runnable or Consumer<Map<String, Object>> or MenuItemRunnable",
                                runnableClass));
            }

            Constructor<?> constructor;
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new DevelopmentException(String.format("Unable to get constructor of %s", runnableClass));
            }

            Object classInstance;
            try {
                classInstance = constructor.newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new DevelopmentException(String.format("Failed to get a new instance of %s", runnableClass));
            }

            if (classInstance instanceof BeanLocatorAware) {
                ((BeanLocatorAware) classInstance).setBeanLocator(beanLocator);
            }

            if (classInstance instanceof MenuItemRunnable) {
                ((MenuItemRunnable) classInstance).run(origin, item);
            } else if (classInstance instanceof Consumer) {
                ((Consumer) classInstance).accept(params);
            } else {
                ((Runnable) classInstance).run();
            }

            sw.stop();
        }

        @Override
        public String getDescription() {
            return String.format("Running \"%s\"", runnableClass);
        }
    }
}