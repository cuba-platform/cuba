/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

import javax.annotation.Nullable;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author devyatkin
 * @version $Id$
 */
public abstract class AbstractPropertyConditionDescriptor<T extends AbstractParam> extends AbstractConditionDescriptor<T> {
    protected String entityParamWhere;
    protected String entityParamView;

    public AbstractPropertyConditionDescriptor(String name,
                                               @Nullable String caption,
                                               String messagesPack,
                                               String filterComponentName,
                                               CollectionDatasource datasource) {
        super(name, filterComponentName, datasource);
        this.caption = caption;

        Messages messages = AppBeans.get(Messages.class);

        if (!isBlank(caption)) {
            this.locCaption = messages.getTools().loadString(messagesPack, caption);
        } else {
            this.caption = messages.getTools().getMessageRef(this.metaClass, name);
            this.locCaption = messages.getMessage(this.metaClass.getJavaClass(), this.metaClass.getJavaClass().getSimpleName() + "." + name);
            if (this.locCaption == null || this.locCaption.equals(this.metaClass.getJavaClass().getSimpleName() + "." + name))
                this.locCaption = messages.getTools().getPropertyCaption(this.metaClass, name);
        }
    }

    public AbstractPropertyConditionDescriptor(Element element, String messagesPack, String filterComponentName,
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
    public T createParam(AbstractCondition condition) {
        MetaProperty metaProperty = datasource.getMetaClass().getProperty(name);
        T param = paramFactory.createParam(condition.createParamName(), getJavaClass(),
                getEntityParamWhere(), getEntityParamView(), datasource, metaProperty, inExpr, condition.isRequired());
        return param;
    }

    @Override
    public Class getJavaClass() {
        MetaProperty metaProperty = metaClass.getPropertyEx(name).getMetaProperty();
        Class paramClass;
        if (metaProperty != null)
            paramClass = metaProperty.getJavaType();
        else
            throw new IllegalStateException("Unable to find property '" + name + "' in entity " + metaClass);
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
}