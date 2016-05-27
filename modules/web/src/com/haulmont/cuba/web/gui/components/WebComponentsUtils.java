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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.LookupField;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;

public class WebComponentsUtils {

    public static void allowHtmlContent(Label label) {
        com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
        vLabel.setContentMode(ContentMode.HTML);
    }

    public static void disallowHtmlContent(Label label) {
        com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
        vLabel.setContentMode(ContentMode.TEXT);
    }

    public static void allowNullSelection(LookupField lookupField) {
        ComboBox vCombobox = (ComboBox) WebComponentsHelper.unwrap(lookupField);
        vCombobox.setNullSelectionAllowed(true);
    }

    public static void disallowNullSelection(LookupField lookupField) {
        ComboBox vCombobox = (ComboBox) WebComponentsHelper.unwrap(lookupField);
        vCombobox.setNullSelectionAllowed(false);
    }
}