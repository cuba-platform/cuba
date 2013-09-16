/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropConditionCreator extends ConditionCreator {
    public RuntimePropConditionCreator(String filterComponentName, CollectionDatasource datasource) {
        super(filterComponentName, datasource);
        locCaption = AppBeans.get(Messages.class).getMessage(MESSAGES_PACK, "runtimePropConditionCreator");
        showImmediately = true;
    }

    @Override
    public AbstractCondition createCondition() {
        return new RuntimePropCondition(this, "", null, entityAlias);
    }
}