/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Devyatkin
 * Created: 15.03.11 10:55
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.config.*;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultString;

@Prefix("cuba.")
@Source(type = SourceType.APP)
public interface SecurityConfig extends Config {

    @DefaultBoolean(false)
    public boolean getPasswordPolicyEnabled();

    @DefaultString("((?=.*\\d)(?=.*\\p{javaLowerCase})(?=.*\\p{javaUpperCase}).{6,20})")
    public String getPasswordPolicyRegExp();
}
