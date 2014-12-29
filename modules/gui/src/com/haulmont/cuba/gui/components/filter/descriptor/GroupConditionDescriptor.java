/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.GroupCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Base GUI class for grouping conditions descriptors (AND and OR groups).
 *
 * @author krivopustov
 * @version $Id$
 */
public class GroupConditionDescriptor extends AbstractConditionDescriptor {

    protected GroupType groupType;

    public GroupConditionDescriptor(GroupType groupType,
                                    String filterComponentName, CollectionDatasource datasource) {
        super("group", filterComponentName, datasource);
        this.groupType = groupType;
    }

    public GroupType getGroupType() {
        return groupType;
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