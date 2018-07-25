/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.components.filter.descriptor;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.FtsCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;

@MetaClass(name = "sec$FtsConditionDescriptor")
@SystemLevel
public class FtsConditionDescriptor extends AbstractConditionDescriptor {

    public FtsConditionDescriptor(String filterComponentName, com.haulmont.chile.core.model.MetaClass metaClass,
                                  String entityAlias) {
        super("fts", filterComponentName, metaClass, entityAlias);
    }

    @Override
    public AbstractCondition createCondition() {
        FtsCondition ftsCondition = new FtsCondition(this);
        return ftsCondition;
    }

    @Override
    public String getTreeCaption() {
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMainMessage("filter.addCondition.ftsCondition");
    }

    @Override
    public Class getJavaClass() {
        return String.class;
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
