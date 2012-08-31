/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.exceptions;

import com.haulmont.cuba.security.global.NoUserSessionException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private Log log = LogFactory.getLog(getClass());

    private String noUserSessionUrl;

    public String getNoUserSessionUrl() {
        return noUserSessionUrl;
    }

    public void setNoUserSessionUrl(String noUserSessionUrl) {
        this.noUserSessionUrl = noUserSessionUrl;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof NoUserSessionException) {
            log.warn(ex.getMessage());

            HttpSession httpSession = request.getSession();
            httpSession.invalidate();

            if (StringUtils.isNotEmpty(noUserSessionUrl))
                return new ModelAndView("redirect:" + noUserSessionUrl);
            else
                return new ModelAndView("redirect:/");
        }

        return null;
    }
}
