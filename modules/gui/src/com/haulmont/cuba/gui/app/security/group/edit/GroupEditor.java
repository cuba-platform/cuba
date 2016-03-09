/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.group.edit;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.PickerField;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Named;
import java.util.Map;

/**
 * @author artamonov
 */
public class GroupEditor extends AbstractEditor {

    @Named("fieldGroup.parent")
    protected PickerField parentField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        if (BooleanUtils.isTrue((Boolean) params.get("edit"))) {
            parentField.setVisible(true);
            PickerField.LookupAction lookupAction = new PickerField.LookupAction(parentField);
            lookupAction.setLookupScreenParams(ParamsMap.of("exclude", params.get("ITEM")));
            parentField.addAction(lookupAction);
        }
    }
}