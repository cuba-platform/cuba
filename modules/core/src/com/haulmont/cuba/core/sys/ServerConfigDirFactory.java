/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.11.2009 10:29:15
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.config.type.TypeFactory;

import java.net.URI;
import java.io.File;

public class ServerConfigDirFactory extends TypeFactory {

    public Object build(String string) {
        String confUrl = System.getProperty("jboss.server.config.url");
        String path = URI.create(confUrl).getPath();
        if (path.endsWith(File.separator))
            return path.substring(0, path.length()-1);
        else
            return path;
    }
}
