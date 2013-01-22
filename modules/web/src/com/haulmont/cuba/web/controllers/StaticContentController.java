/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 11.03.2011 10:37:27
 *
 * $Id$
 */
package com.haulmont.cuba.web.controllers;

import com.haulmont.cuba.core.global.FileTypesHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;

@Controller
@RequestMapping(value = "/static/**")
public class StaticContentController implements LastModified {

    public static interface LookupResult {
        public void respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException;

        public void respondHead(HttpServletRequest req, HttpServletResponse resp) throws IOException;

        public long getLastModified();
    }

    public static class Error implements LookupResult {
        protected final int statusCode;
        protected final String message;

        public Error(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }

        public long getLastModified() {
            return -1;
        }

        public void respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.sendError(statusCode, message);
        }

        public void respondHead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    public static class StaticFile implements LookupResult {
        protected final long lastModified;
        protected final String mimeType;
        protected final int contentLength;
        protected final boolean acceptsDeflate;
        protected final URL url;

        public StaticFile(long lastModified, String mimeType, int contentLength, boolean acceptsDeflate, URL url) {
            this.lastModified = lastModified;
            this.mimeType = mimeType;
            this.contentLength = contentLength;
            this.acceptsDeflate = acceptsDeflate;
            this.url = url;
        }

        public long getLastModified() {
            return lastModified;
        }

        protected boolean willDeflate() {
            return acceptsDeflate && deflatable(mimeType) && contentLength >= deflateThreshold;
        }

        protected void setHeaders(HttpServletResponse resp) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(mimeType);
            if (contentLength >= 0 && !willDeflate())
                resp.setContentLength(contentLength);
        }

        public void respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            setHeaders(resp);
            final OutputStream os;
            if (willDeflate()) {
                resp.setHeader("Content-Encoding", "gzip");
                os = new GZIPOutputStream(resp.getOutputStream(), bufferSize);
            } else
                os = resp.getOutputStream();
            InputStream is = url.openStream();
            try {
                IOUtils.copy(is, os);
            } finally {
                is.close();
                os.close();
            }
        }

        public void respondHead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            if (willDeflate())
                throw new UnsupportedOperationException();
            setHeaders(resp);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String handleGetRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        lookup(request).respondGet(request, response);
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String handlePostRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return handleGetRequest(request, response);
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public String handleHeadRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            lookup(request).respondHead(request, response);
        } catch (UnsupportedOperationException e) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
        return null;
    }

    public long getLastModified(HttpServletRequest req) {
        return lookup(req).getLastModified();
    }

    protected LookupResult lookup(HttpServletRequest req) {
        LookupResult r = (LookupResult) req.getAttribute("lookupResult");
        if (r == null) {
            r = lookupNoCache(req);
            req.setAttribute("lookupResult", r);
        }
        return r;
    }

    protected LookupResult lookupNoCache(HttpServletRequest req) {
        final String path = getPath(req);
        if (isForbidden(path))
            return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");

        ServletContext context = req.getSession().getServletContext();

        final URL url;
        try {
            url = context.getResource(path);
        } catch (MalformedURLException e) {
            return new Error(HttpServletResponse.SC_BAD_REQUEST, "Malformed path");
        }
        if (url == null)
            return new Error(HttpServletResponse.SC_NOT_FOUND, "Not found");

        final String mimeType = getMimeType(path);

        final String realpath = context.getRealPath(path);
        if (realpath != null) {
            // Try as an ordinary file
            File f = new File(realpath);
            if (!f.isFile())
                return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            else {
                return createLookupResult(req, f.lastModified(), mimeType, (int) f.length(), acceptsDeflate(req), url);
            }
        } else {
            try {
                // Try as a JAR Entry
                final ZipEntry ze = ((JarURLConnection) url.openConnection()).getJarEntry();
                if (ze != null) {
                    if (ze.isDirectory())
                        return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                    else
                        return createLookupResult(req, ze.getTime(), mimeType, (int) ze.getSize(), acceptsDeflate(req), url);
                } else
                    // Unexpected?
                    return new StaticFile(-1, mimeType, -1, acceptsDeflate(req), url);
            } catch (ClassCastException e) {
                // Unknown resource type
                return createLookupResult(req, -1, mimeType, -1, acceptsDeflate(req), url);
            } catch (IOException e) {
                return new Error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
            }
        }
    }

    protected LookupResult createLookupResult(
            HttpServletRequest req, long lastModified, String mimeType, int contentLength, boolean acceptsDeflate, URL url)
    {
        return new StaticFile(lastModified, mimeType, contentLength, acceptsDeflate, url);
    }

    protected String getPath(HttpServletRequest req) {
        String path = ControllerUtils.getControllerPath(req);
        String pathInfo = coalesce(req.getPathInfo(), "");
        return path + pathInfo;
    }

    protected boolean isForbidden(String path) {
        String lpath = path.toLowerCase();
        return lpath.startsWith("/web-inf/") || lpath.startsWith("/meta-inf/");
    }

    protected String getMimeType(String path) {
        return coalesce(FileTypesHelper.getMIMEType(path), "application/octet-stream");
    }

    protected static boolean acceptsDeflate(HttpServletRequest req) {
        final String ae = req.getHeader("Accept-Encoding");
        return ae != null && ae.contains("gzip");
    }

    protected static boolean deflatable(String mimetype) {
        return mimetype.startsWith("text/")
                || mimetype.equals("application/postscript")
                || mimetype.startsWith("application/ms")
                || mimetype.startsWith("application/vnd")
                || mimetype.endsWith("xml");
    }

    public static <T> T coalesce(T...ts) {
        for(T t: ts)
            if(t != null)
                return t;
        return null;
    }

    protected static final int deflateThreshold = 4 * 1024;

    protected static final int bufferSize = 4 * 1024;
}
