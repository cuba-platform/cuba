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

/**
 * Service interface to provide initial information for clients. Can be invoked before login when user session
 * is not yet established.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface CubaDeployerService {

    String NAME = "cuba_DeployerService";

    String getReleaseNumber();

    String getReleaseTimestamp();

    MetadataBuildInfo getMetadataBuildInfo();

    List<View> getViews();
}
