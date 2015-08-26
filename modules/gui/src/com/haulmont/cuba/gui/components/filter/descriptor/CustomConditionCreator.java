/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.ConditionParamBuilder;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Condition descriptor is used for creating new custom condition
 * @author krivopustov
 * @version $Id$
 */
@MetaClass(name = "sec$CustomConditionCreator")
@SystemLevel
public class CustomConditionCreator extends AbstractConditionDescriptor {

    public CustomConditionCreator(String filterComponentName, CollectionDatasource datasource) {
        super(RandomStringUtils.randomAlphabetic(10), filterComponentName, datasource);

        Messages messages = AppBeans.get(Messages.NAME);
        this.locCaption = messages.getMessage(CustomConditionCreator.class, "CustomCondition.new");
        showImmediately = true;
    }

    @Override
    public AbstractCondition createCondition() {
        CustomCondition customCondition = new CustomCondition(this, null, null, entityAlias, false);

        // default editor - text
        customCondition.setJavaClass(String.class);
        Param param = AppBeans.get(ConditionParamBuilder.class).createParam(customCondition);
        customCondition.setParam(param);

        return customCondition;
    }

    @Override
    public String getTreeCaption() {
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMessage(CustomConditionCreator.class, "conditionCreator");
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