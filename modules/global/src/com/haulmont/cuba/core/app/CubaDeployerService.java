/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.08.2009 12:49:45
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Service interface to the CubaDeployer MBean
 */
public interface CubaDeployerService {

    String NAME = "cuba_DeployerService";

    @Deprecated
    String JNDI_NAME = NAME;

    String getReleaseNumber();

    String getReleaseTimestamp();
}
