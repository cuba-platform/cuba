/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(ConfigStorageService.NAME)
public class ConfigStorageServiceBean implements ConfigStorageService {

    @Inject
    private ConfigStorageAPI api;

    @Override
    public Map<String, String> getDbProperties() {
        return api.getDbProperties();
    }

    @Override
    public String getDbProperty(String name) {
        return api.getDbProperty(name);
    }

    @Override
    public void setDbProperty(String name, String value) {
        api.setDbProperty(name, value);
    }
}
