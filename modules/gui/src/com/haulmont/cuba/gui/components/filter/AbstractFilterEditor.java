/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.ValuePathHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;
import java.util.regex.Pattern;

import static org.apache.commons.lang.BooleanUtils.isTrue;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public abstract class AbstractFilterEditor {

    public static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";

    protected Filter filter;
    protected FilterEntity filterEntity;
    protected Element filterDescriptor;
    protected MetaClass metaClass;
    protected CollectionDatasource datasource;
    protected List<AbstractConditionDescriptor> descriptors = new ArrayList<AbstractConditionDescriptor>();
    protected List<String> existingNames;

    protected ConditionsTree conditions = new ConditionsTree();
    protected String messagesPack;
    protected String filterComponentName;

    protected static List<String> defaultExcludedProps = Arrays.asList("version");

    protected Boolean manualApplyRequired;

    protected Messages messages = AppBeans.get(Messages.NAME, Messages.class);

    public AbstractFilterEditor(final Filter filter, FilterEntity filterEntity,
                                Element filterDescriptor, List<String> existingNames) {
        this.filter = filter;
        this.filterEntity = filterEntity;
        this.filterDescriptor = filterDescriptor;
        this.datasource = filter.getDatasource();
        this.messagesPack = filter.getFrame().getMessagesPack();
        this.metaClass = datasource.getMetaClass();
        this.existingNames = existingNames;

        this.manualApplyRequired = filter.getManualApplyRequired() != null
                ? filter.getManualApplyRequired() : ConfigProvider.getConfig(ClientConfig.class).getGenericFilterManualApplyRequired();

        String[] strings = ValuePathHelper.parse(filterEntity.getComponentId());
        this.filterComponentName = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));

        parseDescriptorXml();

        AbstractFilterParser parser = createFilterParser(this.filterEntity.getXml(), messagesPack, filterComponentName, datasource);
        this.conditions = parser.fromXml().getConditions();
    }

    public abstract void init();

    protected abstract AbstractFilterParser createFilterParser(
            String xml,
            String messagePack,
            String filterComponentName,
            CollectionDatasource datasource);

    protected abstract AbstractPropertyConditionDescriptor createPropertyConditionDescriptor(
            Element element,
            String messagePack,
            String filterComponentName,
            CollectionDatasource datasource);

    protected abstract AbstractPropertyConditionDescriptor createPropertyConditionDescriptor(
            String name,
            String caption,
            String messagesPack,
            String filterComponentName,
            CollectionDatasource datasource);

    protected abstract AbstractCustomConditionDescriptor createCustomConditionDescriptor(
            Element element,
            String messagesPack,
            String filterComponentName,
            CollectionDatasource datasource);

    protected abstract AbstractFilterParser createFilterParser(
            ConditionsTree conditions,
            String messagesPack,
            String filterComponentName,
            Datasource datasource);

    protected abstract String getName();

    protected abstract boolean isGlobal();

    protected abstract void showNotification(String caption, String description);

    protected void parseDescriptorXml() {
        for (Element element : Dom4j.elements(filterDescriptor)) {
            AbstractConditionDescriptor conditionDescriptor;
            if ("properties".equals(element.getName())) {
                addMultiplePropertyDescriptors(element, filterComponentName);
            } else if ("property".equals(element.getName())) {
                conditionDescriptor = createPropertyConditionDescriptor(element, messagesPack, filterComponentName, datasource);
                descriptors.add(conditionDescriptor);
            } else if ("custom".equals(element.getName())) {
                conditionDescriptor = createCustomConditionDescriptor(element, messagesPack, filterComponentName, datasource);
                descriptors.add(conditionDescriptor);
            } else
                throw new UnsupportedOperationException("Element not supported: " + element.getName());
        }

        Collections.sort(descriptors, new Comparator<AbstractConditionDescriptor>() {
            @Override
            public int compare(AbstractConditionDescriptor cd1, AbstractConditionDescriptor cd2) {
                return cd1.getLocCaption().compareTo(cd2.getLocCaption());
            }
        });
    }

    private void addMultiplePropertyDescriptors(Element element, String filterComponentName) {
        List<String> includedProps = new ArrayList<String>();

        String inclRe = element.attributeValue("include");
        Pattern inclPattern = Pattern.compile(inclRe);

        for (MetaProperty property : metaClass.getProperties()) {
            Map<String,Object> propertyAnnotation = property.getAnnotations();
            if (propertyAnnotation.size() == 0 || propertyAnnotation.get("persistent") == null)
                continue;
            if (property.getRange().getCardinality().isMany())
                continue;
            if (defaultExcludedProps.contains(property.getName()))
                continue;
            if (!AppBeans.get(MessageTools.class).hasPropertyCaption(property))
                continue;

            if (inclPattern.matcher(property.getName()).matches()) {
                includedProps.add(property.getName());
            }
        }

        String exclRe = element.attributeValue("exclude");
        Pattern exclPattern = null;
        if (!StringUtils.isBlank(exclRe))
            exclPattern = Pattern.compile(exclRe);

        for (String prop : includedProps) {
            if (exclPattern == null || !exclPattern.matcher(prop).matches()) {
                AbstractConditionDescriptor conditionDescriptor =
                        createPropertyConditionDescriptor(prop, null, messagesPack, filterComponentName, datasource);
                descriptors.add(conditionDescriptor);
            }
        }
    }

    protected String getMessage(String key) {
        return messages.getMessage(MESSAGES_PACK, key);
    }

    public FilterEntity getFilterEntity() {
        return filterEntity;
    }

    public boolean commit() {
        if (StringUtils.isBlank(getName())) {
            showNotification(
                    getMessage("FilterEditor.commitError"),
                    getMessage("FilterEditor.nameNotSet"));
            return false;
        }

        if (filterEntity.getFolder() == null) {
            if (existingNames.contains(getName())) {
                showNotification(
                        getMessage("FilterEditor.commitError"),
                        getMessage("FilterEditor.nameAlreadyExists"));
                return false;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (AbstractCondition condition : conditions.toConditionsList()) {
            String error = condition.getError();
            if (error != null)
                sb.append(error).append("\n");
        }
        if (sb.length() > 0) {
            showNotification(
                    getMessage("FilterEditor.commitError"),
                    sb.toString());
            return false;
        }

        AbstractFilterParser parser = createFilterParser(conditions, messagesPack, filterComponentName, datasource);
        String xml = parser.toXml().getXml();

        filterEntity.setName(getName().trim());
        filterEntity.setXml(xml);

        if (isTrue(isGlobal()))
            filterEntity.setUser(null);
        else
            filterEntity.setUser(UserSessionProvider.getUserSession().getCurrentOrSubstitutedUser());

        return true;
    }

    public List<AbstractCondition> getConditions() {
        return conditions.toConditionsList();
    }
}
