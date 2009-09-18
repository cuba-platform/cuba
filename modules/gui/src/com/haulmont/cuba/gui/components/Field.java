/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 16:33:50
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface Field
    extends
        DatasourceComponent, Component.HasCaption, Component.HasValue, Component.Editable,
        Component.BelongToFrame, Component.Expandable
{

    boolean isRequired();
    void setRequired(boolean required);
    void setRequiredMessage(String msg);

    void addValidator(Validator validator);
    void removeValidator(Validator validator);

    boolean isValid();
    void validate() throws ValidationException;

    interface Validator {
        void validate(Object value) throws ValidationException;
    }
}
