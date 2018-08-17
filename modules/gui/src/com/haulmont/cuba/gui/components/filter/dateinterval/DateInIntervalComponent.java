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
 */

package com.haulmont.cuba.gui.components.filter.dateinterval;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.haulmont.cuba.gui.WindowManager.OpenType;

/**
 * Class generates the UI component for "In interval" date condition of the generic filter component
 */
@org.springframework.stereotype.Component("cuba_DateInIntervalComponent")
@Scope("prototype")
public class DateInIntervalComponent {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected WindowManagerProvider windowManagerProvider;

    @Inject
    protected WindowConfig windowConfig;

    protected List<ValueChangeListener> valueChangeListeners = new ArrayList<>();

    protected DateIntervalValue value;

    public interface ValueChangeListener {
        void valueChanged(DateIntervalValue newValue);
    }

    public Component createComponent(String dateIntervalDescription) {
        HBoxLayout layout = componentsFactory.createComponent(HBoxLayout.class);
        layout.setStyleName("c-dateintervaleditor");

        TextField textField = componentsFactory.createComponent(TextField.class);
        value = AppBeans.getPrototype(DateIntervalValue.NAME, dateIntervalDescription);
        textField.setValue(value.getLocalizedValue());
        textField.setStyleName("c-dateintervaleditor-text");
        textField.setEditable(false);
        layout.add(textField);

        Button openEditorBtn = componentsFactory.createComponent(Button.class);
        openEditorBtn.setIconFromSet(CubaIcon.PICKERFIELD_LOOKUP);
        openEditorBtn.setStyleName("c-dateintervaleditor-button");
        openEditorBtn.setCaption("");
        openEditorBtn.setAction(new AbstractAction("openEditor") {
            @Override
            public void actionPerform(Component component) {
                WindowManager windowManager = windowManagerProvider.get();
                WindowInfo windowInfo = windowConfig.getWindowInfo("date-interval-editor");
                DateIntervalEditor editor = (DateIntervalEditor) windowManager.openWindow(windowInfo,
                        OpenType.DIALOG,
                        Collections.singletonMap("dateIntervalDescription", value.getDescription()));
                editor.addListener(actionId -> {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        value = editor.getDateIntervalValue();
                        textField.setValue(value.getLocalizedValue());
                        fireValueChangeListeners(value);
                    }
                });
            }

            @Override
            public String getCaption() {
                return "";
            }
        });
        layout.add(openEditorBtn);

        Button clearBtn = componentsFactory.createComponent(Button.class);
        clearBtn.setIconFromSet(CubaIcon.PICKERFIELD_CLEAR);
        clearBtn.setStyleName("c-dateintervaleditor-button");
        clearBtn.setCaption("");
        clearBtn.setAction(new AbstractAction("clear") {
            @Override
            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                textField.setValue(null);
                fireValueChangeListeners(null);
            }
        });
        layout.add(clearBtn);

        layout.expand(textField);

        return layout;
    }

    public void addValueChangeListener(ValueChangeListener listener) {
        valueChangeListeners.add(listener);
    }

    public List<ValueChangeListener> getValueChangeListeners() {
        return valueChangeListeners;
    }

    protected void fireValueChangeListeners(DateIntervalValue newValue) {
        for (ValueChangeListener listener : valueChangeListeners) {
            listener.valueChanged(newValue);
        }
    }
}
