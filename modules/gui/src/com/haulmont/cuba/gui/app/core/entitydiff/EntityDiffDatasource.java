/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.gui.data.impl.DatasourceImpl;

/**
 * Fake datasource for non-persistent entity EntityDiff
 *
 * @author artamonov
 * @version $Id$
 */
public class EntityDiffDatasource extends DatasourceImpl {

    @Override
    public boolean isModified() {
        return false;
    }
}
