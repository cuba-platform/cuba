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

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.sys.ValuePathHelper;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.descriptor.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Builds a {@link com.haulmont.bali.datastruct.Tree} of {@link com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor}.
 * These descriptors are used in a new condition dialog.
 */
@Component(ConditionDescriptorsTreeBuilderAPI.NAME)
@Scope("prototype")
public class ConditionDescriptorsTreeBuilder implements ConditionDescriptorsTreeBuilderAPI {

    private static final Logger log = LoggerFactory.getLogger(ConditionDescriptorsTreeBuilder.class);

    protected static final List<String> defaultExcludedProps = Collections.unmodifiableList(Collections.singletonList("version"));
    protected static final String CUSTOM_CONDITIONS_PERMISSION = "cuba.gui.filter.customConditions";

    protected Filter filter;
    protected int hierarchyDepth;
    protected Security security;
    protected String filterComponentName;
    protected MetadataTools metadataTools;
    protected DynamicAttributes dynamicAttributes;
    protected List<String> excludedProperties;
    protected final String storeName;
    protected final boolean hideDynamicAttributes;
    protected final boolean hideCustomConditions;
    protected ConditionsTree conditionsTree;

    /**
     * @param filter                filter
     * @param hierarchyDepth        max level of properties hierarchy
     * @param hideDynamicAttributes hide dynamic attributes conditions from wizard
     */
    public ConditionDescriptorsTreeBuilder(Filter filter,
                                           int hierarchyDepth,
                                           boolean hideDynamicAttributes,
                                           boolean hideCustomConditions,
                                           ConditionsTree conditionsTree) {
        this.filter = filter;
        this.hierarchyDepth = hierarchyDepth;
        this.hideDynamicAttributes = hideDynamicAttributes;
        this.hideCustomConditions = hideCustomConditions;
        this.conditionsTree = conditionsTree;
        security = AppBeans.get(Security.class);
        metadataTools = AppBeans.get(MetadataTools.NAME);
        dynamicAttributes = AppBeans.get(DynamicAttributes.class);
        filterComponentName = getFilterComponentName();
        excludedProperties = new ArrayList<>();
        storeName = metadataTools.getStoreName(filter.getDatasource().getMetaClass());
    }

