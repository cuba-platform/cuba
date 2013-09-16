/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;

/**
 * Button is used together with field to display field's tooltip
 *
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class ToolTipButton extends JXHyperlink {

    public ToolTipButton() {
        setText("[?]");
        setFocusable(false);
    }
}
