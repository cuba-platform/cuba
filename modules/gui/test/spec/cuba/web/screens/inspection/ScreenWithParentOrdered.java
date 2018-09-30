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
import com.haulmont.cuba.gui.screen.Subscribe;
import org.springframework.core.annotation.Order;

public class ScreenWithParentOrdered extends ScreenWithOrderedSubscribe {

    // parent @Subscribe first
    @Override
    protected void onAfterInit(AfterInitEvent event) {
        super.onAfterInit(event);
    }

    @Subscribe
    protected void childAfterInit(AfterInitEvent event) {

    }

    @Order(-10)
    @Subscribe("btn1")
    protected void onClick6(Button.ClickEvent event) {

    }

    @Order(15)
    @Subscribe("btn1")
    protected void onClick7(Button.ClickEvent event) {

    }
}