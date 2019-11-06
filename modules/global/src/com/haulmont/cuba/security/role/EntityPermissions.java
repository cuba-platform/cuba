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

public class EntityPermissions extends Permissions {

    private static final long serialVersionUID = 1810208523892912417L;

    public boolean isReadOperationPermitted(MetaClass metaClass) {
        return  PermissionsUtils.isReadOperationPermitted(this, metaClass);
    }

    public boolean isCreateOperationPermitted(MetaClass metaClass) {
        return  PermissionsUtils.isCreateOperationPermitted(this, metaClass);
    }

    public boolean isUpdateOperationPermitted(MetaClass metaClass) {
        return  PermissionsUtils.isUpdateOperationPermitted(this, metaClass);
    }

    public boolean isDeleteOperationPermitted(MetaClass metaClass) {
        return  PermissionsUtils.isDeleteOperationPermitted(this, metaClass);
    }

}
