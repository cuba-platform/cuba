/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.ComponentVisitor;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.settings.Settings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class WindowDelegate {

    protected Window window;
    protected WindowManager windowManager;
    protected Window wrapper;
    protected Settings settings;

    protected WindowConfig windowConfig = AppContext.getBean(WindowConfig.class);

    private Log log = LogFactory.getLog(getClass());

    public WindowDelegate(Window window, WindowManager windowManager) {
        this.window = window;
        this.windowManager = windowManager;
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
            throw new IllegalStateException("Can't find main datasource");
        else
            return ds;
    }

    public Settings getSettings() {
        return settings;
    }

    public void saveSettings() {
        ComponentsHelper.walkComponents(
                window,
                new ComponentVisitor() {
                    public void visit(Component component, String name) {
                        if (component instanceof Component.HasSettings && WindowDelegate.this.settings != null) {
                            log.trace("Saving settings for : " + name + " : " + component);
                            Element e = WindowDelegate.this.settings.get(name);
                            boolean modified = ((Component.HasSettings) component).saveSettings(e);
                            if (component instanceof Component.HasPresentations && ((Component.HasPresentations) component).isUsePresentations()) {
                                Object def = ((Component.HasPresentations) component).getDefaultPresentationId();
                                if (def != null) {
                                    e.addAttribute("presentation", def.toString());
                                }
                                ((Component.HasPresentations) component).getPresentations().commit();
                            }
                            WindowDelegate.this.settings.setModified(modified);
                        }
                        if (component instanceof Component.Disposable) {
                            ((Component.Disposable) component).dispose();
                        }
                    }
                }
        );
        if (settings != null) {
            settings.commit();
        }
    }

    public void applySettings(Settings settings) {
        this.settings = settings;
        ComponentsHelper.walkComponents(
                window,
                new ComponentVisitor() {
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

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openWindow(windowInfo, openType, params);
    }

    public <T extends Window> T openWindow(String windowAlias, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openWindow(windowInfo, openType);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType, params, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType, params);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType, Datasource parentDs) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType, parentDs);
    }

    public <T extends Window> T openEditor(String windowAlias, Entity item, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openEditor(windowInfo, item, openType);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openLookup(windowInfo, handler, openType, params);
    }

    public <T extends Window> T openLookup(String windowAlias, Window.Lookup.Handler handler, WindowManager.OpenType openType) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openLookup(windowInfo, handler, openType);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openFrame(wrapper, parent, windowInfo);
    }

    public <T extends IFrame> T openFrame(Component parent, String windowAlias, Map<String, Object> params) {
        WindowInfo windowInfo = windowConfig.getWindowInfo(windowAlias);
        return windowManager.<T>openFrame(wrapper, parent, windowInfo, params);
    }
}
