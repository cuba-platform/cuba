/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.security.MultiplePermissionTargetsDatasource;
import com.haulmont.cuba.gui.security.RestorablePermissionDatasource;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.ui.MultiplePermissionTarget;

import javax.inject.Inject;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class AttributePermissionsFrame extends AbstractFrame {

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private RestorablePermissionDatasource propertyPermissionsDs;

    @Inject
    private MultiplePermissionTargetsDatasource attributeTargetsDs;

    @Inject
    private Table propertyPermissionsTable;

    @Inject
    private Label selectedTargetCaption;

    /* Filter */

    @Inject
    private TextField entityFilter;

    @Inject
    private CheckBox assignedOnlyCheckBox;

    /* Buttons */

    @Inject
    private Button applyFilterBtn;

    /* Checkboxes */

    @Inject
    private CheckBox allModifyCheck;

    @Inject
    private CheckBox allReadOnlyCheck;

    @Inject
    private CheckBox allHideCheck;

    private class AttributeOperationControl {

        private CheckBox modifyCheckBox;
        private CheckBox readOnlyCheckBox;
        private CheckBox hideCheckBox;


    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        assignedOnlyCheckBox.setValue(Boolean.TRUE);

        attributeTargetsDs.setPermissionDs(propertyPermissionsDs);
        attributeTargetsDs.setFilter(
                new EntityNameFilter<MultiplePermissionTarget>(assignedOnlyCheckBox, entityFilter));

        applyFilterBtn.setAction(new AbstractAction("action.apply") {
            @Override
            public void actionPerform(Component component) {
                attributeTargetsDs.refresh();
            }
        });
    }
}