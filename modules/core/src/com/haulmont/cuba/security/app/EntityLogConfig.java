/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;

/**
 * {@link com.haulmont.cuba.security.app.EntityLog} configuration parameters
 *
 * @author krivopustov
 * @version $Id$
 */
@Source(type = SourceType.DATABASE)
public interface EntityLogConfig extends Config {
    /**
     * @return Whether the EntityLog is enabled
     */
    @Property("cuba.entityLog.enabled")
    @DefaultBoolean(true)
    boolean getEnabled();
    void setEnabled(boolean value);
}