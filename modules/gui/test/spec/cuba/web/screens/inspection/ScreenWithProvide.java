/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.inspection;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.Provide;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;

public class ScreenWithProvide extends Screen {

    @Provide(subject = "formatter", to = "label1")
    public String format(Date date) {
        return "";
    }

    @Provide(type = Table.StyleProvider.class, to = "usersTable")
    protected String getCellStyleName(User user, String property) {
        return "red";
    }

    @Provide
    private String getData() {
        return "";
    }

    @Provide
    protected void ignoredMethod() {
    }

    @Provide("button1")
    protected void consumeEvent(Button.ClickEvent event) {

    }
}