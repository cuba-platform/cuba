/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Gennady Pavlov
 * Created: 08.06.2010 13:56:12
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface PopupButton extends Button, Component.ActionsHolder {

    void setPopupComponent(Component component);

    void removePopupComponent();

    Component getPopupComponent();

    boolean isPopupVisible();

    void setPopupVisible(boolean popupVisible);

    void setMenuWidth(String width);

    boolean isAutoClose();

    void setAutoClose(boolean autoClose);
}
