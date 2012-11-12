/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * SearchPickerField adds to PickerField the ability to search an entity.
 *
 * @author artamonov
 * @version $Id$
 */
public interface SearchPickerField extends SearchField, PickerField {

    String NAME = "searchPickerField";
}