/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.io.InputStream;

/**
 * @author abramov
 * @version $Id$
 */
public class IFrameLoader extends ContainerLoader implements ComponentLoader {

    public IFrameLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException {

        String src = element.attributeValue("src");
        final String screenId = element.attributeValue("screen");
        if (src == null && screenId == null) {
            throw new RuntimeException("Either src or screen must be specified for <iframe>");
        }
        if (src == null) {
            WindowInfo windowInfo = AppBeans.get(WindowConfig.class).getWindowInfo(screenId);
            src = windowInfo.getTemplate();
            if (src == null) {
                throw new RuntimeException("Screen " + screenId + " doesn't have template path configured");
            }
        }

        String screenPath = StringUtils.isEmpty(screenId) ? src : screenId;

        if (element.attributeValue("id") != null)
            screenPath = element.attributeValue("id");

        String frameId = screenPath;

        if (context.getFrame() != null) {
            String parentId = context.getFullFrameId();
            if (StringUtils.isNotEmpty(parentId))
                screenPath = parentId + "." + screenPath;
        }

        final LayoutLoader loader = new LayoutLoader(context, factory, LayoutLoaderConfig.getFrameLoaders());
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());

        InputStream stream = resources.getResourceAsStream(src);
        if (stream == null) {
            stream = getClass().getResourceAsStream(src);
            if (stream == null) {
                throw new RuntimeException("Bad template path: " + src);
            }
        }

        StopWatch loadDescriptorWatch = new Log4JStopWatch(screenPath + "#" +
                UIPerformanceLogger.LifeCycle.LOAD_DESCRIPTOR,
                Logger.getLogger(UIPerformanceLogger.class));
        loadDescriptorWatch.start();

        final IFrame component;
        try {
            context.setCurrentIFrameId(frameId);
            component = (IFrame) loader.loadComponent(stream, parent, context.getParams());
        } finally {
            IOUtils.closeQuietly(stream);
        }
        if (component.getMessagesPack() == null) {
            component.setMessagesPack(messagesPack);
        }

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);

        loadHeight(component, element, ComponentsHelper.getComponentHeigth(component));
        loadWidth(component, element, ComponentsHelper.getComponentWidth(component));

        if (context.getFrame() != null)
            component.setFrame(context.getFrame());

        loadDescriptorWatch.stop();

        return component;
    }
}