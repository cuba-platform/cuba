/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.google.common.base.Predicate;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.security.EntityPermissionTargetsDatasource;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.ui.OperationPermissionTarget;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityPermissionsFrame extends AbstractFrame {

    @Inject
    private Datasource<Role> roleDs;

    @Inject
    private CollectionDatasource<Permission, UUID> entityPermissionsDs;

    @Inject
    private EntityPermissionTargetsDatasource entityTargetsDs;

    @Inject
    private Table entityPermissionsTable;

    @Inject
    private TextField entityFilter;

    @Inject
    private CheckBox assignedOnlyCheckBox;

    @Inject
    private Button applyFilterBtn;

    /* Checkboxes */

    @Inject
    private CheckBox allAllowCheck;

    @Inject
    private CheckBox allDenyCheck;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        assignedOnlyCheckBox.setValue(Boolean.TRUE);

        entityTargetsDs.setPermissionDs(entityPermissionsDs);
        entityTargetsDs.setFilter(new Predicate<OperationPermissionTarget>() {
            @Override
            public boolean apply(@Nullable OperationPermissionTarget input) {
                if (input != null) {
                    if (Boolean.TRUE.equals(assignedOnlyCheckBox.getValue()) && !input.isAssigned())
                        return false;

                    String filterValue = entityFilter.<String>getValue();
                    if (StringUtils.isNotBlank(filterValue))
                        return input.getPermissionValue().contains(filterValue);
                    else
                        return true;
                }
                return false;
            }
        });
        applyFilterBtn.setAction(new AbstractAction("action.apply") {
            @Override
            public void actionPerform(Component component) {
                entityTargetsDs.refresh();
            }
        });
        entityPermissionsDs.refresh();
        entityTargetsDs.refresh();
    }
}
