/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractParam;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ConditionCreator extends AbstractConditionDescriptor {

    public ConditionCreator(String filterComponentName, CollectionDatasource datasource) {
        super("creator", filterComponentName, datasource);

        this.locCaption = AppBeans.get(Messages.class).getMessage(MESSAGES_PACK, "conditionCreator");
        this.showImmediately = true;
    }

    @Override
    public AbstractCondition createCondition() {
        NewCustomCondition newCustomCondition = new NewCustomCondition(this, null, null, entityAlias);
        // default editor - text
        newCustomCondition.setJavaClass(String.class);

        AbstractParam param = paramFactory.createParam(
                newCustomCondition.getName(), newCustomCondition.getJavaClass(),
                newCustomCondition.getWhere(), newCustomCondition.getJoin(), newCustomCondition.getDatasource(),
                newCustomCondition.isInExpr(), newCustomCondition.isRequired());
        newCustomCondition.setParam((Param) param);

        return newCustomCondition;
    }

    @Override
    public Param createParam(AbstractCondition condition) {
        return null;
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }

    @Override
    public Class getJavaClass() {
        return null;
    }

    @Override
    public String getEntityParamWhere() {
        return null;
    }

    @Override
    public String getEntityParamView() {
        return null;
    }
}