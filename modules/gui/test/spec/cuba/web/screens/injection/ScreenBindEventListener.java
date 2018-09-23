/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.gui.screen.Screen;
import org.springframework.context.event.EventListener;

public class ScreenBindEventListener extends Screen {
    @EventListener
    private void onGlobalEvent(TestGlobalEvent event) {

    }

    @EventListener(TestGlobalEvent.class)
    public void onEvent() {

    }
}