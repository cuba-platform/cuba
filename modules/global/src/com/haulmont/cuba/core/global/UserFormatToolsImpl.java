/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author kovalenko
 * @version $Id$
 */
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
