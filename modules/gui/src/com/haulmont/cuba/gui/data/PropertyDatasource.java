/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

/**
 * Datasource containing an entity instance which is loaded as a non-embedded property of another entity instance.
 * <p> Usually defined in XML descriptor inside the parent datasource element.
 * @param <T> type of enclosed entity
 *
 * @author Krivopustov
 * @version $Id$
 */
public interface PropertyDatasource<T extends Entity>
        extends NestedDatasource<T> {
}
