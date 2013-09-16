/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.AbstractPropertyCondition;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class PropertyCondition extends AbstractPropertyCondition<Param> {

    private OperationEditor operationEditor;

    public PropertyCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);
    }

    public PropertyCondition(AbstractConditionDescriptor descriptor, String entityAlias) {
        super(descriptor, entityAlias);
    }

    @Override
    public OperationEditor createOperationEditor() {
        operationEditor = new PropertyOperationEditor(this);
        return operationEditor;
    }

    @Override
    public AbstractOperationEditor getOperationEditor() {
        return operationEditor;
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }
}
