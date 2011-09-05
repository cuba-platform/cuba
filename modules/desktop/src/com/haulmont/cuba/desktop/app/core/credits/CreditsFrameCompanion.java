/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.app.core.credits;

import com.haulmont.cuba.gui.app.core.credits.CreditsFrame;
import com.haulmont.cuba.gui.app.core.credits.CreditsItem;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LinkButton;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
                try {
                    Desktop.getDesktop().browse(new URI(item.getWebPage()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
