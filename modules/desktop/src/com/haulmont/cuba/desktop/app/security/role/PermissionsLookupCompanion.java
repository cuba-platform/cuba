/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.app.security.role;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.app.security.role.edit.PermissionsLookup;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Label;

import javax.swing.*;
import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class PermissionsLookupCompanion extends AbstractCompanion implements PermissionsLookup.Companion {

    public PermissionsLookupCompanion(AbstractFrame frame) {
        super(frame);
    }

    @Override
    public void initPermissionsTree(WidgetsTree tree) {
    }

    @Override
    public void initPermissionsTreeComponents(BoxLayout box, Label label, CheckBox checkBox) {
        JPanel jPanel = (JPanel) DesktopComponentsHelper.unwrap(box);
        jPanel.setBackground(Color.white);
    }
}
