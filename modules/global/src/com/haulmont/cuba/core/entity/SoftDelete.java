/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.11.2008 12:29:58
 *
 * $Id$
 */
package com.haulmont.cuba.core.entity;

import java.util.Date;

/**
 * Interface implemented by entities supporting soft delete
 */
public interface SoftDelete extends Updatable {

    String[] PROPERTIES = {"deleteTs", "deletedBy"};

    Boolean isDeleted();

    Date getDeleteTs();

    void setDeleteTs(Date deleteTs);

    String getDeletedBy();

    void setDeletedBy(String deletedBy);
}
