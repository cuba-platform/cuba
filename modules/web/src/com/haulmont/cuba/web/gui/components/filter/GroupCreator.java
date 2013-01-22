/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.filter.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Web-client implementation of the grouping condition descriptor.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class GroupCreator extends AbstractGroupConditionDescriptor {

    public GroupCreator(GroupType groupType, String filterComponentName, CollectionDatasource datasource) {
        super(groupType, "groupCreator", filterComponentName, datasource);
        locCaption = MessageProvider.getMessage(MESSAGES_PACK, "groupCreator." + groupType);
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
