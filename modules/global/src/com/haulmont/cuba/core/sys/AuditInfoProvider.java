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

package com.haulmont.cuba.core.sys;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * INTERNAL.
 * Provides information about current user for audit purposes -
 * Creatable, Updatable, SoftDelete entities and for Entity Log.
 */
public interface AuditInfoProvider {

    String NAME = "cuba_AuditInfoProvider";

    @Nullable
    String getCurrentUserLogin();

    @Nullable
    UUID getCurrentUserId();
}
