/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
