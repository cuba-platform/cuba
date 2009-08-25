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

public interface PropertyDatasource<T extends Entity> extends Datasource<T> {

    MetaProperty getProperty();
}
