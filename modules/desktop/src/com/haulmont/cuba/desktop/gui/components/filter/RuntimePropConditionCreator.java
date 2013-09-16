/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropConditionCreator extends ConditionCreator {
    public RuntimePropConditionCreator(String filterComponentName, CollectionDatasource datasource) {
        super(filterComponentName, datasource);
        locCaption = MessageProvider.getMessage(MESSAGES_PACK, "runtimePropConditionCreator");
    }

    @Override
    public AbstractCondition createCondition() {
        return new RuntimePropCondition(this, "", null, entityAlias);
    }
}
