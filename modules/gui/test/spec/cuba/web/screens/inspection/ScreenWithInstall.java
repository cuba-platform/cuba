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

package spec.cuba.web.screens.inspection;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.Install;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;

@SuppressWarnings({"unused", "CubaInstalledDelegateInspection"})
public class ScreenWithInstall extends Screen {

    @Install(subject = "formatter", to = "label1")
    public String format(Date date) {
        return "";
    }

    @Install(type = Table.StyleProvider.class, to = "usersTable")
    protected String getCellStyleName(User user, String property) {
        return "red";
    }

    @Install
    private String getData() {
        return "";
    }

    @Install
    protected void runnableMethod() {
    }

    @Install(to = "button1")
    protected void consumeEvent(Button.ClickEvent event) {
    }
}