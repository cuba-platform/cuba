/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import java.util.List;

public interface DbUpdater {

    String NAME = "cuba_DbUpdater";

    void updateDatabase();

    List<String> findUpdateDatabaseScripts() throws DBNotInitializedException;
}
