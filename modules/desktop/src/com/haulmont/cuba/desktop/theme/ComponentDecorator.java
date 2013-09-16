/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.theme;

import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public interface ComponentDecorator {
    /*
     * Accepts cuba component, if style is applied to it,
     * or awt component, if style applied via Table.StyleProvider
     */
    void decorate(Object component, Set<String> state);
}
