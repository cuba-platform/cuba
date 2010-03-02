/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 26.02.2010 19:58:02
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class AppCookies {
    public static final int COOKIE_MAX_AGE = 31536000; //1 year (in seconds)

    private final Map<String, Cookie> requestedCookies;
    private final Set<Cookie> responsedCookies;

    private String cookiePath = "/";
    private boolean cookiesEnabled;

    public AppCookies() {
        requestedCookies = new HashMap<String, Cookie>();
        responsedCookies = new HashSet<Cookie>();
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

    protected void addCookie(Cookie cookie) {
        responsedCookies.add(cookie);
    }

    public void processRequestedCookies(HttpServletRequest request) {
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

    public void processResponsedCookies(HttpServletResponse response) {
        if (isCookiesEnabled()) {
            synchronized (responsedCookies) {
                for (final Cookie cookie : responsedCookies) {
                    response.addCookie(cookie);
                }
                responsedCookies.clear();
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
