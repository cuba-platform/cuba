/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.model;

import java.util.Map;

/**
 * Ancestor of main metadata objects: {@link com.haulmont.chile.core.model.MetaClass} and {@link com.haulmont.chile.core.model.MetaProperty}
 *
 * @author krivopustov
 * @version $Id$
 */
public interface MetadataObject {

    /**
     * MetadataObject unique name.
     */
    String getName();

    /**
     * MetadataObject annotations. Annotations here are simply name-value pairs, not correlated with Java annotations.
     */
    Map<String, Object> getAnnotations();
}