/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.samples;

import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;

public interface ScreenEventMixin {

    @Subscribe
    default void onInitMixin(Screen.InitEvent event) {

    }
}