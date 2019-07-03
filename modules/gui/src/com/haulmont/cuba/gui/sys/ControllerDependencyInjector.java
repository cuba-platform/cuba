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

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.gui.components.compatibility.LegacyFragmentAdapter;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.ScreenOptions;

/**
 * Implementations of the interface are used for wiring of fields/setters to the screen controllers
 */
public interface ControllerDependencyInjector {

    /**
     * The method is invoked when the screen instance is created
     */
    void inject(InjectionContext injectionContext);

    class InjectionContext {

        protected FrameOwner frameOwner;
        protected ScreenOptions screenOptions;

        public InjectionContext(FrameOwner frameOwner, ScreenOptions screenOptions) {
            // support legacy windows inside of fragments
            if (!(frameOwner instanceof LegacyFragmentAdapter)) {
                this.frameOwner = frameOwner;
            } else {
                this.frameOwner = ((LegacyFragmentAdapter) frameOwner).getRealScreen();
            }
            this.screenOptions = screenOptions;
        }

        public FrameOwner getFrameOwner() {
            return frameOwner;
        }

        public void setFrameOwner(FrameOwner frameOwner) {
            this.frameOwner = frameOwner;
        }

        public ScreenOptions getScreenOptions() {
            return screenOptions;
        }

        public void setScreenOptions(ScreenOptions screenOptions) {
            this.screenOptions = screenOptions;
        }
    }
}
