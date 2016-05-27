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

package com.haulmont.cuba.web.app.ui.jmxinstance.edit;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.Map;

public class JmxInstanceEditor extends AbstractEditor<JmxInstance> {

    @Inject
    protected ComponentsFactory factory;

    @Inject
    protected FieldGroup jmxFieldGroup;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected ThemeConstants themeConstants;

    protected PasswordField passwordField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogOptions().setWidth(themeConstants.getInt("cuba.web.JmxInstanceEditor.width"));

        jmxFieldGroup.addCustomField("password", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                passwordField = factory.createComponent(PasswordField.class);
                passwordField.setDatasource(datasource, propertyId);
                passwordField.setRequired(true);
                passwordField.setRequiredMessage(getMessage("passwordRequiredMsg"));
                return passwordField;
            }
        });
    }

    @Override
    protected void postInit() {
        super.postInit();

        passwordField.setValue(getItem().getPassword());
    }

    protected boolean validateConnection() {
        getItem().setPassword(passwordField.getValue());
        // try to connect to instance and assign cluster node name
        try {
            String remoteNodeName = jmxControlAPI.getRemoteNodeName(getItem());
            if (StringUtils.isEmpty(getItem().getNodeName()))
                getItem().setNodeName(remoteNodeName);
        } catch (SecurityException e) {
            showNotification(getMessage("invalidCredentials"), NotificationType.WARNING);
            return false;
        } catch (JmxControlException e) {
            showNotification(getMessage("unableToConnectToInterface"), NotificationType.WARNING);
            return false;
        }
        return true;
    }

    public void testConnection() {
        if (validateAll() && validateConnection()) {
            showNotification(getMessage("successfulConnection"), NotificationType.HUMANIZED);
        }
    }
}