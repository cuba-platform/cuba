/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.Map;

/**
 */

public class DynamicAttributesConditionEditor extends AbstractWindow {

    @Inject
    protected DynamicAttributesConditionFrame dynamicAttributesConditionFrame;

    @Inject
    protected ThemeConstants theme;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions().setWidth(400);

        hideUnnecessaryFields();
    }

    protected void hideUnnecessaryFields() {
        hideComponent(dynamicAttributesConditionFrame, "width");
        hideComponent(dynamicAttributesConditionFrame, "widthLabel");
        hideComponent(dynamicAttributesConditionFrame, "hidden");
        hideComponent(dynamicAttributesConditionFrame, "hiddenLabel");
        hideComponent(dynamicAttributesConditionFrame, "required");
        hideComponent(dynamicAttributesConditionFrame, "requiredLabel");
        hideComponent(dynamicAttributesConditionFrame, "defaultValueLayout");
        hideComponent(dynamicAttributesConditionFrame, "defaultValueLayoutLabel");
    }

    protected void hideComponent(AbstractFrame parentFrame, String componentId) {
        Component component = parentFrame.getComponent(componentId);
        if (component != null)
            component.setVisible(false);
    }

    public void commit() {
        if (dynamicAttributesConditionFrame.commit()) {
            close(COMMIT_ACTION_ID);
        }
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }
}
