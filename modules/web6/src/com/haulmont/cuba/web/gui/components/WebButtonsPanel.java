/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.ButtonsPanel;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebButtonsPanel extends WebHBoxLayout implements ButtonsPanel {

    public WebButtonsPanel() {
        setSpacing(true);
        setMargin(false);
    }
}