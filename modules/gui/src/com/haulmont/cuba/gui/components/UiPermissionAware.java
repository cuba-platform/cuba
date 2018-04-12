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

package com.haulmont.cuba.gui.components;


import com.haulmont.cuba.gui.app.security.role.edit.UiPermissionDescriptor;

/**
 * State of subcomponents can be managed by UI permissions.
 */
public interface UiPermissionAware {

    /**
     * Change state of subcomponent according to the {@code permissionValue}.
     *
     * @param permissionDescriptor descriptor which contains id of subcomponent and UI permission value
     *                             which will be applied to this subcomponent or ids of subcomponent and its action
     *                             and UI permission value which will be applied to subcomponent's action
     */
    void applyPermission(UiPermissionDescriptor permissionDescriptor);
}