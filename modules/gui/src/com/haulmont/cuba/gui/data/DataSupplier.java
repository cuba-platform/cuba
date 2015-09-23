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
 * Interface for CRUD operations on screen level.
 *
 * <p>The standard implementation simply delegates to {@link DataManager}. A screen can define its implementation of
 * the DataSupplier in {@code dataSupplier} attribute of the {@code window} element.</p>
 *
 * <p>DataSupplier implementation can be injected to the screen controller by defining a field of {@code DataSupplier}
 * type annotated with {@code @Inject}.</p>
 *
 * @author abramov
 * @version $Id$
 */
public interface DataSupplier extends DataManager, DataService /* for backward compatibility */ {

    /**
     * Do not try to obtain DataSupplier through {@code AppBeans.get()} or by injection to regular Spring beans.
     * Only injection to screens works.
     */
    String NAME = "ERROR: DataSupplier is not a Spring bean";

    /**
     * Create a new entity instance
     * @param metaClass     entity MetaClass
     * @return              created instance
     */
    <A extends Entity> A newInstance(MetaClass metaClass);
}