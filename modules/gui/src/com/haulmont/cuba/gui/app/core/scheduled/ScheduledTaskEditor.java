/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.ValueListener;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ScheduledTaskEditor extends AbstractEditor {

    @Inject
    protected LookupField beanNameField;

    @Inject
    protected LookupField methodNameField;

    @Inject
    protected LookupField userNameField;

    @Inject
    protected SchedulingService service;

    @Override
    public void init(Map<String, Object> params) {
        final Map<String,List<String>> availableBeans = service.getAvailableBeans();
        beanNameField.setOptionsList(new ArrayList<String>(availableBeans.keySet()));
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
    }
}
