/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.09.2009 17:37:58
 *
 * $Id: NamePattern.java 834 2009-09-21 14:31:11Z krivopustov $
 */
package com.haulmont.chile.core.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines an instance name format pattern in the form {0}|{1}, where
 * <br>{0} - format string as for {@link String#format}
 * <br>{1} - comma-separated list of field names, corresponding to format {0}
 * <br>No extra spaces between parts allowed.
 * <p>
 * Example: <code>@NamePattern("%s : %s|name,address")</code>
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NamePattern {
    String value();
}
