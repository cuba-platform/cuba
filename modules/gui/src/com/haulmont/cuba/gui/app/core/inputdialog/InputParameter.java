/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.app.core.inputdialog;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Field;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;
import java.util.function.Supplier;

/**
 * Describes field that can be used in {@link InputDialog}.
 */
public class InputParameter {

    protected String id;
    protected String caption;
    protected boolean required;
    protected Datatype datatype;
    protected Supplier<Field> field;
    protected Object defaultValue;
    protected Class<? extends Entity> entityClass;

    protected Class datatypeJavaClass;

    /**
     * @param id field id
     */
    public InputParameter(String id) {
        Preconditions.checkNotNullArgument(id);

        this.id = id;
    }

    /**
     * @return field id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets field id.
     *
     * @param id field id
     * @return input parameter
     */
    public InputParameter withId(String id) {
        Preconditions.checkNotNullArgument(id);

        this.id = id;
        return this;
    }

    /**
     * @return field caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets caption to the field.
     *
     * @param caption caption
     * @return input parameter
     */
    public InputParameter withCaption(String caption) {
        this.caption = caption;
        return this;
    }

    /**
     * @return true if field is required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets required for the field.
     *
     * @param required required option
     * @return input parameter
     */
    public InputParameter withRequired(boolean required) {
        this.required = required;
        return this;
    }

    /**
     * @return field Datatype
     */
    public Datatype getDatatype() {
        return datatype;
    }

    /**
     * Sets datatype to the field. Cannot be used with {@link #withEntityClass(Class)} and with predefined static methods.
     * <p>
     * Note, it doesn't support custom Datatype. Use {@link #withField(Supplier)}.
     *
     * @param datatype datatype
     * @return input parameter
     */
    public InputParameter withDatatype(Datatype datatype) {
        checkNullEntityClass("InputParameter cannot contain Datatype and entity class at the same time");
        checkNullDatatypeJavaClass("Datatype cannot be used with a parameter that has already data type");

        this.datatype = datatype;
        return this;
    }

    protected InputParameter withDatatypeJavaClass(Class javaClass) {
        this.datatypeJavaClass = javaClass;
        return this;
    }

    protected Class getDatatypeJavaClass() {
        return datatypeJavaClass;
    }

    /**
     * @return field supplier
     */
    public Supplier<Field> getField() {
        return field;
    }

    /**
     * Sets field supplier.
     * <p>
     * Note, in order to get value from this field you must use an id that is set to the InputParameter, not to the
     * created field.
     *
     * @param field supplier
     * @return input parameter
     */
    public InputParameter withField(Supplier<Field> field) {
        this.field = field;
        return this;
    }

    /**
     * @return default value
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets default value to the field.
     *
     * @param defaultValue default value
     * @return input parameter
     */
    public InputParameter withDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * @return entity class
     */
    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    /**
     * Sets entity class. Cannot be used with {@link #withDatatype(Datatype)} and with predefined static methods.
     *
     * @param entityClass entity class
     * @return input parameter
     */
    public InputParameter withEntityClass(Class<? extends Entity> entityClass) {
        checkNullDatatype("InputParameter cannot contain entity class and Datatype at the same time");
        checkNullDatatypeJavaClass("Entity class cannot be used with a parameter that has data type");

        this.entityClass = entityClass;
        return this;
    }

    /**
     * Creates parameter with String type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter parameter(String id) {
        return new InputParameter(id);
    }

    /**
     * Creates parameter with String type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter stringParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(String.class);
    }

    /**
     * Creates parameter with Integer type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter intParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(Integer.class);
    }

    /**
     * Creates parameter with Double type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter doubleParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(Double.class);
    }

    /**
     * Creates parameter with BigDecimal type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter bigDecimalParamater(String id) {
        return new InputParameter(id).withDatatypeJavaClass(BigDecimal.class);
    }

    /**
     * Creates parameter with Long type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter longParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(Long.class);
    }

    /**
     * Creates parameter with Date type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter dateParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(java.sql.Date.class);
    }

    /**
     * Creates parameter with Time type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter timeParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(Time.class);
    }

    /**
     * Creates parameter with DateTime type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter dateTimeParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(Date.class);
    }

    /**
     * Creates parameter with Entity type.
     *
     * @param id          field id
     * @param entityClass entity class
     * @return input parameter
     */
    public static InputParameter entityParameter(String id, Class<? extends Entity> entityClass) {
        return new InputParameter(id).withEntityClass(entityClass);
    }

    /**
     * Creates parameter with Boolean type.
     *
     * @param id field id
     * @return input parameter
     */
    public static InputParameter booleanParameter(String id) {
        return new InputParameter(id).withDatatypeJavaClass(Boolean.class);
    }


    protected void checkNullDatatype(String message) {
        if (datatype != null) {
            throw new IllegalStateException(message);
        }
    }

    protected void checkNullEntityClass(String message) {
        if (entityClass != null) {
            throw new IllegalStateException(message);
        }
    }

    protected void checkNullDatatypeJavaClass(String message) {
        if (datatypeJavaClass != null) {
            throw new IllegalStateException(message);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        InputParameter inputParameter = (InputParameter) obj;
        return id.equals(inputParameter.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
