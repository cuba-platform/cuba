/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.app.security.sessionattr.edit;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.SessionAttribute;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

public class SessionAttributeEditor extends AbstractEditor<SessionAttribute> {

    @Named("attribute")
    protected Datasource<SessionAttribute> datasource;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected FieldGroup fields;
    @Inject
    protected Button windowCommit;

    protected LookupField datatypeLookup;

    @Override
    public void init(Map<String, Object> params) {

        datatypeLookup = uiComponents.create(LookupField.NAME);
        datatypeLookup.setDatasource(datasource, "datatype");
        datatypeLookup.setRequiredMessage(getMessage("datatypeMsg"));
        datatypeLookup.setRequired(true);
        datatypeLookup.setPageLength(15);

        Map<String, Object> options = new TreeMap<>();
        for (String datatypeId : Datatypes.getIds()) {
            options.put(messages.getMainMessage("Datatype." + datatypeId), datatypeId);
        }
        datatypeLookup.setOptionsMap(options);

        fields.getField("datatype").setComponent(datatypeLookup);
    }

    @Override
    protected void postInit() {
        super.postInit();
        if (getItem().isPredefined()) {
            setReadOnly(true);
            showNotification(getMessage("predefinedGroupIsUnchangeable"));
        }
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