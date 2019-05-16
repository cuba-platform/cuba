/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.web.gui.facets;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.compatibility.LegacyFragmentAdapter;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader.ComponentContext;
import com.haulmont.cuba.web.gui.components.WebTimer;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component("cuba_TimerFacetProvider")
public class TimerFacetProvider implements FacetProvider<Timer> {
    @Override
    public Class<Timer> getFacetClass() {
        return Timer.class;
    }

    @Override
    public Timer create() {
        return new WebTimer();
    }

    @Override
    public String getFacetTag() {
        return "timer";
    }

    @Override
    public void loadFromXml(Timer facet, Element element, ComponentContext context) {
        loadTimer(facet, element, context);
    }

    protected void loadTimer(Timer timer, Element element, ComponentContext context) {
        String id = element.attributeValue("id");
        if (isNotEmpty(id)) {
            timer.setId(id);
        }

        String delay = element.attributeValue("delay");
        if (StringUtils.isEmpty(delay)) {
            throw new GuiDevelopmentException("Timer 'delay' can't be empty", context,
                    "Timer ID", timer.getId());
        }

        int value = parseInt(delay);
        if (value <= 0) {
            throw new GuiDevelopmentException("Timer 'delay' must be greater than 0",
                    context, "Timer ID", timer.getId());
        }

        timer.setDelay(value);
        timer.setRepeating(parseBoolean(element.attributeValue("repeating")));

        // use @Subscribe event handlers instead
        String onTimer = element.attributeValue("onTimer");
        if (isNotEmpty(onTimer)) {
            String timerMethodName = onTimer;
            if (StringUtils.startsWith(onTimer, "invoke:")) {
                timerMethodName = StringUtils.substring(onTimer, "invoke:".length());
            }
            timerMethodName = StringUtils.trim(timerMethodName);

            addInitTimerMethodTask(timer, timerMethodName, context);
        }

        String autostart = element.attributeValue("autostart");
        if (isNotEmpty(autostart)
                && parseBoolean(autostart)) {
            timer.start();
        }
    }

    // for compatibility only

    @Deprecated
    protected void addInitTimerMethodTask(Timer timer, String timerMethodName, ComponentContext context) {
        FrameOwner controller = context.getFrame().getFrameOwner();
        if (controller instanceof LegacyFragmentAdapter) {
            controller = ((LegacyFragmentAdapter) controller).getRealScreen();
        }

        Class<? extends FrameOwner> windowClass = controller.getClass();

        Method timerMethod;
        try {
            timerMethod = windowClass.getMethod(timerMethodName, Timer.class);
        } catch (NoSuchMethodException e) {
            throw new GuiDevelopmentException("Unable to find invoke method for timer", context,
                    ParamsMap.of(
                            "Timer Id", timer.getId(),
                            "Method name", timerMethodName));
        }

        timer.addTimerActionListener(new DeclarativeTimerActionHandler(timerMethod, controller));
    }

    @Deprecated
    protected static class DeclarativeTimerActionHandler implements Consumer<Timer.TimerActionEvent> {
        protected final Method timerMethod;
        protected final FrameOwner controller;

        public DeclarativeTimerActionHandler(Method timerMethod, FrameOwner controller) {
            this.timerMethod = timerMethod;
            this.controller = controller;
        }

        @Override
        public void accept(Timer.TimerActionEvent e) {
            try {
                timerMethod.invoke(controller, e.getSource());
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException("Unable to invoke onTimer", ex);
            }
        }
    }
}