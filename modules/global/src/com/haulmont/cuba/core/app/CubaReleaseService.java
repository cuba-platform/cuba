/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 14.09.2010 12:28:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface CubaReleaseService {
    String NAME = "cuba_CubaDeployer";

    String getReleaseTimestamp();
}
