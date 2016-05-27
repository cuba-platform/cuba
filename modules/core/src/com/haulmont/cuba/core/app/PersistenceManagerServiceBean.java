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

import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(PersistenceManagerService.NAME)
public class PersistenceManagerServiceBean implements PersistenceManagerService {

    @Inject
    private PersistenceManagerAPI pm;

    @Override
    public boolean useLazyCollection(String entityName) {
        return pm.useLazyCollection(entityName);
    }

    @Override
    public boolean useLookupScreen(String entityName) {
        return pm.useLookupScreen(entityName);
    }

    @Override
    public int getFetchUI(String entityName) {
        return pm.getFetchUI(entityName);
    }

    @Override
    public int getMaxFetchUI(String entityName) {
        return pm.getMaxFetchUI(entityName);
    }

    @Override
    public String getDbmsType() {
        return DbmsType.getType();
    }

    @Override
    public String getDbmsVersion() {
        return DbmsType.getVersion();
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return DbmsSpecificFactory.getDbmsFeatures().getUniqueConstraintViolationPattern();
    }

    @Override
    public boolean isNullsLastSorting() {
        return DbmsSpecificFactory.getDbmsFeatures().isNullsLastSorting();
    }
}