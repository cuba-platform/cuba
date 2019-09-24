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

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.core.entity.SecurityState;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestEmbeddableEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AttributeAccessSupportTest {

    @Test
    public void getEmbeddableEntitySecurityStateTest() {
        EmbeddableEntity entity = new TestEmbeddableEntity();
        SecurityState securityState = BaseEntityInternalAccess.getOrCreateSecurityState(entity);

        BaseEntityInternalAccess.setSecurityState(entity, securityState);

        AttributeAccessSupport attributeAccessSupport = new AttributeAccessSupport();
        Assertions.assertNotNull(
                attributeAccessSupport.getSecurityState(entity),
                "com.haulmont.cuba.gui.AttributeAccessSupport#getSecurityState returns null"
        );
    }
}
