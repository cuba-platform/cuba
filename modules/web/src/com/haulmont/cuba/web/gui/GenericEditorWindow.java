/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 27.01.2009 11:06:56
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.ui.*;
import org.apache.commons.lang.StringUtils;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class GenericEditorWindow
    extends
        Window.Editor
{
    private DataService dataservice;

    public GenericEditorWindow() {
        dataservice = new GenericDataService(false);
    }

    @Override
    protected Component createLayout() {
        final Component layout = super.createLayout();

        form.setFieldFactory(new FieldFactory());
        form.setImmediate(true);

        return layout;
    }

    public void setItem(Object item) {
        this.item = item;

        final MetaClass metaClass = getMetaClass(item);
        setCaption("Edit " + metaClass.getName());

        final Collection<MetaProperty> properties = getProperties(item);
        form.setItemDataSource(new ItemWrapper(item, properties));
        form.setVisibleItemProperties(properties);

        for (MetaProperty metaProperty : properties) {
            final com.itmill.toolkit.ui.Field field = form.getField(metaProperty);
            if (field != null) {
                field.setRequired(metaProperty.isMandatory());
            }
        }
    }

    protected DataService getDataService() {
        return dataservice;
    }

    private Collection<MetaProperty> getProperties(Object item) {
        final MetaClass metaClass = getMetaClass(item);

        final List<MetaProperty> res = new ArrayList<MetaProperty>();

        for (MetaProperty metaProperty : metaClass.getProperties()) {
            // TODO filter properties
            res.add(metaProperty);
        }

        Collections.sort(res, new Comparator<MetaProperty>() {
            public int compare(MetaProperty o1, MetaProperty o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return res;
    }

    private static class FieldFactory extends BaseFieldFactory {
        @Override
        public com.itmill.toolkit.ui.Field createField(Item item, Object propertyId, Component uiContext) {
            com.itmill.toolkit.ui.Field field = null;

            MetaProperty metaProperty = (MetaProperty) propertyId;
            final Range range = metaProperty.getRange();

            if (range != null) {
                final Range.Cardinality cardinality = range.getCardinality();
    
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality)) {
                    if (range.isDatatype()) {
                        field = createField(range.asDatatype().getImplementationClass(), uiContext);
                    } else if (range.isClass()) {

                    }
                } else {

                }
            }

            if (field != null) {
                final String caption = metaProperty.getCaption();
                field.setCaption(StringUtils.capitalize(caption == null ? metaProperty.getName() : caption));
            }

            return field;
        }

        @Override
        public com.itmill.toolkit.ui.Field createField(final Class type, Component uiContext) {
            // Null typed properties can not be edited
            if (type == null) {
                return null;
            }

            // Date field
            if (Date.class.isAssignableFrom(type)) {
                final DateField df = new DateField();
                df.setResolution(DateField.RESOLUTION_DAY);
                return df;
            }

            // Boolean field
            if (Boolean.class.isAssignableFrom(type)) {
                final Button button = new Button();
                button.setSwitchMode(true);
                button.setImmediate(false);
                return button;
            }

            // Nested form is used by default
            final Datatypes datatypes = Datatypes.getInstance();
            final TextField field = new TextField();
            final Datatype datatype = datatypes.get(type);

            field.setFormat(new DatatypeFormat(datatype));
            return field;
        }

        private static class DatatypeFormat extends Format {
            private final Datatype datatype;

            public DatatypeFormat(Datatype datatype) {
                this.datatype = datatype;
            }

            @Override
                public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                return obj instanceof String ? toAppendTo.append(obj) : toAppendTo.append(datatype.format(obj));
            }

            @Override
                public Object parseObject(String source, ParsePosition pos) {
                try {
                    return datatype.parse(source);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
