/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class CustomConditionEditor extends AbstractWindow {

    @Inject
    private CustomConditionFrame customConditionFrame;

    @Inject
    protected ThemeConstants theme;

    @Inject
    protected ClientConfig clientConfig;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidth(theme.getInt("cuba.gui.customConditionEditor.dialog.width"));

        hideUnnecessaryFields();
    }

    protected void hideUnnecessaryFields() {
        hideComponent(customConditionFrame, "width");
        hideComponent(customConditionFrame, "widthLabel");
        hideComponent(customConditionFrame, "hidden");
        hideComponent(customConditionFrame, "hiddenLabel");
        hideComponent(customConditionFrame, "required");
        hideComponent(customConditionFrame, "requiredLabel");
        hideComponent(customConditionFrame, "defaultValueLayout");
        hideComponent(customConditionFrame, "defaultValueLayoutLabel");
    }

    protected void hideComponent(AbstractFrame parentFrame, String componentId) {
        Component component = parentFrame.getComponent(componentId);
        if (component != null)
            component.setVisible(false);
    }

    public void commit() {
        if (!validateAll()) {
            return;
        }
        if (customConditionFrame.commit()) {
            close(COMMIT_ACTION_ID);
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}