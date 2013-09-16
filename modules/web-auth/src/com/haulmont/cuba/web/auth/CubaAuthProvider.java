/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.security.global.LoginException;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaAuthProvider extends Filter {

    public static final String NAME = "cuba_AuthProvider";

    /**
     * Login procedure with user and password
     *
     * @param login    User login
     * @param password User password
     * @param loc      Locale
     * @throws LoginException Login exception
     */
    void authenticate(String login, String password, Locale loc) throws LoginException;

    /**
     * @param request Http request
     * @return True if auth needed
     */
    boolean needAuth(ServletRequest request);

    /**
     * @param session HTTP sesstion
     * @return True if session supported by auth mechanism
     */
    boolean authSupported(HttpSession session);
}
