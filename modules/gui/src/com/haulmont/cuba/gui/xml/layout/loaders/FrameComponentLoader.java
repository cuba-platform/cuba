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
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.LoggerFactory;

/**
 * todo rename to FragmentLoader
 */
public class FrameComponentLoader extends ContainerLoader<Frame> {

    protected String frameId;
    protected ComponentLoader frameLoader;

    protected WindowConfig getWindowConfig() {
        return beanLocator.get(WindowConfig.NAME);
    }

    @Override
    public void createComponent() {
        String src = element.attributeValue("src");
        String screenId = element.attributeValue("screen");
        if (src == null && screenId == null) {
            throw new GuiDevelopmentException("Either 'src' or 'screen' must be specified for 'frame'", context.getFullFrameId());
        }
        if (src == null) {
            WindowInfo windowInfo = getWindowConfig().getWindowInfo(screenId);
            src = windowInfo.getTemplate();
            if (src == null) {
                throw new GuiDevelopmentException(
                        String.format("Screen %s doesn't have template path configured", screenId),
                        context.getFullFrameId()
                );
            }
        }

        if (element.attributeValue("id") != null) {
            frameId = element.attributeValue("id");
        }

        LayoutLoader layoutLoader = beanLocator.getPrototype(LayoutLoader.NAME, context);
        layoutLoader.setLocale(getLocale());
        layoutLoader.setMessagesPack(getMessagesPack());

        String currentFrameId = context.getCurrentFrameId();
        context.setCurrentFrameId(frameId);
        try {
            Pair<ComponentLoader, Element> loaderElementPair = layoutLoader.createFrameComponent(src, frameId, context.getParams());
            frameLoader = loaderElementPair.getFirst();
            resultComponent = (Frame) frameLoader.getResultComponent();
        } finally {
            context.setCurrentFrameId(currentFrameId);
        }
    }

    @Override
    public void loadComponent() {
        // todo
        /*if (resultComponent.getMessagesPack() == null) {
            resultComponent.setMessagesPack(messagesPack);
        }*/

        assignXmlDescriptor(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadResponsive(resultComponent, element);

        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element, ComponentsHelper.getComponentHeight(resultComponent));
        loadWidth(resultComponent, element, ComponentsHelper.getComponentWidth(resultComponent));

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadAliases();

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

        StopWatch loadDescriptorWatch = new Slf4JStopWatch(screenPath + "#" +
                UIPerformanceLogger.LifeCycle.LOAD,
                LoggerFactory.getLogger(UIPerformanceLogger.class));
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

    protected void loadAliases() {
        if (frameLoader instanceof FrameLoader) {
            ComponentLoaderContext frameLoaderInnerContext = ((FrameLoader) frameLoader).getInnerContext();
            for (Element aliasElement : element.elements("dsAlias")) {
                String aliasDatasourceId = aliasElement.attributeValue("alias");
                String originalDatasourceId = aliasElement.attributeValue("datasource");
                if (StringUtils.isNotBlank(aliasDatasourceId) && StringUtils.isNotBlank(originalDatasourceId)) {
                    frameLoaderInnerContext.getAliasesMap().put(aliasDatasourceId, originalDatasourceId);
                }
            }
        }
    }
}