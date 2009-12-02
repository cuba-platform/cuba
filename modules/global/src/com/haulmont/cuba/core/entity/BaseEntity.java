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

/**
 * Base interface for persistent entities
 * @param <T> identifier type
 */
public interface BaseEntity<T> extends Entity<T>
{
    int LOGIN_FIELD_LEN = 50;

    UUID getUuid();

    Date getCreateTs();

    void setCreateTs(Date date);

    String getCreatedBy();

    void setCreatedBy(String createdBy);
}
