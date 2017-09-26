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

package com.haulmont.restapi.service.filter;

import com.google.common.base.Strings;
import com.google.gson.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.QueryUtils;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.core.global.filter.OpManager;
import com.haulmont.cuba.core.global.filter.ParametersHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.*;

/**
 * Class for REST API search filter JSON parsing
 */
@Component(RestFilterParser.NAME)
public class RestFilterParser {

    public static final String NAME = "cuba_RestFilterParser";

    @Inject
    protected Metadata metadata;

    @Inject
    protected OpManager opManager;

    /**
     * Parses the JSON with entities filter and returns an object with JPQL query string and query parameters. The
     * method expects a JSON object like this:
     * <p>
     * <pre>
     * {
     *  "conditions": [
     *      {
     *          "group": "OR",
     *          "conditions": [
     *              {
     *                  "property": "stringField",
     *                  "operator": "&lt;&gt;",
     *                  "value": "stringValue"
     *              },
     *              {
     *                  "property": "intField",
     *                  "operator": "&gt;",
     *                  "value": 100
     *              }
     *          ]
     *      },
     *      {
     *          "property": "booleanField",
     *          "operator": "=",
     *          "value": true
     *      }
     *  ]
     * }
     * </pre>
     * <p>
     * Conditions here may be of two types: property condition and group condition (AND and OR) . Root conditions are
     * automatically placed to the group condition of type AND.
     */
    public RestFilterParseResult parse(String filterJson, MetaClass metaClass) throws RestFilterParseException {
        RestFilterGroupCondition rootCondition = new RestFilterGroupCondition();
        rootCondition.setType(RestFilterGroupCondition.Type.AND);

        JsonObject filterObject = new JsonParser().parse(filterJson).getAsJsonObject();
        JsonArray conditions = filterObject.get("conditions").getAsJsonArray();
        if (conditions != null) {
            for (JsonElement conditionElement : conditions) {
                JsonObject conditionObject = conditionElement.getAsJsonObject();
                RestFilterCondition restFilterCondition = parseConditionObject(conditionObject, metaClass);
                rootCondition.getConditions().add(restFilterCondition);
            }
        }

        Map<String, Object> queryParameters = new HashMap<>();
        collectQueryParameters(rootCondition, queryParameters);

        return new RestFilterParseResult(rootCondition.toJpql(), queryParameters);
    }

    protected void collectQueryParameters(RestFilterCondition condition, Map<String, Object> queryParameters) {
        if (condition instanceof RestFilterPropertyCondition) {
            //queryParamName can be empty, e.g. for notEmpty operator
            if (!Strings.isNullOrEmpty(((RestFilterPropertyCondition) condition).getQueryParamName())) {
                queryParameters.put(((RestFilterPropertyCondition) condition).getQueryParamName(), ((RestFilterPropertyCondition) condition).getValue());
            }
        } else if (condition instanceof RestFilterGroupCondition) {
            for (RestFilterCondition childCondition : ((RestFilterGroupCondition) condition).getConditions()) {
                collectQueryParameters(childCondition, queryParameters);
            }
        }
    }

    protected RestFilterCondition parseConditionObject(JsonObject jsonConditionObject, MetaClass metaClass) throws RestFilterParseException {
        JsonElement group = jsonConditionObject.get("group");
        if (group != null) {
            return parseGroupCondition(jsonConditionObject, metaClass);
        } else {
            return parsePropertyCondition(jsonConditionObject, metaClass);
        }

    }

