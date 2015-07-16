/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter.edit;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;

/**
 * @author gorelov
 * @version $Id$
 */
public class DesktopFilterEditorCompanion implements FilterEditor.Companion {
    @Override
    public void showComponentName(WindowManager windowManager, String title, String message) {
        windowManager.showMessageDialog(title,
                String.format("<input value=\"%s\" size=\"40\"/>", message),
                IFrame.MessageType.CONFIRMATION_HTML);
    }
}
