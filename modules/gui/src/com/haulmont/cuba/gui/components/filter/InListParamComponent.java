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
package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrBuilder;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Component for 'In list' filter condition
 */
public class InListParamComponent {

    protected TextField field;
    protected Button pickerButton;
    protected Button clearButton;

    protected Class itemClass;
    protected MetaClass metaClass;
    protected CollectionDatasource collectionDatasource;
    protected List<String> runtimeEnum;

    protected List listValue;
    protected Map<Object, String> values = new LinkedHashMap<>();

    protected List<InListValueListener> listeners = new LinkedList<>();
    protected BoxLayout composition;

    public InListParamComponent(Class itemClass) {
        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        final WindowManager windowManager = AppBeans.get(WindowManagerProvider.class).get();
        final WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

        this.itemClass = itemClass;

        field = componentsFactory.createComponent(TextField.class);
        field.setEditable(false);
        field.setWidth("100%");
        FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
        filterHelper.setFieldReadOnlyFocusable(field, true);

        pickerButton = componentsFactory.createComponent(Button.class);
        pickerButton.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                WindowInfo windowInfo = windowConfig.getWindowInfo("inListParamEditor");

                HashMap<String, Object> params = new HashMap<>();
                params.put("values", values);
                params.put("collectionDatasource", collectionDatasource);
                params.put("metaClass", metaClass);
                params.put("runtimeEnum", runtimeEnum);
                params.put("itemClass", itemClass);

                InListParamEditor editor = (InListParamEditor) windowManager.openWindow(windowInfo, OpenType.DIALOG, params);
                editor.addCloseListener(actionId -> {
                    if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                        setValues(editor.getValues());
                    }
                    field.requestFocus();
                });
            }
        });
        pickerButton.setIcon("components/pickerfield/images/lookup-btn.png");
        filterHelper.setComponentFocusable(pickerButton, false);

        clearButton = componentsFactory.createComponent(Button.class);
        clearButton.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                setValue(null);
                values.clear();
                field.setEditable(true);
                field.setValue(null);
                field.setEditable(false);
            }
        });
        clearButton.setIcon("components/pickerfield/images/clear-btn.png");
        filterHelper.setComponentFocusable(clearButton, false);

        composition = componentsFactory.createComponent(HBoxLayout.class);
        composition.setWidth("100%");

        composition.add(field);
        composition.add(pickerButton);
        composition.add(clearButton);
        composition.expand(field);

        composition.setStyleName("c-pickerfield");
    }

    public InListParamComponent(CollectionDatasource collectionDatasource) {
        this(collectionDatasource.getMetaClass().getJavaClass());
        this.collectionDatasource = collectionDatasource;
    }

    public InListParamComponent(MetaClass metaClass) {
        this(metaClass.getJavaClass());
        this.metaClass = metaClass;
    }

    public InListParamComponent(List<String> values) {
        this(String.class);
        this.runtimeEnum = values;
    }

    public Component getComponent() {
        return composition;
    }

    public void addValueListener(InListValueListener listener) {
        listeners.add(listener);
    }

    public void removeValueListener(InListValueListener listener) {
        listeners.remove(listener);
    }

    public Object getValue() {
        return listValue;
    }

    public void setValue(Object newValue) {
        if (!ObjectUtils.equals(listValue, newValue)) {
            for (InListValueListener listener : listeners) {
                listener.valueChanged(listValue, newValue);
            }
            listValue = (List) newValue;
        }
    }

    public void setValues(Map<Object, String> values) {
        this.values = values;
        if (values.isEmpty()) {
            setValue(null);
        } else {
            //noinspection unchecked
            setValue(new ArrayList(values.keySet()));
        }

        String caption = new StrBuilder().appendWithSeparators(values.values(), ",").toString();
        field.setEditable(true);
        field.setValue(caption);
        field.setEditable(false);
    }

    public Map<Object, String> getValues() {
        return values;
    }

    public interface InListValueListener {
        void valueChanged(@Nullable Object prevValue, @Nullable Object value);
    }
}