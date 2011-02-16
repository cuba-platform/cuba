/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 25.08.2010 10:32:39
 * $Id$
 */

package cuba.client.web.ui.jmxcontrol.util;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.jmxcontrol.util.AttributeHelper;
import com.haulmont.cuba.web.gui.components.WebCheckBox;
import com.haulmont.cuba.web.gui.components.WebTextField;
import com.haulmont.cuba.web.gui.components.WebVBoxLayout;
import org.apache.commons.lang.BooleanUtils;

public class AttributeEditor {

    private CheckBox checkBox;
    private TextField textField;
    private BoxLayout layout;
    private String type;

    public AttributeEditor(IFrame frame, String type) {
        this(frame, type, null);
    }

    public AttributeEditor(IFrame frame, String type, Object value) {
        this.type = type;
        if (AttributeHelper.isBoolean(type)) {
            checkBox = new WebCheckBox();
            checkBox.setFrame(frame);
            if (value != null) {
                checkBox.setValue(value);
            }
        }
        else if (AttributeHelper.isArray(type)) {
            layout = new WebVBoxLayout();
            layout.setSpacing(true);
        }
        else {
            textField = new WebTextField();
            textField.setWidth("500px");
            textField.setFrame(frame);
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

    public Object getAttributeValue() {
        if (checkBox != null) {
            Boolean value = (Boolean) checkBox.getValue();
            return BooleanUtils.isTrue(value);
        }
        else if (textField != null) {
            String strValue = textField.getValue();
            return AttributeHelper.convert(type, strValue);
        }
        // array
        return null;
    }
}
