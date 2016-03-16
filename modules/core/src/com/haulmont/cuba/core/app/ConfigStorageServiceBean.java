/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.AppPropertiesLocator;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 */
@Service(ConfigStorageService.NAME)
public class ConfigStorageServiceBean implements ConfigStorageService {

    @Inject
    private ConfigStorageAPI api;

    @Inject
    private AppPropertiesLocator appPropertiesLocator;

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

    @Override
    public List<AppPropertyEntity> getAppProperties() {
        return appPropertiesLocator.getAppProperties();
    }
}
