/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.AbstractGroupConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.components.filter.ParamFactory;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Web-client implementation of the grouping condition descriptor.
 *
 * @author krivopustov
 * @version $Id$
 */
public class GroupCreator extends AbstractGroupConditionDescriptor {

    public GroupCreator(GroupType groupType, String filterComponentName, CollectionDatasource datasource) {
        super(groupType, "groupCreator", filterComponentName, datasource);
        Messages messages = AppBeans.get(Messages.NAME);
        locCaption = messages.getMessage(MESSAGES_PACK, "groupCreator." + groupType);
    }

    @Override
    protected ParamFactory getParamFactory() {
        return new ParamFactoryImpl();
    }

    @Override
    public AbstractCondition createCondition() {
        return new GroupCondition(this);
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