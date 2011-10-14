/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link CubaDeployer} MBean.
 */
public interface CubaDeployerMBean
{
    String NAME = "cuba_CubaDeployer";

    String getReleaseNumber();

    String getReleaseTimestamp();
}
