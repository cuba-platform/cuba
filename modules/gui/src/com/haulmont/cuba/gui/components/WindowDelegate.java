/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentVisitor;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WindowDelegate {

    public static final String LOOKUP_ITEM_CLICK_ACTION_ID = "lookupItemClickAction";
    public static final String LOOKUP_ENTER_PRESSED_ACTION_ID = "lookupEnterPressed";
    public static final String LOOKUP_SELECTED_ACTION_ID = "lookupAction";

    protected Window window;
    protected Window wrapper;
    protected Settings settings;

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    private Log log = LogFactory.getLog(getClass());

    public WindowDelegate(Window window) {
        this.window = window;
    }

    public Window wrapBy(Class<Window> wrapperClass) {
        try {
            Constructor<?> constructor = null;
            // First try to find an old-style constructor with IFrame parameter
            try {
                constructor = wrapperClass.getConstructor(Window.class);
            } catch (NoSuchMethodException e) {
                try {
                    constructor = wrapperClass.getConstructor(IFrame.class);
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
            throw new RuntimeException(e);
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
            final DsContext context = window.getDsContext();
            if (context != null) {
                ds = context.get(datasourceName);
            }
        }
        if (ds == null)
            throw new GuiDevelopmentException("Can't find main datasource", window.getId());
        else
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
                    new ComponentVisitor() {
                        @Override
                        public void visit(Component component, String name) {
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
                new ComponentVisitor() {
                    @Override
                    public void visit(Component component, String name) {
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
                }
        );
    }

    public void disposeComponents() {
        ComponentsHelper.walkComponents(
                window,
                new ComponentVisitor() {
                    @Override
                    public void visit(Component component, String name) {
                        if (component instanceof Component.Disposable) {
                            ((Component.Disposable) component).dispose();
                        }
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
        if (wrapper instanceof AbstractWindow)
            ((AbstractWindow) wrapper).postValidate(errors);
    }

    public boolean preClose(String actionId) {
        if (wrapper instanceof AbstractWindow)
            return ((AbstractWindow) wrapper).preClose(actionId);
        else
            return true;
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openWindow(windowInfo, openType, params);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openWindow(windowInfo, openType);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType, params, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType, params);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openLookup(windowInfo, handler, openType);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openFrame(wrapper, parent, windowInfo);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return window.getWindowManager().openFrame(wrapper, parent, windowInfo, params);
    }
}