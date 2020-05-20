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

package com.haulmont.cuba.web.gui.components.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Categorized;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSourceProvider;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributeComponentsGenerator;
import com.haulmont.cuba.gui.meta.CanvasIconSize;
import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioComponent;
import com.haulmont.cuba.gui.meta.StudioProperty;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.web.gui.components.CompositeComponent;
import com.haulmont.cuba.web.gui.components.CompositeDescriptor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.Positive;
import java.util.*;
import java.util.stream.Collectors;

@StudioComponent(category = "Components",
        unsupportedProperties = {"enable", "responsive"},
        icon = "icon/dynamicAttributesPanel.svg",
        canvasIcon = "icon/dynamicAttributesPanel_canvas.svg",
        canvasIconSize = CanvasIconSize.LARGE,
        documentationURL = "https://doc.cuba-platform.com/manual-%VERSION%/categorized_entity.html")
@CompositeDescriptor("dynamic-attributes-panel.xml")
public class DynamicAttributesPanel extends CompositeComponent<VBoxLayout> implements Validatable {

    public static final String NAME = "dynamicAttributesPanel";

    public static final String DEFAULT_FIELD_WIDTH = "100%";

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected UiComponentsGenerator uiComponentsGenerator;

    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected DynamicAttributeComponentsGenerator dynamicAttributeComponentsGenerator;

    @Inject
    protected Messages messages;

    @Inject
    protected Security security;

    protected InstanceContainer<BaseGenericIdEntity> instanceContainer;

    protected Integer cols;
    protected Integer rows;
    protected String fieldWidth = DEFAULT_FIELD_WIDTH;
    protected String fieldCaptionWidth;

    protected Form propertiesForm;
    protected HBoxLayout categoryFieldBox;
    protected LookupField<Category> categoryField;

    public DynamicAttributesPanel() {
        addCreateListener(this::onCreate);
    }

    protected void onCreate(CreateEvent createEvent) {
        categoryField = getInnerComponent("categoryField");
        categoryField.addValueChangeListener(e -> initPropertiesForm());

        propertiesForm = getInnerComponent("propertiesForm");
        propertiesForm.setHeightAuto();

        categoryFieldBox = getInnerComponent("categoryFieldBox");
    }

    protected void initPropertiesForm() {
        propertiesForm.removeAll();

        List<DynamicAttributesMetaProperty> metaProperties = getPropertiesFilteredByCategory();
        Map<CategoryAttribute, Component> fields = new HashMap<>();
        for (DynamicAttributesMetaProperty property : metaProperties) {
            Component formField = generateFieldComponent(property);
            fields.put(property.getAttribute(), prepareFieldComponent(formField, property));
        }
        addFieldsToForm(propertiesForm, fields);
        initFieldCaptionWidth(propertiesForm);
    }

    protected void addFieldsToForm(Form newPropertiesForm, Map<CategoryAttribute, Component> fields) {
        if (fields.keySet().stream().anyMatch(attr -> attr.getConfiguration().getXCoordinate() != null
                && attr.getConfiguration().getYCoordinate() != null)) {

            List<CategoryAttribute> attributesToAdd = fields.keySet().stream()
                    .filter(attr -> attr.getConfiguration().getXCoordinate() != null
                            && attr.getConfiguration().getYCoordinate() != null)
                    .collect(Collectors.toList());

            int maxColumnIndex = attributesToAdd.stream()
                    .mapToInt(attr -> attr.getConfiguration().getXCoordinate())
                    .max()
                    .orElse(0);

            newPropertiesForm.setColumns(maxColumnIndex + 1);

            for (int i = 0; i <= maxColumnIndex; i++) {
                int columnIndex = i;
                List<CategoryAttribute> columnAttributes = attributesToAdd.stream()
                        .filter(attr -> columnIndex == attr.getConfiguration().getXCoordinate())
                        .sorted(Comparator.comparing(attr -> attr.getConfiguration().getYCoordinate()))
                        .collect(Collectors.toList());

                int currentRowNumber = 0;
                for (CategoryAttribute attr : columnAttributes) {
                    while (attr.getConfiguration().getYCoordinate() > currentRowNumber) {
                        //add empty row
                        newPropertiesForm.add(createEmptyComponent(), columnIndex, currentRowNumber);
                        currentRowNumber++;
                    }
                    newPropertiesForm.add(fields.get(attr), columnIndex, currentRowNumber);
                    currentRowNumber++;
                }
            }
        } else {
            int propertiesCount = getPropertiesFilteredByCategory().size();
            int rowsPerColumn = getRowsPerColumn(propertiesCount);
            int columnNo = 0;
            int fieldsCount = 0;
            for (Component field : fields.values()) {
                fieldsCount++;
                newPropertiesForm.add(field, columnNo);
                if (fieldsCount % rowsPerColumn == 0) {
                    columnNo++;
                    newPropertiesForm.setColumns(columnNo + 1);
                }
            }
        }
    }

