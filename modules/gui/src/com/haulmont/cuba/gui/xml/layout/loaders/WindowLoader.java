/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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

import java.lang.reflect.Method;
import java.util.Collections;
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

    private void loadTimers(ComponentsFactory factory, Window component, Element element) {
        Element timersElement = element.element("timers");
        if (timersElement != null) {
            final List timers = timersElement.elements("timer");
            for (final Object o : timers) {
                loadTimer(factory, component, (Element) o);
            }
        }
    }

    private void loadTimer(ComponentsFactory factory, final Window component, Element element) {
        final Timer timer = factory.createTimer();
        timer.setXmlDescriptor(element);
        timer.setId(element.attributeValue("id"));
        String delay = element.attributeValue("delay");
        if (StringUtils.isEmpty(delay))
            throw new GuiDevelopmentException("Timer 'delay' can't be empty", context.getCurrentIFrameId(),
                    "Timer ID", timer.getId());
        int value;
        try {
            value = Integer.parseInt(delay);
        } catch (NumberFormatException e) {
            Map<String, Object> info = new HashMap<>(2);
            info.put("Timer delay", delay);
            info.put("Timer ID", timer.getId());
            throw new GuiDevelopmentException("Timer 'delay' must be numeric", context.getFullFrameId(), info);
        }
        if (value <= 0)
            throw new GuiDevelopmentException("Timer 'delay' must be greater than 0", context.getFullFrameId(),
                    "Timer ID", timer.getId());
        timer.setDelay(value);

        timer.setRepeating(BooleanUtils.toBoolean(element.attributeValue("repeating")));

        addAssignTimerFrameTask(timer);

        final String onTimer = element.attributeValue("onTimer");
        if (!StringUtils.isEmpty(onTimer)) {
            timer.addTimerListener(new Timer.TimerListener() {
                private Method timerMethod;

                @Override
                public void onTimer(Timer timer) {
                    Window window = timer.getFrame();
                    try {
                        if (timerMethod == null) {
                            String methodName = onTimer;
                            // legacy syntax support
                            if (onTimer.startsWith("invoke:")) {
                                methodName = onTimer.substring("invoke:".length()).trim();
                            }
                            timerMethod = window.getClass().getMethod(methodName, Timer.class);
                        }
                        timerMethod.invoke(window, timer);
                    } catch (Throwable e) {
                        throw new RuntimeException("Unable to invoke onTimer", e);
                    }
                }

                @Override
                public void onStopTimer(Timer timer) {
                    //do nothing
                }
            });
        }

        component.addTimer(timer);
    }

    protected void loadFocusedComponent(Window window, Element element) {
        String componentId = element.attributeValue("focusComponent");
        window.setFocusComponent(componentId);
    }

    private void addAssignTimerFrameTask(final Timer timer) {
        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, IFrame window) {
                timer.setFrame(window);
            }
        });
    }
}