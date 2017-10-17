/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.client;

import com.haulmont.cuba.core.app.AttributeAccessService;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SecurityState;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Client layer bean that updates security state of entity instances.
 */
@Component(AttributeAccessUpdater.NAME)
public class AttributeAccessUpdater {

    public static final String NAME = "cuba_AttributeAccessUpdater";

    @Inject
    protected AttributeAccessService service;

    /**
     * Updates security state of a given entity instance.
     *
     * @param entity instance
     */
    public void updateAttributeAccess(Entity entity) {
        SecurityState securityState = service.computeSecurityState(entity);
        BaseEntityInternalAccess.setSecurityState(entity, securityState);
    }
}
