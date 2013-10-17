/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 12.10.2009 11:43:57
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

public class Browser {

    private boolean gecko;
    private boolean appleWebKit;
    private boolean safari;
    private boolean opera;
    private boolean ie;
    private float version = -1;

    private boolean chromeFrame;

    private Browser() {
    }

    public static Browser getBrowserInfo(HttpServletRequest request) {
        return getBrowserInfo(getUserAgent(request));
    }

    public static Browser getBrowserInfo(String userAgent) {
        if (userAgent == null) {
            throw new NullPointerException("User-Agent cannot be NULL");
        }
        final Browser browser = new Browser();
        browser.init(userAgent);
        return browser;
    }

    private void init(String ua) {
        try {
            ua = ua.toLowerCase();

            // browser engine name
            gecko = ua.indexOf("gecko") != -1 && ua.indexOf("webkit") == -1;
            appleWebKit = ua.indexOf("applewebkit") != -1;

            // browser name
            safari = ua.indexOf("safari") != -1;
            opera = ua.indexOf("opera") != -1;
            ie = ua.indexOf("msie") != -1 && !opera
                    && (ua.indexOf("webtv") == -1);

            if (gecko) {
                String tmp = ua.substring(ua.indexOf("rv:") + 3);
                tmp = tmp.replaceFirst("(\\.[0-9]+).+", "$1");
                version = Float.parseFloat(tmp);
            }
            if (appleWebKit) {
                String tmp = ua.substring(ua.indexOf("webkit/") + 7);
                tmp = tmp.replaceFirst("([0-9]+)[^0-9].+", "$1");
                version = Float.parseFloat(tmp);

            }

            if (ie) {
                String ieVersionString = ua.substring(ua.indexOf("msie ") + 5);
                ieVersionString = ieVersionString.substring(0, ieVersionString
                        .indexOf(";"));
                version = Float.parseFloat(ieVersionString);

                chromeFrame = ua.indexOf("chromeframe") != -1;
            }
        } catch (NumberFormatException e) {
            LogFactory.getLog(Browser.class).error("User-Agent parsing error", e);
        }
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public boolean isGecko() {
        return gecko;
    }

    public boolean isAppleWebKit() {
        return appleWebKit;
    }

    public boolean isSafari() {
        return safari;
    }

    public boolean isOpera() {
        return opera;
    }

    public boolean isIE() {
        return ie;
    }

    public float getVersion() {
        return version;
    }

    public boolean isChromeFrame() {
        return chromeFrame;
    }
}
