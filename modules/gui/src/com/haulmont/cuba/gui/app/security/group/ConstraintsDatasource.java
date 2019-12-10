/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.app.security.group;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.group.AccessGroupsService;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class ConstraintsDatasource extends CustomCollectionDatasource<Constraint, UUID> {
    protected AccessGroupsService groupsService = AppBeans.get(AccessGroupsService.NAME);

    @Override
    protected Collection<Constraint> getEntities(Map<String, Object> params) {
        return groupsService.getGroupConstraints((Group) params.get("group"));
    }
}
