/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

/**
 * @author gorodnov
 * @version $Id$
 */
public class EmbeddedLoader extends ComponentLoader {

    protected static final String URL_PREFIX = "url";

    protected static final String FILE_PREFIX = "file";

    public EmbeddedLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(
            ComponentsFactory factory,
            Element element,
            Component parent) throws InstantiationException, IllegalAccessException {
        Embedded component = factory.createComponent("embedded");

        String srcAttr = element.attributeValue("src");
        if (srcAttr != null) {
            if (srcAttr.startsWith(URL_PREFIX + "://")) {
                try {
                    String src = srcAttr.substring(srcAttr.indexOf("//") + 2);
                    component.setType(Embedded.Type.BROWSER);
                    component.setSource(new URL(src));
                } catch (MalformedURLException e) {
                    throw new DevelopmentException("Unable to instantiate component", context.getFullFrameId(),
                            Collections.<String, Object>singletonMap("Scr Attribute Value", srcAttr));
                }
            } else if (srcAttr.startsWith(FILE_PREFIX + "://")) {
                String src = srcAttr.substring(srcAttr.indexOf("//") + 2);
                component.setType(Embedded.Type.OBJECT);
                component.setSource(src);
            } else {
                throw new DevelopmentException("Illegal src attribute value. Expect 'url:' or 'file:' prefix",
                        context.getFullFrameId(),
                        Collections.<String, Object>singletonMap("Scr Attribute Value", srcAttr));
            }
        }

        loadId(component, element);
        loadVisible(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        assignFrame(component);

        return component;
    }
}