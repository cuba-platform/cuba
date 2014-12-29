/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.components.filter.condition.FilterConditionUtils;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

import javax.annotation.Nullable;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author devyatkin
 * @version $Id$
 */
@MetaClass(name = "sec$PropertyConditionDescriptor")
@SystemLevel
public class PropertyConditionDescriptor extends AbstractConditionDescriptor {
    protected String entityParamWhere;
    protected String entityParamView;

    public PropertyConditionDescriptor(String name,
                                       @Nullable String caption,
                                       String messagesPack,
                                       String filterComponentName,
                                       CollectionDatasource datasource) {
        super(name, filterComponentName, datasource);
        this.caption = caption;

        Messages messages = AppBeans.get(Messages.NAME);

        if (!isBlank(caption)) {
            this.locCaption = messages.getTools().loadString(messagesPack, caption);
        } else {
            this.locCaption = FilterConditionUtils.getPropertyLocCaption(datasourceMetaClass, name);
        }
    }

    public PropertyConditionDescriptor(Element element, String messagesPack, String filterComponentName,
                                       CollectionDatasource datasource) {
        this(element.attributeValue("name"),
                element.attributeValue("caption"),
                messagesPack,
                filterComponentName,
                datasource);
        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
        entityParamWhere = element.attributeValue("paramWhere");
        entityParamView = element.attributeValue("paramView");
    }

    @Override
    public Class getJavaClass() {
        MetaProperty metaProperty = datasourceMetaClass.getPropertyEx(name).getMetaProperty();
        Class paramClass;
        if (metaProperty != null)
            paramClass = metaProperty.getJavaType();
        else
            throw new IllegalStateException("Unable to find property '" + name + "' in entity " + datasourceMetaClass);
        return paramClass;
    }

    @Override
    public String getEntityParamWhere() {
        return entityParamWhere;
    }

    @Override
    public String getEntityParamView() {
        return entityParamView;
    }

    @Override
    public AbstractCondition createCondition() {
        PropertyCondition propertyCondition = new PropertyCondition(this, entityAlias);
        propertyCondition.setOperator(Op.EQUAL);
        return propertyCondition;
    }

    @Override
    public String getTreeCaption() {
        MessageTools messageTools = AppBeans.get(MessageTools.class);
        MetaPropertyPath mpp = datasourceMetaClass.getPropertyPath(name);
        return mpp != null ? messageTools.getPropertyCaption(mpp.getMetaProperty()) : name;
    }
}