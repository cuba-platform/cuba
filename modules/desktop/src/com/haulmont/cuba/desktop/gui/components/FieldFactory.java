/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.impl.StringDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.Datasource;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class FieldFactory {

    public Component createField(Datasource datasource, String property) {
        MetaClass metaClass = datasource.getMetaClass();
        MetaPropertyPath mpp = metaClass.getPropertyPath(property);
        if (mpp.getRange().isDatatype() && mpp.getRange().asDatatype().getName().equals(StringDatatype.NAME)) {
            return createStringField(datasource, property);
        } else {
            return createUnsupportedField(datasource, property);
        }

    }

    private Component createStringField(Datasource datasource, String property) {
        DesktopTextField textField = new DesktopTextField();
        textField.setDatasource(datasource, property);
        return textField;
    }

    private Component createUnsupportedField(Datasource datasource, String property) {
        DesktopLabel label = new DesktopLabel();
        label.setValue("<not supported yet>");
        return label;
    }
}
