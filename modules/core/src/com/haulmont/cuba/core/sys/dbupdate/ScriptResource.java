/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.dbupdate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URLDecoder;

public class ScriptResource {
    protected String dir;
    protected String name;

    protected String path;
    protected Resource resource;

    public ScriptResource(Resource resource) {
        try {
            this.resource = resource;
            this.name = resource.getFilename();
            this.path = URLDecoder.decode(resource.getURL().getPath(), "UTF-8");
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

    @Override
    public String toString() {
        return path;
    }
}