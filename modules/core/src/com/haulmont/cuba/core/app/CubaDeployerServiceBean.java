/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.08.2009 12:50:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import org.springframework.stereotype.Service;

/**
 * Service facade for {@link com.haulmont.cuba.core.app.CubaDeployer} MBean
 */
@Service(CubaDeployerService.NAME)
public class CubaDeployerServiceBean implements CubaDeployerService {

    public String getReleaseNumber() {
        CubaDeployerMBean mBean = Locator.lookup(CubaDeployerMBean.NAME);
        return mBean.getReleaseNumber();
    }

    public String getReleaseTimestamp() {
        CubaDeployerMBean mBean = Locator.lookup(CubaDeployerMBean.NAME);
        return mBean.getReleaseTimestamp();
    }
}
