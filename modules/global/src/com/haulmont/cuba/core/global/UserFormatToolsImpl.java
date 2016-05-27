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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

@Component(UserFormatTools.NAME)
public class UserFormatToolsImpl implements UserFormatTools {

    @Inject
    protected Messages messages;

    @Override
    public String formatOfficial(User user) {
        return StringUtils.defaultIfBlank(user.getName(), user.getLogin());
    }

    @Override
    public String formatSubstitution(@Nonnull User user, @Nullable User substitutedUser) {
        Preconditions.checkNotNullArgument(user);

        if (substitutedUser == null || user.equals(substitutedUser)) {
            return formatOfficial(user);
        } else {
            return messages.formatMessage(getClass(), "onBehalfOf",
                    formatOfficial(user),
                    formatOfficial(substitutedUser));
        }
    }

    @Override
    public String formatUser(@Nonnull User user, @Nullable User substitutedUser) {
        if (substitutedUser != null && !ObjectUtils.equals(user, substitutedUser)) {
            return formatSubstitution(user, substitutedUser);
        } else {
            return formatOfficial(user);
        }
    }
}