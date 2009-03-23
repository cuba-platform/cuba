/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:27:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceFactoryImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

import java.util.Collections;

public class FrameLoader extends ContainerLoader implements ComponentLoader {

    public FrameLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final IFrame component = factory.createComponent("iframe");

        final Element dsContextElement = element.element("dsContext");
        final DsContext dsContext;
        if (dsContextElement != null) {
            final DsContextLoader contextLoader =
                    new DsContextLoader(new DatasourceFactoryImpl(), context.getDSContext().getDataService());
            dsContext = contextLoader.loadDatasources(dsContextElement);
            
            final ComponentLoaderContext context =
                    new ComponentLoaderContext(dsContext, Collections.<String, Object>emptyMap());
            setContext(context);
        } else {
            dsContext = null;
        }
        
        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadMessagesPack(component, element);
        loadSubcomponentsAndExpand(component, element.element("layout"));

        if (dsContext != null) {
            component.setDsContext(dsContext);

            for (Datasource ds : dsContext.getAll()) {
                if (ds instanceof DatasourceImplementation) {
                    ((DatasourceImplementation) ds).initialized();
                }
            }

            dsContext.setContext(new FrameContext(component));

            ((ComponentLoaderContext) context).setFrame(component);
            context.executeLazyTasks();
        }

        return component;
    }

    protected void loadMessagesPack(IFrame frame, Element element) {
        String msgPack = element.attributeValue("messagesPack");
        if (msgPack != null) {
            frame.setMessagesPack(msgPack);
            setMessagesPack(msgPack);
        } else {
            frame.setMessagesPack(msgPack);
            setMessagesPack(this.messagesPack);
        }
    }
}