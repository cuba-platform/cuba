/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.core.credits;

import com.haulmont.cuba.gui.app.core.credits.CreditsFrame;
import com.haulmont.cuba.gui.app.core.credits.CreditsItem;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LinkButton;
import com.haulmont.cuba.web.App;
import com.vaadin.terminal.ExternalResource;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CreditsFrameCompanion implements CreditsFrame.Companion {

    @Override
    public void initWebPageButton(LinkButton button, final CreditsItem item) {
        button.setAction(new AbstractAction("webpage") {
            @Override
            public void actionPerform(Component component) {
                App.getInstance().getAppWindow().open(new ExternalResource(item.getWebPage()), "_blank");
            }
        });
    }
}
