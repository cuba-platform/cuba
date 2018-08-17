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

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.gui.Screens.LaunchMode;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.Collections;
import java.util.Map;

public class WindowContextImpl extends FrameContextImpl implements WindowContext {

    private final LaunchMode launchMode;
    private final ScreenOptions options;

    public WindowContextImpl(Frame window, LaunchMode launchMode, ScreenOptions options) {
        super(window, getParams(options));
        this.launchMode = launchMode;
        this.options = options;
    }

    @Override
    public LaunchMode getLaunchMode() {
        return launchMode;
    }

    @Override
    public ScreenOptions getOptions() {
        return options;
    }

    private static Map<String, Object> getParams(ScreenOptions options) {
        if (options instanceof MapScreenOptions) {
            return ((MapScreenOptions) options).getParams();
        }
        return Collections.emptyMap();
    }
}