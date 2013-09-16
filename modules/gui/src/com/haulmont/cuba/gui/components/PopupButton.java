/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author pavlov
 * @version $Id$
 */
public interface PopupButton extends Component.ActionsHolder,
        Component.HasCaption, Component.BelongToFrame, Component.HasIcon {

    String NAME = "popupButton";

    boolean isPopupVisible();

    void setPopupVisible(boolean popupVisible);

    void setMenuWidth(String width);

    boolean isAutoClose();

    void setAutoClose(boolean autoClose);
}