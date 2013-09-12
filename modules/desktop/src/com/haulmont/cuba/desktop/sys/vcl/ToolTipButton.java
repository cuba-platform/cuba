/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
