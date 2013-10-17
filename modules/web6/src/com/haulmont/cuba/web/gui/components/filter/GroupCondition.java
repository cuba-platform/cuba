/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.*;
import org.dom4j.Element;

/**
 * Web-client implementation of the grouping condition.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class GroupCondition extends AbstractGroupCondition<Param> {

    private OperationEditor operationEditor;

    public GroupCondition(Element element, String filterComponentName) {
        super(element, filterComponentName);
    }

    public GroupCondition(AbstractGroupConditionDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public AbstractOperationEditor createOperationEditor() {
        operationEditor = new GroupOperationEditor(this);
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
