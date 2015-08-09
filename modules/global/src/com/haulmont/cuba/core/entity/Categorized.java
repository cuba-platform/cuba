/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

/**
 * Interface to be implemented by entities which can be separated by categories and hence have several sets of
 * dynamic attributes.
 * Such entities can be displayed in RuntimePropertiesFrame.
 *
 * @author degtyarjov
 * @version $Id$
 */
public interface Categorized {

    Category getCategory();

    void setCategory(Category category);
}
