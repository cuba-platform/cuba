/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.autocomplete;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface AutoCompleteSupport {

    int getCursorPosition();

    Object getValue();
}