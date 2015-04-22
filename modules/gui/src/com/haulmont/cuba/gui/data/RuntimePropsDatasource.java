/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * @author devyatkin
 * @version $Id$
 */
public interface RuntimePropsDatasource<T extends Entity> extends Datasource<T> {

    Datasource getMainDs();

    Collection<MetaProperty> getPropertiesFilteredByCategory();
}
