/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 17:43:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CubaDeployer implements CubaDeployerMBean
{
    private Log log = LogFactory.getLog(CubaDeployer.class);

    public void create() {
        log.debug("create");
    }

    public void start() {
        log.debug("start");
    }
}
