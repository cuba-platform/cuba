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

import com.haulmont.cuba.security.app.EntityLogAPI;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

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