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

package com.haulmont.cuba.core.sys.remoting.discovery;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.ClientBasedSession;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Abstract implementation of the {@link ServerSelector} interface providing selection of the same server for all
 * requests in a user session.
 */
public abstract class StickySessionServerSelector implements ServerSelector {

    private static final Logger log = LoggerFactory.getLogger(StickySessionServerSelector.class);

    protected String servletPath = "remoting";

    protected Consumer<List<String>> serverSorter;

    protected SessionUrlsHolder anonymousSessionUrlsHolder;

    protected String id;

    protected Set<String> failedUrls = new CopyOnWriteArraySet<>();

    protected ThreadLocal<List<String>> lastNoSessionUrls = new ThreadLocal<>();

    protected static class Context {
        private List<String> urls;
        private String lastUrl;

        @Override
        public String toString() {
            return "Context@" + Integer.toHexString(hashCode()) + "{" +
                    "urls=" + urls +
                    ", lastUrl='" + lastUrl + '\'' +
                    '}';
        }
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public void setServerSorter(Consumer<List<String>> serverSorter) {
        this.serverSorter = serverSorter;
    }

    public void setAnonymousSessionUrlsHolder(SessionUrlsHolder anonymousSessionUrlsHolder) {
        this.anonymousSessionUrlsHolder = anonymousSessionUrlsHolder;
    }

    public void setSelectorId(String id) {
        this.id = id;
    }

    /**
     * Must be implemented in concrete classes to return a list of available servers.
     */
    public abstract List<String> getUrls();

    @Override
    public Object initContext() {
        List<String> sessionUrls;
        boolean isNewSession = false;
        SessionUrlsHolder sessionUrlsHolder = getSessionUrlsHolder();
        if (sessionUrlsHolder == null) {
            sessionUrls = sortUrls();
            lastNoSessionUrls.set(sessionUrls);
        } else {
            //noinspection unchecked
            sessionUrls = sessionUrlsHolder.getUrls(id);
            if (sessionUrls == null) {
                sessionUrls = lastNoSessionUrls.get();
                isNewSession = true;
            }
            if (sessionUrls == null)
                sessionUrls = sortUrls();
            sessionUrlsHolder.setUrls(id, sessionUrls);
            lastNoSessionUrls.remove();
        }

        Context ctx = new Context();
        ctx.urls = new ArrayList<>(sessionUrls.size());
        if (isNewSession) {
            // each new session retries access to all servers - this way we can find out if some failed server is back online
            ctx.urls.addAll(sessionUrls);
        } else {
            // the session is not new or there is no session
            // first add all successful
            for (String url : sessionUrls) {
                if (!failedUrls.contains(url))
                    ctx.urls.add(url);
            }
            // then add all failed
            for (String url : sessionUrls) {
                if (failedUrls.contains(url))
                    ctx.urls.add(url);
            }
        }
        log.trace("Context initialized: {} ", ctx);
        return ctx;
    }

    private List<String> sortUrls() {
        List<String> list = new ArrayList<>(getUrls());
        if (serverSorter != null) {
            serverSorter.accept(list);
        }
        return list;
    }

    @Override
    @Nullable
    public String getUrl(Object context) {
        Context ctx = (Context) context;

        if (ctx.urls.isEmpty())
            return null;
        else {
            ctx.lastUrl = ctx.urls.get(0);
            return ctx.lastUrl;
        }
    }

    @Override
    public void success(Object context) {
        Context ctx = (Context) context;
        Preconditions.checkNotNullArgument(ctx.lastUrl, "lastUrl is null");
        log.trace("Success: {}", ctx);

        failedUrls.remove(ctx.lastUrl);
    }

    @Override
    public void fail(Object context) {
        Context ctx = (Context) context;
        Preconditions.checkNotNullArgument(ctx.lastUrl, "lastUrl is null");
        log.trace("Fail: {}", ctx);

        failedUrls.add(ctx.lastUrl);
        ctx.urls.remove(ctx.lastUrl);
    }

    @Nullable
    protected SessionUrlsHolder getSessionUrlsHolder() {
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext == null)
            return null;

        UserSession session = securityContext.getSession();
        if (session == null || session instanceof ClientBasedSession && ((ClientBasedSession) session).hasRequestScopedInfo())
            return anonymousSessionUrlsHolder;

        return new UserSessionUrlsHolder(session);
    }
}
