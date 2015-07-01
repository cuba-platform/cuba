/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.annotations.TransientCompatibleAnnotations;

import javax.persistence.Temporal;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EclipseLinkCustomizer {

    public static void initTransientCompatibleAnnotations() {
        TransientCompatibleAnnotations.getTransientCompatibleAnnotations().add(Temporal.class.getName());
    }
}
