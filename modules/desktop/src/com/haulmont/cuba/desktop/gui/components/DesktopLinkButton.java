/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
