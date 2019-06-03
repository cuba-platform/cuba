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

import com.google.common.base.Preconditions;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.sys.FragmentImplementation;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.logging.ScreenLifeCycle;
import com.haulmont.cuba.gui.model.impl.ScreenDataImpl;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.ScreenOptions;
import com.haulmont.cuba.gui.sys.FragmentContextImpl;
import com.haulmont.cuba.gui.sys.FragmentHelper;
import com.haulmont.cuba.gui.sys.FragmentHelper.FragmentLoaderInjectTask;
import com.haulmont.cuba.gui.sys.ScreenContextImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;

import java.util.Objects;

import static com.haulmont.cuba.gui.logging.UIPerformanceLogger.createStopWatch;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.*;
import static com.haulmont.cuba.gui.sys.FragmentHelper.FragmentLoaderInitTask;
import static com.haulmont.cuba.gui.sys.FragmentHelper.NAME;

public class RuntimePropertiesFrameLoader extends ContainerLoader<Frame> {

    protected static final String DEFAULT_DESCRIPTOR =
            "/com/haulmont/cuba/gui/app/core/dynamicattributes/runtime-properties-frame.xml";

    protected ComponentLoader fragmentLoader;
    protected ComponentLoaderContext innerContext;

    @Override
    public ComponentContext getContext() {
        return (ComponentContext) super.getContext();
    }

    @Override
    public void setContext(Context context) {
        Preconditions.checkArgument(context instanceof ComponentContext,
                "'context' must implement com.haulmont.cuba.gui.xml.layout.ComponentLoader.ComponentContext");
        super.setContext(context);
    }

    @Override
    public void createComponent() {
        String src = element.attributeValue("src");
        String screenId = element.attributeValue("id");

        if (src == null) {
            src = DEFAULT_DESCRIPTOR;
        }

        String fragmentId = screenId != null ? screenId : src;

        FragmentHelper fragmentHelper = getFragmentHelper();

        WindowInfo windowInfo = fragmentHelper.createFakeWindowInfo(src, fragmentId);

        Fragment fragment = factory.create(Fragment.NAME);
        ScreenFragment controller = fragmentHelper.createController(windowInfo, fragment);

        // setup screen and controller
        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext();

        FrameOwner hostController = parentContext.getFrame().getFrameOwner();

        // setup screen and controller

        setHostController(controller, hostController);
        setWindowId(controller, windowInfo.getId());
        setFrame(controller, fragment);
        setScreenContext(controller,
                new ScreenContextImpl(windowInfo, parentContext.getOptions(), getScreenContext(hostController))
        );
        setScreenData(controller, new ScreenDataImpl());

        FragmentImplementation fragmentImpl = (FragmentImplementation) fragment;
        fragmentImpl.setFrameOwner(controller);
        fragmentImpl.setId(fragmentId);

        FragmentContextImpl frameContext = new FragmentContextImpl(fragment, innerContext);
        ((FrameImplementation) fragment).setContext(frameContext);

        // load from XML if needed

        if (windowInfo.getTemplate() != null) {
            String frameId = fragmentId;
            if (parentContext.getFullFrameId() != null) {
                frameId = parentContext.getFullFrameId() + "." + frameId;
            }

            innerContext = new ComponentLoaderContext(getContext().getOptions());
            innerContext.setMessagesPack(fragmentHelper.getMessagePack(windowInfo.getTemplate()));
            innerContext.setCurrentFrameId(fragmentId);
            innerContext.setFullFrameId(frameId);
            innerContext.setFrame(fragment);
            innerContext.setParent(parentContext);

            LayoutLoader layoutLoader = getLayoutLoader(innerContext);

            ScreenXmlLoader screenXmlLoader = beanLocator.get(ScreenXmlLoader.NAME);

            Element rootElement = screenXmlLoader.load(windowInfo.getTemplate(), windowInfo.getId(),
                    getContext().getParams());

            String messagesPack = rootElement.attributeValue("messagesPack");
            if (messagesPack != null) {
                innerContext.setMessagesPack(messagesPack);
            }

            this.fragmentLoader = layoutLoader.createFragmentContent(fragment, rootElement);
        }

        this.resultComponent = fragment;
    }

    protected FragmentHelper getFragmentHelper() {
        return beanLocator.get(NAME);
    }

    @Override
    public void loadComponent() {
        if (getContext().getFrame() != null) {
            resultComponent.setFrame(getContext().getFrame());
        }

        String src = element.attributeValue("src");
        if (src == null) {
            src = DEFAULT_DESCRIPTOR;
        }
        String runtimeDs = element.attributeValue("runtimeDs");
        if (StringUtils.isEmpty(runtimeDs)) {
            throw new GuiDevelopmentException("runtimePropsDatasource is not set for runtimeProperties component",
                    getContext().getFullFrameId());
        }
        getContext().getParams().put("runtimeDs", runtimeDs);

        String categoriesDs = element.attributeValue("categoriesDs");
        if (StringUtils.isEmpty(categoriesDs)) {
            throw new GuiDevelopmentException("categoriesDs is not set for runtimeProperties component",
                    getContext().getFullFrameId());
        }
        getContext().getParams().put("categoriesDs", categoriesDs);

        String rows = element.attributeValue("rows");
        getContext().getParams().put("rows", rows);
        String cols = element.attributeValue("cols");
        getContext().getParams().put("cols", cols);
        String fieldWidth = element.attributeValue("fieldWidth");
        getContext().getParams().put("fieldWidth", fieldWidth);
        String fieldCaptionWidth = element.attributeValue("fieldCaptionWidth");
        getContext().getParams().put("fieldCaptionWidth", fieldCaptionWidth);

        String screenPath = Objects.equals(src, DEFAULT_DESCRIPTOR) ? "runtimeProperties" : src;
        if (element.attributeValue("id") != null) {
            screenPath = element.attributeValue("id");
        }
        if (getContext().getFrame() != null) {
            String parentId = getContext().getFullFrameId();
            if (StringUtils.isNotEmpty(parentId)) {
                screenPath = parentId + "." + screenPath;
            }
        }

        StopWatch loadStopWatch = createStopWatch(ScreenLifeCycle.LOAD, screenPath);

        // if fragment has XML descriptor

        if (fragmentLoader != null) {
            fragmentLoader.loadComponent();
        }

        // load properties after inner context, they must override values defined inside of fragment

        assignXmlDescriptor(resultComponent, element);
        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadResponsive(resultComponent, element);
        loadCss(resultComponent, element);

        loadAlign(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadStopWatch.stop();

        // propagate init phases

        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext();
        if (innerContext != null) {

            parentContext.getInjectTasks().addAll(innerContext.getInjectTasks());
            parentContext.getInitTasks().addAll(innerContext.getInitTasks());
            parentContext.getPostInitTasks().addAll(innerContext.getPostInitTasks());
        }

        ScreenOptions options = parentContext.getOptions();
        parentContext.addInjectTask(new FragmentLoaderInjectTask((Fragment) resultComponent, options, beanLocator));
        parentContext.addInitTask(new FragmentLoaderInitTask((Fragment) resultComponent, options, (ComponentLoaderContext) context, beanLocator));
    }

    protected WindowConfig getWindowConfig() {
        return beanLocator.get(WindowConfig.NAME);
    }
}