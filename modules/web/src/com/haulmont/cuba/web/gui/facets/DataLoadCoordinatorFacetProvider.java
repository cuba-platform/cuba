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

package com.haulmont.cuba.web.gui.facets;

import com.google.common.base.Preconditions;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.DataLoadCoordinator;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector;
import com.haulmont.cuba.gui.xml.FacetProvider;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.web.gui.components.WebDataLoadCoordinator;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Component("cuba_DataLoadCoordinatorFacetProvider")
public class DataLoadCoordinatorFacetProvider implements FacetProvider<DataLoadCoordinator> {

    private UiControllerReflectionInspector reflectionInspector;

    @Inject
    public void setReflectionInspector(UiControllerReflectionInspector reflectionInspector) {
        this.reflectionInspector = reflectionInspector;
    }

    @Override
    public Class<DataLoadCoordinator> getFacetClass() {
        return DataLoadCoordinator.class;
    }

    @Override
    public DataLoadCoordinator create() {
        return new WebDataLoadCoordinator(reflectionInspector);
    }

    @Override
    public String getFacetTag() {
        return "dataLoadCoordinator";
    }

    @Override
    public void loadFromXml(DataLoadCoordinator facet, Element element, ComponentLoader.ComponentContext context) {
        facet.setOwner(context.getFrame());

        String id = element.attributeValue("id");
        if (id != null) {
            facet.setId(id);
        }

        String containerPrefix = element.attributeValue("containerPrefix");
        if (containerPrefix != null) {
            facet.setContainerPrefix(containerPrefix);
        }
        String componentPrefix = element.attributeValue("componentPrefix");
        if (componentPrefix != null) {
            facet.setComponentPrefix(componentPrefix);
        }

        for (Element loaderEl : element.elements("refresh")) {
            String loaderId = loaderEl.attributeValue("loader");
            if (loaderId == null) {
                throw new GuiDevelopmentException("'dataLoadCoordinator.loader' element has no 'ref' attribute", context);
            }

            String onScreenEvent = loaderEl.attributeValue("onScreenEvent");
            if (onScreenEvent != null) {
                Class eventClass;
                switch (onScreenEvent) {
                    case "Init":
                        eventClass = Screen.InitEvent.class;
                        break;
                    case "AfterInit":
                        eventClass = Screen.AfterInitEvent.class;
                        break;
                    case "BeforeShow":
                        eventClass = Screen.BeforeShowEvent.class;
                        break;
                    case "AfterShow":
                        eventClass = Screen.AfterShowEvent.class;
                        break;
                    default:
                        throw new GuiDevelopmentException("Unsupported 'dataLoadCoordinator/refresh/onScreenEvent' value: " + onScreenEvent, context);
                }
                context.addInjectTask(new OnFrameOwnerEventLoadTriggerInitTask(facet, loaderId, eventClass));
                continue;
            }

            String onFragmentEvent = loaderEl.attributeValue("onFragmentEvent");
            if (onFragmentEvent != null) {
                Class eventClass;
                switch (onFragmentEvent) {
                    case "Init":
                        eventClass = ScreenFragment.InitEvent.class;
                        break;
                    case "AfterInit":
                        eventClass = ScreenFragment.AfterInitEvent.class;
                        break;
                    case "Attach":
                        eventClass = ScreenFragment.AttachEvent.class;
                        break;
                    default:
                        throw new GuiDevelopmentException("Unsupported 'dataLoadCoordinator/refresh/onFragmentEvent' value: " + onFragmentEvent, context);
                }
                context.addInjectTask(new OnFrameOwnerEventLoadTriggerInitTask(facet, loaderId, eventClass));
                continue;
            }

            String onContainerItemChanged = loaderEl.attributeValue("onContainerItemChanged");
            if (onContainerItemChanged != null) {
                String param = loaderEl.attributeValue("param");
                context.addInjectTask(new OnContainerItemChangedLoadTriggerInitTask(facet, loaderId, onContainerItemChanged, param));
                continue;
            }

            String onComponentValueChanged = loaderEl.attributeValue("onComponentValueChanged");
            if (onComponentValueChanged != null) {
                String param = loaderEl.attributeValue("param");

                String likeClauseAttr = loaderEl.attributeValue("likeClause");
                DataLoadCoordinator.LikeClause likeClause = likeClauseAttr == null ? DataLoadCoordinator.LikeClause.NONE : DataLoadCoordinator.LikeClause.valueOf(likeClauseAttr);

                context.addInjectTask(new OnComponentValueChangedLoadTriggerInitTask(
                        facet, loaderId, onComponentValueChanged, param, likeClause));
            }
        }

        if (Boolean.parseBoolean(element.attributeValue("auto"))) {
            context.addInjectTask(new AutoConfigurationInitTask(facet));
        }
    }

