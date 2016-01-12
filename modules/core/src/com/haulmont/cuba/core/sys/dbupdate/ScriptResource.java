/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.dbupdate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
* @author degtyarjov
* @version $Id$
*/
public class ScriptResource {
    protected String dir;
    protected String name;
    protected String path;
    protected Resource resource;

    public ScriptResource(Resource resource) {
        try {
            this.resource = resource;
            this.name = resource.getFilename();
            this.path = resource.getFile().getPath();
            this.dir = StringUtils.substringBeforeLast(this.path, "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getContent() throws IOException {
        return IOUtils.toString(resource.getInputStream());
    }

    public String getDir() {
        return dir;
    }
}
