/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface TwinColumn extends OptionsField {

    String NAME = "twinColumn";

    int getColumns();
    void setColumns(int columns);

    int getRows();
    void setRows(int rows);

    void setStyleProvider(StyleProvider styleProvider);

    void setAddAllBtnEnabled(boolean enabled);
    boolean isAddAllBtnEnabled();

    interface StyleProvider {
        String getStyleName(Entity item, Object property, boolean selected);
        String getItemIcon(Entity item, boolean selected);
    }
}