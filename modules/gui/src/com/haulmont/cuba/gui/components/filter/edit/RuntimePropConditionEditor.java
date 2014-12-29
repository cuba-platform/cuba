/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.filter.condition.RuntimePropCondition;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author gorbunkov
 * @version $Id$
 */

public class RuntimePropConditionEditor extends AbstractWindow {

    @Inject
    protected RuntimePropConditionFrame runtimePropConditionFrame;

    @Inject
    protected ThemeConstants theme;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams()
                .setWidth(theme.getInt("cuba.gui.runtimePropConditionEditor.dialog.width"))
                .setResizable(true);

        hideUnnecessaryFields();
    }

    protected void hideUnnecessaryFields() {
        hideComponent(runtimePropConditionFrame, "width");
        hideComponent(runtimePropConditionFrame, "widthLabel");
        hideComponent(runtimePropConditionFrame, "hidden");
        hideComponent(runtimePropConditionFrame, "hiddenLabel");
        hideComponent(runtimePropConditionFrame, "required");
        hideComponent(runtimePropConditionFrame, "requiredLabel");
        hideComponent(runtimePropConditionFrame, "defaultValueLayout");
        hideComponent(runtimePropConditionFrame, "defaultValueLayoutLabel");
    }

    protected void hideComponent(AbstractFrame parentFrame, String componentId) {
        Component component = parentFrame.getComponent(componentId);
        if (component != null)
            component.setVisible(false);
    }

    public void commit() {
        if (runtimePropConditionFrame.commit()) {
            close(COMMIT_ACTION_ID);
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}
