/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Service(ConfigStorageService.NAME)
public class ConfigStorageServiceBean implements ConfigStorageService {

    @Inject
    private ConfigStorageAPI api;

    @Override
    public String getConfigProperty(String name) {
        return api.getConfigProperty(name);
    }

    @Override
    public void setConfigProperty(String name, String value) {
        api.setConfigProperty(name, value);
    }
}
