/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
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
public class FrameComponentLoader extends ContainerLoader<Frame> {

    protected String frameId;
    protected ComponentLoader frameLoader;

    @Override
    public void createComponent() {
        String src = element.attributeValue("src");
        String screenId = element.attributeValue("screen");
        if (src == null && screenId == null) {
            throw new GuiDevelopmentException("Either 'src' or 'screen' must be specified for 'frame'", context.getFullFrameId());
        }
        if (src == null) {
            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
            WindowInfo windowInfo = windowConfig.getWindowInfo(screenId);
            src = windowInfo.getTemplate();
            if (src == null) {
                throw new GuiDevelopmentException("Screen " + screenId + " doesn't have template path configured", context.getFullFrameId());
            }
        }

        if (element.attributeValue("id") != null) {
            frameId = element.attributeValue("id");
        }

        LayoutLoader layoutLoader = new LayoutLoader(context, factory, LayoutLoaderConfig.getFrameLoaders());
        layoutLoader.setLocale(getLocale());
        layoutLoader.setMessagesPack(getMessagesPack());

        InputStream stream = resources.getResourceAsStream(src);
        if (stream == null) {
            stream = getClass().getResourceAsStream(src);
            if (stream == null) {
                throw new GuiDevelopmentException("Template is not found", context.getFullFrameId(), "src", src);
            }
        }

        String currentFrameId = context.getCurrentFrameId();
        context.setCurrentFrameId(frameId);
        try {
            Pair<ComponentLoader, Element> loaderElementPair = layoutLoader.createFrameComponent(stream, frameId, context.getParams());
            frameLoader = loaderElementPair.getFirst();
            resultComponent = (Frame) frameLoader.getResultComponent();
        } finally {
            context.setCurrentFrameId(currentFrameId);
            IOUtils.closeQuietly(stream);
        }
    }

    @Override
    public void loadComponent() {
        if (resultComponent.getMessagesPack() == null) {
            resultComponent.setMessagesPack(messagesPack);
        }

        assignXmlDescriptor(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element, ComponentsHelper.getComponentHeigth(resultComponent));
        loadWidth(resultComponent, element, ComponentsHelper.getComponentWidth(resultComponent));

        if (context.getFrame() != null) {
            resultComponent.setFrame(context.getFrame());
        }

        String src = element.attributeValue("src");
        String screenId = element.attributeValue("screen");
        String screenPath = StringUtils.isEmpty(screenId) ? src : screenId;
        if (element.attributeValue("id") != null) {
            screenPath = element.attributeValue("id");
        }
        if (context.getFrame() != null) {
            String parentId = context.getFullFrameId();
            if (StringUtils.isNotEmpty(parentId)) {
                screenPath = parentId + "." + screenPath;
            }
        }

        StopWatch loadDescriptorWatch = new Log4JStopWatch(screenPath + "#" +
                UIPerformanceLogger.LifeCycle.LOAD,
                Logger.getLogger(UIPerformanceLogger.class));
        loadDescriptorWatch.start();

        String currentFrameId = context.getCurrentFrameId();
        try {
            context.setCurrentFrameId(frameId);
            frameLoader.loadComponent();
        } finally {
            context.setCurrentFrameId(currentFrameId);
            loadDescriptorWatch.stop();
        }
    }
}