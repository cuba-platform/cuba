/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a reference as composition.
 * Composition implyes ownership, that is the referenced object exists only as part of the owning entity.
 * <p>An attribute marked with this annotation yields the {@link com.haulmont.chile.core.model.MetaProperty}
 * of type {@link com.haulmont.chile.core.model.MetaProperty.Type#COMPOSITION}</p>
 *
 * @author abramov
 * @version $Id$
 */
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Composition {
}