/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.popupbutton;

import com.vaadin.shared.annotations.NoLayout;
import org.vaadin.hene.popupbutton.widgetset.client.ui.PopupButtonState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaPopupButtonState extends PopupButtonState {

    @NoLayout
    public boolean autoClose = true;

    @NoLayout
    public boolean customLayout = false;
}