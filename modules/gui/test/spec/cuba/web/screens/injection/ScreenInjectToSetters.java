/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.Screen;

import javax.inject.Inject;

public class ScreenInjectToSetters extends Screen {

    private Button button;
    private Messages messages;

    @Inject
    public void setButton(Button button) {
        this.button = button;
    }

    @Inject
    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}