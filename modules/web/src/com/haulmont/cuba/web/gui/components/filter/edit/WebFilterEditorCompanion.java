/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter.edit;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.edit.FilterEditor;

/**
 * @author gorelov
 * @version $Id$
 */
public class WebFilterEditorCompanion implements FilterEditor.Companion {
    @Override
    public void showComponentName(WindowManager windowManager, String title, String message) {
        windowManager.showMessageDialog(title, message, IFrame.MessageType.CONFIRMATION);
    }
}
