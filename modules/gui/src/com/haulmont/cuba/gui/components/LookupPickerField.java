/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * LookupPickerField adds to PickerField the ability to select an entity from drop-down list.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface LookupPickerField extends LookupField, PickerField {

    String NAME = "lookupPickerField";
}
