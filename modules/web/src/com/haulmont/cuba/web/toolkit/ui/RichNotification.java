/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 08.07.2010 12:15:29
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

public class RichNotification extends Window.Notification {

    private Component layout;

    /**
     * indicates that delay to fade notification will be started immediately, not waiting for a mouse move etc.
     *
     */
    private boolean autoFade;
    
    private static final long serialVersionUID = 1860697230584695854L;

    public RichNotification(Component layout) {
        super("");
        this.layout = layout;
    }

    public RichNotification(int type, Component layout) {
        super("", type);
        this.layout = layout;
    }

    public RichNotification(String caption, String description, int type) {
        super(caption, description, type);
    }

    public Component getLayout() {
        return layout;
    }

    public boolean isAutoFade() {
        return autoFade;
    }

    public void setAutoFade(boolean autoFade) {
        this.autoFade = autoFade;
    }
}
