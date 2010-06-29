/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.05.2009 18:51:16
 *
 * $Id$
 */
package com.haulmont.cuba.web.app;

import com.haulmont.cuba.core.config.Prefix;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.defaults.DefaultInteger;

/**
 * Common WebUI components configuration parameters  
 */
@Prefix("cuba.UIComponentsConfig.")
@Source(type = SourceType.DATABASE)
public interface UIComponentsConfig extends Config
{
    @DefaultInteger(20)
    Integer getMaxUploadSizeMb();
    void setMaxUploadSizeMb(Integer maxUploadSizeMb);

    String getResourcesRoot();
}
