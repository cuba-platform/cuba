/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.auth;

import com.haulmont.cuba.security.global.LoginException;

import javax.servlet.Filter;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaAuthProvider extends Filter {

    public static final String NAME = "cuba_AuthProvider";

    void authenticate(String login, String password, Locale loc) throws LoginException;
}
