/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:27:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

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
import java.util.List;

public class WindowLoader extends FrameLoader implements ComponentLoader {

    public WindowLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final Window window = createComponent(factory);

        context.setFrame(window);

        assignXmlDescriptor(window, element);
        loadMessagesPack(window, element);
        loadCaption(window, element);
        loadActions(window, element);

        final Element layoutElement = element.element("layout");
        loadExpandLayout(window, layoutElement);
        loadSubComponentsAndExpand(window, layoutElement);
        loadSpacing(window, layoutElement);
        loadMargin(window, layoutElement);
        loadWidth(window, layoutElement);
        loadHeight(window, layoutElement);
        loadStyleName(window, layoutElement);

        loadTimers(factory, window, element);

        loadFocusedComponent(window,element);

        return window;
    }

    protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
        return factory.createComponent("window");
    }

    public static class Editor extends WindowLoader {
        public Editor(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
            super(context, config, factory);
        }

        @Override
        protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
            return factory.createComponent("window.editor");
        }
    }

    public static class Lookup extends WindowLoader {
        public Lookup(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
            super(context, config, factory);
        }

        @Override
        protected Window createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
            return factory.createComponent("window.lookup");
        }
    }

    private void loadTimers(ComponentsFactory factory, Window component, Element element) throws InstantiationException {
        Element timersElement = element.element("timers");
        if (timersElement != null) {
            final List timers = timersElement.elements("timer");
            for (final Object o : timers) {
                loadTimer(factory, component, (Element) o);
            }
        }
    }

    private void loadTimer(ComponentsFactory factory, final Window component, Element element) throws InstantiationException {
        try {
            final Timer timer = factory.createTimer();
            timer.setXmlDescriptor(element);
            timer.setId(element.attributeValue("id"));
            String delay = element.attributeValue("delay");
            if (StringUtils.isEmpty(delay)) {
                throw new InstantiationException("Timer delay cannot be empty");
            }
            timer.setDelay(Integer.parseInt(delay));
            timer.setRepeating(BooleanUtils.toBoolean(element.attributeValue("repeating")));

            addAssignTimerFrameTask(timer);

            final String onTimer = element.attributeValue("onTimer");
            if (!StringUtils.isEmpty(onTimer)) {
                timer.addTimerListener(new Timer.TimerListener() {
                    public void onTimer(Timer timer) {
                        if (onTimer.startsWith("invoke:")) {
                            String methodName = onTimer.substring("invoke:".length()).trim();
                            Window window = timer.getFrame();
                            try {
                                Method method = window.getClass().getMethod(methodName, Timer.class);
                                method.invoke(window, timer);
                            } catch (Throwable e) {
                                throw new RuntimeException("Unable to invoke onTimer", e);
                            }
                        } else {
                            throw new UnsupportedOperationException("Unsupported onTimer format: " + onTimer);
                        }
                    }

                    public void onStopTimer(Timer timer) {
                        //do nothing
                    }
                });
            }

            component.addTimer(timer);
        } catch (NumberFormatException e) {
            throw new InstantiationException("Timer delay must be numeric");
        }
    }

    protected void loadFocusedComponent(Window window, Element element) {
        String componentId = element.attributeValue("focusComponent");
        if (componentId != null) {
            window.setFocusComponent(componentId);
        }
    }

    private void addAssignTimerFrameTask(final Timer timer) {
        context.addPostInitTask(new PostInitTask() {
            public void execute(Context context, IFrame window) {
                timer.setFrame((Window) window);
            }
        });
    }
}
