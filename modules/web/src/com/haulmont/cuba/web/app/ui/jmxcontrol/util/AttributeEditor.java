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

package com.haulmont.cuba.web.app.ui.jmxcontrol.util;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.jmx.entity.AttributeHelper;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

public class AttributeEditor {

    protected CheckBox checkBox;
    protected TextField textField;
    protected DateField dateField;
    protected BoxLayout layout;
    protected String type;

    protected Messages messages = AppBeans.get(Messages.NAME);
    protected ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.NAME);

    public AttributeEditor(Frame frame, String type) {
        this(frame, type, null, false, false);
    }

    public AttributeEditor(Frame frame, String type, Object value, boolean requestFocus, boolean isFixedSize) {
        this.type = type;
        if (AttributeHelper.isBoolean(type)) {
            checkBox = componentsFactory.createComponent(WebCheckBox.class);
            checkBox.setFrame(frame);
            if (requestFocus) {
                checkBox.requestFocus();
            }
            if (value != null) {
                checkBox.setValue(value);
            }

        } else if (AttributeHelper.isArrayOrCollection(type)) {
            initArrayLayout(value, isFixedSize, AttributeHelper.isObjectArrayOrCollection(type));
        } else if (AttributeHelper.isDate(type)) {
            dateField = AppConfig.getFactory().createComponent(DateField.class);
            dateField.setWidth("500px");
            dateField.setFrame(frame);
            if (value != null) {
                dateField.setValue(value.toString());
            }
        } else {
            textField = componentsFactory.createComponent(WebTextField.class);

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

    protected void initArrayLayout(Object value, boolean isFixedSize, boolean isReadOnly) {

        layout = componentsFactory.createComponent(WebVBoxLayout.class);
        layout.setSpacing(true);
        ThemeConstants theme = App.getInstance().getThemeConstants();
        layout.setWidth(theme.get("cuba.web.jmx.AttributeEditor.arrayLayout.width"));
        if (isFixedSize) {
            layout.setHeight(theme.get("cuba.web.jmx.AttributeEditor.arrayLayout.height"));
        }

        Button btnAdd = componentsFactory.createComponent(WebButton.class);
        btnAdd.setIcon("icons/plus-btn.png");
        btnAdd.setDescription(messages.getMessage(getClass(), "editAttribute.array.btnAdd"));
        layout.add(btnAdd);

        ScrollBoxLayout scrollBoxLayout = componentsFactory.createComponent(WebScrollBoxLayout.class);
        scrollBoxLayout.setWidth("100%");
        scrollBoxLayout.setSpacing(true);
        layout.add(scrollBoxLayout);
        layout.expand(scrollBoxLayout);

        btnAdd.setAction(new AbstractAction("addRow") {
            @Override
            public void actionPerform(Component component) {
                addRow(null, scrollBoxLayout, false);
            }

            @Override
            public String getCaption() {
                return "";
            }

            @Override
            public boolean isEnabled() {
                return !isReadOnly;
            }
        });

        if (value != null) {
            List values = objectToStringArray(value);
            for (Object obj : values) {
                addRow(obj, scrollBoxLayout, isReadOnly);
            }
        }
    }

    protected void addRow(Object value, Component.Container parent, boolean isReadOnly) {
        BoxLayout row = componentsFactory.createComponent(WebHBoxLayout.class);
        row.setSpacing(true);
        row.setWidth("100%");

        TextField valueField = componentsFactory.createComponent(WebTextField.class);
        valueField.setValue(value);
        valueField.setEditable(!isReadOnly);
        row.add(valueField);
        row.expand(valueField);

        Button btnRemove = componentsFactory.createComponent(WebButton.class);
        btnRemove.setIcon("icons/remove.png");
        btnRemove.setDescription(messages.getMessage(getClass(), "editAttribute.array.btnRemove"));
        btnRemove.setAction(new AbstractAction("removeRow") {
            @Override
            public void actionPerform(Component component) {
                parent.remove(row);
            }

            @Override
            public String getCaption() {
                return "";
            }

            @Override
            public boolean isEnabled() {
                return !isReadOnly;
            }
        });
        row.add(btnRemove);

        parent.add(row);
    }

    protected List objectToStringArray(Object value) {
        if (value instanceof Collection) {
            return new ArrayList((Collection) value);
        }
        int length = Array.getLength(value);
        List<Object> output = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            output.add(Array.get(value, i));
        }
        return output;
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
        } else if (layout != null) {
            if (AttributeHelper.isList(type)) {
                return getValuesFromArrayLayout(layout);
            } else if (AttributeHelper.isArray(type)) {
                Class clazz = AttributeHelper.getArrayType(type);
                if (clazz != null) {
                    List<String> strValues = getValuesFromArrayLayout(layout);
                    Object array = Array.newInstance(clazz, strValues.size());
                    for (int i = 0; i < strValues.size(); i++) {
                        Array.set(array, i, AttributeHelper.convert(clazz.getName(), strValues.get(i)));
                    }
                    return array;
                }
            }
        }
        return null;
    }

    protected List<String> getValuesFromArrayLayout(BoxLayout layout) {
        List<String> values = new ArrayList<>(layout.getComponents().size() - 1);
        for (Component component : layout.getComponents()) {
            if (component instanceof TextField) {
                values.add(((TextField) component).getValue());
            }
        }
        return values;
    }
}