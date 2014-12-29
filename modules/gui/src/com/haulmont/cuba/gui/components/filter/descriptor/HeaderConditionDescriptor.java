/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Condition descriptor that doesn't describe any condition.
 * It is used as parent node for property condition descriptors and custom condition descriptors.
 *
 * @author gorbunkov
 * @version $Id$
 */
@MetaClass(name = "sec$HeaderConditionDescriptor")
@SystemLevel
public class HeaderConditionDescriptor extends AbstractConditionDescriptor {


    public HeaderConditionDescriptor(String name, String locCaption, String filterComponentName, CollectionDatasource datasource) {
        super(name, filterComponentName, datasource);
        this.locCaption = locCaption;
    }

    @Override
    public AbstractCondition createCondition() {
        throw new UnsupportedOperationException();
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
