/* 
 * Copyright 2010 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.terminal.gwt.client;

import com.haulmont.cuba.toolkit.gwt.client.swfupload.VSwfUpload;
import com.haulmont.cuba.toolkit.gwt.client.ui.*;
import com.vaadin.terminal.gwt.client.ui.*;

public class DefaultWidgetSet extends WidgetSet {

    protected Class<? extends Paintable> resolveWidgetType(UIDL uidl,
                                                           ApplicationConfiguration conf) {
        final String tag = uidl.getTag();

        Class<? extends Paintable> widgetClass = conf
                .getWidgetClassByEncodedTag(tag);

        // add our historical quirks

        if (widgetClass == VButton.class && uidl.hasAttribute("type")) {
            return VCheckBox.class;
        } else if (widgetClass == VView.class && uidl.hasAttribute("sub")) {
            return VWindow.class;
        } else if (widgetClass == VFilterSelect.class) {
            if (uidl.hasAttribute("type")) {
                final String type = uidl.getStringAttribute("type").intern();
                if ("legacy-multi" == type) {
                    return VListSelect.class;
                } else if (type == "nativetwincolumn") {
                    return VNativeTwinColumnSelect.class;
                } else if (type == "twincolumn") {
                    return VTwinColumnSelect.class;
                }
            }
        } else if (widgetClass == VTextField.class) {
            if (uidl.hasAttribute("multiline")) {
                return VTextArea.class;
            } else if (uidl.hasAttribute("secret")) {
                return VPasswordField.class;
            }
        } else if (widgetClass == VResizableTextField.class) {
            if (!uidl.hasAttribute("multiline")) {
                if (uidl.hasAttribute("secret"))
                    return VPasswordField.class;
                else
                    return VTextField.class;
            }
        } else if (widgetClass == VSplitPanelHorizontal.class
                && uidl.hasAttribute("vertical")) {
            return VSplitPanelVertical.class;
        } else if (widgetClass == IScrollTreeTable.class) {
            if (uidl.hasAttribute("pagingMode") && "PAGE".equals(uidl.getStringAttribute("pagingMode"))) {
                return IPageTreeTable.class;
            } else {
                return IScrollTreeTable.class;
            }
        } else if (widgetClass == IScrollTable.class) {
            if (uidl.hasAttribute("pagingMode") && "PAGE".equals(uidl.getStringAttribute("pagingMode"))) {
                return IPageTable.class;
            } else {
                return IScrollTable.class;
            }
        } else if (widgetClass == VUpload.class) {
            if (uidl.hasAttribute("multiple") && (uidl.getBooleanAttribute("multiple") == true)) {
                return VSwfUpload.class;
            }
        } else if (widgetClass == VMenuBar.class && uidl.hasAttribute("vertical")) {
            return VerticalMenuBar.class;
        }

        return widgetClass;
    }
}
