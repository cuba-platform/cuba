/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.*;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropCondition extends AbstractRuntimePropCondition<Param> {

    private OperationEditor operationEditor;

    public RuntimePropCondition(AbstractConditionDescriptor descriptor, String where, String join, String entityAlias) {
        super(descriptor, where, join, entityAlias);
        paramFactory = new ParamFactoryImpl();
    }

    public RuntimePropCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);
    }

    @Override
    public OperationEditor createOperationEditor() {
        operationEditor = new RuntimePropOperationEditor(this);
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
