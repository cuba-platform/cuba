/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 20.07.2010 14:39:14
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui.report.screen.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.report.ReportScreen;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReportScreenEditor extends AbstractEditor {
    private static final long serialVersionUID = -5176428889018585775L;
    private ReportScreen reportScreen;

    public ReportScreenEditor(IFrame frame) {
        super(frame);
    }

    @Override
    public void setItem(Entity item) {
        super.setItem(item);
        reportScreen = (ReportScreen) getItem();
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        final LookupField screenLookupField = getComponent("screenId");
        Collection<WindowInfo> windowInfoCollection = AppConfig.getInstance().getWindowConfig().getWindows();
        Map<String, Object> screens = new HashMap<String, Object>();
        for (WindowInfo windowInfo : windowInfoCollection) {
            String id = windowInfo.getId();
            String menuId = "menu-config." + id;
            String localeMsg = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), menuId);
            String title = menuId.equals(localeMsg) ? id : id + " ( " + localeMsg + " )";
            screens.put(title, id);
        }
        screenLookupField.setOptionsMap(screens);
        screenLookupField.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (value != null) {
                    String valueStr = value != null ? value.toString() : null;
                    reportScreen.setScreenId(valueStr);
                } else
                    reportScreen.setScreenId(null);
            }
        });

    }
}
