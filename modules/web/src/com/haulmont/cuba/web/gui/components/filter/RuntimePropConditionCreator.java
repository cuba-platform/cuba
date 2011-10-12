/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

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
        showImmediately = true;
    }

    @Override
    public AbstractCondition createCondition() {
        return new RuntimePropCondition(this, "", null, entityAlias);
    }
}
