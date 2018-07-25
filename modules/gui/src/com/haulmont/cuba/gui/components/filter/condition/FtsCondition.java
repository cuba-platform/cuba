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

package com.haulmont.cuba.gui.components.filter.condition;

import com.google.common.base.Strings;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.filter.ConditionType;
import com.haulmont.cuba.gui.components.filter.FtsFilterHelper;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.descriptor.FtsConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.operationedit.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.operationedit.FtsOperationEditor;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Element;

/**
 * An FTS condition that is used in regular (not FTS) filter
 */
@MetaClass(name = "sec$FtsCondition")
@SystemLevel
public class FtsCondition extends AbstractCondition {

    protected String queryKeyParamName;
    protected String sessionIdParamName;

    public FtsCondition(FtsCondition other) {
        super(other);
        this.queryKeyParamName = other.queryKeyParamName;
        this.sessionIdParamName = other.sessionIdParamName;
    }

    @Override
    public AbstractOperationEditor createOperationEditor() {
        return new FtsOperationEditor(this);
    }

    public FtsCondition(FtsConditionDescriptor descriptor) {
        super(descriptor);
        if (AppBeans.containsBean(FtsFilterHelper.NAME)) {
            FtsFilterHelper ftsFilterHelper = AppBeans.get(FtsFilterHelper.class);
            this.queryKeyParamName = generateQueryKeyParamName();
            this.sessionIdParamName = generateSessionIdParamName();
            this.text = ftsFilterHelper.createFtsWhereClause(metaClass.getName(),
                    queryKeyParamName, sessionIdParamName);
        }
    }

    public FtsCondition(Element element, String messagesPack, String filterComponentName, com.haulmont.chile.core.model.MetaClass metaClass) {
        super(element, messagesPack, filterComponentName, metaClass);
        queryKeyParamName = element.attributeValue("queryKeyParamName");
        sessionIdParamName = element.attributeValue("sessionIdParamName");
    }

    @Override
    public AbstractCondition createCopy() {
        return new FtsCondition(this);
    }

    @Override
    public String getLocCaption() {
        if (Strings.isNullOrEmpty(caption)) {
            Messages messages = AppBeans.get(Messages.class);
            return messages.getMainMessage("filter.ftsCondition.caption");
        } else {
            MessageTools messageTools = AppBeans.get(MessageTools.class);
            return messageTools.loadString(messagesPack, caption);
        }
    }

    @Override
    public void toXml(Element element, Param.ValueProperty valueProperty) {
        super.toXml(element, valueProperty);
        element.addAttribute("type", ConditionType.FTS.name());
        element.addAttribute("queryKeyParamName", queryKeyParamName);
        element.addAttribute("sessionIdParamName", sessionIdParamName);
    }

    public String getQueryKeyParamName() {
        //for backward compatibility with v 6.7 a default param name may be returned, if filter xml doesn't contain it
        return queryKeyParamName != null ? queryKeyParamName : FtsFilterHelper.QUERY_KEY_PARAM_NAME;
    }

    public String getSessionIdParamName() {
        //for backward compatibility with v 6.7 a default param name may be returned, if filter xml doesn't contain it
        return sessionIdParamName != null ? sessionIdParamName : FtsFilterHelper.SESSION_ID_PARAM_NAME;
    }

    protected String generateQueryKeyParamName() {
        return "__queryKey" + RandomStringUtils.randomAlphanumeric(6);
    }

    protected String generateSessionIdParamName() {
        return "__sessionId" + RandomStringUtils.randomAlphanumeric(6);
    }
}
