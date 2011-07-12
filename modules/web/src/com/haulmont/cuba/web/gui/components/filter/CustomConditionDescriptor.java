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

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractCustomConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

public class CustomConditionDescriptor extends AbstractCustomConditionDescriptor<Param> {

    public CustomConditionDescriptor(Element element,
                                     String messagesPack,
                                     String filterComponentName,
                                     CollectionDatasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected ParamFactory<Param> getParamFactory() {
        return new ParamFactoryImpl();
    }

    @Override
    public AbstractCondition createCondition() {
        CustomCondition condition = new CustomCondition(this,
                element.getText(), element.attributeValue("join"), entityAlias);
        condition.setInExpr(inExpr);
        return condition;
    }
}
