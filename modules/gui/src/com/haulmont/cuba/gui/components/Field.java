/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 16:33:50
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.chile.core.model.MetaProperty;

public interface Field
    extends
        Component, Component.HasCaption, Component.Field, Component.Editable,
        Component.BelongToFrame, Component.Expandable
{

    boolean isRequired();
    void setRequired(boolean required);
    void setRequiredMessage(String msg);

    Datasource getDatasource();
    MetaProperty getMetaProperty();

    void setDatasource(Datasource datasource, String property);

    void addValidator(Validator validator);
    void removeValidator(Validator validator);

    boolean isValid();
    void validate() throws ValidationException;

    interface Validator {
        boolean isValid(Object value);
        void validate(Object value) throws ValidationException;
    }
}
