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

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.config.ConfigStorageCommon;
import com.haulmont.cuba.web.auth.WebAuthConfig;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("cuba_ConfigStorageMBean")
public class ConfigStorage implements ConfigStorageMBean {

    @Inject
    protected ConfigStorageCommon configStorageCommon;

    @Inject
    protected WebAuthConfig webAuthConfig;

    @Override
    public String printAppProperties() {
        return printAppProperties(null);
    }

    @Override
    public String printAppProperties(String prefix) {
        return configStorageCommon.printAppProperties(prefix);
    }

    @Override
    public String getAppProperty(String name) {
        return configStorageCommon.getAppProperty(name);
    }

    @Override
    public String setAppProperty(String name, String value) {
        return configStorageCommon.setAppProperty(name, value);
    }

    @Override
    public String getConfigValue(String classFQN, String methodName, String userLogin) {
        String trustedPassword = webAuthConfig.getTrustedClientPassword();
        return configStorageCommon.getConfigValue(classFQN, methodName, userLogin, trustedPassword);
    }
}