    private Component createEmptyComponent() {
        Label<String> component = uiComponents.create(Label.TYPE_STRING);
        component.setValue("\u2060");
        return component;
    }

    protected int getRowsPerColumn(int propertiesCount) {
        if (cols != null) {
            if (propertiesCount % cols == 0) {
                return propertiesCount / cols;
            }
            return propertiesCount / cols + 1;
        }
        if (rows != null) {
            return rows;
        }
        return propertiesCount;
    }

    protected List<DynamicAttributesMetaProperty> getPropertiesFilteredByCategory() {
        MetaClass metaClass = instanceContainer.getEntityMetaClass();
        Category category = categoryField.getValue();
        if (category == null) {
            return Collections.emptyList();
        }

        List<DynamicAttributesMetaProperty> result = new ArrayList<>();
        for (CategoryAttribute categoryAttribute : dynamicAttributes.getAttributesForMetaClass(metaClass)) {
            if (category.equals(categoryAttribute.getCategory())) {
                result.add(new DynamicAttributesMetaProperty(metaClass, categoryAttribute));
            }
        }
        return result;
    }

    protected Component generateFieldComponent(DynamicAttributesMetaProperty property) {
        CategoryAttribute attribute = property.getAttribute();
        ValueSource valueSource = new ContainerValueSource<>(instanceContainer, property.getName());
        ComponentGenerationContext componentGenerationContext =
                new ComponentGenerationContext(instanceContainer.getEntityMetaClass(), property.getName());
        componentGenerationContext.setValueSource(valueSource);

        Component formField;
        if (Boolean.TRUE.equals(attribute.getIsCollection())) {
            formField = dynamicAttributeComponentsGenerator
                    .generateComponent(valueSource, attribute);
        } else {
            formField = uiComponentsGenerator.generate(componentGenerationContext);
        }
        return formField;
    }

    protected Component prepareFieldComponent(Component component, DynamicAttributesMetaProperty property) {
        CategoryAttribute attribute = property.getAttribute();
        String caption = (attribute != null) ? attribute.getLocaleName() : property.getName();
        String width = (attribute != null)
                && StringUtils.isNotBlank(attribute.getWidth())
                ? attribute.getWidth() : fieldWidth;
        component.setWidth(width);
        if (component instanceof Component.HasCaption) {
            ((Component.HasCaption) component).setCaption(caption);
        }
        if (attribute != null
                && StringUtils.isNoneBlank(attribute.getLocaleDescription())
                && component instanceof Component.HasDescription) {
            ((Component.HasDescription) component).setDescription(attribute.getLocaleDescription());
        }
        return component;
    }

    protected void initCategoryField(InstanceContainer<BaseGenericIdEntity> instanceContainer) {
        categoryField.setOptionsList(getCategoriesOptionsList());
        categoryField.setValueSource(new ContainerValueSource<>(instanceContainer, "category"));
    }

    @Nullable
    protected Category getDefaultCategory() {
        for (Category category : getCategoriesOptions()) {
            if (category != null && Boolean.TRUE.equals(category.getIsDefault())) {
                return category;
            }
        }
        return null;
    }

    protected Collection<Category> getCategoriesOptions() {
        return dynamicAttributes.getCategoriesForMetaClass(
                instanceContainer.getEntityMetaClass());
    }

