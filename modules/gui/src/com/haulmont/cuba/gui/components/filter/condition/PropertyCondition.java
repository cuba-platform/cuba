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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaClass;
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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
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

    protected String propertiesPath;

    protected String join;

    public PropertyCondition(PropertyCondition condition) {
        super(condition);
        this.operator = condition.operator;
        this.join = condition.join;
    }

    public PropertyCondition(Element element, String messagesPack, String filterComponentName, com.haulmont.chile.core.model.MetaClass metaClass) {
        super(element, messagesPack, filterComponentName, metaClass);

        String text = element.getText();
        this.propertiesPath = element.attributeValue("propertiesPath");
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

            //if it hasn't been read in a superclass constructor from the XML attribute
            if (Strings.isNullOrEmpty(entityAlias)) {
                entityAlias = prop.substring(0, prop.indexOf('.'));
            }
        } else {
            entityAlias = "{E}";
            param.setDateInterval(true);
        }

        Element joinElement = element.element("join");
        if (joinElement != null) {
            this.join = joinElement.getText();
        }
    }

    public PropertyCondition(AbstractConditionDescriptor descriptor, String entityAlias) {
        this(descriptor, entityAlias, null);
    }

    public PropertyCondition(AbstractConditionDescriptor descriptor, String entityAlias, String propertiesPath) {
        super(descriptor);
        this.entityAlias = entityAlias;
        this.propertiesPath = propertiesPath;
    }

    @Override
    protected void updateText() {
        Metadata metadata = AppBeans.get(Metadata.class);
        MetadataTools metadataTools = metadata.getTools();

        String nameToUse = !Strings.isNullOrEmpty(propertiesPath) ? propertiesPath : name;
        boolean useCrossDataStoreRefId = false;
        boolean stringType = false;
        String thisStore = metadataTools.getStoreName(metaClass);
        MetaPropertyPath propertyPath = metaClass.getPropertyPath(name);
        if (propertyPath != null) {
            MetaProperty metaProperty = propertyPath.getMetaProperty();
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
            stringType = String.class.equals(metaProperty.getJavaType());
        }

        if (operator == Op.DATE_INTERVAL) {
            text = dateIntervalConditionToJpql(nameToUse);
            return;
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder sbJoin = new StringBuilder();
        if (operator == Op.NOT_IN) {
            sb.append("((");
        }

        String path = (metaClass instanceof KeyValueMetaClass && Strings.isNullOrEmpty(propertiesPath)) ?
            entityAlias :
            entityAlias + "." + nameToUse;

        String joinAlias = nameToUse.replace(".", "_")
                + RandomStringUtils.randomAlphabetic(5);

        if (Param.Type.ENTITY == param.getType() && !useCrossDataStoreRefId) {
            String primaryKeyName = metadataTools.getPrimaryKeyName(metadata.getClassNN(param.getJavaClass()));
            if (operator == Op.NOT_IN) {
                sbJoin.append("left join ").append(path).append(" ").append(joinAlias);
                sb.append(joinAlias).append(".").append(primaryKeyName);
            } else {
                sb.append(path).append(".").append(primaryKeyName);
            }
        } else {
            sb.append(path);
        }

        if (operator != Op.NOT_EMPTY) {
            PersistenceManagerClient persistenceManager = AppBeans.get(PersistenceManagerClient.class);
            if (operator == Op.EQUAL && stringType && persistenceManager.emulateEqualsByLike(thisStore)) {
                sb.append(" ").append(Op.CONTAINS.forJpql());
            } else if (operator == Op.NOT_EQUAL && stringType && persistenceManager.emulateEqualsByLike(thisStore)) {
                sb.append(" ").append(Op.DOES_NOT_CONTAIN.forJpql());
            } else {
                sb.append(" ").append(operator.forJpql());
            }
        }

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
                if (Param.Type.ENTITY == param.getType() && !useCrossDataStoreRefId) {
                    String primaryKeyName = metadataTools.getPrimaryKeyName(metadata.getClassNN(param.getJavaClass()));
                    sb.append(") or (").append(joinAlias).append(".").append(primaryKeyName).append(" is null)) ");
                } else {
                    sb.append(") or (").append(path).append(" is null)) ");
                }
            }
        }

        if (operator == Op.NOT_EMPTY) {
            sb.append(BooleanUtils.isTrue((Boolean) param.getValue()) ? " is not null" : " is null");
        }

        text = sb.toString();
        join = sbJoin.toString();
    }

    protected String dateIntervalConditionToJpql(String propertyName) {
        if (param.getValue() == null) return null;
        DateIntervalValue filterDateIntervalValue = AppBeans.getPrototype(DateIntervalValue.NAME, param.getValue());
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

        if (metaClass instanceof KeyValueMetaClass){
            element.addAttribute("entityAlias", entityAlias);
            element.addAttribute("propertiesPath", propertiesPath);
        }

        if (!Strings.isNullOrEmpty(join)) {
            Element joinElement = element.addElement("join");
            joinElement.addCDATA(join);
        }
    }

    @Override
    public void setOperator(Op operator) {
        if (!Objects.equals(this.operator, operator)) {
            this.operator = operator;
            String paramName = param.getName();
            ConditionParamBuilder paramBuilder = AppBeans.get(ConditionParamBuilder.class);
            unary = false;
            if (operator.isUnary()) {
                inExpr = false;
                Param param = Param.Builder.getInstance()
                        .setName(paramName)
                        .setJavaClass(Boolean.class)
                        .setInExpr(false)
                        .setRequired(required)
                        .build();
                setParam(param);
            } else {
                inExpr = operator.equals(Op.IN) || operator.equals(Op.NOT_IN);
                Param param = paramBuilder.createParam(this);
                if (operator == Op.DATE_INTERVAL) {
                    // each parameter value change must modify the condition text
                    param.addValueChangeListener(event -> updateText());
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
        return FilterConditionUtils.getPropertyLocCaption(metaClass, name);
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