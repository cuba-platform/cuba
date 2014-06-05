/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;

/**
 * @author artamonov
 * @version $Id$
 */
public interface BulkEditor extends Component, Component.HasCaption, Component.BelongToFrame, Component.HasIcon {

    String NAME = "bulkEditor";
    String PERMISSION = "cuba.gui.bulkEdit";

    WindowManager.OpenType getOpenType();
    void setOpenType(WindowManager.OpenType openType);

    String getExcludePropertiesRegex();
    void setExcludePropertiesRegex(String excludeRegex);

    ListComponent getListComponent();
    void setListComponent(ListComponent listComponent);
}