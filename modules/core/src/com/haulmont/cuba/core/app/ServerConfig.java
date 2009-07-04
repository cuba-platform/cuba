/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.07.2009 12:34:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.SYSTEM)
public interface ServerConfig extends Config {

    @Property("jboss.server.log.dir")
    String getServerLogDir();

    @Property("jboss.server.temp.dir")
    String getServerTempDir();

    @Property("jboss.server.data.dir")
    String getServerDataDir();
}
