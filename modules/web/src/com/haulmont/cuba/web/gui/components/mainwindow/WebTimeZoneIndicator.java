/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeZones;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.mainwindow.TimeZoneIndicator;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.vaadin.ui.Label;
import org.apache.commons.lang.StringUtils;

import java.util.TimeZone;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebTimeZoneIndicator extends WebAbstractComponent<Label> implements TimeZoneIndicator {

    protected String styleName;

    public WebTimeZoneIndicator() {
        component = new Label();
        component.setSizeUndefined();
        component.setStyleName("cuba-user-timezone-label");

        UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);
        TimeZone timeZone = uss.getUserSession().getTimeZone();
        TimeZones timeZones = AppBeans.get(TimeZones.NAME);
        component.setValue(timeZones.getDisplayNameShort(timeZone));
    }

    @Override
    public void setStyleName(String styleName) {
        if (StringUtils.isNotEmpty(this.styleName)) {
            getComposition().removeStyleName(this.styleName);
        }

        this.styleName = styleName;

        if (StringUtils.isNotEmpty(styleName)) {
            getComposition().addStyleName(styleName);
        }
    }
}