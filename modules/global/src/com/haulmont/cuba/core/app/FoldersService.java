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

import javax.ejb.Local;
import java.util.List;

@Local
public interface FoldersService {

    String JNDI_NAME = "cuba/core/FoldersService";

    List<AppFolder> loadAppFolders();

    List<SearchFolder> loadSearchFolders();
}
