/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.role;

import com.haulmont.cuba.gui.app.security.role.edit.PermissionsLookup;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

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
        WebComponentsHelper.unwrap(tree).addStyleName("empty");
    }

    @Override
    public void initPermissionsTreeComponents(BoxLayout box, Label label, CheckBox checkBox) {
    }
}
