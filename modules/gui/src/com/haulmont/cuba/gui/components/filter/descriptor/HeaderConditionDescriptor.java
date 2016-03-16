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

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Condition descriptor that doesn't describe any condition.
 * It is used as parent node for property condition descriptors and custom condition descriptors.
 *
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
