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
import com.haulmont.cuba.security.entity.PermissionTarget;

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
                Set<PermissionTarget> current = new HashSet<PermissionTarget>();
                if (value != null) {
                    for (Object obj : ((Collection) value)) {
                        if (obj != null)
                            current.add((PermissionTarget) obj);
                    }
                }

                PermissionTarget item = entityPermissionsDs.getItem();
                for (PermissionTarget target : new ArrayList<PermissionTarget>(targets)) {
                    if (sameEntity(item, target))
                        targets.remove(target);
                }

                for (PermissionTarget target : current) {
                    targets.add(target);
                }
            }

            private boolean sameEntity(PermissionTarget t1, PermissionTarget t2) {
                String[] s1 = t1.getId().split(":");
                String[] s2 = t2.getId().split(":");
                return s1[1].equals(s2[1]);
            }
        });
    }
}
