/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Categorized;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.ValuePathHelper;
import com.haulmont.cuba.gui.components.filter.descriptor.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Builds a {@link com.haulmont.bali.datastruct.Tree} of {@link com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor}.
 * These descriptors are used in a new condition dialog.
 *
 * @author gorbunkov
 * @version $Id$
 */
public class ConditionDescriptorsTreeBuilder {

    protected static final List<String> defaultExcludedProps = Collections.unmodifiableList(Arrays.asList("version"));
    protected static final String CUSTOM_CONDITIONS_PERMISSION = "cuba.gui.filter.customConditions";

    protected static Log log = LogFactory.getLog(ConditionDescriptorsTreeBuilder.class);

    protected Filter filter;
    protected int hierarchyDepth;
    protected Security security;
    protected String filterComponentName;
    protected MessageTools messageTools;
    protected MetadataTools metadataTools;

    /**
     * @param filter filter
     * @param hierarchyDepth max level of properties hierarchy
     */
    public ConditionDescriptorsTreeBuilder(Filter filter, int hierarchyDepth) {
        this.filter = filter;
        this.hierarchyDepth = hierarchyDepth;
        security = AppBeans.get(Security.class);
        messageTools = AppBeans.get(MessageTools.NAME);
        metadataTools = AppBeans.get(MetadataTools.NAME);
        filterComponentName = getFilterComponentName();
    }

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

        Collections.sort(propertyDescriptors, new ConditionDescriptorComparator());
        Collections.sort(customDescriptors, new ConditionDescriptorComparator());

        HeaderConditionDescriptor propertyHeaderDescriptor = new HeaderConditionDescriptor("propertyConditions",
                messages.getMessage(ConditionDescriptorsTreeBuilder.class, "AddCondition.propertyConditions"), filterComponentName, datasource);
        HeaderConditionDescriptor customHeaderDescriptor = new HeaderConditionDescriptor("customConditions",
                messages.getMessage(ConditionDescriptorsTreeBuilder.class, "AddCondition.customConditions"), filterComponentName, datasource);

        Node<AbstractConditionDescriptor> propertyHeaderNode = new Node<AbstractConditionDescriptor>(propertyHeaderDescriptor);
        Node<AbstractConditionDescriptor> customHeaderNode = new Node<AbstractConditionDescriptor>(customHeaderDescriptor);
        int currentDepth = 0;

        for (AbstractConditionDescriptor propertyDescriptor : propertyDescriptors) {
            MetaPropertyPath propertyPath = propertyDescriptor.getDatasourceMetaClass().getPropertyPath(propertyDescriptor.getName());
            if (propertyPath == null) {
                log.error("Property path for " + propertyDescriptor.getName() + " of metaClass" + propertyDescriptor.getDatasourceMetaClass().getName() + " not found");
                continue;
            }
            MetaProperty metaProperty = propertyPath.getMetaProperty();
            if (isPropertyAllowed(metaProperty)) {
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

        if (security.isSpecificPermitted(CUSTOM_CONDITIONS_PERMISSION)) {
            rootNodes.add(new Node<AbstractConditionDescriptor>(new CustomConditionCreator(filterComponentName, datasource)));
        }

        if (Categorized.class.isAssignableFrom(datasource.getMetaClass().getJavaClass())) {//todo eude not only for categorized?
            rootNodes.add(new Node<AbstractConditionDescriptor>(new DynamicAttributesConditionCreator(filterComponentName, datasource)));
        }

        tree.setRootNodes(rootNodes);

        return tree;
    }

    private void recursivelyFillPropertyDescriptors(Node<AbstractConditionDescriptor> parentNode, int currentDepth) {
        currentDepth++;
        List<AbstractConditionDescriptor> descriptors = new ArrayList<>();
        MetaClass filterMetaClass = filter.getDatasource().getMetaClass();
        String propertyId = parentNode.getData().getName();
        MetaPropertyPath mpp = filterMetaClass.getPropertyPath(propertyId);
        if (mpp == null) {
            throw new RuntimeException("Unable to find property " + propertyId);
        }

        MetaProperty metaProperty = mpp.getMetaProperty();
        if (metaProperty.getRange().isClass()) {
            MetaClass childMetaClass = metaProperty.getRange().asClass();
            for (MetaProperty property : childMetaClass.getProperties()) {
                if (isPropertyAllowed(property)) {
                    String propertyPath = mpp.toString() + "." + property.getName();
                    PropertyConditionDescriptor childPropertyConditionDescriptor =
                            new PropertyConditionDescriptor(propertyPath, null, filter.getFrame().getMessagesPack(), filterComponentName, filter.getDatasource());
                    descriptors.add(childPropertyConditionDescriptor);
                }
            }
        }

        Collections.sort(descriptors, new ConditionDescriptorComparator());
        for (AbstractConditionDescriptor descriptor : descriptors) {
            Node<AbstractConditionDescriptor> newNode = new Node<>(descriptor);
            parentNode.addChild(newNode);
            if (currentDepth < hierarchyDepth) {
                recursivelyFillPropertyDescriptors(newNode, currentDepth);
            }
        }
    }

    private void addMultiplePropertyDescriptors(Element element, List<AbstractConditionDescriptor> descriptors, Filter filter) {
        String includeRe = element.attributeValue("include");
        String excludeRe = element.attributeValue("exclude");
        addMultiplePropertyDescriptors(includeRe, excludeRe, descriptors, filter);
    }

    private void addMultiplePropertyDescriptors(String includeRe, String excludeRe, List<AbstractConditionDescriptor> descriptors, Filter filter) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Messages messages = AppBeans.get(Messages.class);

        List<String> includedProps = new ArrayList<>();

        Pattern inclPattern = Pattern.compile(includeRe.replace(" ", ""));

        for (MetaProperty property : filter.getDatasource().getMetaClass().getProperties()) {
            if (metadata.getTools().isTransient(property))
                continue;
            if (property.getRange().getCardinality().isMany())
                continue;
            if (defaultExcludedProps.contains(property.getName()))
                continue;
            if (!messages.getTools().hasPropertyCaption(property))
                continue;

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

    private boolean isPropertyAllowed(MetaProperty property) {
        return security.isEntityAttrPermitted(property.getDomain(), property.getName(), EntityAttrAccess.VIEW)
                && !metadataTools.isSystemLevel(property)           // exclude system level attributes
                && metadataTools.isPersistent(property)             // exclude transient properties
                && messageTools.hasPropertyCaption(property)        // exclude not localized properties (they are usually not for end user)
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

    private class ConditionDescriptorComparator implements Comparator<AbstractConditionDescriptor> {
        @Override
        public int compare(AbstractConditionDescriptor cd1, AbstractConditionDescriptor cd2) {
            return cd1.getLocCaption().compareTo(cd2.getLocCaption());
        }
    }
}
