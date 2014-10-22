/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

/**
 * @author zlatoverov
 * @version $Id$
 */
@Source(type = SourceType.APP)
public interface RestConfig extends Config {

    @Property("cuba.rest.productionMode")
    @Default("false")
    boolean getProductionMode();
}
