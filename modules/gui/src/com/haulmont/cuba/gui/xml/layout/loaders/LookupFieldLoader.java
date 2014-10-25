/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author abramov
 * @version $Id$
 */
public class LookupFieldLoader extends AbstractFieldLoader {

    public LookupFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Field field, Element element, Component parent) {
        super.initComponent(field, element, parent);

        LookupField component = (LookupField) field;

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        String nullName = element.attributeValue("nullName");
        if (StringUtils.isNotEmpty(nullName)) {
            nullName = loadResourceString(nullName);
            component.setNullOption(nullName);
        }

        loadFilterMode(component, element);
        loadNewOptionHandler(component, element);
    }

    protected void loadNewOptionHandler(final LookupField component, Element element) {
        String newOptionAllowed = element.attributeValue("newOptionAllowed");
        if (StringUtils.isNotEmpty(newOptionAllowed)) {
            component.setNewOptionAllowed(Boolean.valueOf(newOptionAllowed));
        }

        final String newOptionHandlerMethod = element.attributeValue("newOptionHandler");
        if (StringUtils.isNotEmpty(newOptionHandlerMethod)) {
            context.addPostInitTask(new PostInitTask() {
                @Override
                public void execute(Context context, final IFrame window) {
                    final Method newOptionHandler;
                    try {
                        Class<? extends IFrame> windowClass = window.getClass();
                        newOptionHandler = windowClass.getMethod(newOptionHandlerMethod, LookupField.class, String.class);
                    } catch (NoSuchMethodException e) {
                        Map<String, Object> params = new HashMap<>(2);
                        params.put("LookupField Id", component.getId());
                        params.put("Method name", newOptionHandlerMethod);

                        throw new GuiDevelopmentException("Unable to find new option handler method for lookup field",
                                context.getFullFrameId(), params);
                    }

                    component.setNewOptionHandler(new LookupField.NewOptionHandler() {
                        @Override
                        public void addNewOption(String caption) {
                            try {
                                newOptionHandler.invoke(window, component, caption);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException("Unable to invoke new option handler", e);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        super.loadDatasource(component, element);

        final String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDsContext().get(datasource);
            ((LookupField) component).setOptionsDatasource((CollectionDatasource) ds);
        }
    }

    protected void loadFilterMode(LookupField component, Element element) {
        final String filterMode = element.attributeValue("filterMode");
        if (!StringUtils.isEmpty(filterMode)) {
            component.setFilterMode(LookupField.FilterMode.valueOf(filterMode));
        }
    }
}