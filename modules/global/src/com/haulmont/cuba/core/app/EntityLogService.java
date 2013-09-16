/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