    protected List<Category> getCategoriesOptionsList() {
        Collection<Category> options = getCategoriesOptions();

        List<Category> optionsList;
        if (options instanceof List) {
            optionsList = (List<Category>) options;
        } else {
            optionsList = new ArrayList<>(options);
        }

        return optionsList;
    }

    protected void initFieldCaptionWidth(Form newRuntimeForm) {
        if (fieldCaptionWidth != null) {
            SizeWithUnit sizeWithUnit = SizeWithUnit.parseStringSize(fieldCaptionWidth);
            if (SizeUnit.PERCENTAGE.equals(sizeWithUnit.getUnit())) {
                throw new IllegalStateException("DynamicAttributesPanel fieldCaptionWidth with '%' unit is unsupported");
            }
            newRuntimeForm.setChildrenCaptionWidth(Math.round(sizeWithUnit.getSize()));
        }
    }

    protected void onInstanceContainerItemChangeEvent(InstanceContainer.ItemChangeEvent<BaseGenericIdEntity> event) {
        if (event.getItem() instanceof Categorized
                && ((Categorized) event.getItem()).getCategory() == null) {
            ((Categorized) event.getItem()).setCategory(getDefaultCategory());
        }
        if (event.getItem() == null) {
            propertiesForm.removeAll();
        }
    }

    /**
     * Defines InstanceContainer for DynamicAttributesPanel.
     *
     * @param instanceContainer {@link InstanceContainer} object with editing entity
     */
    @StudioProperty(name = "dataContainer", type = PropertyType.DATACONTAINER_REF, required = true)
    public void setInstanceContainer(InstanceContainer<BaseGenericIdEntity> instanceContainer) {
        this.instanceContainer = instanceContainer;
        propertiesForm.setValueSourceProvider(new ContainerValueSourceProvider<>(instanceContainer));
        initCategoryField(instanceContainer);
        initPropertiesForm();
        this.instanceContainer.addItemChangeListener(this::onInstanceContainerItemChangeEvent);
    }

    /**
     * Sets the number of columns. If {@code null} value is passed, columns count will be determined
     * based on the {@code rows} parameter.
     *
     * @param cols positive integer or {@code null}
     */
    @StudioProperty(name = "cols")
    @Positive
    public void setColumnsCount(Integer cols) {
        if (cols != null && cols <= 0) {
            throw new GuiDevelopmentException(
                    "DynamicAttributesPanel element has incorrect value of the 'cols' attribute", this.id);
        }
        this.cols = cols;
    }

    /**
     * Sets the number of rows. This parameter will only be taken into account if {@code cols == null}.
     *
     * @param rows positive integer or {@code null}
     */
    @StudioProperty(name = "rows")
    @Positive
    public void setRowsCount(Integer rows) {
        if (rows != null && rows <= 0) {
            throw new GuiDevelopmentException(
                    "DynamicAttributesPanel element has incorrect value of the 'rows' attribute", this.id);
        }
        this.rows = rows;
    }

    /**
     * Sets the width of the fields. This parameter is used if some dynamic attribute does not have own width value.
     *
     * @param fieldWidth width of the fields
     */
    @StudioProperty(type = PropertyType.SIZE)
    public void setFieldWidth(String fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    /**
     * Sets the width of the fields caption. {@code fieldCaptionWidth} with '%' unit is unsupported.
     *
     * @param fieldCaptionWidth width of the fields caption
     */
    @StudioProperty(type = PropertyType.SIZE)
    public void setFieldCaptionWidth(String fieldCaptionWidth) {
        this.fieldCaptionWidth = fieldCaptionWidth;
    }

    /**
     * Sets visibility of the {@code CategoryField} component.
     *
     * @param visible visibility flag
     */
    public void setCategoryFieldVisible(boolean visible) {
        categoryFieldBox.setVisible(visible);
    }

    @Override
    public boolean isValid() {
        Collection<Component> components = ComponentsHelper.getComponents(propertiesForm);
        for (Component component : components) {
            if (component instanceof Validatable) {
                Validatable validatable = (Validatable) component;
                if (validatable.isValidateOnCommit() && !validatable.isValid())
                    return false;
            }
        }
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        ComponentsHelper.traverseValidatable(propertiesForm, Validatable::validate);
    }
}