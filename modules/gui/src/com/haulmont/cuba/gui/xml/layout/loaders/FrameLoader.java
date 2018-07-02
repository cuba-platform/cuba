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

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.WrappedFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FrameLoader<T extends Frame> extends ContainerLoader<T> {

    protected Element layoutElement;
    protected ComponentLoaderContext innerContext;

    protected String frameId;

    private ScreenViewsLoader screenViewsLoader = AppBeans.get(ScreenViewsLoader.NAME);

    protected Frame wrapByCustomClass(Frame frame) {
        String screenClass = element.attributeValue("class");
        if (StringUtils.isBlank(screenClass)) {
            screenClass = AbstractFrame.class.getName();
        }

        Class<?> aClass = scripting.loadClass(screenClass);
        if (aClass == null) {
            aClass = ReflectionHelper.getClass(screenClass);
        }
        if (aClass == null) {
            throw new GuiDevelopmentException("Unable to load controller class", frame.getId());
        }

        if (AbstractWindow.class.isAssignableFrom(aClass)) {
            LoggerFactory.getLogger(FrameLoader.class).warn(
                    "Frame class '{}' should not be inherited from AbstractWindow. " +
                            "It may cause problems with controller life cycle. " +
                            "Frame controllers should inherit AbstractFrame.",
                    aClass.getSimpleName());
        }

        return ((WrappedFrame) frame).wrapBy(aClass);
    }

    protected void initWrapperFrame(Frame wrappingFrame, Element rootFrameElement, Map<String, Object> params,
                                    ComponentLoaderContext parentContext) {
        parentContext.addInjectTask(new FrameInjectPostInitTask(wrappingFrame, params));

        boolean wrapped = StringUtils.isNotBlank(rootFrameElement.attributeValue("class"));
        parentContext.addInitTask(new FrameLoaderInitTask(wrappingFrame, params, wrapped));
        parentContext.addPostInitTask(new FrameLoaderPostInitTask(wrappingFrame, params, wrapped));
    }

    protected void initCompanion(Element companionsElem, AbstractFrame frame) {
        Element element = companionsElem.element(AppConfig.getClientType().toString().toLowerCase());
        if (element != null) {
            String className = element.attributeValue("class");
            if (!StringUtils.isBlank(className)) {
                Class aClass = scripting.loadClassNN(className);
                Object companion;
                try {
                    companion = aClass.newInstance();
                    frame.setCompanion(companion);

                    CompanionDependencyInjector cdi = new CompanionDependencyInjector(frame, companion);
                    cdi.inject();
                } catch (Exception e) {
                    throw new RuntimeException("Unable to init companion for frame", e);
                }
            }
        }
    }

    protected void loadMessagesPack(Frame frame, Element element) {
        String msgPack = element.attributeValue("messagesPack");
        if (msgPack != null) {
            frame.setMessagesPack(msgPack);
            setMessagesPack(msgPack);
        } else {
            frame.setMessagesPack(this.messagesPack);
            setMessagesPack(this.messagesPack);
        }
    }

    @Override
    public void createComponent() {
        //noinspection unchecked
        Frame clientSpecificFrame = (T) factory.createComponent(Frame.NAME);
        clientSpecificFrame.setId(frameId);

        loadMessagesPack(clientSpecificFrame, element);

        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext();

        String frameId = parentContext.getCurrentFrameId();
        if (parentContext.getFullFrameId() != null) {
            frameId = parentContext.getFullFrameId() + "." + frameId;
        }

        innerContext = new ComponentLoaderContext(context.getParams());
        innerContext.setCurrentFrameId(parentContext.getCurrentFrameId());
        innerContext.setFullFrameId(frameId);
        innerContext.setFrame(clientSpecificFrame);
        innerContext.setParent(parentContext);
        setContext(innerContext);

        layoutElement = element.element("layout");
        if (layoutElement == null) {
            throw new GuiDevelopmentException("Required element 'layout' is not found", this.context.getFullFrameId());
        }

        createSubComponents(clientSpecificFrame, layoutElement);

        setContext(parentContext);

        //noinspection unchecked
        resultComponent = (T) wrapByCustomClass(clientSpecificFrame);
    }

    @Override
    public void loadComponent() {
        screenViewsLoader.deployViews(element);

        Element dsContextElement = element.element("dsContext");
        DsContextLoader contextLoader = new DsContextLoader(context.getDsContext().getDataSupplier());

        DsContext dsContext = contextLoader.loadDatasources(dsContextElement, context.getDsContext(), innerContext.getAliasesMap());

        assignXmlDescriptor(resultComponent, element);

        loadVisible(resultComponent, layoutElement);
        loadActions(resultComponent, element);

        loadSpacing(resultComponent, layoutElement);
        loadMargin(resultComponent, layoutElement);
        loadWidth(resultComponent, layoutElement);
        loadHeight(resultComponent, layoutElement);
        loadStyleName(resultComponent, layoutElement);
        loadResponsive(resultComponent, layoutElement);

        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext();
        setContext(innerContext);

        FrameContext frameContext = new FrameContextImpl(resultComponent, context.getParams());
        resultComponent.setContext(frameContext);

        if (dsContext != null) {
            resultComponent.setDsContext(dsContext);

            for (Datasource ds : dsContext.getAll()) {
                if (ds instanceof DatasourceImplementation) {
                    ((DatasourceImplementation) ds).initialized();
                }
            }

            dsContext.setFrameContext(frameContext);
        }

        innerContext.setDsContext(dsContext);

        loadSubComponentsAndExpand(resultComponent, layoutElement);

        initWrapperFrame(resultComponent, element, parentContext.getParams(), parentContext);

        parentContext.getInjectTasks().addAll(innerContext.getInjectTasks());
        parentContext.getInitTasks().addAll(innerContext.getInitTasks());
        parentContext.getPostInitTasks().addAll(innerContext.getPostInitTasks());
        parentContext.getPostWrapTasks().addAll(innerContext.getPostWrapTasks());

        setContext(parentContext);
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    protected class FrameInjectPostInitTask implements InjectTask {
        protected final Frame wrappingFrame;
        protected final Map<String, Object> params;

        public FrameInjectPostInitTask(Frame wrappingFrame, Map<String, Object> params) {
            this.wrappingFrame = wrappingFrame;
            this.params = params;
        }

        @Override
        public void execute(Context context, Frame window) {
            String loggingId = context.getFullFrameId();
            try {
                if (wrappingFrame instanceof AbstractFrame) {
                    Element companionsElem = element.element("companions");
                    if (companionsElem != null) {
                        StopWatch companionStopWatch = new Slf4JStopWatch(loggingId + "#" +
                                UIPerformanceLogger.LifeCycle.COMPANION,
                                LoggerFactory.getLogger(UIPerformanceLogger.class));

                        initCompanion(companionsElem, (AbstractFrame) wrappingFrame);

                        companionStopWatch.stop();
                    }
                }

                StopWatch injectStopWatch = new Slf4JStopWatch(loggingId + "#" +
                        UIPerformanceLogger.LifeCycle.INJECTION,
                        LoggerFactory.getLogger(UIPerformanceLogger.class));

                ControllerDependencyInjector dependencyInjector =
                        AppBeans.getPrototype(ControllerDependencyInjector.NAME, wrappingFrame, params);
                dependencyInjector.inject();

                injectStopWatch.stop();
            } catch (Throwable e) {
                throw new RuntimeException("Unable to init custom frame class", e);
            }
        }
    }

    protected class FrameLoaderInitTask implements InitTask {
        protected final Frame frame;
        protected final Map<String, Object> params;
        protected final boolean wrapped;

        public FrameLoaderInitTask(Frame frame, Map<String, Object> params, boolean wrapped) {
            this.frame = frame;
            this.params = params;
            this.wrapped = wrapped;
        }

        @Override
        public void execute(Context context, Frame window) {
            if (wrapped) {
                String loggingId = ComponentsHelper.getFullFrameId(this.frame);

                if (this.frame instanceof AbstractFrame) {
                    StopWatch initStopWatch = new Slf4JStopWatch(loggingId + "#" +
                            UIPerformanceLogger.LifeCycle.INIT,
                            LoggerFactory.getLogger(UIPerformanceLogger.class));

                    ((AbstractFrame) this.frame).init(params);

                    initStopWatch.stop();
                }
            }
        }
    }

    protected class FrameLoaderPostInitTask implements PostInitTask {
        protected final Frame frame;
        protected final Map<String, Object> params;
        protected final boolean wrapped;

        public FrameLoaderPostInitTask(Frame frame, Map<String, Object> params, boolean wrapped) {
            this.frame = frame;
            this.params = params;
            this.wrapped = wrapped;
        }

        @Override
        public void execute(Context context, Frame window) {
            if (wrapped) {
                String loggingId = ComponentsHelper.getFullFrameId(this.frame);

                StopWatch uiPermissionsWatch = new Slf4JStopWatch(loggingId + "#" +
                        UIPerformanceLogger.LifeCycle.UI_PERMISSIONS,
                        LoggerFactory.getLogger(UIPerformanceLogger.class));

                // apply ui permissions
                WindowCreationHelper.applyUiPermissions(window);

                uiPermissionsWatch.stop();

                FrameLoader.this.context.executePostInitTasks();
            }
        }
    }

    public ComponentLoaderContext getInnerContext() {
        return innerContext;
    }
}