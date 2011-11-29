/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.ValueListener;

import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityPermissionsLookup extends PermissionsLookup {

    public EntityPermissionsLookup(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        initOptionsGroup();
    }

    private void initOptionsGroup() {
        OptionsGroup targetsGroup = getComponent("permissions");
        targetsGroup.addListener(new ValueListener<OptionsGroup>() {
            @Override
            public void valueChanged(OptionsGroup source, String property, Object prevValue, Object value) {
                Set<PermissionConfig.Target> current = new HashSet<PermissionConfig.Target>();
                if (value != null) {
                    for (Object obj : ((Collection) value)) {
                        if (obj != null)
                            current.add((PermissionConfig.Target) obj);
                    }
                }

                PermissionConfig.Target item = entityPermissionsDs.getItem();
                for (PermissionConfig.Target target : new ArrayList<PermissionConfig.Target>(targets)) {
                    if (sameEntity(item, target))
                        targets.remove(target);
                }

                for (PermissionConfig.Target target : current) {
                    targets.add(target);
                }
            }

            private boolean sameEntity(PermissionConfig.Target t1, PermissionConfig.Target t2) {
                String[] s1 = t1.getId().split(":");
                String[] s2 = t2.getId().split(":");
                return s1[1].equals(s2[1]);
            }
        });
    }
}
