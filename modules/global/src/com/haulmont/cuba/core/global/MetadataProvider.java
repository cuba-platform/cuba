/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 11:57:54
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.sys.AppContext;

import java.util.Map;

/**
 * Utility class to provide metadata functionality in static context.<br>
 * <p>Injected {@link Metadata} interface should be used instead of this class wherever possible.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class MetadataProvider
{
    private static Metadata getMetadata() {
        return AppContext.getApplicationContext().getBean(Metadata.NAME, Metadata.class);
    }

    /**
     * Get current metadata session
     * @return metadata session instance
     */
    public static Session getSession() {
        return getMetadata().getSession();
    }

    /**
     * Get the view repository
     * @return view repository instance
     */
    public static ViewRepository getViewRepository() {
        return getMetadata().getViewRepository();
    }

    /**
     * Get the entity classes replacement map defined in metadata configuration
     * @return entity classes replacement map
     */
    public static Map<Class, Class> getReplacedEntities() {
        return getMetadata().getReplacedEntities();
    }
}
