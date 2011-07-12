/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractPropertyConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class PropertyConditionDescriptor extends AbstractPropertyConditionDescriptor<Param> {
    public PropertyConditionDescriptor(String name, String caption, String messagesPack,
                                       String filterComponentName, CollectionDatasource datasource) {
        super(name, caption, messagesPack, filterComponentName, datasource);
    }

    public PropertyConditionDescriptor(Element element, String messagesPack,
                                       String filterComponentName, CollectionDatasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);
    }

    @Override
    public AbstractCondition createCondition() {
        return new PropertyCondition(this, entityAlias);
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }
}
