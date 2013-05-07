/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.08.2010 17:01:55
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;

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
