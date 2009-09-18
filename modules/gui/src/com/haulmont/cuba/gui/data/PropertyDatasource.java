/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.08.2009 16:25:54
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Datasource containing entity instance which is in fact a property of another entity instance.
 * <br>Usually defined in XML descriptor inside the parent datasource element.  
 * @param <T> type of enclosed entity
 */
public interface PropertyDatasource<T extends Entity> extends Datasource<T> {

    /** Bind to this property of the parent datasource */
    MetaProperty getProperty();
}
