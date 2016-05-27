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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.condition.DynamicAttributesCondition;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.RandomStringUtils;

@MetaClass(name = "sec$DynamicAttributesConditionCreator")
@SystemLevel
public class DynamicAttributesConditionCreator extends AbstractConditionDescriptor {

    protected String propertyPath;

    public DynamicAttributesConditionCreator(String filterComponentName, CollectionDatasource datasource, String propertyPath) {
        super(RandomStringUtils.randomAlphabetic(10), filterComponentName, datasource);
        this.propertyPath = propertyPath;
        Messages messages = AppBeans.get(Messages.NAME);
        locCaption = messages.getMainMessage("filter.dynamicAttributeConditionCreator");
        showImmediately = true;
    }

    @Override
    public AbstractCondition createCondition() {
        return new DynamicAttributesCondition(this, entityAlias, propertyPath);
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