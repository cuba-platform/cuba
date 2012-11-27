/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.entity;

import java.util.Date;
import java.util.UUID;

/**
 * Base interface for persistent entities.
 * @param <T> identifier type
 *
 * @author krivopustov
 * @version $Id$
 */
public interface BaseEntity<T> extends Entity<T> {

    int LOGIN_FIELD_LEN = 50;

    String[] PROPERTIES = {"id", "createTs", "createdBy"};

    UUID getUuid();

    Date getCreateTs();

    void setCreateTs(Date date);

    String getCreatedBy();

    void setCreatedBy(String createdBy);
}
