/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.app.security.sessionattr.edit;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.SessionAttribute;

import java.text.ParseException;
import java.util.*;

public class SessionAttributeEditor extends AbstractEditor {

    protected Datasource<SessionAttribute> datasource;

    @Override
    public void init(Map<String, Object> params) {
        datasource = getDsContext().get("attribute");

        FieldGroup fields = getComponent("fields");
        FieldGroup.Field field = fields.getField("datatype");
        fields.addCustomField(field,
                new FieldGroup.CustomFieldGenerator() {
                    public Component generateField(Datasource datasource, String propertyId) {
                        LookupField lookup = AppConfig.getFactory().createComponent(LookupField.NAME);
                        lookup.setDatasource(datasource, propertyId);
                        lookup.setRequiredMessage(getMessage("datatypeMsg"));
                        lookup.setRequired(true);

                        Map<String, Object> options = new TreeMap<String, Object>();
                        Set<String> names = Datatypes.getNames();
                        String mainMessagePack = AppConfig.getMessagesPack();
                        for (String name : names) {
                            options.put(MessageProvider.getMessage(mainMessagePack, "Datatype." + name), name);
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
                item.setStringValue(object.toString());
            } catch (IllegalArgumentException | ParseException e) {
                showNotification(getMessage("unableToParseValue"), NotificationType.ERROR);
                return;
            }
        }
        super.commitAndClose();
    }
}
