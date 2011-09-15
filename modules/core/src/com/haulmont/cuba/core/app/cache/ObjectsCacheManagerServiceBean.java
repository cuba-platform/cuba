/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@Service(ObjectsCacheManagerService.NAME)
public class ObjectsCacheManagerServiceBean implements ObjectsCacheManagerService{

    @Inject
    private ObjectsCacheManagerAPI managerAPI;

    public Collection<ObjectsCacheInstance> getActiveInstances() {
        return managerAPI.getActiveInstances();
    }
}