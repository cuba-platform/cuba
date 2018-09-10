/*
 * Copyright (c) 2008-2018 Haulmont.
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

package spec.cuba.web.screens.samples;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.Target;

public class ScreenWithSubscribe extends Screen {

    @Subscribe("btn1")
    public void onClick(Button.ClickEvent event) {
    }

    @Subscribe
    private void onShow(BeforeShowEvent event) {
    }

    @Subscribe(target = Target.WINDOW)
    protected void onAfterShow(AfterShowEvent event) {

    }

    @Subscribe
    private void init(InitEvent event) {
    }
}