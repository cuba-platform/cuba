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

package com.haulmont.cuba.security.role;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;

public class EntityAttributeAccessPermissions extends Permissions {

    private static final long serialVersionUID = -4057315403116500344L;

    public boolean isReadOperationPermitted(MetaClass metaClass, String property) {
        return PermissionsUtils.isAttributeReadOperationPermitted(this, metaClass, property);
    }

    public boolean isReadOperationPermitted(MetaClass metaClass, MetaProperty property) {
        return PermissionsUtils.isAttributeReadOperationPermitted(this, metaClass, property.getName());
    }

    public boolean isModifyOperationPermitted(MetaClass metaClass, String property) {
        return PermissionsUtils.isAttributeModifyOperationPermitted(this, metaClass, property);
    }

    public boolean isModifyOperationPermitted(MetaClass metaClass, MetaProperty property) {
        return PermissionsUtils.isAttributeModifyOperationPermitted(this, metaClass, property.getName());
    }
}
