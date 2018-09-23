/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
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

    @Subscribe("btn1")
    private void onBtn1Click(Button.ClickEvent event) {

    }

    @Subscribe("textField1")
    private void onValueChange(HasValue.ValueChangeEvent<String> event) {

    }

    @Subscribe("split1")
    protected void onSplitChange(SplitPanel.SplitPositionChangeEvent event) {

    }

    @Subscribe("commit")
    protected void onCommit(Action.ActionPerformedEvent event) {

    }
}