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

package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

public final class RelatedEntitiesSecurity {

    private RelatedEntitiesSecurity() {
    }

    public static boolean isSuitableProperty(MetaProperty metaProperty, MetaClass effectiveMetaClass) {
        if (metaProperty.getRange().isClass()
                && !Category.class.isAssignableFrom(metaProperty.getJavaType())) {

            Security security = AppBeans.get(Security.NAME);
            // check security
            if (security.isEntityAttrPermitted(effectiveMetaClass, metaProperty.getName(), EntityAttrAccess.VIEW)
                    && security.isEntityOpPermitted(metaProperty.getRange().asClass(), EntityOp.READ)) {
                return true;
            }
        }
        return false;
    }
}