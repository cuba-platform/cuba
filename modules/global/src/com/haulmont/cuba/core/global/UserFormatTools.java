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

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface UserFormatTools {

    String NAME = "cuba_UserFormatTools";

    String formatSubstitution(@Nonnull User user, @Nullable User substitutedUser);

    String formatOfficial(User user);

    String formatUser(@Nonnull User user, @Nullable User substitutedUser);
}