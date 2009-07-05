/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 11:41:24
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.Default;
import com.haulmont.cuba.core.config.defaults.DefaultInt;

@Source(type = SourceType.SYSTEM)
@Prefix("cuba.web.")
public interface WebConfig extends Config
{
    String getLoginDialogDefaultUser();

    String getLoginDialogDefaultPassword();

    @DefaultBoolean(false)
    boolean getUseActiveDirectory();

    String getActiveDirectoryDomains();

    @Default("TABBED")
    String getAppWindowMode();
}
