/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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