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

package com.haulmont.cuba.web.app.ui.jmxcontrol.inspect.attribute;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.app.ui.jmxcontrol.util.AttributeEditor;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.Map;

/**
 */
public class AttributeEditWindow extends AbstractEditor<ManagedBeanAttribute> {

    protected AttributeEditor valueHolder;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected GridLayout valueContainer;

    @Inject
    protected ThemeConstants themeConstants;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        getDialogOptions().setWidth(themeConstants.getInt("cuba.web.AttributeEditWindow.width"));
    }

    @Override
    protected void postInit() {
        ManagedBeanAttribute mba = getItem();
        final String type = mba.getType();

        valueHolder = new AttributeEditor(this, type, mba.getValue(), true, true);

        valueContainer.add(valueHolder.getComponent(), 1, 0);

        if (mba.getName() != null) {
            setCaption(formatMessage("editAttribute.title.format", mba.getName()));
        }
    }

    @Override
    public void commitAndClose() {
        if (assignValue()) {
            close(COMMIT_ACTION_ID, true);
        }
    }

    private boolean assignValue() {
        ManagedBeanAttribute mba = getItem();

        Object oldValue = mba.getValue();
        try {
            Object newValue = valueHolder != null ? valueHolder.getAttributeValue(false) : null;
            if (newValue != null) {
                if (!ObjectUtils.equals(mba.getValue(), newValue)) {
                    mba.setValue(newValue);
                    jmxControlAPI.saveAttributeValue(mba);
                }
                return true;
            }
        } catch (Exception e) {
            getDialogOptions().setWidth(themeConstants.getInt("cuba.web.AttributeEditWindow.messageDialog.width"));

            showMessageDialog(String.format(getMessage("editAttribute.exception"), mba.getName()),
                    e.getClass().getCanonicalName() + " " + e.getMessage() + "\n",
                    MessageType.WARNING);
            mba.setValue(oldValue);
            return false;
        }
        showNotification(getMessage("editAttribute.conversionError"), NotificationType.HUMANIZED);
        return false;
    }
}