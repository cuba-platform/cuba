/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.samples;

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