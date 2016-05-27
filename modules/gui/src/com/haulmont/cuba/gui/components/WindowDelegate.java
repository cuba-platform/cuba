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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.gui.settings.Settings;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public class WindowDelegate {

    public static final String LOOKUP_ITEM_CLICK_ACTION_ID = "lookupItemClickAction";
    public static final String LOOKUP_ENTER_PRESSED_ACTION_ID = "lookupEnterPressed";
    public static final String LOOKUP_SELECTED_ACTION_ID = "lookupAction";

    protected Window window;
    protected Window wrapper;
    protected Settings settings;

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    private Logger log = LoggerFactory.getLogger(getClass());

    public WindowDelegate(Window window) {
        this.window = window;
    }

    public Window wrapBy(Class<?> wrapperClass) {
        try {
            Constructor<?> constructor = null;
            // First try to find an old-style constructor with Frame parameter
            try {
                constructor = wrapperClass.getConstructor(Window.class);
            } catch (NoSuchMethodException e) {
                try {
                    constructor = wrapperClass.getConstructor(Frame.class);
                } catch (NoSuchMethodException e1) {
                    //
                }
            }
            if (constructor != null) {
                wrapper = (Window) constructor.newInstance(window);
            } else {
                // If not found, get the default constructor
                constructor = wrapperClass.getConstructor();
                wrapper = (Window) constructor.newInstance();
                ((AbstractFrame) wrapper).setWrappedFrame(window);
            }
            return wrapper;
        } catch (Throwable e) {
            throw new RuntimeException("Unable to init window controller", e);
        }
    }

    public Window getWrapper() {
        return wrapper;
    }

    public Datasource getDatasource() {
        Datasource ds = null;
        Element element = ((Component.HasXmlDescriptor) window).getXmlDescriptor();
        String datasourceName = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasourceName)) {
            DsContext context = window.getDsContext();
            if (context != null) {
                ds = context.get(datasourceName);
            }
        }

        if (ds == null) {
            throw new GuiDevelopmentException("Can't find main datasource", window.getId());
        }

        return ds;
    }

    public Settings getSettings() {
        return settings;
    }

    public void saveSettings() {
        if (settings != null) {
            final Set<String> visitedIds = new HashSet<>();

            ComponentsHelper.walkComponents(
                    window,
                    (component, name) -> {
                        if (component instanceof Component.HasSettings) {
                            log.trace("Saving settings for : " + name + " : " + component);

                            if (visitedIds.contains(name)) {
                                log.warn("Names of some HasSettings components clashed, set Id for component explicitly, name=" + name);
                            }

                            visitedIds.add(name);

                            Element e = WindowDelegate.this.settings.get(name);
                            boolean modified = ((Component.HasSettings) component).saveSettings(e);

                            if (component instanceof Component.HasPresentations
                                    && ((Component.HasPresentations) component).isUsePresentations()) {
                                Object def = ((Component.HasPresentations) component).getDefaultPresentationId();
                                e.addAttribute("presentation", def != null ? def.toString() : "");
                                Presentations presentations = ((Component.HasPresentations) component).getPresentations();
                                if (presentations != null) {
                                    presentations.commit();
                                }
                            }
                            WindowDelegate.this.settings.setModified(modified);
                        }
                    }
            );
            settings.commit();
        }
    }

    public void deleteSettings() {
        settings.delete();
    }

    public void applySettings(Settings settings) {
        this.settings = settings;
        ComponentsHelper.walkComponents(
                window,
                (component, name) -> {
                    if (component instanceof Component.HasSettings) {
                        log.trace("Applying settings for : " + name + " : " + component);
                        Element e = WindowDelegate.this.settings.get(name);
                        ((Component.HasSettings) component).applySettings(e);
                        if (component instanceof Component.HasPresentations && e.attributeValue("presentation") != null) {
                            final String def = e.attributeValue("presentation");
                            if (!StringUtils.isEmpty(def)) {
                                UUID defaultId = UUID.fromString(def);
                                ((Component.HasPresentations) component).applyPresentationAsDefault(defaultId);
                            }
                        }
                    }
                }
        );
    }

    public void disposeComponents() {
        ComponentsHelper.walkComponents(
                window,
                (component, name) -> {
                    if (component instanceof Component.Disposable) {
                        ((Component.Disposable) component).dispose();
                    }
                }
        );
    }

    public boolean isValid() {
        Collection<Component> components = ComponentsHelper.getComponents(window);
        for (Component component : components) {
            if (component instanceof Component.Validatable) {
                if (!((Component.Validatable) component).isValid())
                    return false;
            }
        }
        return true;
    }

    public void validate() throws ValidationException {
        Collection<Component> components = ComponentsHelper.getComponents(window);
        for (Component component : components) {
            if (component instanceof Component.Validatable) {
                ((Component.Validatable) component).validate();
            }
        }
    }

    public void postValidate(ValidationErrors errors) {
        if (wrapper instanceof AbstractWindow) {
            ((AbstractWindow) wrapper).postValidate(errors);
        }
    }

    public boolean preClose(String actionId) {
        if (wrapper instanceof AbstractWindow) {
            return ((AbstractWindow) wrapper).preClose(actionId);
        }

        return true;
    }

    public Window openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openWindow(windowInfo, openType, params);
    }

    public Window openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openWindow(windowInfo, openType);
    }

    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType) {
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return window.getWindowManager().openEditor(editorScreen, item, openType);
    }

    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return window.getWindowManager().openEditor(editorScreen, item, openType, params);
    }

    public Window.Editor openEditor(Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo editorScreen = windowConfig.getEditorScreen(item);
        return window.getWindowManager().openEditor(editorScreen, item, openType, params, parentDs);
    }

    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType, params, parentDs);
    }

    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType, params);
    }

    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType, parentDs);
    }

    public Window.Editor openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType);
    }

    public Window.Lookup openLookup(Class<? extends Entity>  entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        return window.getWindowManager().openLookup(lookupScreen, handler, openType);
    }

    public Window.Lookup openLookup(Class<? extends Entity>  entityClass, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo lookupScreen = windowConfig.getLookupScreen(entityClass);
        return window.getWindowManager().openLookup(lookupScreen, handler, openType, params);
    }

    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openLookup(windowInfo, handler, openType, params);
    }

    public Window.Lookup openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openLookup(windowInfo, handler, openType);
    }

    public Frame openFrame(Component parent, String windowAlias) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openFrame(wrapper, parent, windowInfo);
    }

    public Frame openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openFrame(wrapper, parent, windowInfo, params);
    }
}