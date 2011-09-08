/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Session;

import java.util.Map;

/**
 * Central interface to provide metadata functionality
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface Metadata {

    String NAME = "cuba_Metadata";

    Session getSession();

    ViewRepository getViewRepository();

    Map<Class, Class> getReplacedEntities();
}
