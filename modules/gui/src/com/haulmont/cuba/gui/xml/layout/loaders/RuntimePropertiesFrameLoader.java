/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.io.IOUtils;
import org.dom4j.Element;

import java.io.InputStream;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropertiesFrameLoader extends IFrameLoader {

    private static final String DEFAULT_DESCRIPTOR = "/com/haulmont/cuba/gui/runtimeprops/runtime-properties-frame.xml";

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
         String rows = element.attributeValue("rows");
         context.getParams().put("rows", rows);
         String cols = element.attributeValue("cols");
         context.getParams().put("cols", cols);
         String fieldWidth = element.attributeValue("fieldWidth");
         context.getParams().put("fieldWidth", fieldWidth);

         final LayoutLoader loader = new LayoutLoader(context, factory, LayoutLoaderConfig.getFrameLoaders());
         loader.setLocale(getLocale());
         loader.setMessagesPack(getMessagesPack());

         InputStream stream = ScriptingProvider.getResourceAsStream(src);
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
