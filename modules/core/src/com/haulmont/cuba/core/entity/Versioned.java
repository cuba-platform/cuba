/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.11.2008 12:23:01
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

public interface Versioned
{
    Integer getVersion();

    void setVersion(Integer version);
}
