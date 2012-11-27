/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.entity;

import java.util.Date;

/**
 * Interface to be implemented by entities that support soft deletion.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface SoftDelete extends Updatable {

    String[] PROPERTIES = {"deleteTs", "deletedBy"};

    Boolean isDeleted();

    Date getDeleteTs();

    void setDeleteTs(Date deleteTs);

    String getDeletedBy();

    void setDeletedBy(String deletedBy);
}
