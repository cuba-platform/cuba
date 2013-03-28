/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.entity.ui;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface EntityPermissionTarget extends AssignableTarget {

    Class getEntityClass();
}
