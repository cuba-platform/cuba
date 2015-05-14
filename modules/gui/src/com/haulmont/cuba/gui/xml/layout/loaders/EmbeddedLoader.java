/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author gorodnov
 * @version $Id$
 */
public class EmbeddedLoader extends ComponentLoader {

    protected static final String URL_PREFIX = "url://";

    protected static final String FILE_PREFIX = "file://";

    protected static final String THEME_PREFIX = "theme://";

    public EmbeddedLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Embedded component = factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(Embedded component, Element element, Component parent) {
        String typeAttribute = element.attributeValue("type");
        if (StringUtils.isNotEmpty(typeAttribute)) {
            Embedded.Type type = Embedded.Type.valueOf(typeAttribute);
            if (type != null) {
                component.setType(type);
            }
        }

        String srcAttr = element.attributeValue("src");
        if (srcAttr != null) {
            if (srcAttr.startsWith(URL_PREFIX)) {
                try {
                    String src = srcAttr.substring(URL_PREFIX.length());
                    component.setType(Embedded.Type.BROWSER);
                    component.setSource(new URL(src));
                } catch (MalformedURLException e) {
                    throw new GuiDevelopmentException("Unable to instantiate component", context.getFullFrameId(),
                            "src", srcAttr);
                }
            } if (srcAttr.startsWith(THEME_PREFIX)) {
                component.setSource(srcAttr);
            } else if (srcAttr.startsWith(FILE_PREFIX)) {
                String src = srcAttr.substring(FILE_PREFIX.length());
                component.setType(Embedded.Type.OBJECT);
                component.setSource(src);
            } else {
                throw new GuiDevelopmentException("Illegal src attribute value. 'url://' or 'file://' or theme:// prefix expected",
                        context.getFullFrameId(), "src", srcAttr);
            }
        }

        loadId(component, element);
        loadVisible(component, element);
        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);
        loadAlign(component, element);

        assignFrame(component);
    }
}