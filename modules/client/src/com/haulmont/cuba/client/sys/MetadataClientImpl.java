/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AbstractMetadata;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Metadata.NAME)
public class MetadataClientImpl extends AbstractMetadata {

    @Inject
    private ServerInfoService serverInfoService;

    @Inject
    private Configuration configuration;

    @Inject
    private Resources resources;

    protected MetadataBuildInfo getMetadataBuildInfo() {
        return serverInfoService.getMetadataBuildInfo();
    }
}
