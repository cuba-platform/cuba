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

import javax.ejb.Local;

/**
 * Local interface to the {@link CubaDeployerServiceBean} MBean
 */
@Local
public interface CubaDeployerService {

    String JNDI_NAME = "cuba/core/DeployerService";

    String getReleaseNumber();

    String getReleaseTimestamp();
}
