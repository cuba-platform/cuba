/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.LinkButton;
import org.jdesktop.swingx.JXHyperlink;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopLinkButton extends DesktopButton implements LinkButton {
    @Override
    protected JButton createImplementation() {
        final JXHyperlink link = new JXHyperlink();
        return link;
    }
}
