/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2009 17:40:39
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.security.entity.SearchFolder;

import java.util.List;

public interface FoldersService {

    String NAME = "cuba_FoldersService";

    @Deprecated
    String JNDI_NAME = NAME;

    List<AppFolder> loadAppFolders();

    List<AppFolder> reloadAppFolders(List<AppFolder> folders);

    List<SearchFolder> loadSearchFolders();
}
