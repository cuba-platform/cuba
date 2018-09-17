/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.web.app.ui.demo.user;

import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;

public interface UserListMixin {
    @Subscribe
    default void initLogo(Screen.InitEvent event) {
        System.out.println(1);
    }
}