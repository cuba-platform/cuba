/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.screens.injection;

import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.Provide;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;

public class ScreenBindProvide extends Screen {

    @Provide(subject = "formatter", to = "label1")
    private String format(Date date) {
        return "formatted-date";
    }

    @Provide(subject = "styleProvider", to = "usersTable")
    private String getStyleName(User user, String columnId) {
        return "awesome-style";
    }

    @Provide(type = Table.StyleProvider.class, to = "groupTable")
    public String getGroupStyle(User user, String columnId){
        return "ok-style";
    }

    @Provide(subject = "iconProvider", to = "tree")
    public String getIcon(User user) {
        return "ok.png";
    }
}