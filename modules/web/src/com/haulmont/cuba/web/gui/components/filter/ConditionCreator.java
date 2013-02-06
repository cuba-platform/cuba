/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ConditionCreator extends AbstractConditionDescriptor {

    public ConditionCreator(String filterComponentName, CollectionDatasource datasource) {
        super("creator", filterComponentName, datasource);
        locCaption = AppBeans.get(Messages.class).getMessage(MESSAGES_PACK, "conditionCreator");
        showImmediately = true;
    }

    @Override
    public AbstractCondition createCondition() {
        return new NewCustomCondition(this, "", null, entityAlias);
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