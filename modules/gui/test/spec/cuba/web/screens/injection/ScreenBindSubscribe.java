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

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;

public class ScreenBindSubscribe extends Screen {
    @Subscribe
    private void init(InitEvent event) {

    }

    // private is also supported
    @Subscribe("btn1")
    private void onBtn1Click(Button.ClickEvent event) {

    }

    @Subscribe("textField1")
    private void onValueChange(HasValue.ValueChangeEvent<String> event) {

    }

    @Subscribe("split1")
    private void onSplitChange(SplitPanel.SplitPositionChangeEvent event) {

    }

    @Subscribe("commit")
    protected void onCommit(Action.ActionPerformedEvent event) {

    }
}