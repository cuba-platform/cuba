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

public interface PopupButton extends Component.ActionsHolder,
        Component.HasCaption, Component.BelongToFrame, Component.Expandable, Component.HasIcon {

    String NAME = "popupButton";

    boolean isPopupVisible();

    void setPopupVisible(boolean popupVisible);

    void setMenuWidth(String width);

    boolean isAutoClose();

    void setAutoClose(boolean autoClose);
}
