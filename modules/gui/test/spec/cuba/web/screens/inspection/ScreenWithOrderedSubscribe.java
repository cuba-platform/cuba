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
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import org.springframework.core.annotation.Order;

public class ScreenWithOrderedSubscribe extends Screen {
    @Subscribe
    protected void onAfterInit(AfterInitEvent event) {
        System.out.println(1);
    }

    @Subscribe
    protected void onInit(InitEvent event) {
    }

    @Order(0)
    @Subscribe("btn1")
    protected void onClick1(Button.ClickEvent event) {

    }

    @Order(10)
    @Subscribe("btn1")
    protected void onClick2(Button.ClickEvent event) {

    }

    // No @Order
    @Subscribe("btn2")
    protected void onClick3(Button.ClickEvent event) {

    }

    // No @Order
    @Subscribe("btn1")
    protected void onClick4(Button.ClickEvent event) {

    }
}