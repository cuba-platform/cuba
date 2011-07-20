/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
