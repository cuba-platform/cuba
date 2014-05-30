/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CellProvider<E extends Entity> {

    @Nullable
    Component generateCell(E entity, MetaPropertyPath propertyPath);
}