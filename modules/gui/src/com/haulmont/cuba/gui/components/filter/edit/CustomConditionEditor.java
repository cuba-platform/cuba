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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.theme.ThemeConstants;

import javax.inject.Inject;
import java.util.Map;

/**
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

        getDialogOptions().setWidth(theme.getInt("cuba.gui.customConditionEditor.dialog.width"));

        hideUnnecessaryFields();
    }

    protected void hideUnnecessaryFields() {
        hideComponent(customConditionFrame, "width");
        hideComponent(customConditionFrame, "widthLabel");
        hideComponent(customConditionFrame, "widthHelp");
        hideComponent(customConditionFrame, "hidden");
        hideComponent(customConditionFrame, "hiddenLabel");
        hideComponent(customConditionFrame, "hiddenHelp");
        hideComponent(customConditionFrame, "required");
        hideComponent(customConditionFrame, "requiredLabel");
        hideComponent(customConditionFrame, "requiredHelp");
        hideComponent(customConditionFrame, "defaultValueLayout");
        hideComponent(customConditionFrame, "defaultValueLayoutLabel");
        hideComponent(customConditionFrame, "defaultValueLayoutHelp");
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