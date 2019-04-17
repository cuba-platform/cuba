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

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.ValueSourceProvider;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSourceProvider;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributeComponentsGenerator;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils.getCategoryAttribute;
import static com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils.isDynamicAttribute;

public class FormLoader extends AbstractComponentLoader<Form> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(Form.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadEditable(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadIcon(resultComponent, element);
        loadContextHelp(resultComponent, element);
        loadCss(resultComponent, element);

        loadAlign(resultComponent, element);

        loadCaptionPosition(resultComponent, element);
        loadChildrenCaptionWidth(resultComponent, element);

        loadDataContainer(resultComponent, element);

        loadColumns(resultComponent, element);
    }

    protected void loadDataContainer(Form resultComponent, Element element) {
        String containerId = element.attributeValue("dataContainer");
        if (!Strings.isNullOrEmpty(containerId)) {
            FrameOwner frameOwner = context.getFrame().getFrameOwner();
            ScreenData screenData = UiControllerUtils.getScreenData(frameOwner);
            InstanceContainer container = screenData.getContainer(containerId);
            //noinspection unchecked
            resultComponent.setValueSourceProvider(new ContainerValueSourceProvider(container));
        }
    }

    protected void loadColumns(Form resultComponent, Element element) {
        ValueSourceProvider valueSourceProvider = resultComponent.getValueSourceProvider();
        if (element.elements("column").isEmpty()) {
            Iterable<Component> rootComponents = loadComponents(element, null);
            Iterable<Component> dynamicAttributeComponents = loadDynamicAttributeComponents(valueSourceProvider, null);
            for (Component component : Iterables.concat(rootComponents, dynamicAttributeComponents)) {
                resultComponent.add(component);
            }
        } else {
            List<Element> columnElements = element.elements("column");
            if (element.elements().size() > columnElements.size()) {
                String fieldGroupId = resultComponent.getId();
                Map<String, Object> params = Strings.isNullOrEmpty(fieldGroupId)
                        ? Collections.emptyMap()
                        : ParamsMap.of("Form ID", fieldGroupId);
                throw new GuiDevelopmentException("Form component elements have to be placed within its column.",
                        context.getFullFrameId(), params);
            }

            resultComponent.setColumns(columnElements.size());

            int colIndex = 0;
            for (Element columnElement : columnElements) {
                String columnWidth = loadThemeString(columnElement.attributeValue("width"));
                Iterable<Component> columnComponents = loadComponents(columnElement, columnWidth);
                if (colIndex == 0) {
                    columnComponents = Iterables.concat(columnComponents,
                            loadDynamicAttributeComponents(valueSourceProvider, columnWidth));
                }
                for (Component component : columnComponents) {
                    resultComponent.add(component, colIndex);
                }

                loadChildrenCaptionWidth(resultComponent, columnElement, colIndex);

                colIndex++;
            }
        }
    }

    protected List<Component> loadComponents(Element element, @Nullable String columnWidth) {
        List<Element> elements = element.elements();
        if (elements.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Component> components = new ArrayList<>(elements.size());
            for (Element componentElement : elements) {
                Component component = loadComponent(componentElement, columnWidth);
                components.add(component);
            }
            return components;
        }
    }

    protected Component loadComponent(Element element, @Nullable String columnWidth) {
        LayoutLoader loader = beanLocator.getPrototype(LayoutLoader.NAME, context);
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());

        ComponentLoader childComponentLoader = loader.createComponent(element);
        childComponentLoader.loadComponent();

        Component component = childComponentLoader.getResultComponent();

        // Set default width
        String componentWidth = element.attributeValue("width");
        if (Strings.isNullOrEmpty(componentWidth)
                && columnWidth != null) {
            component.setWidth(columnWidth);
        }

        // Set default caption
        if (component instanceof HasValueSource
                && ((HasValueSource) component).getValueSource() instanceof EntityValueSource
                && component instanceof Component.HasCaption
                && ((Component.HasCaption) component).getCaption() == null) {
            EntityValueSource valueSource = ((EntityValueSource) ((HasValueSource) component).getValueSource());

            MetaPropertyPath metaPropertyPath = valueSource.getMetaPropertyPath();

            String propertyName = metaPropertyPath != null ? metaPropertyPath.getMetaProperty().getName() : null;
            if (metaPropertyPath != null) {
                if (isDynamicAttribute(metaPropertyPath.getMetaProperty())) {
                    CategoryAttribute categoryAttribute = getCategoryAttribute(metaPropertyPath.getMetaProperty());
                    ((Component.HasCaption) component).setCaption(categoryAttribute != null
                            ? categoryAttribute.getLocaleName()
                            : propertyName);
                } else {
                    MetaClass propertyMetaClass = getMetadataTools().getPropertyEnclosingMetaClass(metaPropertyPath);
                    String propertyCaption = getMessageTools().getPropertyCaption(propertyMetaClass, propertyName);
                    ((Component.HasCaption) component).setCaption(propertyCaption);
                }
            }
        }

        return component;
    }

    protected List<Component> loadDynamicAttributeComponents(ValueSourceProvider provider,
                                                             @Nullable String columnWidth) {
        if (provider instanceof ContainerValueSourceProvider
                && getMetadataTools().isPersistent(
                ((ContainerValueSourceProvider) provider).getContainer().getEntityMetaClass())) {
            String windowId = getWindowId(context);

            InstanceContainer instanceContainer = ((ContainerValueSourceProvider) provider).getContainer();
            MetaClass metaClass = instanceContainer.getEntityMetaClass();

            List<CategoryAttribute> attributesToShow =
                    getDynamicAttributesGuiTools().getSortedAttributesToShowOnTheScreen(
                            metaClass, windowId, resultComponent.getId());

            if (!attributesToShow.isEmpty()) {
                List<Component> components = new ArrayList<>();

                if (instanceContainer instanceof HasLoader) {
                    DataLoader dataLoader = ((HasLoader) instanceContainer).getLoader();
                    if (dataLoader instanceof InstanceLoader) {
                        ((InstanceLoader) dataLoader).setLoadDynamicAttributes(true);
                    }
                }

                for (CategoryAttribute attribute : attributesToShow) {
                    String code = DynamicAttributesUtils.encodeAttributeCode(attribute.getCode());

                    Component dynamicAttrComponent;
                    if (Boolean.TRUE.equals(attribute.getIsCollection())) {
                        dynamicAttrComponent = getDynamicAttributesComponentsGenerator()
                                .generateComponent(provider.getValueSource(code), attribute);
                    } else {
                        ComponentGenerationContext context = new ComponentGenerationContext(metaClass, code);
                        dynamicAttrComponent = getUiComponentsGenerator().generate(context);
                    }

                    if (dynamicAttrComponent instanceof Component.HasCaption) {
                        ((Component.HasCaption) dynamicAttrComponent).setCaption(attribute.getLocaleName());
                    }
                    if (dynamicAttrComponent instanceof HasValueSource) {
                        //noinspection unchecked
                        ((HasValueSource) dynamicAttrComponent).setValueSource(provider.getValueSource(code));
                    }
                    if (dynamicAttrComponent instanceof Field) {
                        ((Field) dynamicAttrComponent).setRequired(attribute.getRequired());
                        ((Field) dynamicAttrComponent).setRequiredMessage(getMessages()
                                .formatMainMessage("validation.required.defaultMsg", attribute.getLocaleName()));
                    }
                    String defaultWidth =
                            Strings.isNullOrEmpty(attribute.getWidth())
                                    ? columnWidth : attribute.getWidth();
                    loadWidth(dynamicAttrComponent, defaultWidth);

                    components.add(dynamicAttrComponent);
                }
                return components;
            }
        }

        return Collections.emptyList();
    }

    protected void loadWidth(Component component, String width) {
        if ("auto".equalsIgnoreCase(width)) {
            component.setWidth(Component.AUTO_SIZE);
        } else if (StringUtils.isNotBlank(width)) {
            component.setWidth(loadThemeString(width));
        }
    }

    protected MetadataTools getMetadataTools() {
        return beanLocator.get(MetadataTools.NAME);
    }

    protected DynamicAttributesGuiTools getDynamicAttributesGuiTools() {
        return beanLocator.get(DynamicAttributesGuiTools.NAME);
    }

    protected UiComponentsGenerator getUiComponentsGenerator() {
        return beanLocator.get(UiComponentsGenerator.NAME);
    }

    protected DynamicAttributeComponentsGenerator getDynamicAttributesComponentsGenerator() {
        return beanLocator.get(DynamicAttributeComponentsGenerator.NAME);
    }

    protected void loadCaptionPosition(Form resultComponent, Element element) {
        String captionAlignment = element.attributeValue("captionPosition");
        if (!Strings.isNullOrEmpty(captionAlignment)) {
            resultComponent.setCaptionPosition(Form.CaptionPosition.valueOf(captionAlignment));
        }
    }

    @Nullable
    protected String loadChildrenCaptionWidth(Element element) {
        String childCaptionWidth = element.attributeValue("childrenCaptionWidth");
        if (!Strings.isNullOrEmpty(childCaptionWidth)) {
            if (childCaptionWidth.startsWith(MessageTools.MARK)) {
                childCaptionWidth = loadResourceString(childCaptionWidth);
            }
            if (childCaptionWidth.endsWith("px")) {
                childCaptionWidth = childCaptionWidth.substring(0, childCaptionWidth.indexOf("px"));
            }

            return childCaptionWidth;
        }

        return null;
    }

    protected void loadChildrenCaptionWidth(Form resultComponent, Element element) {
        String childrenCaptionWidth = loadChildrenCaptionWidth(element);
        if (childrenCaptionWidth != null) {
            resultComponent.setChildrenCaptionWidth(Integer.parseInt(childrenCaptionWidth));
        }
    }

    protected void loadChildrenCaptionWidth(Form resultComponent, Element element, int colIndex) {
        String childrenCaptionWidth = loadChildrenCaptionWidth(element);
        if (childrenCaptionWidth != null) {
            resultComponent.setChildrenCaptionWidth(colIndex, Integer.parseInt(childrenCaptionWidth));
        }
    }
}
