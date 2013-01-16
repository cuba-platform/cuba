/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.Entity;

/**
 * @author devyatkin
 * @version $Id$
 */
public interface RuntimePropsDatasource<T extends Entity> extends Datasource<T> {

    public enum PropertyType {
        STRING, INTEGER, DOUBLE, DATE, BOOLEAN, ENTITY, ENUMERATION
    }

    Datasource getMainDs();
}
