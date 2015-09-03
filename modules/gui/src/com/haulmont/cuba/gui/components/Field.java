/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * Base interface for "fields" - components intended to display and edit value of a certain entity attribute.
 *
 * @author abramov
 * @version $Id$
 */
public interface Field extends DatasourceComponent, Component.HasCaption, Component.HasValue, Component.Editable,
                               Component.BelongToFrame, Component.Validatable {
    /**
     * @return whether the field must contain a non-null value
     */
    boolean isRequired();
    void setRequired(boolean required);

    /**
     * A message that will be displayed to user if the field is required but has null value
     */
    void setRequiredMessage(String msg);
    String getRequiredMessage();

    /**
     * Add {@link Validator} instance.
     */
    void addValidator(Validator validator);
    void removeValidator(Validator validator);

    /**
     * Field validator.<br>
     * Validators are invoked when {@link Validatable#validate()} is called.
     * Editor screen calls {@code validate()} on commit.
     */
    interface Validator {
        /**
         * @param value field value to validate
         * @throws ValidationException this exception must be thrown by the validator if the value is not valid
         */
        void validate(Object value) throws ValidationException;
    }
}