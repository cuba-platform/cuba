/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.security.role;

import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import junit.framework.TestCase;
import org.junit.Test;

public class PermissionUtilsTest extends TestCase {

    @Test
    public void testEntityAttrWildcardPermissionValue() {
        EntityAttributePermissionsContainer permissionsContainer = new EntityAttributePermissionsContainer();
        permissionsContainer.getExplicitPermissions().put("petclinic_Visit:*", EntityAttrAccess.VIEW.getId());
        Integer wildcardPermissionValue = PermissionsUtils.getWildcardPermissionValue(permissionsContainer, "petclinic_Visit:pet");
        assertEquals(EntityAttrAccess.VIEW.getId(), wildcardPermissionValue);
    }

    @Test
    public void testEntityWildcardPermissionValue() {
        EntityPermissionsContainer permissionsContainer = new EntityPermissionsContainer();
        permissionsContainer.getExplicitPermissions().put("*:create", Access.ALLOW.getId());
        Integer wildcardPermissionValue = PermissionsUtils.getWildcardPermissionValue(permissionsContainer, "created_Visit:create");
        assertEquals(Access.ALLOW.getId(), wildcardPermissionValue);
    }
}
