/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:51:51
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import java.io.Serializable;
import java.util.UUID;
import java.util.Date;

public interface BaseEntity<T> extends Serializable
{
    T getId();

    UUID getUuid();

    Date getCreateTs();

    void setCreateTs(Date date);

    String getCreatedBy();

    void setCreatedBy(String createdBy);
}
