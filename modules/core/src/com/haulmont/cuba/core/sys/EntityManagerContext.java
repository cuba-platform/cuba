/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityManagerContext {

    private boolean softDeletion = true;

    private Map<String, Object> dbHints = new HashMap<>();

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    public Map<String, Object> getDbHints() {
        return dbHints;
    }

    public void setDbHints(Map<String, Object> dbHints) {
        this.dbHints = dbHints;
    }
}
