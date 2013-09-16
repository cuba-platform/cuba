/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.gui.components.filter.AbstractGroupCondition;
import com.haulmont.cuba.gui.components.filter.AbstractGroupConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import org.dom4j.Element;

/**
 * Desktop-client implementation of the grouping condition.
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
