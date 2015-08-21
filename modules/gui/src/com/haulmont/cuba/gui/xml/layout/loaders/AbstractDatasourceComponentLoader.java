/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;

/**
 * @author Gorodnov
 * @version $Id$
 */
public abstract class AbstractDatasourceComponentLoader extends ComponentLoader {
    public AbstractDatasourceComponentLoader(Context context) {
        super(context);
    }

    protected void loadDatasource(DatasourceComponent component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDsContext().get(datasource);
            if (ds == null){
                throw new GuiDevelopmentException(String.format("Datasource '%s' is not defined", datasource),
                        getContext().getFullFrameId(), "Component ID", component.getId());
            }
            final String property = element.attributeValue("property");
            if (StringUtils.isEmpty(property))
                throw new GuiDevelopmentException(
                        String.format("Can't set datasource '%s' for component '%s' because 'property' " +
                                "attribute is not defined", datasource, component.getId()),
                        context.getFullFrameId());

            component.setDatasource(ds, property);
        }
    }

    protected Formatter loadFormatter(Element element) {
        final Element formatterElement = element.element("formatter");
        if (formatterElement != null) {
            final String className = formatterElement.attributeValue("class");
            final Class<?> aClass = scripting.loadClass(className);
            if (aClass == null)
                throw new GuiDevelopmentException("Class " + className + " is not found", context.getFullFrameId());
            try {
                final Constructor<?> constructor = aClass.getConstructor(Element.class);
                try {
                    return (Formatter) constructor.newInstance(formatterElement);
                } catch (Throwable e) {
                    throw new GuiDevelopmentException("Unable to instatiate class " + className + ": " + e.toString(),
                            context.getFullFrameId());
                }
            } catch (NoSuchMethodException e) {
                try {
                    return (Formatter) aClass.newInstance();
                } catch (Exception e1) {
                    throw new GuiDevelopmentException("Unable to instatiate class " + className + ": " + e1.toString(),
                            context.getFullFrameId());
                }
            }
        } else {
            return null;
        }
    }

}
