/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author pavlov
 * @version $Id$
 */
public class ActionsFieldLoader extends AbstractFieldLoader {
    public ActionsFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final ActionsField component = (ActionsField) super.loadComponent(factory, element, parent);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        String dropdown = element.attributeValue(ActionsField.DROPDOWN, "false");
        if (Boolean.valueOf(dropdown)) {
            component.enableButton(ActionsField.DROPDOWN, true);
        }

        ActionsFieldHelper helper = new ActionsFieldHelper(component);

        String lookup = element.attributeValue(ActionsField.LOOKUP, "true");
        if (Boolean.valueOf(lookup)) {
            component.enableButton(ActionsField.LOOKUP, true);
            String lookupScreen = element.attributeValue("lookupScreen");
            if (!StringUtils.isEmpty(lookupScreen)) {
                helper.createLookupAction(lookupScreen);
            } else {
                helper.createLookupAction();
            }
        }

        String open = element.attributeValue(ActionsField.OPEN, "true");
        if (Boolean.valueOf(open)) {
            component.enableButton(ActionsField.OPEN, true);
            helper.createOpenAction();
        }

        List<Element> buttons = element.elements("button");

        if (buttons != null && !buttons.isEmpty()) {
            ButtonLoader loader = (ButtonLoader) getLoader("button");
            for (Element buttonElement : buttons) {
                Button button = (Button) loader.loadComponent(factory, buttonElement, null);
                component.addButton(button);
            }
        }

        return component;
    }

    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        super.loadDatasource(component, element);

        CollectionDatasource ds;

        String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            ds = context.getDsContext().get(datasource);
        } else {
            MetaClass metaClass = component.getMetaProperty().getRange().asClass();
            ds = new DsBuilder()
                    .setMetaClass(metaClass)
                    .setViewName(View.MINIMAL)
                    .buildCollectionDatasource();
            ds.setQuery("select e from " + metaClass.getName() + " e where e.id is null");
            ds.refresh();
        }
        ((ActionsField) component).setOptionsDatasource(ds);
    }

    private com.haulmont.cuba.gui.xml.layout.ComponentLoader getLoader(String name) {
        Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loaderClass = config.getLoader(name);
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + name, context.getFullFrameId());
        }

        com.haulmont.cuba.gui.xml.layout.ComponentLoader loader;
        try {
            final Constructor<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> constructor =
                    loaderClass.getConstructor(Context.class);
            loader = constructor.newInstance(context);

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        } catch (NoSuchMethodException e) {
            try {
                loader = loaderClass.newInstance();
                loader.setLocale(locale);
                loader.setMessagesPack(messagesPack);
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new GuiDevelopmentException("Loader instatiation error: " + e1, context.getFullFrameId());
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new GuiDevelopmentException("Loader instatiation error: " + e, context.getFullFrameId());
        }

        return loader;
    }

}
