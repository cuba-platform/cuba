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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.cuba.gui.model.LoadBeforeShowStrategy;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.LoadDataBeforeShow;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;

/**
 * Default implementation of {@code LoadBeforeShowStrategy} that triggers all loaders if {@link LoadDataBeforeShow}
 * annotation is present on the controller class.
 */
public class AnnotationBasedLoaderBeforeShow implements LoadBeforeShowStrategy {

    @Override
    public void loadData(Screen screen) {
        LoadDataBeforeShow annotation = screen.getClass().getAnnotation(LoadDataBeforeShow.class);
        if (annotation != null && annotation.value()) {
            ScreenData screenData = UiControllerUtils.getScreenData(screen);
            screenData.loadAll();
        }
    }
}
