/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.entity;

import java.util.Date;

/**
 * Interface to be implemented by entities that support update information saving.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Updatable {

    String[] PROPERTIES = {"updateTs", "updatedBy"};

    Date getUpdateTs();

    void setUpdateTs(Date updateTs);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);
}
