/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.EmbeddableEntity;

/**
 * Datasource containing an embedded entity from a property of another entity instance.
 * <p>Usually defined in XML descriptor inside the parent datasource element.
 * @param <T> type of enclosed embeddable entity
 *
 * @author Artamonov
 * @version $Id$
 */
public interface EmbeddedDatasource<T extends EmbeddableEntity>
        extends NestedDatasource<T> {
}