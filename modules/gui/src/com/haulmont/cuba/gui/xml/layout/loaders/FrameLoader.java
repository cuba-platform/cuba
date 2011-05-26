/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:27:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.WrappedFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.XmlInheritanceProcessor;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class FrameLoader extends ContainerLoader implements ComponentLoader {

    public FrameLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException
    {
        final Map<String,Object> params = context.getParams();
        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(element.getDocument(), params);
        element = processor.getResultRoot();

        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext();

        IFrame component = factory.createComponent("iframe");

        MetadataHelper.deployViews(element);

        final Element dsContextElement = element.element("dsContext");
        final DsContext dsContext;

        if (dsContextElement != null) {
            final DsContextLoader contextLoader =
                    new DsContextLoader(context.getDsContext().getDataService());

            dsContext = contextLoader.loadDatasources(dsContextElement, parentContext.getDsContext());
        } else {
            dsContext = null;
        }
        ComponentLoaderContext newContext = new ComponentLoaderContext(
                dsContext == null ? parentContext.getDsContext() : dsContext,
                params);
        newContext.setFrame(component);
        newContext.setParent(parentContext);
        setContext(newContext);

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadMessagesPack(component, element);

        final Element layoutElement = element.element("layout");
        loadExpandLayout(component, layoutElement);
        loadSubComponentsAndExpand(component, layoutElement);
        loadSpacing(component, layoutElement);
        loadMargin(component, layoutElement);
        loadWidth(component, layoutElement);
        loadHeight(component, layoutElement);
        loadStyleName(component, layoutElement);

        FrameContext frameContext = new FrameContext(component, params);
        component.setContext(frameContext);

        if (dsContext != null) {
            component.setDsContext(dsContext);

            for (Datasource ds : dsContext.getAll()) {
                if (ds instanceof DatasourceImplementation) {
                    ((DatasourceImplementation) ds).initialized();
                }
            }

            dsContext.setWindowContext(frameContext);
        }
        component = wrapByCustomClass(component, element, params, parentContext);

        parentContext.getPostInitTasks().addAll(newContext.getPostInitTasks());

        return component;
    }

    protected IFrame wrapByCustomClass(IFrame frame, Element element, Map<String, Object> params,
                                       ComponentLoaderContext parentContext)
    {
        IFrame res = frame;
        final String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            try {
                Class<Window> aClass = null;
                if (ConfigProvider.getConfig(GlobalConfig.class).isGroovyClassLoaderEnabled()) {
                    aClass = ScriptingProvider.loadClass(screenClass);
                }
                if (aClass == null)
                    aClass = ReflectionHelper.getClass(screenClass);
                res = ((WrappedFrame) frame).wrapBy(aClass);

                parentContext.addPostInitTask(new FrameLoaderPostInitTask(res, params, true));

                return res;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            parentContext.addPostInitTask(new FrameLoaderPostInitTask(res, params, false));
            return res;
        }
    }

    protected <T> T invokeMethod(IFrame frame, String name, Object...params) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<Class> paramClasses = new ArrayList<Class>();
        for (Object param : params) {
            if (param == null) throw new IllegalStateException("Null parameter");

            final Class aClass = param.getClass();
            if (List.class.isAssignableFrom(aClass)) {
                paramClasses.add(List.class);
            } else if (Set.class.isAssignableFrom(aClass)) {
                paramClasses.add(Set.class);
            } else if (Map.class.isAssignableFrom(aClass)) {
                paramClasses.add(Map.class);
            } else {
                paramClasses.add(aClass);
            }
        }

        final Class<? extends IFrame> aClass = frame.getClass();
        Method method;
        try {
            method = aClass.getDeclaredMethod(name, paramClasses.toArray(new Class<?>[paramClasses.size()]));
        } catch (NoSuchMethodException e) {
            method = aClass.getMethod(name, paramClasses.toArray(new Class<?>[paramClasses.size()]));
        }
        method.setAccessible(true);
        //noinspection unchecked
        return (T) method.invoke(frame, params);
    }

    protected void loadExpandLayout(Component.HasLayout component, Element element) {
        final String expandLayout = element.attributeValue("expandLayout");
        if (!StringUtils.isEmpty(expandLayout)) {
            if (isBoolean(expandLayout)) {
                component.expandLayout(Boolean.valueOf(expandLayout));
            }
        }
    }

    protected void loadMessagesPack(IFrame frame, Element element) {
        String msgPack = element.attributeValue("messagesPack");
        if (msgPack != null) {
            frame.setMessagesPack(msgPack);
            setMessagesPack(msgPack);
        } else {
            frame.setMessagesPack(msgPack);
            setMessagesPack(this.messagesPack);
        }
    }

    private class FrameLoaderPostInitTask implements PostInitTask {

        private IFrame frame;
        private Map<String, Object> params;
        private boolean wrapped;

        public FrameLoaderPostInitTask(IFrame frame, Map<String, Object> params, boolean wrapped) {
            this.frame = frame;
            this.params = params;
            this.wrapped = wrapped;
        }

        public void execute(Context context, IFrame window) {
            if (wrapped) {
                try {
                    ReflectionHelper.invokeMethod(this.frame, "init", params);
                } catch (NoSuchMethodException e) {
                    // do nothing
                } 

                FrameLoader.this.context.executePostInitTasks();
            }
        }
    }
}
