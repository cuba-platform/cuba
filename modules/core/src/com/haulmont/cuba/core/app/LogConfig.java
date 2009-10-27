/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.10.2009 11:25:32
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;

@Source(type = SourceType.SYSTEM)
public interface LogConfig extends Config {

    @Property("cuba.log.cutLoadListQueries")
    @DefaultBoolean(false)
    boolean getCutLoadListQueries();
}
