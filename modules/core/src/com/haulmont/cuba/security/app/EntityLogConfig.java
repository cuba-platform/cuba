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
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;

@Prefix("cuba.security.EntityLog.")
@Source(type = SourceType.DATABASE)
public interface EntityLogConfig extends Config
{
    @DefaultBoolean(true)
    boolean getEnabled();
    void setEnabled(boolean value);
}
