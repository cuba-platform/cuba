/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.testsupport;

import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.config.AppPropertyEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConfigStorageServiceProxy implements ConfigStorageService {

    @Override
    public Map<String, String> getDbProperties() {
        return Collections.emptyMap();
    }

    @Override
    public String getDbProperty(String name) {
        return null;
    }

    @Override
    public void setDbProperty(String name, String value) {

    }

    @Override
    public List<AppPropertyEntity> getAppProperties() {
        return Collections.emptyList();
    }
}
