/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.WrappedFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.xml.XmlInheritanceProcessor;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class FrameLoader<T extends Frame> extends ContainerLoader<T> {

    protected Element layoutElement;
    protected ComponentLoaderContext innerContext;
    protected Element rootFrameElement;

    protected String frameId;

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
        return ((WrappedFrame) frame).wrapBy(aClass);
    }

    protected void initWrapperFrame(Frame wrappingFrame, Element rootFrameElement, Map<String, Object> params,
                                    ComponentLoaderContext parentContext) {
        parentContext.addInjectTask(new FrameInjectPostInitTask(wrappingFrame, params));

        boolean wrapped = StringUtils.isNotBlank(rootFrameElement.attributeValue("class"));
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

        Map<String, Object> params = context.getParams();
        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(element.getDocument(), params);
        rootFrameElement = processor.getResultRoot();

        loadMessagesPack(clientSpecificFrame, rootFrameElement);

        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext();

        String frameId = parentContext.getCurrentFrameId();
        if (parentContext.getFullFrameId() != null) {
            frameId = parentContext.getFullFrameId() + "." + frameId;
        }

        innerContext = new ComponentLoaderContext(params);
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
        WindowCreationHelper.deployViews(rootFrameElement);

        Element dsContextElement = rootFrameElement.element("dsContext");
        DsContextLoader contextLoader = new DsContextLoader(context.getDsContext().getDataSupplier());

        DsContext dsContext = contextLoader.loadDatasources(dsContextElement, context.getDsContext());

        assignXmlDescriptor(resultComponent, rootFrameElement);

        loadVisible(resultComponent, layoutElement);
        loadActions(resultComponent, rootFrameElement);

        loadSpacing(resultComponent, layoutElement);
        loadMargin(resultComponent, layoutElement);
        loadWidth(resultComponent, layoutElement);
        loadHeight(resultComponent, layoutElement);
        loadStyleName(resultComponent, layoutElement);

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

        initWrapperFrame(resultComponent, rootFrameElement, parentContext.getParams(), parentContext);

        parentContext.getInjectTasks().addAll(innerContext.getInjectTasks());
        parentContext.getPostInitTasks().addAll(innerContext.getPostInitTasks());

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
                    Element companionsElem = rootFrameElement.element("companions");
                    if (companionsElem != null) {
                        StopWatch companionStopWatch = new Log4JStopWatch(loggingId + "#" +
                                UIPerformanceLogger.LifeCycle.COMPANION,
                                Logger.getLogger(UIPerformanceLogger.class));

                        initCompanion(companionsElem, (AbstractFrame) wrappingFrame);

                        companionStopWatch.stop();
                    }
                }

                StopWatch injectStopWatch = new Log4JStopWatch(loggingId + "#" +
                        UIPerformanceLogger.LifeCycle.INJECTION,
                        Logger.getLogger(UIPerformanceLogger.class));

                ControllerDependencyInjector dependencyInjector = new ControllerDependencyInjector(wrappingFrame, params);
                dependencyInjector.inject();

                injectStopWatch.stop();
            } catch (Throwable e) {
                throw new RuntimeException("Unable to init custom frame class", e);
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

                if (this.frame instanceof AbstractFrame) {
                    StopWatch initStopWatch = new Log4JStopWatch(loggingId + "#" +
                            UIPerformanceLogger.LifeCycle.INIT,
                            Logger.getLogger(UIPerformanceLogger.class));

                    ((AbstractFrame) this.frame).init(params);

                    initStopWatch.stop();
                }

                StopWatch uiPermissionsWatch = new Log4JStopWatch(loggingId + "#" +
                        UIPerformanceLogger.LifeCycle.UI_PERMISSIONS,
                        Logger.getLogger(UIPerformanceLogger.class));

                // apply ui permissions
                WindowCreationHelper.applyUiPermissions(window);

                uiPermissionsWatch.stop();

                FrameLoader.this.context.executePostInitTasks();
            }
        }
    }
}