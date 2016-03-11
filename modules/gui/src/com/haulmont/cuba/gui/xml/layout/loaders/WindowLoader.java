/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.DialogOptions;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class WindowLoader extends FrameLoader<Window> {

    protected String windowId;

    protected Window createComponent(ComponentsFactory factory) {
        return factory.createComponent(Window.class);
    }

    @Override
    public void createComponent() {
        resultComponent = createComponent(factory);
        resultComponent.setId(windowId);

        Element layoutElement = element.element("layout");
        createSubComponents(resultComponent, layoutElement);
    }

    @Override
    public void loadComponent() {
        context.setFrame(resultComponent);

        loadDialogOptions(resultComponent, element);

        assignXmlDescriptor(resultComponent, element);
        loadMessagesPack(resultComponent, element);
        loadCaption(resultComponent, element);
        loadActions(resultComponent, element);

        Element layoutElement = element.element("layout");
        if (layoutElement == null) {
            throw new GuiDevelopmentException("Required 'layout' element is not found", context.getFullFrameId());
        }

        loadSpacing(resultComponent, layoutElement);
        loadMargin(resultComponent, layoutElement);
        loadWidth(resultComponent, layoutElement);
        loadHeight(resultComponent, layoutElement);
        loadStyleName(resultComponent, layoutElement);
        loadVisible(resultComponent, layoutElement);

        loadTimers(factory, resultComponent, element);

        loadSubComponentsAndExpand(resultComponent, layoutElement);

        loadFocusedComponent(resultComponent, element);
    }

    protected void loadDialogOptions(Window resultComponent, Element element) {
        Element dialogModeElement = element.element("dialogMode");
        if (dialogModeElement != null) {
            DialogOptions dialogOptions = resultComponent.getDialogOptions();

            String width = dialogModeElement.attributeValue("width");
            if (StringUtils.isNotEmpty(width)) {
                if ("auto".equalsIgnoreCase(width)) {
                    dialogOptions.setWidth(Component.AUTO_SIZE_PX);
                } else if (!StringUtils.isBlank(width)) {
                    dialogOptions.setWidth(loadThemeInt(width));
                }
            }

            String height = dialogModeElement.attributeValue("height");
            if (StringUtils.isNotEmpty(height)) {
                if ("auto".equalsIgnoreCase(height)) {
                    dialogOptions.setHeight(Component.AUTO_SIZE_PX);
                } else if (!StringUtils.isBlank(height)) {
                    dialogOptions.setHeight(loadThemeInt(height));
                }
            }

            String closeable = dialogModeElement.attributeValue("closeable");
            if (StringUtils.isNotEmpty(closeable)) {
                dialogOptions.setCloseable(Boolean.parseBoolean(closeable));
            }

            String resizable = dialogModeElement.attributeValue("resizable");
            if (StringUtils.isNotEmpty(resizable)) {
                dialogOptions.setResizable(Boolean.parseBoolean(resizable));
            }

            String modal = dialogModeElement.attributeValue("modal");
            if (StringUtils.isNotEmpty(modal)) {
                dialogOptions.setModal(Boolean.parseBoolean(modal));
            }

            String forceDialog = dialogModeElement.attributeValue("forceDialog");
            if (StringUtils.isNotEmpty(forceDialog)) {
                dialogOptions.setForceDialog(Boolean.parseBoolean(forceDialog));
            }
        }
    }

    public String getWindowId() {
        return windowId;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    public static class Editor extends WindowLoader {
        @Override
        protected Window createComponent(ComponentsFactory factory) {
            return factory.createComponent(Window.Editor.class);
        }
    }

    public static class Lookup extends WindowLoader {
        @Override
        protected Window createComponent(ComponentsFactory factory) {
            return factory.createComponent(Window.Lookup.class);
        }
    }

    protected void loadTimers(ComponentsFactory factory, Window component, Element element) {
        Element timersElement = element.element("timers");
        if (timersElement != null) {
            final List timers = timersElement.elements("timer");
            for (final Object o : timers) {
                loadTimer(factory, component, (Element) o);
            }
        }
    }

    protected void loadTimer(ComponentsFactory factory, final Window component, Element element) {
        Timer timer = factory.createTimer();
        timer.setXmlDescriptor(element);
        timer.setId(element.attributeValue("id"));
        String delay = element.attributeValue("delay");
        if (StringUtils.isEmpty(delay)) {
            throw new GuiDevelopmentException("Timer 'delay' can't be empty", context.getCurrentFrameId(),
                    "Timer ID", timer.getId());
        }

        int value;
        try {
            value = Integer.parseInt(delay);
        } catch (NumberFormatException e) {
            Map<String, Object> info = new HashMap<>(2);
            info.put("Timer delay", delay);
            info.put("Timer ID", timer.getId());
            throw new GuiDevelopmentException("Timer 'delay' must be numeric", context.getFullFrameId(), info);
        }

        if (value <= 0) {
            throw new GuiDevelopmentException("Timer 'delay' must be greater than 0", context.getFullFrameId(),
                    "Timer ID", timer.getId());
        }

        timer.setDelay(value);
        timer.setRepeating(Boolean.parseBoolean(element.attributeValue("repeating")));

        final String onTimer = element.attributeValue("onTimer");
        if (!StringUtils.isEmpty(onTimer)) {
            String timerMethodName = onTimer;
            if (StringUtils.startsWith(onTimer, "invoke:")) {
                timerMethodName = StringUtils.substring(onTimer, "invoke:".length());
            }
            timerMethodName = StringUtils.trim(timerMethodName);

            addInitTimerMethodTask(timer, timerMethodName);
        }

        boolean autostart = "true".equals(element.attributeValue("autostart"));
        if (autostart) {
            addAutoStartTimerTask(timer);
        }
        timer.setFrame(context.getFrame());

        component.addTimer(timer);
    }

    protected void loadFocusedComponent(Window window, Element element) {
        String componentId = element.attributeValue("focusComponent");
        window.setFocusComponent(componentId);
    }

    protected void addInitTimerMethodTask(Timer timer, String timerMethodName) {
        context.addPostInitTask((context1, window) -> {
            Method timerMethod;
            try {
                timerMethod = window.getClass().getMethod(timerMethodName, Timer.class);
            } catch (NoSuchMethodException e) {
                Map<String, Object> params = new HashMap<>(2);
                params.put("Timer Id", timer.getId());
                params.put("Method name", timerMethodName);

                throw new GuiDevelopmentException("Unable to find invoke method for timer",
                        context1.getFullFrameId(), params);
            }

            timer.addActionListener(t -> {
                try {
                    timerMethod.invoke(window, t);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Unable to invoke onTimer", e);
                }
            });
        });
    }

    protected void addAutoStartTimerTask(Timer timer) {
        context.addPostInitTask((context1, window) -> timer.start());
    }
}