/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.xml.XmlInheritanceProcessor;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
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
public class FrameLoader extends ContainerLoader implements ComponentLoader {

    public FrameLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {

        final Map<String, Object> params = context.getParams();
        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(element.getDocument(), params);
        element = processor.getResultRoot();

        IFrame component = factory.createComponent("iframe");

        WindowCreationHelper.deployViews(element);

        final Element dsContextElement = element.element("dsContext");
        final DsContextLoader contextLoader = new DsContextLoader(context.getDsContext().getDataSupplier());

        final DsContext dsContext = contextLoader.loadDatasources(dsContextElement, context.getDsContext());

        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext();

        String frameId = parentContext.getCurrentIFrameId();
        if (parentContext.getFullFrameId() != null)
            frameId = parentContext.getFullFrameId() + "." + frameId;

        ComponentLoaderContext newContext = new ComponentLoaderContext(dsContext, params);

        newContext.setFullFrameId(frameId);
        newContext.setFrame(component);
        newContext.setParent(parentContext);
        setContext(newContext);

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);
        loadStyleName(component, element);
        loadMessagesPack(component, element);
        loadActions(component, element);

        final Element layoutElement = element.element("layout");
        if (layoutElement == null)
            throw new GuiDevelopmentException("Required element 'layout' is not found", context.getFullFrameId());

        loadSubComponentsAndExpand(component, layoutElement);
        loadSpacing(component, layoutElement);
        loadMargin(component, layoutElement);
        loadWidth(component, layoutElement);
        loadHeight(component, layoutElement);
        loadStyleName(component, layoutElement);

        FrameContext frameContext = new FrameContextImpl(component, params);
        component.setContext(frameContext);

        if (dsContext != null) {
            component.setDsContext(dsContext);

            for (Datasource ds : dsContext.getAll()) {
                if (ds instanceof DatasourceImplementation) {
                    ((DatasourceImplementation) ds).initialized();
                }
            }

            dsContext.setFrameContext(frameContext);
        }
        component = wrapByCustomClass(component, element, params, parentContext);

        parentContext.getPostInitTasks().addAll(newContext.getPostInitTasks());

        return component;
    }

    protected IFrame wrapByCustomClass(IFrame frame, Element element, Map<String, Object> params,
                                       ComponentLoaderContext parentContext) {
        final String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            try {
                Class<Window> aClass = scripting.loadClass(screenClass);
                if (aClass == null)
                    aClass = ReflectionHelper.getClass(screenClass);
                IFrame wrappingFrame = ((WrappedFrame) frame).wrapBy(aClass);

                String loggingId = context.getFullFrameId();

                if (wrappingFrame instanceof AbstractFrame) {
                    Element companionsElem = element.element("companions");
                    if (companionsElem != null) {
                        StopWatch companionStopWatch = new Log4JStopWatch(loggingId + "#" +
                                UIPerformanceLogger.LifeCycle.COMPANION,
                                Logger.getLogger(UIPerformanceLogger.class));

                        initCompanion(companionsElem, (AbstractFrame) wrappingFrame);

                        companionStopWatch.stop();
                    }
                }
                parentContext.addPostInitTask(new FrameLoaderPostInitTask(wrappingFrame, params, true));

                StopWatch injectStopWatch = new Log4JStopWatch(loggingId + "#" +
                        UIPerformanceLogger.LifeCycle.INJECTION,
                        Logger.getLogger(UIPerformanceLogger.class));

                ControllerDependencyInjector dependencyInjector = new ControllerDependencyInjector(wrappingFrame, params);
                dependencyInjector.inject();

                injectStopWatch.stop();

                return wrappingFrame;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            parentContext.addPostInitTask(new FrameLoaderPostInitTask(frame, params, false));
            return frame;
        }
    }

    protected void initCompanion(Element companionsElem, AbstractFrame frame) {
        Element element = companionsElem.element(AppConfig.getClientType().toString().toLowerCase());
        if (element != null) {
            String className = element.attributeValue("class");
            if (!StringUtils.isBlank(className)) {
                Class aClass = scripting.loadClass(className);
                if (aClass == null)
                    throw new IllegalStateException("Class " + className + " is not found");
                Object companion;
                try {
                    companion = aClass.newInstance();
                    frame.setCompanion(companion);

                    CompanionDependencyInjector cdi = new CompanionDependencyInjector(frame, companion);
                    cdi.inject();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    protected void loadMessagesPack(IFrame frame, Element element) {
        String msgPack = element.attributeValue("messagesPack");
        if (msgPack != null) {
            frame.setMessagesPack(msgPack);
            setMessagesPack(msgPack);
        } else {
            frame.setMessagesPack(this.messagesPack);
            setMessagesPack(this.messagesPack);
        }
    }

    protected class FrameLoaderPostInitTask implements PostInitTask {

        private IFrame frame;
        private Map<String, Object> params;
        private boolean wrapped;

        public FrameLoaderPostInitTask(IFrame frame, Map<String, Object> params, boolean wrapped) {
            this.frame = frame;
            this.params = params;
            this.wrapped = wrapped;
        }

        @Override
        public void execute(Context context, IFrame window) {
            if (wrapped) {
                String loggingId = ComponentsHelper.getFullFrameId(this.frame);
                StopWatch initStopWatch = new Log4JStopWatch(loggingId + "#" +
                        UIPerformanceLogger.LifeCycle.INIT,
                        Logger.getLogger(UIPerformanceLogger.class));

                try {
                    ReflectionHelper.invokeMethod(this.frame, "init", params);
                } catch (NoSuchMethodException e) {
                    // do nothing
                }

                initStopWatch.stop();

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