    @Override
    public Tree<AbstractConditionDescriptor> build() {
        Messages messages = AppBeans.get(Messages.class);
        String messagesPack = filter.getFrame().getMessagesPack();
        CollectionDatasource datasource = filter.getDatasource();

        Tree<AbstractConditionDescriptor> tree = new Tree<>();
        List<AbstractConditionDescriptor> propertyDescriptors = new ArrayList<>();
        List<AbstractConditionDescriptor> customDescriptors = new ArrayList<>();

        boolean propertiesExplicitlyDefined = false;
        if (filter.getXmlDescriptor() != null) {
            for (Element element : Dom4j.elements(filter.getXmlDescriptor())) {
                AbstractConditionDescriptor conditionDescriptor;
                if ("properties".equals(element.getName())) {
                    addMultiplePropertyDescriptors(element, propertyDescriptors, filter);
                    propertiesExplicitlyDefined = true;
                } else if ("property".equals(element.getName())) {
                    conditionDescriptor = new PropertyConditionDescriptor(element, messagesPack, filterComponentName, datasource);
                    propertyDescriptors.add(conditionDescriptor);
                    propertiesExplicitlyDefined = true;
                } else if ("custom".equals(element.getName())) {
                    conditionDescriptor = new CustomConditionDescriptor(element, messagesPack, filterComponentName, datasource);
                    customDescriptors.add(conditionDescriptor);
                    propertiesExplicitlyDefined = true;
                } else {
                    throw new UnsupportedOperationException("Element not supported: " + element.getName());
                }
            }
        }

        if (!propertiesExplicitlyDefined) {
            addMultiplePropertyDescriptors(".*", "", propertyDescriptors, filter);
        }

        propertyDescriptors.sort(new ConditionDescriptorComparator());
        customDescriptors.sort(new ConditionDescriptorComparator());

        HeaderConditionDescriptor propertyHeaderDescriptor = new HeaderConditionDescriptor("propertyConditions",
                messages.getMainMessage("filter.addCondition.propertyConditions"), filterComponentName, datasource);
        HeaderConditionDescriptor customHeaderDescriptor = new HeaderConditionDescriptor("customConditions",
                messages.getMainMessage("filter.addCondition.customConditions"), filterComponentName, datasource);

        Node<AbstractConditionDescriptor> propertyHeaderNode = new Node<>(propertyHeaderDescriptor);
        Node<AbstractConditionDescriptor> customHeaderNode = new Node<>(customHeaderDescriptor);
        int currentDepth = 0;

        for (AbstractConditionDescriptor propertyDescriptor : propertyDescriptors) {
            MetaClass propertyDsMetaClass = propertyDescriptor.getDatasourceMetaClass();
            MetaPropertyPath propertyPath = propertyDsMetaClass.getPropertyPath(propertyDescriptor.getName());
            if (propertyPath == null) {
                log.error("Property path for {} of metaClass {} not found",
                        propertyDescriptor.getName(), propertyDsMetaClass.getName());
                continue;
            }

            MetaProperty metaProperty = propertyPath.getMetaProperty();
            MetaClass propertyEnclosingMetaClass = metadataTools.getPropertyEnclosingMetaClass(propertyPath);

            if (isPropertyAllowed(propertyEnclosingMetaClass, metaProperty)
                    && !excludedProperties.contains(metaProperty.getName())) {
                Node<AbstractConditionDescriptor> node = new Node<>(propertyDescriptor);
                propertyHeaderNode.addChild(node);

                if (currentDepth < hierarchyDepth) {
                    recursivelyFillPropertyDescriptors(node, currentDepth);
                }
            }
        }

        for (AbstractConditionDescriptor customDescriptor : customDescriptors) {
            Node<AbstractConditionDescriptor> node = new Node<>(customDescriptor);
            customHeaderNode.addChild(node);
        }

        List<Node<AbstractConditionDescriptor>> rootNodes = new ArrayList<>();
        rootNodes.add(propertyHeaderNode);
        if (!customDescriptors.isEmpty())
            rootNodes.add(customHeaderNode);

        if (!hideCustomConditions && security.isSpecificPermitted(CUSTOM_CONDITIONS_PERMISSION)) {
            rootNodes.add(new Node<>(new CustomConditionCreator(filterComponentName, datasource)));
        }

        if (!hideDynamicAttributes && !dynamicAttributes.getAttributesForMetaClass(datasource.getMetaClass()).isEmpty()) {
            rootNodes.add(new Node<>(new DynamicAttributesConditionCreator(filterComponentName, datasource, "")));
        }

        if (FtsConfigHelper.getEnabled()) {
            rootNodes.add(new Node<>(new FtsConditionDescriptor(filterComponentName, datasource)));
        }

        tree.setRootNodes(rootNodes);

        return tree;
    }

    protected void recursivelyFillPropertyDescriptors(Node<AbstractConditionDescriptor> parentNode, int currentDepth) {
        currentDepth++;
        List<AbstractConditionDescriptor> descriptors = new ArrayList<>();
        MetaClass filterMetaClass = filter.getDatasource().getMetaClass();
        String propertyId = parentNode.getData().getName();
        MetaPropertyPath mpp = filterMetaClass.getPropertyPath(propertyId);
        if (mpp == null) {
            throw new RuntimeException("Unable to find property " + propertyId);
        }

        MetaProperty metaProperty = mpp.getMetaProperty();
        if (metaProperty.getRange().isClass()
                && (metadataTools.getCrossDataStoreReferenceIdProperty(storeName, metaProperty) == null)) {
            MetaClass childMetaClass = metaProperty.getRange().asClass();
            for (MetaProperty property : childMetaClass.getProperties()) {
                if (isPropertyAllowed(childMetaClass, property)) {
                    String propertyPath = mpp.toString() + "." + property.getName();
                    if (excludedProperties.contains(propertyPath))
                        continue;

                    PropertyConditionDescriptor childPropertyConditionDescriptor =
                            new PropertyConditionDescriptor(propertyPath, null, filter.getFrame().getMessagesPack(), filterComponentName, filter.getDatasource());
                    descriptors.add(childPropertyConditionDescriptor);
                }
            }
        }

        descriptors.sort(new ConditionDescriptorComparator());

        for (AbstractConditionDescriptor descriptor : descriptors) {
            Node<AbstractConditionDescriptor> newNode = new Node<>(descriptor);
            parentNode.addChild(newNode);
            if (currentDepth < hierarchyDepth) {
                recursivelyFillPropertyDescriptors(newNode, currentDepth);
            }
        }

        if (metaProperty.getRange().isClass()) {
            MetaClass childMetaClass = metaProperty.getRange().asClass();
            if (!dynamicAttributes.getAttributesForMetaClass(childMetaClass).isEmpty()) {
                DynamicAttributesConditionCreator descriptor = new DynamicAttributesConditionCreator(filterComponentName, filter.getDatasource(), propertyId);
                Node<AbstractConditionDescriptor> newNode = new Node<>(descriptor);
                parentNode.addChild(newNode);
            }
        }
    }