    protected RestFilterGroupCondition parseGroupCondition(JsonObject conditionJsonObject, MetaClass metaClass) throws RestFilterParseException {
        JsonElement group = conditionJsonObject.get("group");
        String groupName = group.getAsString();
        RestFilterGroupCondition.Type type;
        try {
            type = RestFilterGroupCondition.Type.valueOf(groupName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RestFilterParseException("Invalid conditions group type: " + groupName);
        }
        RestFilterGroupCondition groupCondition = new RestFilterGroupCondition();
        groupCondition.setType(type);

        JsonElement conditions = conditionJsonObject.get("conditions");
        if (conditions != null) {
            for (JsonElement conditionElement : conditions.getAsJsonArray()) {
                RestFilterCondition childCondition = parseConditionObject(conditionElement.getAsJsonObject(), metaClass);
                groupCondition.getConditions().add(childCondition);
            }
        }

        return groupCondition;
    }

    protected RestFilterPropertyCondition parsePropertyCondition(JsonObject conditionJsonObject, MetaClass metaClass) throws RestFilterParseException {
        RestFilterPropertyCondition condition = new RestFilterPropertyCondition();

        JsonElement propertyJsonElem = conditionJsonObject.get("property");
        if (propertyJsonElem == null) {
            throw new RestFilterParseException("Field 'property' is not defined for filter condition");
        }
        String propertyName = propertyJsonElem.getAsString();

        JsonElement operatorJsonElem = conditionJsonObject.get("operator");
        if (operatorJsonElem == null) {
            throw new RestFilterParseException("Field 'operator' is not defined for filter condition");
        }
        String operator = operatorJsonElem.getAsString();
        Op op = findOperator(operator);

        boolean isValueRequired = op != Op.NOT_EMPTY;
        JsonElement valueJsonElem = conditionJsonObject.get("value");
        if (valueJsonElem == null && isValueRequired) {
            throw new RestFilterParseException("Field 'value' is not defined for filter condition");
        }

        MetaPropertyPath propertyPath = metaClass.getPropertyPath(propertyName);
        if (propertyPath == null) {
            throw new RestFilterParseException("Property for " + metaClass.getName() + " not found: " + propertyName);
        }
        MetaProperty metaProperty = propertyPath.getMetaProperty();

        EnumSet<Op> opsAvailableForJavaType = opManager.availableOps(metaProperty.getJavaType());
        if (!opsAvailableForJavaType.contains(op)) {
            throw new RestFilterParseException("Operator " + operator + " is not available for java type " +
                    metaProperty.getJavaType().getCanonicalName());
        }

        if (metaProperty.getRange().isClass()) {
            if (Entity.class.isAssignableFrom(metaProperty.getJavaType())) {
                MetaClass _metaClass = metadata.getClass(metaProperty.getJavaType());
                MetaProperty primaryKeyProperty = metadata.getTools().getPrimaryKeyProperty(_metaClass);
                String pkName = primaryKeyProperty.getName();
                propertyName += "." + pkName;
                propertyPath = metaClass.getPropertyPath(propertyName);
                if (propertyPath == null) {
                    throw new RestFilterParseException("Property " + propertyName + " for " + metaClass.getName() + " not found");
                }
                metaProperty = propertyPath.getMetaProperty();
            }
        }

        if (isValueRequired) {
            Object value = null;
            if (op == Op.IN || op == Op.NOT_IN) {
                if (!valueJsonElem.isJsonArray()) {
                    throw new RestFilterParseException("JSON array was expected as a value for condition with operator " + operator);
                }
                List<Object> parsedArrayValues = new ArrayList<>();
                for (JsonElement arrayItemElem : valueJsonElem.getAsJsonArray()) {
                    parsedArrayValues.add(parseValue(metaProperty, arrayItemElem.getAsString()));
                }
                value = parsedArrayValues;
            } else {
                value = parseValue(metaProperty, valueJsonElem.getAsString());
            }
            condition.setValue(transformValue(value, op));
            condition.setQueryParamName(generateQueryParamName());
        }

        condition.setPropertyName(propertyName);
        condition.setOperator(op);

        return condition;
    }

    protected Object parseValue(MetaProperty metaProperty, String stringValue) throws RestFilterParseException {
        if (metaProperty.getRange().isDatatype()) {
            try {
                return metaProperty.getRange().asDatatype().parse(stringValue);
            } catch (ParseException e) {
                throw new RestFilterParseException("Cannot parse property value: " + stringValue, e);
            }
        } else if (metaProperty.getRange().isEnum()) {
            try {
                return Enum.valueOf((Class<Enum>) metaProperty.getJavaType(), stringValue);
            } catch (IllegalArgumentException e) {
                throw new RestFilterParseException("Cannot parse enum value: " + stringValue, e);
            }
        }
        throw new RestFilterParseException("Cannot parse the condition value: " + stringValue);
    }

    protected Op findOperator(String stringOp) throws RestFilterParseException {
        switch (stringOp) {
            case "=":
            case ">":
            case ">=":
            case "<":
            case "<=":
            case "<>":
                return Op.fromJpqlString(stringOp);
            case "startsWith":
                return Op.STARTS_WITH;
            case "endsWith":
                return Op.ENDS_WITH;
            case "contains":
                return Op.CONTAINS;
            case "doesNotContain":
                return Op.DOES_NOT_CONTAIN;
            case "in":
                return Op.IN;
            case "notIn":
                return Op.NOT_IN;
            case "notEmpty":
                return Op.NOT_EMPTY;
        }
        throw new RestFilterParseException("Operator is not supported: " + stringOp);
    }

    protected Object transformValue(Object value, Op operator) {
        switch (operator) {
            case CONTAINS:
            case DOES_NOT_CONTAIN:
                return ParametersHelper.CASE_INSENSITIVE_MARKER + "%" + QueryUtils.escapeForLike((String) value) + "%";
            case STARTS_WITH:
                return ParametersHelper.CASE_INSENSITIVE_MARKER + QueryUtils.escapeForLike((String) value) + "%";
            case ENDS_WITH:
                return ParametersHelper.CASE_INSENSITIVE_MARKER + "%" + QueryUtils.escapeForLike((String) value);
        }

        return value;
    }

    protected String generateQueryParamName() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
