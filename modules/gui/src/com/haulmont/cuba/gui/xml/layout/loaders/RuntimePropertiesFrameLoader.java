/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.*;
import org.apache.commons.io.IOUtils;
import org.dom4j.Element;

import java.io.InputStream;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropertiesFrameLoader extends IFrameLoader {

    private static final String DEFAULT_DESCRIPTOR = "/com/haulmont/cuba/gui/runtimeprops/edit-runtime-properties.xml";

    public RuntimePropertiesFrameLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

     public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
         String src = element.attributeValue("src");
         if (src == null)
             src = DEFAULT_DESCRIPTOR;
         String runtimeDs = element.attributeValue("runtimeDs");
         context.getParams().put("runtimeDs", runtimeDs);
         String categoriesDs = element.attributeValue("categoriesDs");
         context.getParams().put("categoriesDs",categoriesDs);

         final LayoutLoader loader = new LayoutLoader(context, factory, LayoutLoaderConfig.getFrameLoaders());
         loader.setLocale(getLocale());
         loader.setMessagesPack(getMessagesPack());

         InputStream stream = null;
         if (ConfigProvider.getConfig(GlobalConfig.class).isGroovyClassLoaderEnabled()) {
             stream = ScriptingProvider.getResourceAsStream(src);
         }
         if (stream == null) {
             stream = getClass().getResourceAsStream(src);
             if (stream == null) {
                 throw new RuntimeException("Bad template path: " + src);
             }
         }

         final IFrame component;
         try {
             component = (IFrame) loader.loadComponent(stream, parent, context.getParams());
         } finally {
             IOUtils.closeQuietly(stream);
         }
         if (component.getMessagesPack() == null) {
             component.setMessagesPack(messagesPack);
         }

         assignXmlDescriptor(component, element);
         loadId(component, element);
         loadVisible(component, element);

         loadStyleName(component, element);

         loadAlign(component, element);

         loadHeight(component, element, ComponentsHelper.getComponentHeigth(component));
         loadWidth(component, element, ComponentsHelper.getComponentWidth(component));

         if (context.getFrame() != null)
             component.setFrame(context.getFrame());

         return component;
     }
}
