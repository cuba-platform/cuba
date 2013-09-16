/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractCustomCondition;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CustomCondition extends AbstractCustomCondition<Param>{
        public CustomCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);
    }

    private OperationEditor operationEditor;

    public CustomCondition(AbstractConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor, where, join, entityAlias);
    }

    @Override
    public OperationEditor createOperationEditor() {
        operationEditor = new CustomOperationEditor(this);
        return operationEditor;
    }

    public OperationEditor getOperationEditor() {
        return operationEditor;
    }

    @Override
    protected ParamFactory<Param> getParamFactory() {
        return new ParamFactoryImpl();
    }
}