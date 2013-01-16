/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Datasource containing entity instance which is loaded as a property of another entity instance.
 * <p>Usually defined in XML descriptor inside the parent datasource element.
 * @param <T> type of enclosed entity
 *
 * @author Artamonov
 * @version $Id$
 */
public interface NestedDatasource<T extends Entity> extends Datasource<T> {

    /**
     * Setup the datasource right after creation.
     * This method should be called only once.
     *
     * @param id        datasource ID
     * @param masterDs  master datasource
     * @param property  property of the master datasource to bound this datasource to
     * @throws UnsupportedOperationException    if an implementation doesn't support this method
     */
    void setup(String id, Datasource masterDs, String property) throws UnsupportedOperationException;

    /**
     * @return Master datasource.
     */
    Datasource getMaster();

    /**
     * @return Property of the master datasource which this datasource is bound to.
     */
    MetaProperty getProperty();
}
