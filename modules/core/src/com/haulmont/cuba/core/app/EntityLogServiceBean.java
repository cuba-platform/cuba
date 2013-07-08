/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.security.app.EntityLogAPI;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author hasanov
 * @version $Id$
 */
@Service(EntityLogService.NAME)
public class EntityLogServiceBean implements EntityLogService {

    @Inject
    private EntityLogAPI entityLogAPI;

    @Override
    public boolean isEnabled() {
        return entityLogAPI.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        entityLogAPI.setEnabled(enabled);
    }

    @Override
    public void invalidateCache() {
        entityLogAPI.invalidateCache();
    }
}
