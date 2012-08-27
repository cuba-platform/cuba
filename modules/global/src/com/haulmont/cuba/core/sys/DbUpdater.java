/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.04.2010 15:12:17
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import java.util.List;

public interface DbUpdater {

    String NAME = "cuba_DbUpdater";

    void updateDatabase();

    List<String> findUpdateDatabaseScripts() throws DBNotInitializedException;
}
