/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.DatasourceListener;

/**
 * Interface for datasource listeners that should be removed if listener owner died.
 *
 * @author artamonov
 * @version $Id$
 */
public interface WeakDatasourceListener extends DatasourceListener {

    boolean isAlive();
}