/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.10.2009 18:19:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.*;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

public class PropertyCondition extends AbstractPropertyCondition<Param> {
    private OperationEditor operationEditor;

    public PropertyCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack,filterComponentName, datasource);
    }

    public PropertyCondition(AbstractConditionDescriptor descriptor, String entityAlias) {
        super(descriptor,entityAlias);
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
    protected ParamFactory<Param> getParamFactory() {
        return new ParamFactoryImpl();
    }
}