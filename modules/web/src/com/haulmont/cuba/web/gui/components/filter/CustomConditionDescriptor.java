/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.10.2009 17:09:50
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

public class CustomConditionDescriptor extends ConditionDescriptor {

    public CustomConditionDescriptor(Element element, 
                                     String messagesPack,
                                     String filterComponentName,
                                     CollectionDatasource datasource)
    {
        super(element.attributeValue("name"), filterComponentName, datasource);
        this.element = element;

        this.caption = element.attributeValue("caption");
        if (this.caption != null)
            this.locCaption = MessageUtils.loadString(messagesPack, this.caption);

        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
    }

    @Override
    public Condition createCondition() {
        CustomCondition condition = new CustomCondition(this,
                element.getText(), element.attributeValue("join"), entityAlias);
        condition.setInExpr(inExpr);
        return condition;
    }

    @Override
    public Class getJavaClass() {
        String className = element.attributeValue("paramClass");
        if (className == null)
            className = element.attributeValue("class");
        if (className == null)
            return null;
        else {
            Class paramClass = ScriptingProvider.loadClass(element.attributeValue("paramClass"));
            return paramClass;
        }
    }

    @Override
    public String getEntityParamWhere() {
        return element.attributeValue("paramWhere");
    }

    @Override
    public String getEntityParamView() {
        return element.attributeValue("paramView");
    }
}
