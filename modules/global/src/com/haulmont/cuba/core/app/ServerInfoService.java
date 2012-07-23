/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.View;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Service interface to provide initial information for clients. Can be invoked before login when user session
 * is not yet established.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ServerInfoService {

    String NAME = "cuba_ServerInfoService";

    String getReleaseNumber();

    String getReleaseTimestamp();

    MetadataBuildInfo getMetadataBuildInfo();

    List<View> getViews();

    /**
     * Return time zone used by server application.
     * Useful for remote clients which may run on machines with another default time zone (like desktop client).
     *
     * @return server time zone
     */
    TimeZone getTimeZone();
}
