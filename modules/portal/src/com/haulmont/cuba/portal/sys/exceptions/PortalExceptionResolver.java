/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys.exceptions;

import com.haulmont.cuba.security.global.NoUserSessionException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author artamonov
 * @version $Id$
 */
public class PortalExceptionResolver implements HandlerExceptionResolver {

    private Logger log = LoggerFactory.getLogger(getClass());

    private String noUserSessionUrl;

    private String noMiddlewareConnectionUrl;

    public String getNoUserSessionUrl() {
        return noUserSessionUrl;
    }

    public void setNoUserSessionUrl(String noUserSessionUrl) {
        this.noUserSessionUrl = noUserSessionUrl;
    }

    public String getNoMiddlewareConnectionUrl() {
        return noMiddlewareConnectionUrl;
    }

    public void setNoMiddlewareConnectionUrl(String noMiddlewareConnectionUrl) {
        this.noMiddlewareConnectionUrl = noMiddlewareConnectionUrl;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        if (ex instanceof NoUserSessionException) {
            log.warn(ex.getMessage());

            HttpSession httpSession = request.getSession();
            httpSession.invalidate();

            if (StringUtils.isNotEmpty(noUserSessionUrl))
                return new ModelAndView("redirect:" + noUserSessionUrl);
            else
                return new ModelAndView("redirect:/");
        } else if (ex instanceof NoMiddlewareConnectionException) {
            log.error(ex.getMessage());
            HttpSession httpSession = request.getSession();
            httpSession.invalidate();

            if (StringUtils.isNotEmpty(noMiddlewareConnectionUrl))
                return new ModelAndView(noMiddlewareConnectionUrl);
            else
                return new ModelAndView("/");
        }

        return null;
    }
}