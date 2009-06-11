/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.05.2009 10:09:10
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import java.io.Serializable;

public abstract class DbDialect implements Serializable
{
    public abstract String getDeleteTsColumn();

    public abstract String getUniqueConstraintViolationMarker();

    public abstract String getUniqueConstraintViolationPattern();
}
