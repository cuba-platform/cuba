/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.jmxinstance.edit;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
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

    protected TextField passwordField;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        getDialogParams().setWidth(490);

        jmxFieldGroup.addCustomField("password", new FieldGroup.CustomFieldGenerator() {
            @Override
            public Component generateField(Datasource datasource, String propertyId) {
                passwordField = factory.createComponent(TextField.NAME);
                passwordField.setRequired(true);
                passwordField.setRequiredMessage(getMessage("passwordRequiredMsg"));
                passwordField.setSecret(true);
                return passwordField;
            }
        });
    }

    @Override
    protected void postInit() {
        super.postInit();

        passwordField.setValue(getItem().getPassword());
    }

    @Override
    protected boolean preCommit() {
        return validateConnection() && super.preCommit();
    }

    private boolean validateConnection() {
        getItem().setPassword(passwordField.<String>getValue());
        // try to connect to instance and assign cluster node name
        try {
            String remoteNodeName = jmxControlAPI.getRemoteNodeName(getItem());
            if (StringUtils.isEmpty(getItem().getNodeName()))
                getItem().setNodeName(remoteNodeName);
        } catch (SecurityException e) {
            showNotification(getMessage("invalidCredentials"), NotificationType.WARNING);
            return false;
        } catch (Exception e) {
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