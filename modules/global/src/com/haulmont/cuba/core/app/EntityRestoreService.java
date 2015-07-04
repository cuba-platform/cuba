/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface EntityRestoreService {

    String NAME = "cuba_EntityRestoreService";

    void restoreEntities(Collection<Entity> entities);
}
