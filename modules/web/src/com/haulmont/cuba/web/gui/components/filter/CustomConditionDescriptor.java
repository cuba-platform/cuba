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

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.global.MessageUtils;
import org.dom4j.Element;

public class CustomConditionDescriptor extends ConditionDescriptor {

    private Element element;

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
    }

    @Override
    public Condition createCondition() {
        return new CustomCondition(this, 
                element.getText(), element.attributeValue("join"), entityAlias);
    }

    @Override
    public Class getJavaClass() {
        element.attributeValue("paramClass");
        Class paramClass = ReflectionHelper.getClass(element.attributeValue("paramClass"));
//        
//        Element paramElem = element.element("param");
//        Class paramClass = ReflectionHelper.getClass(paramElem.attributeValue("class"));
        return paramClass;
    }
}
