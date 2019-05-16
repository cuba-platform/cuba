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
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.DialogWindow;
import com.haulmont.cuba.gui.components.DialogWindow.WindowMode;
import com.haulmont.cuba.gui.components.Facet;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.logging.ScreenLifeCycle;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.model.impl.ScreenDataXmlLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.sys.CompanionDependencyInjector;
import com.haulmont.cuba.gui.xml.FacetLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentRootLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.haulmont.cuba.gui.logging.UIPerformanceLogger.createStopWatch;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@ParametersAreNonnullByDefault
public class WindowLoader extends ContainerLoader<Window> implements ComponentRootLoader<Window> {

    public void setResultComponent(Window window) {
        this.resultComponent = window;
    }

    @Override
    public void createComponent() {
        throw new UnsupportedOperationException("Window cannot be created from XML element");
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
        loadScreenData(resultComponent, element);

        loadDialogOptions(resultComponent, element);

        assignXmlDescriptor(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadIcon(resultComponent, element);
        loadActions(resultComponent, element);

        Element layoutElement = element.element("layout");
        if (layoutElement == null) {
            throw new GuiDevelopmentException("Required 'layout' element is not found", context);
        }

        loadSpacing(resultComponent, layoutElement);
        loadMargin(resultComponent, layoutElement);
        loadWidth(resultComponent, layoutElement);
        loadHeight(resultComponent, layoutElement);
        loadStyleName(resultComponent, layoutElement);
        loadResponsive(resultComponent, layoutElement);
        loadCss(resultComponent, element);
        loadVisible(resultComponent, layoutElement);

        loadMinMaxSizes(resultComponent, layoutElement);

        loadSubComponentsAndExpand(resultComponent, layoutElement);
        setComponentsRatio(resultComponent, layoutElement);

        loadFocusedComponent(resultComponent, element);

        loadFacets(resultComponent, element);

        // for compatibility

        loadTimers(resultComponent, element);
        loadCrossFieldValidate(resultComponent, element);
        loadCompanions(resultComponent, element);
    }

    protected void loadMinMaxSizes(Window resultComponent, Element layoutElement) {
        String minHeight = layoutElement.attributeValue("minHeight");
        if (isNotEmpty(minHeight)) {
            resultComponent.setMinHeight(minHeight);
        }

        String minWidth = layoutElement.attributeValue("minWidth");
        if (isNotEmpty(minWidth)) {
            resultComponent.setMinWidth(minWidth);
        }

        String maxHeight = layoutElement.attributeValue("maxHeight");
        if (isNotEmpty(maxHeight)) {
            resultComponent.setMaxHeight(maxHeight);
        }

        String maxWidth = layoutElement.attributeValue("maxWidth");
        if (isNotEmpty(maxWidth)) {
            resultComponent.setMaxWidth(maxWidth);
        }
    }

    protected void loadScreenData(Window window, Element element) {
        Element dataEl = element.element("data");
        if (dataEl != null) {
            ScreenDataXmlLoader screenDataXmlLoader = beanLocator.get(ScreenDataXmlLoader.class);
            ScreenData screenData = UiControllerUtils.getScreenData(window.getFrameOwner());
            screenDataXmlLoader.load(screenData, dataEl, null);

            ((ComponentLoaderContext) context).setScreenData(screenData);
        }
    }

    protected void loadDialogOptions(Window resultComponent, Element element) {
        Element dialogModeElement = element.element("dialogMode");
        if (dialogModeElement != null
                && resultComponent instanceof DialogWindow) {
            // dialog mode applied only if opened as dialog
            DialogWindow dialog = (DialogWindow) resultComponent;

            String xmlWidthValue = dialogModeElement.attributeValue("width");
            if (StringUtils.isNotBlank(xmlWidthValue)) {
                String themeWidthValue = loadThemeString(xmlWidthValue);
                dialog.setWidth(themeWidthValue);
            }

            String xmlHeightValue = dialogModeElement.attributeValue("height");
            if (StringUtils.isNotBlank(xmlHeightValue)) {
                String themeHeightValue = loadThemeString(xmlHeightValue);
                dialog.setHeight(themeHeightValue);
            }

            String closeable = dialogModeElement.attributeValue("closeable");
            if (isNotEmpty(closeable)) {
                dialog.setCloseable(parseBoolean(closeable));
            }

            String resizable = dialogModeElement.attributeValue("resizable");
            if (isNotEmpty(resizable)) {
                dialog.setResizable(parseBoolean(resizable));
            }

            String modal = dialogModeElement.attributeValue("modal");
            if (isNotEmpty(modal)) {
                dialog.setModal(parseBoolean(modal));
            }

            String closeOnClickOutside = dialogModeElement.attributeValue("closeOnClickOutside");
            if (isNotEmpty(closeOnClickOutside)) {
                dialog.setCloseOnClickOutside(parseBoolean(closeOnClickOutside));
            }

            String maximized = dialogModeElement.attributeValue("maximized");
            if (isNotEmpty(maximized) && parseBoolean(maximized)) {
                dialog.setWindowMode(WindowMode.MAXIMIZED);
            }

            String positionX = dialogModeElement.attributeValue("positionX");
            if (isNotEmpty(positionX)) {
                dialog.setPositionX(parseInt(positionX));
            }

            String positionY = dialogModeElement.attributeValue("positionY");
            if (isNotEmpty(positionY)) {
                dialog.setPositionY(parseInt(positionY));
            }
        }
    }

    protected void loadFocusedComponent(Window window, Element element) {
        String focusMode = element.attributeValue("focusMode");
        String componentId = element.attributeValue("focusComponent");
        if (!"NO_FOCUS".equals(focusMode)) {
            window.setFocusComponent(componentId);
        }
    }

    protected void loadFacets(Window resultComponent, Element windowElement) {
        Element facetsElement = windowElement.element("facets");
        if (facetsElement != null) {
            List<Element> facetElements = facetsElement.elements();

            for (Element facetElement : facetElements) {
                FacetLoader loader = beanLocator.get(FacetLoader.NAME);
                Facet facet = loader.load(facetElement, getComponentContext());

                resultComponent.addFacet(facet);
            }
        }
    }

    @Deprecated
    protected void loadTimers(Window resultComponent, Element windowElement) {
        Element timersElement = windowElement.element("timers");
        if (timersElement != null) {
            List<Element> facetElements = timersElement.elements("timer");

            for (Element facetElement : facetElements) {
                FacetLoader loader = beanLocator.get(FacetLoader.NAME);
                Facet facet = loader.load(facetElement, getComponentContext());

                resultComponent.addFacet(facet);
            }
        }
    }

    @Deprecated
    protected void loadCompanions(Window resultComponent, Element element) {
        Screen controller = resultComponent.getFrameOwner();
        if (controller instanceof AbstractWindow) {
            Element companionsElem = element.element("companions");
            if (companionsElem != null) {
                StopWatch companionStopWatch = createStopWatch(ScreenLifeCycle.COMPANION, controller.getId());

                Object companion = initCompanion(companionsElem, (AbstractWindow) controller);

                companionStopWatch.stop();

                if (companion != null) {
                    getComponentContext().addInjectTask((c, w) -> {
                        CompanionDependencyInjector cdi =
                                new CompanionDependencyInjector((LegacyFrame) controller, companion);
                        cdi.setBeanLocator(beanLocator);
                        cdi.inject();
                    });
                }
            }
        }
    }

    @Deprecated
    protected Object initCompanion(Element companionsElem, AbstractWindow window) {
        Element element = companionsElem.element(AppConfig.getClientType().toString().toLowerCase());
        if (element != null) {
            String className = element.attributeValue("class");
            if (!StringUtils.isBlank(className)) {
                Class aClass = getScripting().loadClassNN(className);
                Object companion;
                try {
                    companion = aClass.newInstance();
                    window.setCompanion(companion);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to init Companion", e);
                }
                return companion;
            }
        }
        return null;
    }

    @Deprecated
    protected void loadCrossFieldValidate(Window resultComponent, Element element) {
        Screen controller = resultComponent.getFrameOwner();
        if (controller instanceof AbstractWindow) {
            String crossFieldValidate = element.attributeValue("crossFieldValidate");
            if (isNotEmpty(crossFieldValidate)) {
                if (controller instanceof Window.Editor) {
                    Window.Editor editor = (Window.Editor) controller;
                    editor.setCrossFieldValidate(parseBoolean(crossFieldValidate));
                } else {
                    throw new GuiDevelopmentException("Window should extend Window.Editor to use crossFieldValidate attribute",
                            context);
                }
            }
        }
    }
}