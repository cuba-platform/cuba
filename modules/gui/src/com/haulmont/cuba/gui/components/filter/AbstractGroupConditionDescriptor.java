/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Base GUI class for grouping conditions (AND & OR) descriptors.
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractGroupConditionDescriptor<T extends AbstractParam> extends AbstractConditionDescriptor<T> {

    protected GroupType groupType;

    public AbstractGroupConditionDescriptor(GroupType groupType, String name,
                                            String filterComponentName, CollectionDatasource datasource) {
        super(name, filterComponentName, datasource);
        this.groupType = groupType;
    }

    public GroupType getGroupType() {
        return groupType;
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