    protected void addMultiplePropertyDescriptors(Element element, List<AbstractConditionDescriptor> descriptors, Filter filter) {
        String includeRe = element.attributeValue("include");
        String excludeRe = element.attributeValue("exclude");
        addMultiplePropertyDescriptors(includeRe, excludeRe, descriptors, filter);

        if (element.attribute("excludeProperties") != null) {
            String excludeProperties = element.attributeValue("excludeProperties");
            if (StringUtils.isNotEmpty(excludeProperties)) {
                excludedProperties = Arrays.asList(excludeProperties.replace(" ", "").split(","));
            }
        }
    }

    protected void addMultiplePropertyDescriptors(String includeRe, String excludeRe, List<AbstractConditionDescriptor> descriptors, Filter filter) {
        List<String> includedProps = new ArrayList<>();
        Pattern inclPattern = Pattern.compile(includeRe.replace(" ", ""));

        MetaClass metaClass = filter.getDatasource().getMetaClass();
        for (MetaProperty property : metaClass.getProperties()) {
            if (!isPropertyAllowed(metaClass, property)) {
                continue;
            }

            if (inclPattern.matcher(property.getName()).matches()) {
                includedProps.add(property.getName());
            }
        }

        Pattern exclPattern = null;
        if (!StringUtils.isBlank(excludeRe)) {
            exclPattern = Pattern.compile(excludeRe.replace(" ", ""));
        }

        for (String prop : includedProps) {
            if (exclPattern == null || !exclPattern.matcher(prop).matches()) {
                AbstractConditionDescriptor conditionDescriptor =
                        new PropertyConditionDescriptor(prop, null, filter.getFrame().getMessagesPack(), filterComponentName, filter.getDatasource());
                descriptors.add(conditionDescriptor);
            }
        }
    }

    protected boolean isPropertyAllowed(MetaClass metaClass, MetaProperty property) {
        return security.isEntityAttrPermitted(metaClass, property.getName(), EntityAttrAccess.VIEW)
                && !metadataTools.isSystemLevel(property)           // exclude system level attributes
                && (metadataTools.isPersistent(property)            // exclude transient properties
                || (metadataTools.getCrossDataStoreReferenceIdProperty(storeName, property) != null))
                && !defaultExcludedProps.contains(property.getName())
                && !(byte[].class.equals(property.getJavaType()))
                && !property.getRange().getCardinality().isMany();  // exclude ToMany
    }

    protected String getFilterComponentName() {
        String filterComponentName = ComponentsHelper.getFilterComponentPath(filter);
        String[] parts = ValuePathHelper.parse(filterComponentName);
        if (parts.length > 1) {
            filterComponentName = ValuePathHelper.format(Arrays.copyOfRange(parts, 1, parts.length));
        }
        return filterComponentName;
    }

    protected class ConditionDescriptorComparator implements Comparator<AbstractConditionDescriptor> {
        @Override
        public int compare(AbstractConditionDescriptor cd1, AbstractConditionDescriptor cd2) {
            return cd1.getLocCaption().compareTo(cd2.getLocCaption());
        }
    }
}