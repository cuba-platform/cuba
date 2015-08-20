/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.security.sessionattr.edit;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.SessionAttribute;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author krivopustov
 * @version $Id$
 */
public class SessionAttributeEditor extends AbstractEditor {

    protected Datasource<SessionAttribute> datasource;

    @Override
    public void init(Map<String, Object> params) {
        datasource = getDsContext().get("attribute");

        FieldGroup fields = (FieldGroup) getComponent("fields");
        FieldGroup.FieldConfig field = fields.getField("datatype");
        fields.addCustomField(field,
                new FieldGroup.CustomFieldGenerator() {
                    public Component generateField(Datasource datasource, String propertyId) {
                        LookupField lookup = AppConfig.getFactory().createComponent(LookupField.class);
                        lookup.setDatasource(datasource, propertyId);
                        lookup.setRequiredMessage(getMessage("datatypeMsg"));
                        lookup.setRequired(true);

                        Map<String, Object> options = new TreeMap<>();
                        Set<String> names = Datatypes.getNames();
                        String mainMessagePack = AppConfig.getMessagesPack();
                        for (String name : names) {
                            options.put(messages.getMessage(mainMessagePack, "Datatype." + name), name);
                        }
                        lookup.setOptionsMap(options);

                        return lookup;
                    }
                }
        );
    }

    @Override
    public void commitAndClose() {
        SessionAttribute item = datasource.getItem();
        if (item.getStringValue() != null) {
            Datatype dt = Datatypes.get(item.getDatatype());
            try {
                Object object = dt.parse(item.getStringValue());
                item.setStringValue(object == null ? "" : object.toString());
            } catch (IllegalArgumentException | ParseException e) {
                showNotification(getMessage("unableToParseValue"), NotificationType.ERROR);
                return;
            }
        }
        super.commitAndClose();
    }
}