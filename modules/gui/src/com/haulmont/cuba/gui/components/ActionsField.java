/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * DEPRECATED! Use {@link LookupPickerField}
 *
 */
@Deprecated
public interface ActionsField extends LookupField, Component.ActionsHolder{

    String NAME = "actionsField";

    static String DROPDOWN = "dropdown";
    static String LOOKUP = "lookup";
    static String OPEN = "open";

    void addButton(Button button);

    void enableButton(String buttonId, boolean enable);
}
