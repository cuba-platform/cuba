/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Sergey Shvaytser
 * Created: 17.03.2009 14:16:44
 *
 * $Id$
 */

package com.haulmont.cuba.web.ui;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;

public class SubTitleCap extends HorizontalLayout {

    protected HorizontalLayout titleLayout;

    public SubTitleCap()
    {
        setMargin(true);
        setSpacing(true);
        setWidth(100, Sizeable.UNITS_PERCENTAGE);
        setHeight(-1, Sizeable.UNITS_PIXELS); // TODO (abramov) This is a bit tricky

        titleLayout = new HorizontalLayout();
/*
        titleLayout.setMargin(true);
        titleLayout.setSpacing(true);
*/

//        titleLayout.setStyleName("saneco-subtitle-layout");

        final ThemeResource LOGO_SUBTITLE_ICON = new ThemeResource("icons/logo-subtitle-icon.png");

        Label logoTitle = new Label(getWindow().getCaption());

        addComponent(titleLayout);
        
        titleLayout.addComponent(new Embedded("", LOGO_SUBTITLE_ICON));
        titleLayout.addComponent(logoTitle);
        setSizeFull();
        setComponentAlignment(titleLayout, Alignment.MIDDLE_LEFT);
    }

}
