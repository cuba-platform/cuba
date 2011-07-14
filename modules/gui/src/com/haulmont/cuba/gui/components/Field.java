/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 16:33:50
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.ValueListener;

import java.io.Serializable;

public interface Field
    extends
        DatasourceComponent, Component.HasCaption, Component.HasValue, Component.Editable,
        Component.BelongToFrame, Component.Expandable, Component.HasValidState
{

    boolean isRequired();
    void setRequired(boolean required);
    void setRequiredMessage(String msg);

    /**
     * Use listeners on the component only if the component is not linked to a datasource.<br>
     * Otherwise use {@link ValueListener} bound to the datasource.
     */
    void addListener(ValueListener listener);
    void removeListener(ValueListener listener);

    void addValidator(Validator validator);
    void removeValidator(Validator validator);

    interface Validator extends Serializable {
        void validate(Object value) throws ValidationException;
    }
}
