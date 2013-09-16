/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

/**
 * <p>$Id$</p>
 *
 * Returns, whether component is trying to gain all available space by default,
 * when size isn't set explicitly.
 * Components not implementing this interface, considered non-expanding by default.
 *
 * In vaadin, this logic is built into client GWT side of components.
 *
 * @author Alexander Budarov
 */
public interface AutoExpanding {
    boolean expandsWidth();
    boolean expandsHeight();
}
