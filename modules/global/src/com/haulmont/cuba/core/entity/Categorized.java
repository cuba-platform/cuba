/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.entity;

/**
 * Base interface for entities with fixed category
 * Such entities can be used in RuntimePropertiesFrame
 *
 * @author degtyarjov
 * @version $Id$
 */
public interface Categorized {
    Category getCategory();

    void setCategory(Category category);
}
