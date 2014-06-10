/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.util;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.jmx.entity.AttributeHelper;
import com.haulmont.cuba.web.gui.components.WebCheckBox;
import com.haulmont.cuba.web.gui.components.WebTextField;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author budarov
 * @version $Id$
 */
public class AttributeEditor {

    protected CheckBox checkBox;
    protected TextField textField;
    protected BoxLayout layout;
    protected String type;

    public AttributeEditor(IFrame frame, String type) {
        this(frame, type, null, false);
    }

    public AttributeEditor(IFrame frame, String type, Object value, boolean requestFocus) {
        this.type = type;
        if (AttributeHelper.isBoolean(type)) {
            checkBox = new WebCheckBox();
            checkBox.setFrame(frame);
            if (requestFocus) {
                checkBox.requestFocus();
            }
            if (value != null) {
                checkBox.setValue(value);
            }

        } else if (AttributeHelper.isArray(type)) {
            layout = new WebVBoxLayout();
            layout.setSpacing(true);
        } else {
            textField = new WebTextField();
            textField.setWidth("500px");
            textField.setFrame(frame);

            if (requestFocus) {
                textField.requestFocus();
            }
            if (value != null) {
                textField.setValue(value.toString());
            }
        }
    }

    public Component getComponent() {
        if (checkBox != null) {
            return checkBox;
        }
        if (textField != null) {
            return textField;
        }
        return layout;
    }

    public Object getAttributeValue(boolean allowNull) {
        if (checkBox != null) {
            Boolean value = checkBox.getValue();
            return BooleanUtils.isTrue(value);
        } else if (textField != null) {
            String strValue = textField.getValue();
            if (allowNull && StringUtils.isBlank(strValue)) {
                return null;
            } else {
                if (strValue == null)
                    strValue = "";
                return AttributeHelper.convert(type, strValue);
            }
        }
        // array
        return null;
    }
}