/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 20.08.2009 11:32:53
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.Collections;

public abstract class AbstractDatasourceComponentLoader extends ComponentLoader {
    public AbstractDatasourceComponentLoader(Context context) {
        super(context);
    }

    protected void loadDatasource(DatasourceComponent component, Element element) {
        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDsContext().get(datasource);
            if (ds == null){
                throw new DevelopmentException(String.format(
                        "Datasource '%s' not defined",
                        datasource), getContext().getFullFrameId(),
                        Collections.<String, Object>singletonMap("Component Id",component.getId()));

            }
            final String property = element.attributeValue("property");
            if (StringUtils.isEmpty(property))
                throw new DevelopmentException(
                        String.format(
                                "Can't set assign datasource '%s' for component '%s' due 'property' " +
                                        "attribute is not defined",
                                datasource, component.getId()),context.getFullFrameId());

            component.setDatasource(ds, property);
        }
    }

    protected Formatter loadFormatter(Element element) {
        final Element formatterElement = element.element("formatter");
        if (formatterElement != null) {
            final String className = formatterElement.attributeValue("class");
            final Class<Formatter> aClass = scripting.loadClass(className);
            if (aClass == null)
                throw new DevelopmentException("Class " + className + " is not found",context.getFullFrameId());
            try {
                final Constructor<Formatter> constructor = aClass.getConstructor(Element.class);
                try {
                    return constructor.newInstance(formatterElement);
                } catch (Throwable e) {
                    throw new DevelopmentException(e.getMessage(),context.getFullFrameId());
                }
            } catch (NoSuchMethodException e) {
                try {
                    return aClass.newInstance();
                } catch (Exception e1) {
                    throw new DevelopmentException(e1.getMessage(),context.getFullFrameId());
                }
            }
        } else {
            return null;
        }
    }

}
