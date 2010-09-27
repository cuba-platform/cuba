/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.10.2009 17:09:09
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import org.dom4j.Element;
import static org.apache.commons.lang.StringUtils.isBlank;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.global.MessageUtils;

public class PropertyConditionDescriptor extends ConditionDescriptor {

    protected String entityParamWhere;
    protected String entityParamView;

    public PropertyConditionDescriptor(String name,
                                       String caption,
                                       String messagesPack,
                                       String filterComponentName,
                                       CollectionDatasource datasource)
    {
        super(name, filterComponentName, datasource);
        this.caption = caption;

        if (!isBlank(caption)) {
            this.locCaption = MessageUtils.loadString(messagesPack, caption);
        } else {
            this.caption = MessageUtils.getMessageRef(metaClass, name);
            this.locCaption = MessageUtils.getPropertyCaption(metaClass, name);
        }
    }

    public PropertyConditionDescriptor(Element element,
                                       String messagesPack,
                                       String filterComponentName,
                                       CollectionDatasource datasource)
    {
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
    public Condition createCondition() {
        return new PropertyCondition(this, entityAlias);
    }

    @Override
    public Param createParam(Condition condition) {
        MetaProperty metaProperty = datasource.getMetaClass().getProperty(name);
        Param param = new Param(condition.createParamName(), getJavaClass(),
                getEntityParamWhere(), getEntityParamView(), datasource, metaProperty, inExpr);
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
