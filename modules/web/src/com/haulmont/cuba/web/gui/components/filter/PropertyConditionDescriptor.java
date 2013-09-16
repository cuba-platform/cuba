/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractPropertyConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PropertyConditionDescriptor extends AbstractPropertyConditionDescriptor<Param> {

    public PropertyConditionDescriptor(String name,
                                       @Nullable String caption,
                                       String messagesPack,
                                       String filterComponentName,
                                       CollectionDatasource datasource)
    {
        super(name, caption, messagesPack, filterComponentName, datasource);
    }

    public PropertyConditionDescriptor(Element element,
                                       String messagesPack,
                                       String filterComponentName,
                                       CollectionDatasource datasource)
    {
        super(element,messagesPack,filterComponentName,datasource);
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