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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;

@SuppressWarnings("unchecked")
public class WebScreenFacet<S extends Screen> extends WebAbstractScreenFacet<S> {

    @Override
    public S create() {
        Frame owner = getOwner();
        if (owner == null) {
            throw new IllegalStateException("Screen facet is not attached to Frame");
        }

        screen = createScreen(owner.getFrameOwner());

        initScreenListeners(screen);
        injectScreenProperties(screen, properties);

        return screen;
    }

    @Override
    public S show() {
        return (S) create().show();
    }

    protected S createScreen(FrameOwner frameOwner) {
        S screen;

        Screens screens = UiControllerUtils.getScreenContext(frameOwner)
                .getScreens();

        if (screenId != null) {
            screen = (S) screens.create(screenId, launchMode, getScreenOptions());
        } else if (screenClass != null) {
            screen = screens.create(screenClass, launchMode, getScreenOptions());
        } else {
            throw new DevelopmentException("Unable to open screen because no screen id or screen class are specified");
        }

        return screen;
    }
}
