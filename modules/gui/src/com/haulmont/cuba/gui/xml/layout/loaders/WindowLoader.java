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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.DialogOptions;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger.LifeCycle;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.model.impl.ScreenDataXmlLoader;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.sys.CompanionDependencyInjector;
import com.haulmont.cuba.gui.xml.layout.ComponentRootLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.haulmont.cuba.gui.logging.UIPerformanceLogger.createStopWatch;

public class WindowLoader extends ContainerLoader<Window> implements ComponentRootLoader<Window> {

    protected String windowId;

    protected Window createComponent(ComponentsFactory factory) {
        return factory.createComponent(Window.class);
    }

    @Override
    public void createComponent() {
        throw new UnsupportedOperationException("Window cannot be created from XML element");
    }

    public void setResultComponent(Window window) {
        this.resultComponent = window;
    }

    @Override
    public void createContent(Element layoutElement) {
        if (layoutElement == null) {
            throw new DevelopmentException("Missing required 'layout' element");
        }
        createSubComponents(resultComponent, layoutElement);
    }

    @Override
    public void loadComponent() {
        loadScreenData(resultComponent, element);

        loadDialogOptions(resultComponent, element);

        assignXmlDescriptor(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadIcon(resultComponent, element);
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
        loadResponsive(resultComponent, layoutElement);
        loadVisible(resultComponent, layoutElement);

        loadTimers(factory, resultComponent, element);

        loadSubComponentsAndExpand(resultComponent, layoutElement);

        loadFocusedComponent(resultComponent, element);
        loadCrossFieldValidate(resultComponent, element);

        Screen controller = resultComponent.getFrameOwner();
        if (controller instanceof AbstractWindow) {
            Element companionsElem = element.element("companions");
            if (companionsElem != null) {
                StopWatch companionStopWatch = createStopWatch(LifeCycle.COMPANION, controller.getId());

                Object companion = initCompanion(companionsElem, (AbstractWindow) controller);

                companionStopWatch.stop();

                if (companion != null) {
                    getContext().addInjectTask((c, w) -> {
                        CompanionDependencyInjector cdi = new CompanionDependencyInjector((LegacyFrame) controller, companion);
                        cdi.setBeanLocator(beanLocator);
                        cdi.inject();
                    });
                }
            }
        }
    }

    protected Object initCompanion(Element companionsElem, AbstractWindow window) {
        Element element = companionsElem.element(AppConfig.getClientType().toString().toLowerCase());
        if (element != null) {
            String className = element.attributeValue("class");
            if (!StringUtils.isBlank(className)) {
                Class aClass = getScripting().loadClassNN(className);
                Object companion;
                try {
                    companion = aClass.newInstance();
                    window.setCompanion(companion);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to init Companion", e);
                }
                return companion;
            }
        }
        return null;
    }

    protected void loadMessagesPack(Frame frame, Element element) {
        String msgPack = element.attributeValue("messagesPack");
        if (msgPack != null) {
            setMessagesPack(msgPack);
        } else {
            setMessagesPack(this.messagesPack);
        }
    }

    protected void loadScreenData(Window window, Element element) {
        Element dataEl = element.element("data");
        if (dataEl != null) {
            ScreenDataXmlLoader screenDataXmlLoader = beanLocator.get(ScreenDataXmlLoader.class);
            ScreenData screenData = UiControllerUtils.getScreenData(window.getFrameOwner());
            screenDataXmlLoader.load(screenData, dataEl);
        }
    }

    protected void loadDialogOptions(Window resultComponent, Element element) {
        Element dialogModeElement = element.element("dialogMode");
        if (dialogModeElement != null) {
            DialogOptions dialogOptions = resultComponent.getDialogOptions();

            String xmlWidthValue = dialogModeElement.attributeValue("width");
            if (StringUtils.isNotBlank(xmlWidthValue)) {
                String themeWidthValue = loadThemeString(xmlWidthValue);
                dialogOptions.setWidth(themeWidthValue);
            }

            String xmlHeightValue = dialogModeElement.attributeValue("height");
            if (StringUtils.isNotBlank(xmlHeightValue)) {
                String themeHeightValue = loadThemeString(xmlHeightValue);
                dialogOptions.setHeight(themeHeightValue);
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

            String closeOnClickOutside = dialogModeElement.attributeValue("closeOnClickOutside");
            if (StringUtils.isNotEmpty(closeOnClickOutside)) {
                dialogOptions.setCloseOnClickOutside(Boolean.parseBoolean(closeOnClickOutside));
            }

            String maximized = dialogModeElement.attributeValue("maximized");
            if (StringUtils.isNotEmpty(maximized)) {
                dialogOptions.setMaximized(Boolean.parseBoolean(maximized));
            }

            String positionX = dialogModeElement.attributeValue("positionX");
            if (StringUtils.isNotEmpty(positionX)) {
                dialogOptions.setPositionX(Integer.parseInt(positionX));
            }

            String positionY = dialogModeElement.attributeValue("positionY");
            if (StringUtils.isNotEmpty(positionY)) {
                dialogOptions.setPositionY(Integer.parseInt(positionY));
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
            throw new GuiDevelopmentException("Timer 'delay' must be numeric", context.getFullFrameId(),
                    ParamsMap.of(
                            "Timer delay", delay,
                            "Timer ID", timer.getId()
                    ));
        }

        if (value <= 0) {
            throw new GuiDevelopmentException("Timer 'delay' must be greater than 0", context.getFullFrameId(),
                    "Timer ID", timer.getId());
        }

        timer.setDelay(value);
        timer.setRepeating(Boolean.parseBoolean(element.attributeValue("repeating")));

        String onTimer = element.attributeValue("onTimer");
        if (StringUtils.isNotEmpty(onTimer)) {
            String timerMethodName = onTimer;
            if (StringUtils.startsWith(onTimer, "invoke:")) {
                timerMethodName = StringUtils.substring(onTimer, "invoke:".length());
            }
            timerMethodName = StringUtils.trim(timerMethodName);

            addInitTimerMethodTask(timer, timerMethodName);
        }

        String autostart = element.attributeValue("autostart");
        if (StringUtils.isNotEmpty(autostart)
                && Boolean.parseBoolean(autostart)) {
            timer.start();
        }

        timer.setFrame(context.getFrame());

        component.addTimer(timer);
    }

    protected void loadFocusedComponent(Window window, Element element) {
        String componentId = element.attributeValue("focusComponent");
        window.setFocusComponent(componentId);
    }

    protected void loadCrossFieldValidate(Window window, Element element) {
        String crossFieldValidate = element.attributeValue("crossFieldValidate");
        if (StringUtils.isNotEmpty(crossFieldValidate)) {
            if (window instanceof Window.Editor) {
                ((Window.Editor) window).setCrossFieldValidate(Boolean.parseBoolean(crossFieldValidate));
            } else {
                throw new GuiDevelopmentException("Window should extend Window.Editor to use crossFieldValidate attribute",
                        context.getCurrentFrameId());
            }
        }
    }

    protected void addInitTimerMethodTask(Timer timer, String timerMethodName) {
        FrameOwner controller = context.getFrame().getFrameOwner();
        Class<? extends FrameOwner> windowClass = controller.getClass();

        Method timerMethod;
        try {
            timerMethod = windowClass.getMethod(timerMethodName, Timer.class);
        } catch (NoSuchMethodException e) {
            throw new GuiDevelopmentException("Unable to find invoke method for timer",
                    context.getFullFrameId(),
                    ParamsMap.of(
                            "Timer Id", timer.getId(),
                            "Method name", timerMethodName));
        }

        timer.addActionListener(t -> {
            try {
                timerMethod.invoke(controller, t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Unable to invoke onTimer", e);
            }
        });
    }

}