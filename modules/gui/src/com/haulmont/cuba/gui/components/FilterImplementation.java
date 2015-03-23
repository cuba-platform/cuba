/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

/**
 * The interface is implemented by generic filter components. It contains low-level methods
 * that generally should not be used in client code
 * @author gorbunkov
 * @version $Id$
 */
public interface FilterImplementation {
    void loadFiltersAndApplyDefault();
}
