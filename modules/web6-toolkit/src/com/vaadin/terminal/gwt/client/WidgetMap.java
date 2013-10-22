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
import com.haulmont.cuba.toolkit.gwt.client.ui.VResizableTextArea;
import com.haulmont.cuba.toolkit.gwt.client.ui.VerticalMenuBar;
import com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost;
import com.vaadin.terminal.gwt.client.ui.*;

import java.util.HashMap;

public abstract class WidgetMap {
    protected static HashMap<Class, WidgetInstantiator> instmap = new HashMap<Class, WidgetInstantiator>();

    public Paintable instantiate(Class<? extends Paintable> classType) {

         /* Yes, this (including the generated) may look very odd code, but due
         * the nature of GWT, we cannot do this with reflect. Luckily this is
         * mostly written by WidgetSetGenerator, here are just some hacks. Extra
         * instantiation code is needed if client side widget has no "native"
         * counterpart on client side.*/

        if (VSplitPanelVertical.class == classType) {
            return new VSplitPanelVertical();
        } else if (VResizableTextArea.class == classType) {
            return new VResizableTextArea();
        } if (VTextArea.class == classType) {
            return new VTextArea();
        } else if (VSwfUpload.class == classType) {
            return new VSwfUpload();
        } else if (VDateFieldCalendar.class == classType) {
            return new VDateFieldCalendar();
        } else if (VPasswordField.class == classType) {
            return new VPasswordField();
        } else if (VWindow.class == classType) {
            return new VWindow();
        } else if (VerticalMenuBar.class == classType) {
            return new VerticalMenuBar();
        } else if (VScriptHost.class == classType) {
            return new VScriptHost();
        } else if (VTextField.class == classType) {
            return new VTextField();
        } else {
            return instantiateInternal(classType); // let generated type handle this
        }
    }

    public abstract Class<? extends Paintable> getImplementationByServerSideClassName(
            String fullyqualifiedName);

    private Paintable instantiateInternal(Class<? extends Paintable> classType) {
        return instmap.get(classType).get();
    }

    public abstract Class<? extends Paintable>[] getDeferredLoadedWidgets();

    public abstract void ensureInstantiator(Class<? extends Paintable> classType);
}