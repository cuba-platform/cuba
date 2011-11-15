/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.ui.report.group.edit

import com.haulmont.cuba.gui.components.AbstractEditor
import com.haulmont.cuba.gui.components.IFrame

/**
 * 
 * <p>$Id$</p>
 *
 * @author artamonov
 */
class ReportGroupEditor extends AbstractEditor {

    ReportGroupEditor() {
        prepareDialogMode()
    }

    ReportGroupEditor(IFrame frame) {
        super(frame)
        prepareDialogMode()
    }

    private def prepareDialogMode() {
        getDialogParams().setResizable(false)
        getDialogParams().setWidth(300)
    }
}