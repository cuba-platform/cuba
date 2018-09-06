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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.apache.commons.io.IOUtils.copy;

public class CubaWebJarsHandler implements RequestHandler {

    public static final String VAADIN_WEBJARS_PATH_PREFIX = "/" + WebJarResourceResolver.VAADIN_PREFIX;

    private final Logger log = LoggerFactory.getLogger(CubaWebJarsHandler.class);

    protected WebJarResourceResolver resolver;
    protected ServletContext servletContext;
    protected WebConfig webConfig;

    public CubaWebJarsHandler(ServletContext servletContext) {
        this.servletContext = servletContext;

        this.resolver = AppBeans.get(WebJarResourceResolver.NAME);

        this.webConfig = AppBeans.get(Configuration.class)
                .getConfig(WebConfig.class);
    }

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
        String path = request.getPathInfo();
        if (StringUtils.isEmpty(path)
                || !path.startsWith(VAADIN_WEBJARS_PATH_PREFIX)) {
            return false;
        }

        log.trace("WebJar resource requested: {}", path.replace(VAADIN_WEBJARS_PATH_PREFIX, ""));

        String errorMessage = checkResourcePath(path);
        if (StringUtils.isNotEmpty(errorMessage)) {
            log.warn(errorMessage);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, errorMessage);
            return false;
        }

        URL resourceUrl = getStaticResourceUrl(path);

        if (resourceUrl == null) {
            resourceUrl = getClassPathResourceUrl(path);
        }

        if (resourceUrl == null) {
            String msg = String.format("Requested WebJar resource is not found: %s", path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, msg);
            log.warn(msg);
            return false;
        }

        String resourceName = getResourceName(path);
        String mimeType = servletContext.getMimeType(resourceName);
        response.setContentType(mimeType != null ? mimeType : FileTypesHelper.DEFAULT_MIME_TYPE);

        long resourceCacheTime = getCacheTime();

        String cacheControl = resourceCacheTime > 0
                ? "max-age=" + String.valueOf(resourceCacheTime)
                : "public, max-age=0, no-cache, no-store, must-revalidate";
        response.setHeader("Cache-Control", cacheControl);

        long expires = resourceCacheTime > 0
                ? System.currentTimeMillis() + (resourceCacheTime * 1000)
                : 0;
        response.setDateHeader("Expires", expires);

        InputStream inputStream = null;
        try {
            URLConnection connection = resourceUrl.openConnection();
            long lastModifiedTime = connection.getLastModified();
            // Remove milliseconds to avoid comparison problems (milliseconds
            // are not returned by the browser in the "If-Modified-Since"
            // header).
            lastModifiedTime = lastModifiedTime - lastModifiedTime % 1000;
            response.setDateHeader("Last-Modified", lastModifiedTime);

            if (browserHasNewestVersion(request, lastModifiedTime)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return true;
            }

            inputStream = connection.getInputStream();

            copy(inputStream, response.getOutputStream());

            return true;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    protected String getResourceName(String uri) {
        String[] tokens = uri.split("/");
        return tokens[tokens.length - 1];
    }

    // copy-pasted from VaadinServlet
    protected boolean browserHasNewestVersion(VaadinRequest request, long resourceLastModifiedTimestamp) {
        if (resourceLastModifiedTimestamp < 1) {
            // We do not know when it was modified so the browser cannot have an
            // up-to-date version
            return false;
        }
        /*
         * The browser can request the resource conditionally using an
         * If-Modified-Since header. Check this against the last modification
         * time.
         */
        try {
            // If-Modified-Since represents the timestamp of the version cached
            // in the browser
            long headerIfModifiedSince = request
                    .getDateHeader("If-Modified-Since");

            if (headerIfModifiedSince >= resourceLastModifiedTimestamp) {
                // Browser has this an up-to-date version of the resource
                return true;
            }
        } catch (Exception e) {
            // Failed to parse header. Fail silently - the browser does not have
            // an up-to-date version in its cache.
        }
        return false;
    }

    protected URL getStaticResourceUrl(String path) throws IOException {
        URL resourceUrl = servletContext.getResource(path);

        if (resourceUrl != null) {
            log.trace("Overridden version of WebJar resource found: {}", path);
        }

        return resourceUrl;
    }

    @Nullable
    protected URL getClassPathResourceUrl(String path) {
        String classpathPath = resolver.translateToWebJarPath(path);

        log.trace("Load WebJar resource from classpath: {}", classpathPath);

        return resolver.getResource(classpathPath);
    }

    protected long getCacheTime() {
        long cacheTime = webConfig.getWebJarResourcesCacheTime();

        if (cacheTime == 0 && webConfig.getProductionMode()) {
            log.warn("Web resources caching is not enabled in production mode");
        }

        return cacheTime;
    }

    protected String checkResourcePath(String uri) {
        if (uri.endsWith("/")) {
            return String.format("Directory loading is forbidden: %s", uri);
        }

        if (uri.contains("/../")) {
            return String.format("Loading WebJar resource with the upward path is forbidden: %s", uri);
        }

        return null;
    }
}