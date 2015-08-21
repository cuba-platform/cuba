/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.util;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.jmx.entity.AttributeHelper;
import com.haulmont.cuba.web.gui.components.WebCheckBox;
import com.haulmont.cuba.web.gui.components.WebTextField;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * @author budarov
 * @version $Id$
 */
public class AttributeEditor {

    protected CheckBox checkBox;
    protected TextField textField;
    protected DateField dateField;
    protected BoxLayout layout;
    protected String type;

    public AttributeEditor(Frame frame, String type) {
        this(frame, type, null, false);
    }

    public AttributeEditor(Frame frame, String type, Object value, boolean requestFocus) {
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
        } else if (AttributeHelper.isDate(type)) {
            dateField = AppConfig.getFactory().createComponent(DateField.class);
            dateField.setWidth("500px");
            dateField.setFrame(frame);
            if (value != null) {
                dateField.setValue(value.toString());
            }
        } else {
            textField = new WebTextField();

            ThemeConstants theme = App.getInstance().getThemeConstants();
            textField.setWidth(theme.get("cuba.web.jmx.AttributeEditor.textField.width"));
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
        if (dateField != null) {
            return dateField;
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
        } else if (dateField != null) {
            Date date = dateField.getValue();
            return date;
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