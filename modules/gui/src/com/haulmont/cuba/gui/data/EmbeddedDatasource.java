/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 28.03.11 16:08
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.EmbeddableEntity;

/**
 * Datasource containing embeddable entity instance which is in fact a property of another entity instance.
 * Entity is embeddable and stored to parent table
 * <br>Usually defined in XML descriptor inside the parent datasource element.
 * @param <T> type of enclosed embeddable
 */
public interface EmbeddedDatasource<T extends EmbeddableEntity> extends NestedDatasource<T>{

}