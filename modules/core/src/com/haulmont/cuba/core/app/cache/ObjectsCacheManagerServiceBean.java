/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@Service(ObjectsCacheManagerService.NAME)
public class ObjectsCacheManagerServiceBean implements ObjectsCacheManagerService {

    @Inject
    private ObjectsCacheManagerAPI managerAPI;

    @Override
    public void updateCache(String cacheName, Map<String, Object> params) {
        ObjectsCacheController controller = managerAPI.getController(cacheName);
        controller.updateCache(params);
    }
}