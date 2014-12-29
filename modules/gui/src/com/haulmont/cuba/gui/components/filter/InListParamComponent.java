/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrBuilder;

import java.util.*;

/**
 * Component for 'In list' filter condition
 *
 * @author krivopustov
 * @version $Id$
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

    protected List<ValueListener> listeners = new LinkedList<>();
    protected BoxLayout composition;

    public InListParamComponent(final Class itemClass) {
        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        final WindowManager windowManager = AppBeans.get(WindowManagerProvider.class).get();
        final WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

        this.itemClass = itemClass;

        field = componentsFactory.createComponent(TextField.NAME);
        field.setEditable(false);
        field.setWidth("100%");

        pickerButton = componentsFactory.createComponent(Button.NAME);
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

                final InListParamEditor editor = windowManager.openWindow(windowInfo, WindowManager.OpenType.DIALOG, params);
                editor.addListener(new Window.CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                            setValues(editor.getValues());
                        }
                    }
                });
            }
        });
        pickerButton.setIcon("components/pickerfield/images/lookup-btn.png");

        clearButton = componentsFactory.createComponent(Button.NAME);
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

        composition = componentsFactory.createComponent(BoxLayout.HBOX);
        composition.setWidth("100%");

        composition.add(field);
        composition.add(pickerButton);
        composition.add(clearButton);
        composition.expand(field);

        composition.setStyleName("cuba-pickerfield");
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

    public void addValueListener(ValueListener listener) {
        listeners.add(listener);
    }

    public void removeValueListener(ValueListener listener) {
        listeners.remove(listener);
    }


    public Object getValue() {
        return listValue;
    }

    public void setValue(Object newValue) {
        if (!ObjectUtils.equals(listValue, newValue)) {
            for (ValueListener listener : listeners) {
                listener.valueChanged(this, "value", listValue, newValue);
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
}