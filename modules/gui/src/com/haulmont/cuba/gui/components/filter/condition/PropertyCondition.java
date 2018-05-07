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

package com.haulmont.cuba.gui.components.filter.condition;

import com.google.common.base.Strings;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.ConditionType;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.gui.components.filter.ConditionParamBuilder;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.dateinterval.DateIntervalValue;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.operationedit.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.operationedit.PropertyOperationEditor;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MetaClass(name = "sec$PropertyCondition")
@SystemLevel
public class PropertyCondition extends AbstractCondition {

    private static Pattern PATTERN = Pattern.compile("\\s*(\\S+)\\s+((?:not\\s+)*\\S+)\\s+(\\S+)\\s*(?:ESCAPE '\\S+')?\\s*");
    private static Pattern PATTERN_NOT_IN = Pattern.compile("\\s*[(]\\s*[(]\\s*(\\S+)\\s+((:not\\s+)*\\S+)\\s+(\\S+)[\\S\\s]*");
    private static Pattern PATTERN_NULL = Pattern.compile("\\s*(\\S+)\\s+(is\\s+(?:not\\s+)?null)\\s*");

    public PropertyCondition(PropertyCondition condition) {
        super(condition);
        this.operator = condition.operator;
    }

    public PropertyCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);

        String text = element.getText();
        if (operator != Op.DATE_INTERVAL) {
            Matcher matcher = PATTERN_NULL.matcher(text);
            if (!matcher.matches()) {
                matcher = PATTERN_NOT_IN.matcher(text);
                if (!matcher.matches()) {
                    matcher = PATTERN.matcher(text);
                }
                if (!matcher.matches()) {
                    throw new IllegalStateException("Unable to build condition from: " + text);
                }
            }

            if (operator == null) {
                operator = Op.fromJpqlString(matcher.group(2));
            }

            String prop = matcher.group(1);
            entityAlias = prop.substring(0, prop.indexOf('.'));
        } else {
            entityAlias = "{E}";
            param.setDateInterval(true);
        }
    }

    @SuppressWarnings("unchecked")
    public PropertyCondition(AbstractConditionDescriptor descriptor, String entityAlias) {
        super(descriptor);
        this.entityAlias = entityAlias;
    }

    @Override
    protected void updateText() {

        Metadata metadata = AppBeans.get(Metadata.class);
        MetadataTools metadataTools = metadata.getTools();

        String nameToUse = name;
        boolean useCrossDataStoreRefId = false;
        String thisStore = metadataTools.getStoreName(datasource.getMetaClass());
        MetaPropertyPath propertyPath = datasource.getMetaClass().getPropertyPath(name);
        if (propertyPath != null) {
            String refIdProperty = metadataTools.getCrossDataStoreReferenceIdProperty(thisStore, propertyPath.getMetaProperty());
            if (refIdProperty != null) {
                useCrossDataStoreRefId = true;
                int lastdDot = nameToUse.lastIndexOf('.');
                if (lastdDot == -1) {
                    nameToUse = refIdProperty;
                } else {
                    nameToUse = nameToUse.substring(0, lastdDot + 1) + refIdProperty;
                }
            }
        }

        if (operator == Op.DATE_INTERVAL) {
            text = dateIntervalConditionToJpql(nameToUse);
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (operator == Op.NOT_IN) {
            sb.append("((");
        }
        sb.append(entityAlias).append(".").append(nameToUse);

        if (Param.Type.ENTITY == param.getType() && !useCrossDataStoreRefId) {
            com.haulmont.chile.core.model.MetaClass metaClass = metadata.getClassNN(param.getJavaClass());
            String primaryKeyName = metadataTools.getPrimaryKeyName(metaClass);
            sb.append(".").append(primaryKeyName);
        }

        if (operator != Op.NOT_EMPTY)
            sb.append(" ").append(operator.forJpql());

        if (!operator.isUnary()) {
            sb.append(" :").append(param.getName());

            if (operator == Op.ENDS_WITH || operator == Op.STARTS_WITH
                    || operator == Op.CONTAINS || operator == Op.DOES_NOT_CONTAIN) {
                GlobalConfig config = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);
                if (config.getDisableEscapingLikeForDataStores() == null || !config.getDisableEscapingLikeForDataStores().contains(thisStore)) {
                    sb.append(" ESCAPE '").append(QueryUtils.ESCAPE_CHARACTER).append("' ");
                }
            }

            if (operator == Op.NOT_IN) {
                sb.append(") or (").append(entityAlias).append(".").append(nameToUse).append(" is null)) ");
            }
        }

        if (operator == Op.NOT_EMPTY) {
            sb.append(BooleanUtils.isTrue((Boolean) param.getValue()) ? " is not null" : " is null");
        }

        text = sb.toString();
    }

    protected String dateIntervalConditionToJpql(String propertyName) {
        if (param.getValue() == null) return null;
        DateIntervalValue filterDateIntervalValue = AppBeans.getPrototype(DateIntervalValue.NAME, (String) param.getValue());
        return filterDateIntervalValue.toJPQL(propertyName);
    }

    public String getOperatorType() {
        return operator.name();
    }

    @Override
    public void toXml(Element element, Param.ValueProperty valueProperty) {
        super.toXml(element, valueProperty);
        element.addAttribute("type", ConditionType.PROPERTY.name());
        element.addAttribute("operatorType", getOperatorType());
    }

    @Override
    public void setOperator(Op operator) {
        if (!Objects.equals(this.operator, operator)) {
            this.operator = operator;
            String paramName = param.getName();
            ConditionParamBuilder paramBuilder = AppBeans.get(ConditionParamBuilder.class);
            if (operator.isUnary()) {
                unary = true;
                inExpr = false;
                Param param = Param.Builder.getInstance()
                        .setName(paramName)
                        .setJavaClass(Boolean.class)
                        .setInExpr(false)
                        .setRequired(required)
                        .build();
                setParam(param);
            } else {
                unary = false;
                inExpr = operator.equals(Op.IN) || operator.equals(Op.NOT_IN);
                Param param = paramBuilder.createParam(this);
                if (operator == Op.DATE_INTERVAL) {
                    //each parameter value change must modify the condition text
                    param.addValueChangeListener((prevValue, value) -> updateText());
                }
                setParam(param);
            }
        }
    }

    @Override
    public String getLocCaption() {
        if (Strings.isNullOrEmpty(caption)) {
            return getPropertyLocCaption();
        } else {
            MessageTools messageTools = AppBeans.get(MessageTools.class);
            return messageTools.loadString(messagesPack, caption);
        }
    }

    public String getPropertyLocCaption() {
        return FilterConditionUtils.getPropertyLocCaption(datasource.getMetaClass(), name);
    }

    @Override
    public String getOperationCaption() {
        return operator.getLocCaption();
    }

    @Override
    public AbstractOperationEditor createOperationEditor() {
        operationEditor = new PropertyOperationEditor(this);
        return operationEditor;
    }

    @Override
    public AbstractCondition createCopy() {
        return new PropertyCondition(this);
    }
}