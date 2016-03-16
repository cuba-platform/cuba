/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.GroupCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Base GUI class for grouping conditions descriptors (AND and OR groups).
 *
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