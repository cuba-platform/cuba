/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.sys.FragmentImplementation;
import com.haulmont.cuba.gui.components.sys.FrameImplementation;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.sys.FragmentContextImpl;
import com.haulmont.cuba.gui.sys.FrameContextImpl;
import com.haulmont.cuba.gui.sys.ScreenContextImpl;
import com.haulmont.cuba.gui.sys.ScreenDescriptorUtils;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoaderContext;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.server.ClientConnector;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Scope(UIScope.NAME)
@Component(Fragments.NAME)
public class WebFragments implements Fragments {

    @Inject
    protected ScreenXmlLoader screenXmlLoader;
    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected BeanLocator beanLocator;
    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected UserSessionSource userSessionSource;

    protected AppUI ui;

    public WebFragments(AppUI ui) {
        this.ui = ui;
    }

    @Override
    public <T extends ScreenFragment> T create(FrameOwner parent, Class<T> requiredFragmentClass, ScreenOptions options) {
        checkNotNullArgument(parent);
        checkNotNullArgument(requiredFragmentClass);
        checkNotNullArgument(options);

        WindowInfo windowInfo = getFragmentInfo(requiredFragmentClass);

        return createFragment(parent, windowInfo, options);
    }

    protected <T extends ScreenFragment> WindowInfo getFragmentInfo(Class<T> fragmentClass) {
        UiController uiController = fragmentClass.getAnnotation(UiController.class);
        if (uiController == null) {
            throw new IllegalArgumentException("No @UiController annotation for class " + fragmentClass);
        }

        String screenId = ScreenDescriptorUtils.getInferredScreenId(uiController, fragmentClass);

        return windowConfig.getWindowInfo(screenId);
    }

    @Override
    public ScreenFragment create(FrameOwner parent, WindowInfo windowInfo, ScreenOptions options) {
        checkNotNullArgument(parent);
        checkNotNullArgument(windowInfo);
        checkNotNullArgument(options);

        return createFragment(parent, windowInfo, options);
    }

    protected <T extends ScreenFragment> T createFragment(FrameOwner parent, WindowInfo windowInfo,
                                                          ScreenOptions options) {
        Fragment fragment = componentsFactory.createComponent(Fragment.NAME);
        ScreenFragment controller = createController(windowInfo, fragment, windowInfo.asFragment());

        // setup screen and controller

        UiControllerUtils.setWindowId(controller, windowInfo.getId());
        UiControllerUtils.setFrame(controller, fragment);
        UiControllerUtils.setScreenContext(controller,
                new ScreenContextImpl(windowInfo, options,
                        ui.getScreens(),
                        ui.getDialogs(),
                        ui.getNotifications(),
                        this)
        );
        UiControllerUtils.setScreenData(controller, beanLocator.get(ScreenData.NAME));

        FragmentImplementation fragmentImpl = (FragmentImplementation) fragment;
        fragmentImpl.setFrameOwner(controller);
        fragmentImpl.setId(controller.getId());

        Frame parentFrame = UiControllerUtils.getFrame(parent);

        // fake parent loader context
        ComponentLoaderContext loaderContext = new ComponentLoaderContext(options);

        FrameContextImpl frameContext = new FragmentContextImpl(fragment, loaderContext);
        ((FrameImplementation) fragment).setContext(frameContext);

        loaderContext.setCurrentFrameId(windowInfo.getId());
        loaderContext.setFullFrameId(windowInfo.getId());
        loaderContext.setFrame(fragment);
        loaderContext.setParent(null);
        if (parentFrame instanceof LegacyFrame) {
            loaderContext.setDsContext(((LegacyFrame) parentFrame).getDsContext());
        }

        // load XML if needed
        if (windowInfo.getTemplate() != null) {
            ComponentLoaderContext innerContext = new ComponentLoaderContext(options);
            innerContext.setCurrentFrameId(windowInfo.getId());
            innerContext.setFullFrameId(windowInfo.getId());
            innerContext.setFrame(fragment);
            innerContext.setParent(loaderContext);

            LayoutLoader layoutLoader = beanLocator.getPrototype(LayoutLoader.NAME, innerContext);
            layoutLoader.setLocale(getLocale());
            layoutLoader.setMessagesPack(getMessagePack(windowInfo.getTemplate()));

            ScreenXmlLoader screenXmlLoader = beanLocator.get(ScreenXmlLoader.NAME);

            Element windowElement = screenXmlLoader.load(windowInfo.getTemplate(), windowInfo.getId(),
                    innerContext.getParams());

            ComponentLoader<Fragment> fragmentLoader =
                    layoutLoader.createFragmentContent(fragment, windowElement, windowInfo.getId());

            fragmentLoader.loadComponent();

            loaderContext.getInjectTasks().addAll(innerContext.getInjectTasks());
            loaderContext.getInitTasks().addAll(innerContext.getInitTasks());
            loaderContext.getPostInitTasks().addAll(innerContext.getPostInitTasks());
        }

        loaderContext.executeInjectTasks();

        fragmentImpl.setFrame(parentFrame);

        //noinspection unchecked
        return (T) controller;
    }

    @Override
    public void initialize(ScreenFragment controller) {
        checkNotNullArgument(controller);

        FragmentContextImpl fragmentContext = (FragmentContextImpl) controller.getFragment().getContext();

        ComponentLoaderContext loaderContext = fragmentContext.getLoaderContext();

        loaderContext.executeInitTasks();
        loaderContext.executePostInitTasks();

        // resume listeners after show
        // only if legacy frame
        if (controller instanceof LegacyFrame) {
            com.vaadin.ui.Component vComposition = controller.getFragment().unwrapComposition(com.vaadin.ui.Component.class);
            vComposition.addAttachListener(new ClientConnector.AttachListener() {
                @Override
                public void attach(ClientConnector.AttachEvent event) {
                    resumeDsContextAfterShow((LegacyFrame) controller);
                    // run only once
                    vComposition.removeAttachListener(this);
                }
            });
        }
    }

    protected void resumeDsContextAfterShow(LegacyFrame controller) {
        if (!WindowParams.DISABLE_RESUME_SUSPENDED.getBool(controller.getContext())) {
            DsContext dsContext = controller.getDsContext();
            if (dsContext != null) {
                ((DsContextImplementation) dsContext).resumeSuspended();
            }
        }
    }

    protected String getMessagePack(String descriptorPath) {
        if (descriptorPath.contains("/")) {
            descriptorPath = StringUtils.substring(descriptorPath, 0, descriptorPath.lastIndexOf("/"));
        }

        String messagesPack = descriptorPath.replaceAll("/", ".");
        int start = messagesPack.startsWith(".") ? 1 : 0;
        messagesPack = messagesPack.substring(start);
        return messagesPack;
    }

    protected Locale getLocale() {
        return userSessionSource.getUserSession().getLocale();
    }

    protected <T extends ScreenFragment> T createController(WindowInfo windowInfo, Fragment fragment,
                                                            Class<T> screenClass) {
        Constructor<T> constructor;
        try {
            constructor = screenClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new DevelopmentException("No accessible constructor for screen class " + screenClass);
        }

        T controller;
        try {
            controller = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unable to create instance of screen class " + screenClass);
        }

        return controller;
    }
}