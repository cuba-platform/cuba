/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.entity.ScheduledTaskDefinedBy;
import com.haulmont.cuba.core.global.Encryption;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;

import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ScheduledTaskEditor extends AbstractEditor {

    @Inject
    protected LookupField beanNameField;

    @Inject
    protected LookupField methodNameField;

    @Inject
    protected LookupField userNameField;

    @Inject
    protected LookupField definedByField;

    @Inject
    protected TextField userPasswordField;

    @Inject
    protected TextField classNameField;

    @Inject
    protected TextField scriptNameField;

    @Inject
    protected Label beanNameLabel;

    @Inject
    protected Label methodNameLabel;

    @Inject
    protected Label classNameLabel;

    @Inject
    protected Label scriptNameLabel;

    @Inject
    protected Datasource<ScheduledTask> taskDs;

    @Inject
    protected SchedulingService service;

    @Inject
    protected Encryption encryption;

    @Override
    public void init(Map<String, Object> params) {
        definedByField.setOptionsList(Arrays.asList(ScheduledTaskDefinedBy.values()));
        definedByField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (ScheduledTaskDefinedBy.BEAN == value) {
                    hideAll();
                    show(beanNameField, beanNameLabel, methodNameField, methodNameLabel);
                } else if (ScheduledTaskDefinedBy.CLASS == value) {
                    hideAll();
                    show(classNameField, classNameLabel);
                } else if (ScheduledTaskDefinedBy.SCRIPT == value) {
                    hideAll();
                    show(scriptNameField, scriptNameLabel);
                } else {
                    hideAll();
                }
            }

            private void show(Component... components) {
                for (Component component : components) {
                    component.setVisible(true);
                }
            }

            private void hideAll() {
                classNameField.setVisible(false);
                classNameLabel.setVisible(false);
                scriptNameField.setVisible(false);
                scriptNameLabel.setVisible(false);
                beanNameField.setVisible(false);
                beanNameLabel.setVisible(false);
                methodNameField.setVisible(false);
                methodNameLabel.setVisible(false);
            }
        });

        final Map<String, List<String>> availableBeans = service.getAvailableBeans();
        beanNameField.setOptionsList(new ArrayList<>(availableBeans.keySet()));
        beanNameField.addListener(new ValueListener<LookupField>() {
            @Override
            public void valueChanged(LookupField source, String property, Object prevValue, Object value) {
                if (value == null)
                    methodNameField.setOptionsList(Collections.emptyList());
                else {
                    List<String> list = availableBeans.get(value);
                    methodNameField.setOptionsList(list == null ? Collections.emptyList() : list);
                }
            }
        });

        userNameField.setOptionsList(service.getAvailableUsers());

        userPasswordField.setValue("-----");
        userPasswordField.addListener(new ValueListener<TextField>() {
            @Override
            public void valueChanged(TextField source, String property, Object prevValue, Object value) {
                taskDs.getItem().setUserPassword(encryption.getPlainHash((String) value));
            }
        });
    }
}