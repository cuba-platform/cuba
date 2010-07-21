/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 20.07.2010 14:39:14
 *
 * $Id$
 */
package cuba.client.web.ui.report.edit;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.report.ReportScreen;

import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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

        LookupField screenLookupField = getComponent("screenId");
        Collection<WindowInfo> windowInfoCollection = AppConfig.getInstance().getWindowConfig().getWindows();
        List<String> screenAliases = new ArrayList<String>();
        for (WindowInfo windowInfo : windowInfoCollection) {
            screenAliases.add(windowInfo.getId());
        }
        screenLookupField.setOptionsList(screenAliases);
        screenLookupField.addListener(new ValueListener() {
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                reportScreen.setScreenId(value != null ? value.toString() : null);
            }
        });

    }
}
