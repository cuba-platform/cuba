/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.10.2010 14:16:24
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

@Source(type = SourceType.DATABASE)
public interface PersistenceConfig extends Config {

    @DefaultInt(200)
    int getDefaultLazyCollectionThreshold();
    void setDefaultLazyCollectionThreshold(int value);

    @DefaultInt(10000)
    int getDefaultMaxFetchUI();
    void setDefaultMaxFetchUI(int value);
}
