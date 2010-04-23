/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Gennady Pavlov
 * Created: 12.04.2010 10:15:17
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface ActionsField extends LookupField, Component.ActionsHolder{
    static String DROPDOWN = "dropdown";
    static String LOOKUP = "lookup";
    static String OPEN = "open";

    void addButton(Button button);

    void enableButton(String buttonId, boolean enable);
}
