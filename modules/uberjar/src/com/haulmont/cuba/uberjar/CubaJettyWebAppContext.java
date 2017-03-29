/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.uberjar;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.MalformedURLException;
import java.util.Set;

public class CubaJettyWebAppContext extends WebAppContext {

    protected Set<String> staticContents;

    public Set<String> getStaticContents() {
        return staticContents;
    }

    public void setStaticContents(Set<String> staticContents) {
        this.staticContents = staticContents;
    }

    @Override
    public Resource getResource(String uriInContext) throws MalformedURLException {
        if (uriInContext != null && getClassLoader() != null) {
            boolean isWebInf = isWebInf(uriInContext);
            boolean isStatic = isStatic(uriInContext);
            if (isWebInf || isStatic) {
                if (uriInContext.startsWith("/")) {
                    uriInContext = uriInContext.substring(1);
                }
                if (isStatic) {
                    uriInContext = ServerRunner.STATIC_CONTENT_PATH_IN_JAR + "/" + uriInContext;
                }
                return Resource.newResource(getClassLoader().getResource(uriInContext));
            }
        }
        return super.getResource(uriInContext);
    }

    protected boolean isWebInf(String uriInContext) {
        return uriInContext.startsWith("/WEB-INF/");
    }

    protected boolean isStatic(String uriInContext) {
        if (staticContents != null) {
            return staticContents.stream()
                    .anyMatch(it -> uriInContext.startsWith("/" + it + "/"));
        }
        return false;
    }
}
