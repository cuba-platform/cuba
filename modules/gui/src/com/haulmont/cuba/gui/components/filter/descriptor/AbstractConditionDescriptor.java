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

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import org.dom4j.Element;

/**
 * Class that encapsulates common filter condition descriptor behaviour. Condition descriptors are used for
 * generating condition objects.
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$AbstractConditionDescriptor")
@SystemLevel
public abstract class AbstractConditionDescriptor extends BaseUuidEntity {

    private static final long serialVersionUID = 7975507640064754778L;

    protected Element element;
    protected String name;
    protected String caption;
    protected String locCaption;
    protected String filterComponentName;
    protected MetaClass datasourceMetaClass;
    protected String entityAlias;
    protected Boolean inExpr = false;
    protected Boolean showImmediately = false;
    protected String messagesPack;

    public AbstractConditionDescriptor(String name, String filterComponentName, MetaClass metaClass, String entityAlias) {
        this.name = name;
        this.filterComponentName = filterComponentName;
        this.datasourceMetaClass = metaClass;

        this.entityAlias = entityAlias;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    @MetaProperty
    public String getLocCaption() {
        return locCaption;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public String getFilterComponentName() {
        return filterComponentName;
    }

    public MetaClass getDatasourceMetaClass() {
        return datasourceMetaClass;
    }

    public Boolean isInExpr() {
        return inExpr;
    }

    public void setInExpr(Boolean inExpr) {
        this.inExpr = inExpr;
    }

    public Boolean isShowImmediately() {
        return showImmediately;
    }

    public String getOperatorType() {
        if (element != null)
            return element.attributeValue("operatorType", null);
        else return null;
    }

    public abstract AbstractCondition createCondition();

    public abstract Class getJavaClass();

    public abstract String getEntityParamWhere();

    public abstract String getEntityParamView();

    @MetaProperty
    public String getTreeCaption() {
        return getLocCaption();
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                '}';
    }
}