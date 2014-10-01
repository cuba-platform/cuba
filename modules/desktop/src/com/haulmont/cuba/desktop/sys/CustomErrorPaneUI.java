/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import org.jdesktop.swingx.plaf.basic.BasicErrorPaneUI;

/**
 * @author zlatoverov
 * @version $Id$
 */
public class CustomErrorPaneUI extends BasicErrorPaneUI {

    public void setEnabled(boolean enabled) {
        if (reportButton != null) {
            reportButton.setEnabled(enabled);
        }
    }
}