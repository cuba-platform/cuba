/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractGroupConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractPropertyConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Abstract class to be extended in generic filter condition adding dialogs.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractDescriptorBuilder {

    protected String messagesPack;
    protected String filterComponentName;
    protected CollectionDatasource datasource;

    public AbstractDescriptorBuilder(
            String messagesPack, String filterComponentName, CollectionDatasource datasource) {
        this.messagesPack = messagesPack;
        this.filterComponentName = filterComponentName;
        this.datasource = datasource;
    }

    public abstract AbstractPropertyConditionDescriptor buildPropertyConditionDescriptor(String name, String caption);

    public abstract AbstractGroupConditionDescriptor buildGroupConditionDescriptor(GroupType groupType);

    public abstract AbstractConditionDescriptor buildCustomConditionDescriptor();

    public abstract AbstractConditionDescriptor buildRuntimePropConditionDescriptor();
}
