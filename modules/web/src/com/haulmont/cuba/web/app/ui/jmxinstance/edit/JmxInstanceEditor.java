/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author artamonov
 * @version $Id$
 */
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

        getDialogParams().setWidth(themeConstants.getInt("cuba.web.JmxInstanceEditor.width"));

        jmxFieldGroup.addCustomField("password", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                passwordField = factory.createComponent(PasswordField.NAME);
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
        getItem().setPassword(passwordField.<String>getValue());
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