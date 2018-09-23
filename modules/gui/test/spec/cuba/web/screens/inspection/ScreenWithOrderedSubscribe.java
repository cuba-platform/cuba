/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
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