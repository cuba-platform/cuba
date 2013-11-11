/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

/**
 * Marker interface for swing components with manual focus ability
 *
 * @author Yuriy Artamonov
 * @version $Id$
 */
public interface FocusableComponent {

    void focus();
}