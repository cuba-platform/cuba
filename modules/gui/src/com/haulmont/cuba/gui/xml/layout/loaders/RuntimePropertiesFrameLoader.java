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

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Frame;
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
 */
public class RuntimePropertiesFrameLoader extends ContainerLoader<Frame> {

    private static final String DEFAULT_DESCRIPTOR = "/com/haulmont/cuba/gui/app/core/dynamicattributes/runtime-properties-frame.xml";

    protected String frameId;
    protected ComponentLoader frameLoader;

    @Override
    public void createComponent() {
        String src = element.attributeValue("src");
        if (src == null) {
            src = DEFAULT_DESCRIPTOR;
        }

        String screenPath = src;
        if (element.attributeValue("id") != null) {
            screenPath = element.attributeValue("id");
        }
        frameId = screenPath;

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

        String src = element.attributeValue("src");
        if (src == null) {
            src = DEFAULT_DESCRIPTOR;
        }
        String runtimeDs = element.attributeValue("runtimeDs");
        if (StringUtils.isEmpty(runtimeDs)) {
            throw new GuiDevelopmentException("runtimePropsDatasource is not set for runtimeProperties component", context.getFullFrameId());
        }
        context.getParams().put("runtimeDs", runtimeDs);

        String categoriesDs = element.attributeValue("categoriesDs");
        if (StringUtils.isEmpty(categoriesDs)) {
            throw new GuiDevelopmentException("categoriesDs is not set for runtimeProperties component", context.getFullFrameId());
        }
        context.getParams().put("categoriesDs", categoriesDs);

        String rows = element.attributeValue("rows");
        context.getParams().put("rows", rows);
        String cols = element.attributeValue("cols");
        context.getParams().put("cols", cols);
        String fieldWidth = element.attributeValue("fieldWidth");
        context.getParams().put("fieldWidth", fieldWidth);

        String screenPath = StringUtils.equals(src, DEFAULT_DESCRIPTOR) ? "runtimeProperties" : src;
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