/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.security.entity.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author kovalenko
 * @version $Id$
 */
public interface UserFormatTools {

    public static final String NAME = "cuba_UserFormatTools";

    String formatSubstitution(@Nonnull User user, @Nullable User substitutedUser);

    String formatOfficial(User user);

    String formatUser(@Nonnull User user, @Nullable User substitutedUser);

}
