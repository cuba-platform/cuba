/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.10.2010 17:24:29
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(PersistenceManagerService.NAME)
public class PersistenceManagerServiceBean implements PersistenceManagerService {

    @Inject
    private PersistenceManagerAPI pm;

    public boolean useLazyCollection(String entityName) {
        return pm.useLazyCollection(entityName);
    }

    public int getFetchUI(String entityName) {
        return pm.getFetchUI(entityName);
    }

    public int getMaxFetchUI(String entityName) {
        return pm.getMaxFetchUI(entityName);
    }
}
