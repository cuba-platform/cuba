/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.09.2010 15:08:14
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.security.sessionattr.edit;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.SessionAttribute;
import com.haulmont.cuba.web.gui.components.WebLookupField;

import java.text.ParseException;
import java.util.*;

public class SessionAttributeEditor extends AbstractEditor {

    protected Datasource<SessionAttribute> datasource;

    public SessionAttributeEditor(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        datasource = getDsContext().get("attribute");

        FieldGroup fields = getComponent("fields");
        FieldGroup.Field field = fields.getField("datatype");
        fields.addCustomField(field,
                new FieldGroup.CustomFieldGenerator() {
                    public Component generateField(Datasource datasource, Object propertyId) {
                        WebLookupField lookup = new WebLookupField();
                        lookup.setDatasource(datasource, (String) propertyId);
                        lookup.setRequired(true);

                        Map<String, Object> options = new TreeMap<String, Object>();
                        Set<String> names = Datatypes.getInstance().getNames();
                        String mainMessagePack = AppConfig.getInstance().getMessagesPack();
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
            Datatype dt = Datatypes.getInstance().get(item.getDatatype());
            try {
                dt.parse(item.getStringValue());
            } catch (ParseException e) {
                showNotification(getMessage("unableToParseValue"), NotificationType.ERROR);
                return;
            }
        }
        super.commitAndClose();
    }
}
