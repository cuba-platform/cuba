/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;

import javax.annotation.Nullable;

/**
 * @author artamonov
 * @version $Id$
 */
public interface RelatedEntities extends Component, Component.HasCaption, Component.BelongToFrame {

    String NAME = "relatedEntities";

    WindowManager.OpenType getOpenType();
    void setOpenType(WindowManager.OpenType openType);

    String getExcludePropertiesRegex();
    void setExcludePropertiesRegex(String excludeRegex);

    void addPropertyOption(String property, @Nullable String screen, @Nullable String caption, @Nullable String filterCaption);
    void removePropertyOption(String property);

    ListComponent getListComponent();
    void setListComponent(ListComponent listComponent);
}