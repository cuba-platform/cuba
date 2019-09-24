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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class SecurityTokenManagerTest {

    @RegisterExtension
    public static TestContainer testContainer = TestContainer.Common.INSTANCE;

    @Test
    @Disabled
    public void testSecurityToken() throws Exception {
        SecurityTokenManager securityTokenManager = AppBeans.get(SecurityTokenManager.class);
        User user = new User();
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        UUID id4 = UUID.randomUUID();
        securityTokenManager.addFiltered(user, "userRoles", id1);
        securityTokenManager.addFiltered(user, "userRoles", id2);
        securityTokenManager.addFiltered(user, "userRoles", id3);
        securityTokenManager.addFiltered(user, "userRoles", id4);

        securityTokenManager.writeSecurityToken(user);
        securityTokenManager.readSecurityToken(user);

        List<Object> userRoles = (List<Object>) BaseEntityInternalAccess.getFilteredData(user).get("userRoles");
        Assertions.assertEquals(4, userRoles.size());
        Assertions.assertEquals(id1, userRoles.get(0));
        Assertions.assertEquals(id2, userRoles.get(1));
        Assertions.assertEquals(id3, userRoles.get(2));
        Assertions.assertEquals(id4, userRoles.get(3));
    }
}