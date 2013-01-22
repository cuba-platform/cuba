/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.09.2009 13:27:57
 *
 * $Id$
 */
package com.haulmont.cuba.web;

/**
 * Listener of user substitution events. See {@link com.haulmont.cuba.web.Connection}.
 */
public interface UserSubstitutionListener {

    void userSubstituted(Connection connection);
}
