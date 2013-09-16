/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * LookupPickerField adds to PickerField the ability to select an entity from drop-down list.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface LookupPickerField extends LookupField, PickerField {

    String NAME = "lookupPickerField";
}
