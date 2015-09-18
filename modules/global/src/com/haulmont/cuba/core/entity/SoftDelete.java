/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import java.util.Date;

/**
 * Interface to be implemented by entities that support soft deletion.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface SoftDelete {

    /**
     * Returns true if the entity is deleted.
     */
    Boolean isDeleted();

    /**
     * Returns deletion timestamp or null if not deleted.
     */
    Date getDeleteTs();

    /**
     * Returns login name of the user who deleted the entity
     * or null if not deleted.
     */
    String getDeletedBy();

    /**
     * INTERNAL. Sets soft deletion timestamp.
     */
    void setDeleteTs(Date deleteTs);

    /**
     * INTERNAL. Sets login name of the user who deleted the entity.
     */
    void setDeletedBy(String deletedBy);
}
