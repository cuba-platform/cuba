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

import com.google.common.base.Strings;
import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaClass;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component.HasXmlDescriptor;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.FilterImplementation;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.descriptor.*;
import com.haulmont.cuba.gui.components.sys.ValuePathHelper;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Builds a {@link com.haulmont.bali.datastruct.Tree} of {@link com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor}. These
 * descriptors are used in a new condition dialog.
 */
@Component(ConditionDescriptorsTreeBuilderAPI.NAME)
@Scope("prototype")
public class ConditionDescriptorsTreeBuilder implements ConditionDescriptorsTreeBuilderAPI {

    private static final Logger log = LoggerFactory.getLogger(ConditionDescriptorsTreeBuilder.class);

    protected static final List<String> defaultExcludedProps = Collections.unmodifiableList(Collections.singletonList("version"));
    protected static final String CUSTOM_CONDITIONS_PERMISSION = "cuba.gui.filter.customConditions";
    protected static final Pattern AGGREGATE_JPQL_FUNCTION_PATTERN = Pattern.compile("(COUNT|SUM|AVG|MIN|MAX)\\s*\\(.*\\)", Pattern.CASE_INSENSITIVE);

    protected Filter filter;
    protected int hierarchyDepth;
    protected Security security;
    protected String filterComponentName;
    protected MetadataTools metadataTools;
    protected DynamicAttributes dynamicAttributes;
    protected List<String> excludedProperties;
    protected final String storeName;
    protected final MetaClass entityMetaClass;
    protected final String entityAlias;
    protected final boolean hideDynamicAttributes;
    protected final boolean hideCustomConditions;
    protected ConditionsTree conditionsTree;
    protected boolean excludePropertiesRecursively;
    protected final boolean isKeyValueMetaClass;
    protected QueryTransformerFactory queryTransformerFactory;

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
        queryTransformerFactory = AppBeans.get(QueryTransformerFactory.class);
        filterComponentName = getFilterComponentName();
        excludedProperties = new ArrayList<>();
        storeName = metadataTools.getStoreName(((FilterImplementation) filter).getEntityMetaClass());
        entityMetaClass = ((FilterImplementation) filter).getEntityMetaClass();
        isKeyValueMetaClass = entityMetaClass instanceof KeyValueMetaClass;
        entityAlias = ((FilterImplementation) filter).getEntityAlias();
    }

    @Override
    public Tree<AbstractConditionDescriptor> build() {
        Messages messages = AppBeans.get(Messages.class);

        Class<? extends FrameOwner> controllerClass = filter.getFrame().getFrameOwner().getClass();
        String messagesPack = UiControllerUtils.getPackage(controllerClass); // todo rework

        Tree<AbstractConditionDescriptor> tree = new Tree<>();
        List<AbstractConditionDescriptor> propertyDescriptors = new ArrayList<>();
        List<AbstractConditionDescriptor> customDescriptors = new ArrayList<>();

        boolean propertiesExplicitlyDefined = false;
        if (((HasXmlDescriptor) filter).getXmlDescriptor() != null) {
            for (Element element : ((HasXmlDescriptor) filter).getXmlDescriptor().elements()) {
                AbstractConditionDescriptor conditionDescriptor;
                if ("properties".equals(element.getName())) {
                    addMultiplePropertyDescriptors(element, propertyDescriptors, filter);
                    propertiesExplicitlyDefined = true;
                } else if ("property".equals(element.getName())) {
                    if (isKeyValueMetaClass) {
                        conditionDescriptor = createConditionDescriptorForKeyValueMetaProperty(messagesPack,
                                element.attributeValue("name"),
                                element.attributeValue("caption"));
                        if (conditionDescriptor != null) {
                            propertyDescriptors.add(conditionDescriptor);
                        }
                    } else {
                        conditionDescriptor = new PropertyConditionDescriptor(element, messagesPack, filterComponentName,
                                entityMetaClass, entityAlias);
                        propertyDescriptors.add(conditionDescriptor);
                    }
                    propertiesExplicitlyDefined = true;
                } else if ("custom".equals(element.getName())) {
                    conditionDescriptor = new CustomConditionDescriptor(element, messagesPack, filterComponentName,
                            entityMetaClass, entityAlias);
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
                messages.getMainMessage("filter.addCondition.propertyConditions"), filterComponentName,
                entityMetaClass, entityAlias);
        HeaderConditionDescriptor customHeaderDescriptor = new HeaderConditionDescriptor("customConditions",
                messages.getMainMessage("filter.addCondition.customConditions"), filterComponentName,
                entityMetaClass, entityAlias);

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
                    && !excludedProperties.contains(metaProperty.getName())
                    && (filter.getPropertiesFilterPredicate() == null || filter.getPropertiesFilterPredicate().test(propertyPath))) {
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
            rootNodes.add(new Node<>(new CustomConditionCreator(filterComponentName,
                    ((FilterImplementation) filter).getEntityMetaClass(), entityAlias)));
        }

        if (!hideDynamicAttributes && !dynamicAttributes.getAttributesForMetaClass(entityMetaClass).isEmpty()) {
            rootNodes.add(new Node<>(new DynamicAttributesConditionCreator(filterComponentName, entityMetaClass, "", entityAlias)));
        }

        if (FtsConfigHelper.getEnabled()) {
            rootNodes.add(new Node<>(new FtsConditionDescriptor(filterComponentName, entityMetaClass, entityAlias)));
        }

        tree.setRootNodes(rootNodes);

        return tree;
    }

    /**
     * Method parses the JPQL from a datasource or a dataLoader associated with a generic filter and creates a PropertyConditionDescriptor for a
     * KeyValueEntity property with a given name.
     * <p>
     * For example, we have the following keyValueCollection:
     *
     * <pre>
     * {@code
     * <keyValueCollection id="myKeyValueCollectionDc">
     *    <loader id="myKeyValueCollectionDl">
     *       <query>select  u.login, ur.role.name from sec$User u join u.userRoles ur</query>
     *    </loader>
     *    <properties>
     *       <property datatype="string" name="myLogin"/>
     *       <property datatype="string" name="myRoleName"/>
     *    </properties>
     * </keyValueCollection>
     * }
     * </pre>
     * <p>
     * When we need to build a PropertyConditionDescriptor for the "myRoleName" property then we need:
     *
     * <ol>
     *     <li>Find the position of the myRoleName property in the &lt;properties&gt; tag - it is a second property</li>
     *     <li>Take all path expressions from the JPQL query and take the second one. It is "ur.role.name"</li>
     *     <li>From the "ur.role.name" we extract entityAlias ("ur") and propertiesPath ("role.name") - these values will be used for building the
     *     PropertyConditionDescriptor. The "name" used in ConditionDescriptor is "myRoleName"</li>
     * </ol>
     *
     * @return a PropertyConditionDescriptor or null if the KeyValueEntity meta property is associated with the aggregate function
     */
    @Nullable
    protected PropertyConditionDescriptor createConditionDescriptorForKeyValueMetaProperty(String messagesPack, String propertyName, String propertyCaption) {
        int index = new ArrayList<>(entityMetaClass.getProperties()).indexOf(entityMetaClass.getProperty(propertyName));
        String jpqlQuery = filter.getDataLoader() != null ?
                filter.getDataLoader().getQuery() :
                filter.getDatasource().getQuery();
        QueryParser queryParser = queryTransformerFactory.parser(jpqlQuery);
        List<String> selectedExpressions = queryParser.getSelectedExpressionsList();
        String selectedExpression = selectedExpressions.get(index);

        String entityAliasForCondDescr;
        String propertiesPathForCondDescr;
        if (isAggregateFunction(selectedExpression)) {
            log.debug("Aggregate function {} cannot be used for filter condition", selectedExpression);
            return null;
        }
        if (selectedExpression.contains(".")) {
            int indexOfDot = selectedExpression.indexOf(".");
            entityAliasForCondDescr = selectedExpression.substring(0, indexOfDot);
            propertiesPathForCondDescr = selectedExpression.substring(indexOfDot + 1);
        } else {
            //joined entity may be selected, e.g. "a" alias in query "select b.title, a from app_Book b join b.author a"
            entityAliasForCondDescr = selectedExpression;
            propertiesPathForCondDescr = "";
        }
        return new PropertyConditionDescriptor(propertyName, propertyCaption, messagesPack, filterComponentName,
                entityMetaClass, entityAliasForCondDescr, propertiesPathForCondDescr);
    }

    protected boolean isAggregateFunction(String expression) {
        return AGGREGATE_JPQL_FUNCTION_PATTERN.matcher(expression).matches();
    }

    protected void recursivelyFillPropertyDescriptors(Node<AbstractConditionDescriptor> parentNode, int currentDepth) {
        currentDepth++;
        List<AbstractConditionDescriptor> descriptors = new ArrayList<>();
        String propertyId = parentNode.getData().getName();
        MetaPropertyPath mpp = entityMetaClass.getPropertyPath(propertyId);
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
                    if (excludedProperties.contains(propertyPath)
                            || excludePropertiesRecursively && excludedProperties.contains(property.getName())
                            || filter.getPropertiesFilterPredicate() != null
                            && !filter.getPropertiesFilterPredicate().test(entityMetaClass.getPropertyPath(propertyPath))) {
                        continue;
                    }

                    Class<? extends FrameOwner> controllerClass = filter.getFrame().getFrameOwner().getClass();
                    String messagesPack = UiControllerUtils.getPackage(controllerClass); // todo rework

                    if (isKeyValueMetaClass) {
                        if (parentNode.data instanceof PropertyConditionDescriptor) {
                            PropertyConditionDescriptor parentConditionDescriptor = (PropertyConditionDescriptor) parentNode.data;
                            String parentPropertiesPath = parentConditionDescriptor.getPropertiesPath();
                            String newPropertiesPath = !Strings.isNullOrEmpty(parentPropertiesPath) ?
                                    parentPropertiesPath + "." + property.getName() :
                                    property.getName();
                            PropertyConditionDescriptor childPropertyConditionDescriptor = new PropertyConditionDescriptor(propertyPath, null,
                                    messagesPack, filterComponentName, entityMetaClass, parentConditionDescriptor.getEntityAlias(), newPropertiesPath);
                            descriptors.add(childPropertyConditionDescriptor);
                        }
                    } else {
                        PropertyConditionDescriptor childPropertyConditionDescriptor = new PropertyConditionDescriptor(propertyPath, null,
                                messagesPack, filterComponentName, entityMetaClass, entityAlias);
                        descriptors.add(childPropertyConditionDescriptor);
                    }
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
                DynamicAttributesConditionCreator descriptor = new DynamicAttributesConditionCreator(filterComponentName,
                        entityMetaClass, propertyId, entityAlias);
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

            String excludeRecursively = element.attributeValue("excludeRecursively");
            if (excludeProperties != null && !excludeProperties.isEmpty()) {
                excludePropertiesRecursively = Boolean.parseBoolean(excludeRecursively);
            }
        }
    }

    protected void addMultiplePropertyDescriptors(String includeRe, String excludeRe, List<AbstractConditionDescriptor> descriptors, Filter filter) {
        List<String> includedProps = new ArrayList<>();
        Pattern inclPattern = Pattern.compile(includeRe.replace(" ", ""));

        for (MetaProperty property : entityMetaClass.getProperties()) {
            if (!isPropertyAllowed(entityMetaClass, property)) {
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
                Class<? extends FrameOwner> controllerClass = filter.getFrame().getFrameOwner().getClass();
                String messagesPack = UiControllerUtils.getPackage(controllerClass); // todo rework

                AbstractConditionDescriptor conditionDescriptor;
                if (isKeyValueMetaClass) {
                    conditionDescriptor = createConditionDescriptorForKeyValueMetaProperty(messagesPack, prop, null);
                    if (conditionDescriptor != null) {
                        descriptors.add(conditionDescriptor);
                    }
                } else {
                    conditionDescriptor = new PropertyConditionDescriptor(prop, null, messagesPack,
                            filterComponentName, entityMetaClass, entityAlias);
                    descriptors.add(conditionDescriptor);
                }
            }
        }
    }

    protected boolean isPropertyAllowed(MetaClass metaClass, MetaProperty property) {
        return security.isEntityAttrPermitted(metaClass, property.getName(), EntityAttrAccess.VIEW)
                && !metadataTools.isSystemLevel(property)           // exclude system level attributes
                && ((metadataTools.isPersistent(property) || isKeyValueMetaClass)           // exclude transient properties
                || (metadataTools.getCrossDataStoreReferenceIdProperty(storeName, property) != null))
                && !defaultExcludedProps.contains(property.getName())
                && !(byte[].class.equals(property.getJavaType()))
                && !property.getRange().getCardinality().isMany();  // exclude ToMany
    }

    protected String getFilterComponentName() {
        String filterComponentName = ComponentsHelper.getFilterComponentPath(filter);
        String[] parts = ValuePathHelper.parse(filterComponentName);
        if (parts.length > 1) {
            filterComponentName = ValuePathHelper.pathSuffix(parts);
        }
        return filterComponentName;
    }

    protected static class ConditionDescriptorComparator implements Comparator<AbstractConditionDescriptor> {
        @Override
        public int compare(AbstractConditionDescriptor cd1, AbstractConditionDescriptor cd2) {
            return cd1.getLocCaption().compareTo(cd2.getLocCaption());
        }
    }
}