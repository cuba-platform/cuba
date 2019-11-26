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

package com.haulmont.cuba.core.entity;

import com.google.common.base.Strings;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.entity.CategoryAttributeOptionsLoaderType.*;

@MetaClass(name = "sys$CategoryAttributeConfiguration")
@SystemLevel
public class CategoryAttributeConfiguration extends BaseGenericIdEntity<String> {

    private static final long serialVersionUID = 2670605418267938507L;

    protected transient CategoryAttribute categoryAttribute;

    @Id
    @MetaProperty
    protected String id;

    @MetaProperty
    protected Integer minInt;

    @MetaProperty
    protected Double minDouble;

    @MetaProperty
    protected BigDecimal minDecimal;

    @MetaProperty
    protected Integer maxInt;

    @MetaProperty
    protected Double maxDouble;

    @MetaProperty
    protected BigDecimal maxDecimal;

    @MetaProperty
    protected String validatorGroovyScript;

    @MetaProperty
    protected String columnName;

    @MetaProperty
    protected String columnAlignment;

    @MetaProperty
    protected Integer columnWidth;

    @MetaProperty
    protected String numberFormatPattern;

    @MetaProperty
    protected String optionsLoaderType;

    @MetaProperty
    protected String optionsLoaderScript;

    @MetaProperty
    protected String recalculationScript;

    @MetaProperty
    protected Integer xCoordinate;

    @MetaProperty
    protected Integer yCoordinate;

    @Transient
    protected transient Collection<CategoryAttribute> dependentAttributes;

    @Transient
    protected List<UUID> dependsOnCategoryAttributesIds;

    @MetaProperty
    @Transient
    protected transient List<CategoryAttribute> dependsOnAttributes;

    public Integer getMinInt() {
        return minInt;
    }

    public void setMinInt(Integer minInt) {
        this.minInt = minInt;
    }

    public Double getMinDouble() {
        return minDouble;
    }

    public void setMinDouble(Double minDouble) {
        this.minDouble = minDouble;
    }

    public BigDecimal getMinDecimal() {
        return minDecimal;
    }

    public void setMinDecimal(BigDecimal minDecimal) {
        this.minDecimal = minDecimal;
    }

    public Integer getMaxInt() {
        return maxInt;
    }

    public void setMaxInt(Integer maxInt) {
        this.maxInt = maxInt;
    }

    public Double getMaxDouble() {
        return maxDouble;
    }

    public void setMaxDouble(Double maxDouble) {
        this.maxDouble = maxDouble;
    }

    public BigDecimal getMaxDecimal() {
        return maxDecimal;
    }

    public void setMaxDecimal(BigDecimal maxDecimal) {
        this.maxDecimal = maxDecimal;
    }

    public Number getMinValue() {
        if (categoryAttribute.getDataType() != null) {
            switch (categoryAttribute.getDataType()) {
                case INTEGER:
                    return minInt;
                case DOUBLE:
                    return minDouble;
                case DECIMAL:
                    return minDecimal;
                default:
                    return null;
            }
        }
        return null;
    }

    public Number getMaxValue() {
        if (categoryAttribute.getDataType() != null) {
            switch (categoryAttribute.getDataType()) {
                case INTEGER:
                    return maxInt;
                case DOUBLE:
                    return maxDouble;
                case DECIMAL:
                    return maxDecimal;
                default:
                    return null;
            }
        }
        return null;
    }

    public String getValidatorGroovyScript() {
        return validatorGroovyScript;
    }

    public void setValidatorGroovyScript(String validatorGroovyScript) {
        this.validatorGroovyScript = validatorGroovyScript;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnAlignment() {
        return columnAlignment;
    }

    public void setColumnAlignment(String columnAlignment) {
        this.columnAlignment = columnAlignment;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public CategoryAttribute getCategoryAttribute() {
        return categoryAttribute;
    }

    public void setCategoryAttribute(CategoryAttribute categoryAttribute) {
        this.categoryAttribute = categoryAttribute;
        this.id = generateId(categoryAttribute);
    }

    public String getNumberFormatPattern() {
        return numberFormatPattern;
    }

    public void setNumberFormatPattern(String numberFormatPattern) {
        this.numberFormatPattern = numberFormatPattern;
    }

    public String getRecalculationScript() {
        return recalculationScript;
    }

    public void setRecalculationScript(String recalculationScript) {
        this.recalculationScript = recalculationScript;
    }

    public Collection<CategoryAttribute> getDependentAttributes() {
        if (dependentAttributes == null) {
            DynamicAttributesTools dynamicAttributesTools = AppBeans.get(DynamicAttributesTools.NAME);
            dependentAttributes = dynamicAttributesTools.getDependentCategoryAttributes(categoryAttribute);
        }

        return dependentAttributes;
    }

    public List<CategoryAttribute> getDependsOnAttributes() {
        if (dependsOnCategoryAttributesIds == null || dependsOnCategoryAttributesIds.isEmpty()) {
            return Collections.emptyList();
        }

        if (dependsOnAttributes == null) {
            DataManager dataManager = AppBeans.get(DataManager.class);
            dependsOnAttributes = dataManager.load(CategoryAttribute.class)
                    .ids(dependsOnCategoryAttributesIds)
                    .list();
        }

        return dependsOnAttributes;
    }

    public void setDependsOnAttributes(List<CategoryAttribute> dependsOnAttributes) {
        if (dependsOnAttributes == null) {
            this.dependsOnCategoryAttributesIds = null;
            this.dependsOnAttributes = null;
            return;
        }

        this.dependsOnCategoryAttributesIds = dependsOnAttributes.stream()
                .map(BaseUuidEntity::getId)
                .collect(Collectors.toList());

        this.dependsOnAttributes = dependsOnAttributes;
    }

    public Boolean isReadOnly() {
        return !Strings.isNullOrEmpty(recalculationScript);
    }

    public CategoryAttributeOptionsLoaderType getOptionsLoaderType() {
        return CategoryAttributeOptionsLoaderType.fromId(optionsLoaderType);
    }

    public void setOptionsLoaderType(CategoryAttributeOptionsLoaderType optionsLoaderType) {
        this.optionsLoaderType = optionsLoaderType == null ? null : optionsLoaderType.getId();
    }

    public String getOptionsLoaderScript() {
        return optionsLoaderScript;
    }

    public void setOptionsLoaderScript(String optionsLoaderScript) {
        this.optionsLoaderScript = optionsLoaderScript;
    }

    public boolean hasOptionsLoader() {
        if (getOptionsLoaderType() != null) {
            CategoryAttributeOptionsLoaderType loaderType = getOptionsLoaderType();
            if (SQL == loaderType || GROOVY == loaderType) {
                return !Strings.isNullOrEmpty(getOptionsLoaderScript());
            } else if (JPQL == loaderType) {
                return categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENTITY;
            }
        }
        return false;
    }

    public Integer getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        if (id == null) {
            id = generateId(categoryAttribute);
        }
        return id;
    }

    protected String generateId(CategoryAttribute attribute) {
        String suffix = "-Configuration";
        if (attribute == null) {
            return UUID.randomUUID().toString() + suffix;
        }
        return attribute.getId().toString() + suffix;
    }
}
