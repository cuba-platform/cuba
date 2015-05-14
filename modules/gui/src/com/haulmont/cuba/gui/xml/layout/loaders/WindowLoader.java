/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
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
public class WindowLoader extends FrameLoader implements ComponentLoader {

    public WindowLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final Window window = createComponent(factory);

        context.setFrame(window);

        assignXmlDescriptor(window, element);
        loadMessagesPack(window, element);
        loadCaption(window, element);
        loadActions(window, element);

        final Element layoutElement = element.element("layout");
        if (layoutElement == null)
            throw new GuiDevelopmentException("Required 'layout' element is not found", context.getFullFrameId());

        loadSubComponentsAndExpand(window, layoutElement);
        loadSpacing(window, layoutElement);
        loadMargin(window, layoutElement);
        loadWidth(window, layoutElement);
        loadHeight(window, layoutElement);
        loadStyleName(window, layoutElement);
        loadVisible(window, layoutElement);

        loadTimers(factory, window, element);

        loadFocusedComponent(window, element);

        return window;
    }

    protected Window createComponent(ComponentsFactory factory) {
        return factory.createComponent(Window.NAME);
    }

    public static class Editor extends WindowLoader {
        public Editor(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
            super(context, config, factory);
        }

        @Override
        protected Window createComponent(ComponentsFactory factory) {
            return factory.createComponent(Window.Editor.NAME);
        }
    }

    public static class Lookup extends WindowLoader {
        public Lookup(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
            super(context, config, factory);
        }

        @Override
        protected Window createComponent(ComponentsFactory factory) {
            return factory.createComponent(Window.Lookup.NAME);
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
        final Timer timer = factory.createTimer();
        timer.setXmlDescriptor(element);
        timer.setId(element.attributeValue("id"));
        String delay = element.attributeValue("delay");
        if (StringUtils.isEmpty(delay)) {
            throw new GuiDevelopmentException("Timer 'delay' can't be empty", context.getCurrentIFrameId(),
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
        timer.setRepeating(BooleanUtils.toBoolean(element.attributeValue("repeating")));

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

    protected void addInitTimerMethodTask(final Timer timer, final String timerMethodName) {
        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, final IFrame window) {
                Method timerMethod;
                try {
                    timerMethod = window.getClass().getMethod(timerMethodName, Timer.class);
                } catch (NoSuchMethodException e) {
                    Map<String, Object> params = new HashMap<>(2);
                    params.put("Timer Id", timer.getId());
                    params.put("Method name", timerMethodName);

                    throw new GuiDevelopmentException("Unable to find invoke method for timer",
                            context.getFullFrameId(), params);
                }

                final Method timerInvokeMethod = timerMethod;

                timer.addTimerListener(new Timer.TimerListener() {
                    @Override
                    public void onTimer(Timer timer) {
                        try {
                            timerInvokeMethod.invoke(window, timer);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException("Unable to invoke onTimer", e);
                        }
                    }

                    @Override
                    public void onStopTimer(Timer timer) {
                        //do nothing
                    }
                });
            }
        });
    }

    protected void addAutoStartTimerTask(final Timer timer) {
        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, IFrame window) {
                timer.start();
            }
        });
    }
}