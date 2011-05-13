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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.toolkit.gwt.client.swfupload.VSwfUpload;
import com.haulmont.cuba.toolkit.gwt.client.ui.*;
import com.vaadin.terminal.gwt.client.ui.*;

public class DefaultWidgetSet implements WidgetSet {

    /**
     * DefaultWidgetSet (and its extensions) delegate instantiation of widgets
     * and client-server mathing to WidgetMap. The actual implementations are
     * generated with gwts deferred binding.
     */
    private WidgetMap map;

    /**
     * This is the entry point method. It will start the first
     */
    public void onModuleLoad() {
        try {
            ApplicationConfiguration.initConfigurations(this);
        } catch (Exception e) {
            // Log & don't continue;
            // custom WidgetSets w/ entry points will cause this
            ApplicationConnection.getConsole().log(e.getMessage());
            return;
        }
        ApplicationConfiguration.startNextApplication(); // start first app
        map = GWT.create(WidgetMap.class);
    }

    public Paintable createWidget(UIDL uidl, ApplicationConfiguration conf) {
        final Class<? extends Paintable> classType = resolveWidgetType(uidl,
                conf);
        if (classType == null || classType == VUnknownComponent.class) {
            String serverSideName = conf
                    .getUnknownServerClassNameByEncodedTagName(uidl.getTag());
            return new VUnknownComponent(serverSideName);
        }

        return map.instantiate(classType);
    }

    protected Class<? extends Paintable> resolveWidgetType(UIDL uidl,
            ApplicationConfiguration conf) {
        final String tag = uidl.getTag();

        Class<? extends Paintable> widgetClass = conf
                .getWidgetClassByEncodedTag(tag);

        // TODO add our quirks

        if (widgetClass == VButton.class && uidl.hasAttribute("type")) {
            return VCheckBox.class;
        } else if (widgetClass == VView.class && uidl.hasAttribute("sub")) {
            return VWindow.class;
        } else if (widgetClass == VFilterSelect.class) {
            if (uidl.hasAttribute("type")) {
                // TODO check if all type checks are really neede
                final String type = uidl.getStringAttribute("type").intern();
                if (type == "twincol") {
                    return VTwinColSelect.class;
                } else if (type == "nativetwincolumn") {
                    return VNativeTwinColumnSelect.class;
                } else if (type == "twincolumn") {
                    return VTwinColumnSelect.class;
                } else if (type == "optiongroup") {
                    return VOptionGroup.class;
                } else if (type == "native") {
                    return VNativeSelect.class;
                } else if (type == "list") {
                    return VListSelect.class;
                } else if (uidl.hasAttribute("selectmode")
                        && uidl.getStringAttribute("selectmode")
                                .equals("multi")) {
                    return VListSelect.class;
                }
            }
        } else if (widgetClass == VTextField.class) {
            if (uidl.hasAttribute("multiline")) {
                return VTextArea.class;
            } else if (uidl.hasAttribute("secret")) {
                return VPasswordField.class;
            }
        } else if (widgetClass == VPopupCalendar.class) {
            if (uidl.hasAttribute("type")
                    && uidl.getStringAttribute("type").equals("inline")) {
                return VDateFieldCalendar.class;
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
        } else if (widgetClass == VUpload.class){
            if (uidl.hasAttribute("multiple") && (uidl.getBooleanAttribute("multiple") == true)){
                return VSwfUpload.class;
            }
        }
        else if (widgetClass == VMenuBar.class && uidl.hasAttribute("vertical")) {
            return VerticalMenuBar.class;
        }

        return widgetClass;
    }

    public boolean isCorrectImplementation(Widget currentWidget, UIDL uidl,
            ApplicationConfiguration conf) {
        return currentWidget.getClass() == resolveWidgetType(uidl, conf);
    }

    public Class<? extends Paintable> getImplementationByClassName(
            String fullyqualifiedName) {
        Class<? extends Paintable> implementationByServerSideClassName = map
                .getImplementationByServerSideClassName(fullyqualifiedName);
        return implementationByServerSideClassName;

    }
}
