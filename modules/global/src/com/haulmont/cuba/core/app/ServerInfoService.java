/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import java.util.List;
import java.util.TimeZone;

/**
 * Service interface to provide initial information for clients. Can be invoked before login when user session
 * is not yet established.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ServerInfoService {

    String NAME = "cuba_ServerInfoService";

    String getReleaseNumber();

    String getReleaseTimestamp();

    List<View> getViews();

    View getView(Class<? extends Entity> entityClass, String name);

    /**
     * Return time zone used by server application.
     * Useful for remote clients which may run on machines with another default time zone (like desktop client).
     *
     * @return server time zone
     */
    TimeZone getTimeZone();

    /**
     * @return current time on the server in milliseconds
     */
    long getTimeMillis();
}
