/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

/**
 * @author hasanov
 * @version $Id$
 */
public interface EntityLogService {

    String NAME = "cuba_EntityLogService";

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void invalidateCache();
}