    public static class OnFrameOwnerEventLoadTriggerInitTask implements ComponentLoader.InjectTask {

        private final DataLoadCoordinator facet;
        private final String loaderId;
        private final Class eventClass;

        public OnFrameOwnerEventLoadTriggerInitTask(DataLoadCoordinator facet, String loaderId, Class eventClass) {
            this.facet = facet;
            this.loaderId = loaderId;
            this.eventClass = eventClass;
        }

        @Override
        public void execute(ComponentLoader.ComponentContext context, Frame window) {
            Preconditions.checkNotNull(facet.getOwner());
            ScreenData screenData = UiControllerUtils.getScreenData(facet.getOwner().getFrameOwner());
            DataLoader loader = screenData.getLoader(loaderId);
            facet.addOnFrameOwnerEventLoadTrigger(loader, eventClass);
        }
    }

    public static class OnContainerItemChangedLoadTriggerInitTask implements ComponentLoader.InjectTask {

        private final DataLoadCoordinator facet;
        private final String loaderId;
        private final String containerId;
        private final String param;

        public OnContainerItemChangedLoadTriggerInitTask(
                DataLoadCoordinator facet, String loaderId, String containerId, @Nullable String param) {
            this.facet = facet;
            this.loaderId = loaderId;
            this.containerId = containerId;
            this.param = param;
        }

        @Override
        public void execute(ComponentLoader.ComponentContext context, Frame window) {
            Preconditions.checkNotNull(facet.getOwner());
            ScreenData screenData = UiControllerUtils.getScreenData(facet.getOwner().getFrameOwner());
            DataLoader loader = screenData.getLoader(loaderId);
            InstanceContainer container = screenData.getContainer(containerId);
            facet.addOnContainerItemChangedLoadTrigger(loader, container, param);
        }
    }

    public static class OnComponentValueChangedLoadTriggerInitTask implements ComponentLoader.InjectTask {

        private final DataLoadCoordinator facet;
        private final String loaderId;
        private final String componentId;
        private final String param;
        private DataLoadCoordinator.LikeClause likeClause;

        public OnComponentValueChangedLoadTriggerInitTask(
                DataLoadCoordinator facet, String loaderId, String componentId, @Nullable String param, DataLoadCoordinator.LikeClause likeClause) {
            this.facet = facet;
            this.loaderId = loaderId;
            this.componentId = componentId;
            this.param = param;
            this.likeClause = likeClause;
        }

        @Override
        public void execute(ComponentLoader.ComponentContext context, Frame window) {
            Preconditions.checkNotNull(facet.getOwner());
            ScreenData screenData = UiControllerUtils.getScreenData(facet.getOwner().getFrameOwner());
            DataLoader loader = screenData.getLoader(loaderId);
            com.haulmont.cuba.gui.components.Component component = facet.getOwner().getComponentNN(componentId);
            facet.addOnComponentValueChangedLoadTrigger(loader, component, param, likeClause);
        }
    }

    public static class AutoConfigurationInitTask implements ComponentLoader.InjectTask {

        private final DataLoadCoordinator facet;

        public AutoConfigurationInitTask(DataLoadCoordinator facet) {
            this.facet = facet;
        }

        @Override
        public void execute(ComponentLoader.ComponentContext context, Frame window) {
            facet.configureAutomatically();
        }
    }
}
