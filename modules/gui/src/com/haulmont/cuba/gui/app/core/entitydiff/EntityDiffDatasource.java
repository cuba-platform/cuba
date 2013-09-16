/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
