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

    String[] PROPERTIES = {"deleteTs", "deletedBy"};

    Boolean isDeleted();

    Date getDeleteTs();

    void setDeleteTs(Date deleteTs);

    String getDeletedBy();

    void setDeletedBy(String deletedBy);
}
