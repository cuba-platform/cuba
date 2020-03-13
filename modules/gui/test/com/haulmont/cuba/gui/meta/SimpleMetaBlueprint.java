/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.meta;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.LayoutClickNotifier;
import com.haulmont.cuba.gui.components.data.ValueSource;

import javax.validation.constraints.Min;

import java.util.function.Consumer;

import static com.haulmont.cuba.gui.meta.PropertyType.*;

@SuppressWarnings("unused")
public class SimpleMetaBlueprint {

    @StudioProperties(properties = {
            @StudioProperty(name = "css", category = "CSS", type = CSS_BLOCK)
    })
    private interface BComponent {
        @StudioProperty(type = CSS_CLASSNAME_LIST)
        void setStylename(String stylename);

        @StudioProperty(type = SIZE, category = "Size", defaultValue = "auto")
        void setWidth(String width);

        @StudioProperty(type = SIZE, category = "Size", defaultValue = "auto")
        void setHeight(String height);

        @StudioProperty(type = LOCALIZED_STRING, category = "Misc")
        void setContextHelpText(String contextHelpText);

        @StudioProperty(type = LOCALIZED_STRING)
        void setDescription(String description);

        @Min(-1)
        void setTabIndex(int tabIndex);
    }

    @StudioComponent(
            caption = "TextField",
            category = "Fields",
            canvasBehaviour = CanvasBehaviour.INPUT_FIELD)
    @StudioProperties(groups = {
            @PropertiesGroup(constraint = PropertiesConstraint.ONE_OF, properties = {"container", "datasource"}),
            @PropertiesGroup(constraint = PropertiesConstraint.ALL_OR_NOTHING, properties = {"container", "property"})
    })
    private interface BTextField {
        void setFormatter(Consumer<String> formatter);

        @StudioProperties(properties = {
                @StudioProperty(name = "container", caption = "Container", type = DATACONTAINER_REF),
                @StudioProperty(name = "property", caption = "Property", type = PROPERTY_PATH_REF)
        })
        void setValueSource(ValueSource valueSource);
    }

    @StudioComponent
    private interface BTextArea {
        @StudioProperty(caption = "Rows Count")
        @Min(1)
        void setRows(int rows);
    }

    private interface BLayout extends BComponent {
        Subscription addLayoutClickListener(LayoutClickNotifier.LayoutClickEvent event);
    }

    @StudioComponent
    @StudioProperties(properties = {
            @StudioProperty(name = "width", defaultValue = "100%")
    })
    private interface BVerticalLayout extends BLayout {

    }

    @StudioComponent(caption = "GridLayout", xmlElement = "grid", category = "Layout")
    private interface BGridLayout extends BLayout {

    }
}