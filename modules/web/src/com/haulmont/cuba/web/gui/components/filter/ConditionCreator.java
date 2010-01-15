/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.10.2009 15:12:27
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.global.MessageProvider;

public class ConditionCreator extends ConditionDescriptor {

    public ConditionCreator(String filterComponentName, CollectionDatasource datasource) {
        super("creator", filterComponentName, datasource);
        locCaption = MessageProvider.getMessage(getClass(), "conditionCreator");
    }

    @Override
    public Condition createCondition() {
        return new NewCustomCondition(this, "", null, entityAlias);
    }

    @Override
    public Param createParam(Condition condition) {
        return null;
    }

    @Override
    public Class getJavaClass() {
        return null;
    }

    @Override
    protected String getEntityParamWhere() {
        return null;
    }

    @Override
    protected String getEntityParamView() {
        return null;
    }
}
