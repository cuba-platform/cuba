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

package com.haulmont.cuba.security.entity;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Localized messages for security constraint.
 */
@Entity(name = "sec$LocalizedConstraintMessage")
@Table(name = "SEC_LOCALIZED_CONSTRAINT_MSG")
@SystemLevel
public class LocalizedConstraintMessage extends StandardEntity {

    protected static final String CAPTION_KEY = "caption";
    protected static final String MESSAGE_KEY = "message";

    @Column(name = "ENTITY_NAME", length = 255, nullable = false)
    protected String entityName;

    @Column(name = "OPERATION_TYPE", length = 50, nullable = false)
    protected String operationType;

    @Lob
    @Column(name = "VALUES_")
    protected String values;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public ConstraintOperationType getOperationType() {
        return ConstraintOperationType.fromId(operationType);
    }

    public void setOperationType(ConstraintOperationType operationType) {
        this.operationType = operationType != null ? operationType.getId() : null;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    @Nullable
    protected String getValue(String localeCode, String key) {
        Preconditions.checkNotNullArgument(localeCode);

        if (StringUtils.isEmpty(values)) {
            return null;
        }

        JSONObject localizationObject = new JSONObject(values);

        if (localizationObject.has(localeCode)) {
            JSONObject localeObject = localizationObject.getJSONObject(localeCode);
            return localeObject.has(key)
                    ? localeObject.getString(key)
                    : null;
        }

        return null;
    }

    protected void putValue(String localeCode, String key, String value) {
        Preconditions.checkNotNullArgument(localeCode);

        JSONObject localizationObject = values != null
                ? new JSONObject(values)
                : new JSONObject();

        JSONObject localeObject = localizationObject.has(localeCode)
                ? localizationObject.getJSONObject(localeCode)
                : new JSONObject();

        localeObject.put(key, value);
        localizationObject.put(localeCode, localeObject);

        setValues(localizationObject.toString());
    }

    /**
     * Returns caption value for given locale code.
     *
     * @param localeCode the locale code
     * @return caption value from all values for given locale code
     */
    @Nullable
    public String getLocalizedCaption(String localeCode) {
        return getValue(localeCode, CAPTION_KEY);
    }

    /**
     * Puts caption value with given locale code.
     *
     * @param localeCode the locale code
     * @param value      the value to add
     */
    public void putLocalizedCaption(String localeCode, String value) {
        putValue(localeCode, CAPTION_KEY, value);
    }

    /**
     * Returns message value for given locale code.
     *
     * @param localeCode the locale code
     * @return message value for given locale code
     */
    @Nullable
    public String getLocalizedMessage(String localeCode) {
        return getValue(localeCode, MESSAGE_KEY);
    }

    /**
     * Puts message value with given locale code.
     *
     * @param localeCode the locale code
     * @param value      the value to put
     */
    public void putLocalizedMessage(String localeCode, String value) {
        putValue(localeCode, MESSAGE_KEY, value);
    }
}
