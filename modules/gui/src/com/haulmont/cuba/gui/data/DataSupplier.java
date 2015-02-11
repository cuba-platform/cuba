/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;

/**
 * Interface defining operations with entities on GUI level.
 *
 * @author abramov
 * @version $Id$
 */
public interface DataSupplier extends DataManager, DataService /* for backward compatibility */ {

    /**
     * Create a new entity instance
     * @param metaClass     entity MetaClass
     * @return              created instance
     */
    <A extends Entity> A newInstance(MetaClass metaClass);
}