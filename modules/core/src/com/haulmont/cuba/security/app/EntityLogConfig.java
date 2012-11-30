/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.03.2009 11:22:13
 *
 * $Id$
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
public interface EntityLogConfig extends Config
{
    /**
     * @return Whether the EntityLog is enabled
     */
    @Property("cuba.security.EntityLog.enabled")
    @DefaultBoolean(true)
    boolean getEnabled();
    void setEnabled(boolean value);
}
