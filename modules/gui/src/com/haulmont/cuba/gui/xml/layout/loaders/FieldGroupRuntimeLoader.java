/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */

public class FieldGroupRuntimeLoader extends FieldGroupLoader {

    public FieldGroupRuntimeLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final FieldGroup component = factory.createComponent("fieldGroup");

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadVisible(component, element);

        assignFrame(component);

        return component;
    }


    protected List<FieldGroup.Field> loadFields(FieldGroup component, Datasource ds){
        MetaClass meta = ds.getMetaClass();
        Collection<MetaProperty> metaProperties = meta.getProperties();
        List<FieldGroup.Field> fields = new ArrayList<FieldGroup.Field>();
        for(MetaProperty property : metaProperties){
            FieldGroup.Field field = new FieldGroup.Field(property.getName());
            field.setCaption(property.getName());
            fields.add(field);

        }
        return fields;
    }
}
