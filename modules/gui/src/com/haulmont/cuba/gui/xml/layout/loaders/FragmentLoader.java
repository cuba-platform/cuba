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

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.GenericDataSupplier;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger.LifeCycle;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.ScreenOptions;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.sys.CompanionDependencyInjector;
import com.haulmont.cuba.gui.sys.ScreenViewsLoader;
import com.haulmont.cuba.gui.sys.UiControllerDependencyInjector;
import com.haulmont.cuba.gui.sys.WindowCreationHelper;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentRootLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;

import static com.haulmont.cuba.gui.logging.UIPerformanceLogger.createStopWatch;

public class FragmentLoader extends ContainerLoader<Fragment> implements ComponentRootLoader<Fragment> {

    protected String frameId;

    protected void initCompanion(Element companionsElem, AbstractFrame frame) {
        String clientTypeId = AppConfig.getClientType().toString().toLowerCase();
        Element element = companionsElem.element(clientTypeId);
        if (element != null) {
            String className = element.attributeValue("class");
            if (!StringUtils.isBlank(className)) {
                Class aClass = getScripting().loadClassNN(className);
                Object companion;
                try {
                    companion = aClass.newInstance();
                    frame.setCompanion(companion);

                    CompanionDependencyInjector cdi = new CompanionDependencyInjector(frame, companion);
                    cdi.setBeanLocator(beanLocator);
                    cdi.inject();
                } catch (Exception e) {
                    throw new RuntimeException("Unable to init companion for frame", e);
                }
            }
        }
    }

    protected ScreenViewsLoader getScreenViewsLoader() {
        return beanLocator.get(ScreenViewsLoader.NAME);
    }

    @Override
    public void createComponent() {
        throw new UnsupportedOperationException("Fragment cannot be created from XML element");
    }

    public void setResultComponent(Fragment fragment) {
        this.resultComponent = fragment;
    }

    @Override
    public void createContent(Element layoutElement) {
        if (layoutElement == null) {
            throw new DevelopmentException("Missing required 'layout' element");
        }
        createSubComponents(resultComponent, layoutElement);
    }

    @Override
    public void loadComponent() {
        if (resultComponent.getFrameOwner() instanceof AbstractFrame) {
            getScreenViewsLoader().deployViews(element);
        }

        if (context.getParent() == null) {
            throw new IllegalStateException("FragmentLoader is always called within parent ComponentLoaderContext");
        }

        Element dsContextElement = element.element("dsContext");

        DsContext dsContext = null;
        if (resultComponent.getFrameOwner() instanceof LegacyFrame) {
            DsContextLoader contextLoader;
            DsContext parentDsContext = context.getParent().getDsContext();
            if (parentDsContext != null){
                contextLoader = new DsContextLoader(parentDsContext.getDataSupplier());
            } else {
                contextLoader = new DsContextLoader(new GenericDataSupplier());
            }

            dsContext = contextLoader.loadDatasources(dsContextElement, parentDsContext, getContext().getAliasesMap());
            ((ComponentLoaderContext) context).setDsContext(dsContext);
        }

        assignXmlDescriptor(resultComponent, element);

        Element layoutElement = element.element("layout");
        if (layoutElement == null) {
            throw new GuiDevelopmentException("Required 'layout' element is not found", context.getFullFrameId());
        }

        loadIcon(resultComponent, layoutElement);
        loadCaption(resultComponent, layoutElement);
        loadDescription(resultComponent, layoutElement);

        loadVisible(resultComponent, layoutElement);
        loadEnable(resultComponent, layoutElement);
        loadActions(resultComponent, element);

        loadSpacing(resultComponent, layoutElement);
        loadMargin(resultComponent, layoutElement);
        loadWidth(resultComponent, layoutElement);
        loadHeight(resultComponent, layoutElement);
        loadStyleName(resultComponent, layoutElement);
        loadResponsive(resultComponent, layoutElement);

        if (dsContext != null) {
            FrameOwner frameOwner = getContext().getFrame().getFrameOwner();
            if (frameOwner instanceof LegacyFrame) {
                LegacyFrame frame = (LegacyFrame) frameOwner;
                frame.setDsContext(dsContext);

                for (Datasource ds : dsContext.getAll()) {
                    if (ds instanceof DatasourceImplementation) {
                        ((DatasourceImplementation) ds).initialized();
                    }
                }

                dsContext.setFrameContext(resultComponent.getContext());
            }
        }

        loadSubComponentsAndExpand(resultComponent, layoutElement);

        ComponentLoaderContext parentContext = (ComponentLoaderContext) getContext().getParent();
        ScreenOptions options = parentContext.getOptions();

        parentContext.addInjectTask(new FragmentLoaderInjectTask(resultComponent, options));
        parentContext.addInitTask(new FragmentLoaderInitTask(resultComponent, options));
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    protected WindowCreationHelper getWindowCreationHelper() {
        return beanLocator.get(WindowCreationHelper.NAME);
    }

    protected class FragmentLoaderInjectTask implements InjectTask {
        protected Fragment fragment;
        protected ScreenOptions options;

        public FragmentLoaderInjectTask(Fragment fragment, ScreenOptions options) {
            this.fragment = fragment;
            this.options = options;
        }

        @Override
        public void execute(Context context, Frame window) {
            String loggingId = context.getFullFrameId();
            try {
                if (fragment.getFrameOwner() instanceof AbstractFrame) {
                    Element companionsElem = element.element("companions");
                    if (companionsElem != null) {
                        StopWatch companionStopWatch = createStopWatch(LifeCycle.COMPANION, loggingId);

                        initCompanion(companionsElem, (AbstractFrame) fragment.getFrameOwner());

                        companionStopWatch.stop();
                    }
                }

                StopWatch injectStopWatch = createStopWatch(LifeCycle.INJECTION, loggingId);

                FrameOwner controller = fragment.getFrameOwner();
                UiControllerDependencyInjector dependencyInjector =
                        beanLocator.getPrototype(UiControllerDependencyInjector.NAME, controller, options);
                dependencyInjector.inject();

                injectStopWatch.stop();
            } catch (Throwable e) {
                throw new RuntimeException("Unable to init custom frame class", e);
            }
        }
    }

    protected class FragmentLoaderInitTask implements InitTask {
        protected Fragment fragment;
        protected ScreenOptions options;

        public FragmentLoaderInitTask(Fragment fragment, ScreenOptions options) {
            this.fragment = fragment;
            this.options = options;
        }

        @Override
        public void execute(Context context, Frame window) {
            String loggingId = ComponentsHelper.getFullFrameId(this.fragment);

            StopWatch stopWatch = createStopWatch(LifeCycle.INIT, loggingId);

            ScreenFragment frameOwner = fragment.getFrameOwner();

            UiControllerUtils.fireEvent(frameOwner,
                    ScreenFragment.InitEvent.class,
                    new ScreenFragment.InitEvent(frameOwner, options));

            stopWatch.stop();

            UiControllerUtils.fireEvent(frameOwner,
                    ScreenFragment.AfterInitEvent.class,
                    new ScreenFragment.AfterInitEvent(frameOwner, options));
        }
    }
}