/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.compatibility.LegacyFragmentAdapter;
import com.haulmont.cuba.gui.config.WindowAttributesProvider;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.logging.ScreenLifeCycle;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.ScreenOptions;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoaderContext;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haulmont.cuba.gui.ComponentsHelper.getFullFrameId;
import static com.haulmont.cuba.gui.logging.UIPerformanceLogger.createStopWatch;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.fireEvent;
import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;

/**
 * Provides shared functionality for fragment initialization from XML and programmatic creation.
 */
@Component(FragmentHelper.NAME)
@ParametersAreNonnullByDefault
public class FragmentHelper {

    private static final Logger log = LoggerFactory.getLogger(FragmentHelper.class);

    @Inject
    protected ScreenXmlLoader screenXmlLoader;
    @Inject
    protected Scripting scripting;

    public static final String NAME = "cuba_FragmentHelper";

    @SuppressWarnings("unchecked")
    public ScreenFragment createController(WindowInfo windowInfo, Fragment fragment) {
        Class screenClass = windowInfo.getControllerClass();

        if (AbstractWindow.class.isAssignableFrom(screenClass)) {
            AbstractWindow legacyScreen;
            try {
                legacyScreen = (AbstractWindow) invokeConstructor(screenClass);
            } catch (NoSuchMethodException | IllegalAccessException
                    | InvocationTargetException | InstantiationException e) {
                throw new DevelopmentException("Unable to create " + screenClass);
            }
            LegacyFragmentAdapter adapter = new LegacyFragmentAdapter(legacyScreen);

            legacyScreen.setFrame(fragment);
            adapter.setWrappedFrame(fragment);

            log.warn(
                    "Fragment class '{}' should not be inherited from AbstractWindow. " +
                            "It may cause problems with controller life cycle. " +
                            "Fragment controllers should inherit ScreenFragment.",
                    screenClass.getSimpleName());

            return adapter;
        }

        // new screens cannot be opened in fragments
        if (!ScreenFragment.class.isAssignableFrom(screenClass)) {
            throw new IllegalStateException(
                    String.format("Fragment controllers should inherit ScreenFragment." +
                                    " UI controller is not ScreenFragment - %s %s",
                            windowInfo.toString(), screenClass.getSimpleName()));
        }
        ScreenFragment controller;
        try {
            controller = (ScreenFragment) invokeConstructor(screenClass);
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unable to create instance of screen class " + screenClass);
        }

        return controller;
    }

    public String getMessagePack(String descriptorPath) {
        if (descriptorPath.contains("/")) {
            descriptorPath = StringUtils.substring(descriptorPath, 0, descriptorPath.lastIndexOf("/"));
        }

        String messagesPack = descriptorPath.replace("/", ".");
        int start = messagesPack.startsWith(".") ? 1 : 0;
        messagesPack = messagesPack.substring(start);
        return messagesPack;
    }

    @SuppressWarnings("unchecked")
    public WindowInfo createFakeWindowInfo(String src, String fragmentId) {
        Element screenElement = DocumentHelper.createElement("screen");
        screenElement.addAttribute("template", src);
        screenElement.addAttribute("id", fragmentId);

        Element windowElement = screenXmlLoader.load(src, fragmentId, Collections.emptyMap());
        Class<? extends ScreenFragment> fragmentClass;

        String className = windowElement.attributeValue("class");
        if (StringUtils.isNotEmpty(className)) {
            fragmentClass = (Class<? extends ScreenFragment>) scripting.loadClassNN(className);
        } else {
            fragmentClass = AbstractFrame.class;
        }

        return new WindowInfo(fragmentId, new WindowAttributesProvider() {
            @Override
            public WindowInfo.Type getType(WindowInfo wi) {
                return WindowInfo.Type.FRAGMENT;
            }

            @Override
            public String getTemplate(WindowInfo wi) {
                return src;
            }

            @Nonnull
            @Override
            public Class<? extends FrameOwner> getControllerClass(WindowInfo wi) {
                return fragmentClass;
            }

            @Override
            public WindowInfo resolve(WindowInfo windowInfo) {
                return windowInfo;
            }
        }, screenElement);
    }

    public static class FragmentLoaderInjectTask implements ComponentLoader.InjectTask {
        protected Fragment fragment;
        protected ScreenOptions options;
        protected BeanLocator beanLocator;

        public FragmentLoaderInjectTask(Fragment fragment, ScreenOptions options, BeanLocator beanLocator) {
            this.fragment = fragment;
            this.options = options;
            this.beanLocator = beanLocator;
        }

        @Override
        public void execute(ComponentLoader.ComponentContext windowContext, Frame window) {
            String loggingId = getFullFrameId(this.fragment);

            StopWatch injectStopWatch = createStopWatch(ScreenLifeCycle.INJECTION, loggingId);

            FrameOwner controller = fragment.getFrameOwner();
            beanLocator.getAll(ControllerDependencyInjector.class).values()
                    .forEach(uiControllerDependencyInjector ->
                            uiControllerDependencyInjector.inject(new ControllerDependencyInjector.InjectionContext(controller, options))
                    );
            injectStopWatch.stop();
        }
    }

    public static class FragmentLoaderInitTask implements ComponentLoader.InitTask {
        protected Fragment fragment;
        protected ScreenOptions options;
        protected ComponentLoaderContext fragmentLoaderContext;
        protected BeanLocator beanLocator;

        public FragmentLoaderInitTask(Fragment fragment, ScreenOptions options,
                                      @Nullable ComponentLoaderContext fragmentLoaderContext, BeanLocator beanLocator) {
            this.fragment = fragment;
            this.options = options;
            this.fragmentLoaderContext = fragmentLoaderContext;
            this.beanLocator = beanLocator;
        }

        @Override
        public void execute(ComponentLoader.ComponentContext windowContext, Frame window) {
            String loggingId = getFullFrameId(this.fragment);

            StopWatch stopWatch = createStopWatch(ScreenLifeCycle.INIT, loggingId);

            ScreenFragment frameOwner = fragment.getFrameOwner();

            fireEvent(frameOwner, ScreenFragment.InitEvent.class,
                    new ScreenFragment.InitEvent(frameOwner, options));

            // compatibility with old screens in frames
            if (frameOwner instanceof LegacyFragmentAdapter) {
                Map<String, Object> params = new HashMap<>(0);
                if (options instanceof MapScreenOptions) {
                    params = ((MapScreenOptions) options).getParams();
                }
                ((LegacyFragmentAdapter) frameOwner).init(params);
            }

            stopWatch.stop();

            fireEvent(frameOwner, ScreenFragment.AfterInitEvent.class,
                    new ScreenFragment.AfterInitEvent(frameOwner, options));

            if (fragmentLoaderContext != null) {
                List<UiControllerProperty> properties = fragmentLoaderContext.getProperties();
                if (!properties.isEmpty()) {
                    UiControllerPropertyInjector propertyInjector =
                            beanLocator.getPrototype(UiControllerPropertyInjector.NAME, frameOwner, properties);
                    propertyInjector.inject();
                }
            }

            FragmentContextImpl fragmentContext = (FragmentContextImpl) fragment.getContext();
            fragmentContext.setInitialized(true);

            // fire attached

            if (!fragmentContext.isManualInitRequired()) {
                fireEvent(frameOwner, ScreenFragment.AttachEvent.class,
                        new ScreenFragment.AttachEvent(frameOwner));
            }
        }
    }
}