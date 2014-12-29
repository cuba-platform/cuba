/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.condition.RuntimePropCondition;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.RandomStringUtils;

/**
 * @author devyatkin
 * @version $Id$
 */
@MetaClass(name = "sec$RuntimePropConditionCreator")
@SystemLevel
public class RuntimePropConditionCreator extends AbstractConditionDescriptor {
    public RuntimePropConditionCreator(String filterComponentName, CollectionDatasource datasource) {
        super(RandomStringUtils.randomAlphabetic(10), filterComponentName, datasource);
        Messages messages = AppBeans.get(Messages.NAME);
        locCaption = messages.getMessage(RuntimePropConditionCreator.class, "runtimePropConditionCreator");
        showImmediately = true;
    }

    @Override
    public AbstractCondition createCondition() {
        return new RuntimePropCondition(this, entityAlias);
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