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

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.core.global.filter.OpManager;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.FilterConditionUtils;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static org.apache.commons.lang3.StringUtils.isBlank;

@com.haulmont.chile.core.annotations.MetaClass(name = "sec$PropertyConditionDescriptor")
@SystemLevel
public class PropertyConditionDescriptor extends AbstractConditionDescriptor {
    protected String entityParamWhere;
    protected String entityParamView;

    public PropertyConditionDescriptor(String name,
                                       @Nullable String caption,
                                       String messagesPack,
                                       String filterComponentName,
                                       MetaClass metaClass,
                                       String entityAlias) {
        super(name, filterComponentName, metaClass, entityAlias);
        this.caption = caption;
        this.messagesPack = messagesPack;
        Messages messages = AppBeans.get(Messages.NAME);

        if (!isBlank(caption)) {
            this.locCaption = messages.getTools().loadString(messagesPack, caption);
        } else {
            this.locCaption = FilterConditionUtils.getPropertyLocCaption(datasourceMetaClass, name);
        }
    }

    public PropertyConditionDescriptor(Element element, String messagesPack, String filterComponentName,
                                       MetaClass metaClass, String entityAlias) {
        this(element.attributeValue("name"),
                element.attributeValue("caption"),
                messagesPack,
                filterComponentName,
                metaClass,
                entityAlias);
        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
        entityParamWhere = element.attributeValue("paramWhere");
        entityParamView = element.attributeValue("paramView");
    }

    @Override
    public Class getJavaClass() {
        MetaProperty metaProperty = getMetaProperty();
        if (metaProperty != null) {
            return metaProperty.getJavaType();
        }
        throw new IllegalStateException(String.format("Unable to find property '%s' in entity %s", name, datasourceMetaClass));
    }

    @Override
    public String getEntityParamWhere() {
        return entityParamWhere;
    }

    @Override
    public String getEntityParamView() {
        return entityParamView;
    }

    @Override
    public AbstractCondition createCondition() {
        OpManager opManager = AppBeans.get(OpManager.class);
        MetadataTools metadataTools = AppBeans.get(MetadataTools.class);

        PropertyCondition propertyCondition = new PropertyCondition(this, entityAlias);
        MetaPropertyPath propertyPath = datasourceMetaClass.getPropertyPath(name);
        if (propertyPath == null) {
            throw new IllegalStateException(String.format("Unable to find property '%s' in entity %s", name, datasourceMetaClass));
        }
        MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(propertyPath);
        EnumSet<Op> ops = opManager.availableOps(propertyMetaClass, propertyPath.getMetaProperty());
        propertyCondition.setOperator(ops.iterator().next());
        return propertyCondition;
    }

    @Override
    public String getTreeCaption() {
        if (!Strings.isNullOrEmpty(this.caption)) {
            return locCaption;
        } else {
            MessageTools messageTools = AppBeans.get(MessageTools.class);
            MetaPropertyPath mpp = datasourceMetaClass.getPropertyPath(name);
            return mpp != null ? messageTools.getPropertyCaption(datasourceMetaClass, name) : name;
        }
    }

    @Nullable
    public MetaProperty getMetaProperty() {
        MetaPropertyPath propertyPath = datasourceMetaClass.getPropertyPath(name);
        return propertyPath != null ? propertyPath.getMetaProperty() : null;
    }

    public String getEntityAlias(){
        return entityAlias;
    }
}