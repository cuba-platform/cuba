/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.edit;

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

public class DynamicAttributesConditionEditor extends AbstractWindow {

    @Inject
    protected DynamicAttributesConditionFrame dynamicAttributeConditionFrame;

    @Inject
    protected ThemeConstants theme;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams()
                .setWidth(theme.getInt("cuba.gui.dynamicAttributeConditionEditor.dialog.width"))
                .setResizable(true);

        hideUnnecessaryFields();
    }

    protected void hideUnnecessaryFields() {
        hideComponent(dynamicAttributeConditionFrame, "width");
        hideComponent(dynamicAttributeConditionFrame, "widthLabel");
        hideComponent(dynamicAttributeConditionFrame, "hidden");
        hideComponent(dynamicAttributeConditionFrame, "hiddenLabel");
        hideComponent(dynamicAttributeConditionFrame, "required");
        hideComponent(dynamicAttributeConditionFrame, "requiredLabel");
        hideComponent(dynamicAttributeConditionFrame, "defaultValueLayout");
        hideComponent(dynamicAttributeConditionFrame, "defaultValueLayoutLabel");
    }

    protected void hideComponent(AbstractFrame parentFrame, String componentId) {
        Component component = parentFrame.getComponent(componentId);
        if (component != null)
            component.setVisible(false);
    }

    public void commit() {
        if (dynamicAttributeConditionFrame.commit()) {
            close(COMMIT_ACTION_ID);
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}
