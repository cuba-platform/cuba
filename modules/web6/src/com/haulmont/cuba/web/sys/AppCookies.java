/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gorodnov
 * @version $Id$
 */
public abstract class AppCookies implements Serializable {

    private static final long serialVersionUID = 5958656635050664762L;

    public static final int COOKIE_MAX_AGE = 31536000; //1 year (in seconds)

    private transient Map<String, Cookie> requestedCookies;

    private String cookiePath = "/";
    private boolean cookiesEnabled;

    public AppCookies() {
        requestedCookies = new HashMap<String, Cookie>();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        requestedCookies = new HashMap<String, Cookie>();
    }

    public String getCookieValue(String name) {
        Cookie cookie = getCookieIfEnabled(name);
        return cookie == null ? null : cookie.getValue();
    }

    public int getCookieMaxAge(String name) {
        Cookie cookie = getCookieIfEnabled(name);
        return cookie == null ? 0 : cookie.getMaxAge();
    }

    public void addCookie(String name, String value) {
        addCookie(name, value, COOKIE_MAX_AGE);
    }

    public void addCookie(String name, String value, int maxAge) {
        if (isCookiesEnabled()) {
            if (StringUtils.isEmpty(value)) {
                removeCookie(name);
            } else {
                Cookie cookie = new Cookie(name, value);
                cookie.setPath(getCookiePath());
                cookie.setMaxAge(maxAge);
                addCookie(cookie);
            }
        }
    }

    public void removeCookie(String name) {
        if (isCookiesEnabled()) {
            Cookie cookie = getCookie(name);
            if (cookie != null) {
                cookie.setValue(null);
                cookie.setPath(getCookiePath());
                cookie.setMaxAge(0);
                addCookie(cookie);
            }
        }
    }

    protected Cookie getCookieIfEnabled(String name) {
        return isCookiesEnabled() ? getCookie(name) : null;
    }

    protected Cookie getCookie(String name) {
        return requestedCookies.get(name);
    }

    protected abstract void addCookie(Cookie cookie);

    public void updateCookies(HttpServletRequest request) {
        if (isCookiesEnabled()) {
            synchronized (requestedCookies) {
                requestedCookies.clear();
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (final Cookie cookie : cookies) {
                        requestedCookies.put(cookie.getName(), cookie);
                    }
                }
            }
        }
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public boolean isCookiesEnabled() {
        return cookiesEnabled;
    }

    public void setCookiesEnabled(boolean cookiesEnabled) {
        this.cookiesEnabled = cookiesEnabled;
    }
}