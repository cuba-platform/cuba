/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:25:30
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import java.util.Date;

/**
 * Interface implemented by entities supporting update information saving
 */
public interface Updatable
{
    Date getUpdateTs();

    void setUpdateTs(Date updateTs);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);
}
