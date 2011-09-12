/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.08.2009 12:49:45
 *
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.View;

import java.util.List;
import java.util.Map;

/**
 * Service interface to the CubaDeployer MBean
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

    Map<String, String> getReplacedEntities();
}
