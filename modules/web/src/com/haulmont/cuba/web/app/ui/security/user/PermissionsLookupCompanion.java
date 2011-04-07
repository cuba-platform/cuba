/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.user;

import com.haulmont.cuba.gui.app.security.role.edit.PermissionsLookup;
import com.haulmont.cuba.gui.components.AbstractCompanion;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.gui.components.*;

import java.util.LinkedList;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class PermissionsLookupCompanion extends AbstractCompanion implements PermissionsLookup.Companion {

    public PermissionsLookupCompanion(AbstractFrame frame) {
        super(frame);
    }

    public void initEntityPermissionsTree(WidgetsTree entityPermissionsTree, final LinkedList<PermissionConfig.Target> targets) {
        WebComponentsHelper.unwrap(entityPermissionsTree).addStyleName("empty");
        entityPermissionsTree.setWidgetBuilder(
                new WebWidgetsTree.WidgetBuilder() {
                    public Component build(HierarchicalDatasource datasource, Object itemId, boolean leaf) {
                        final PermissionConfig.Target target = (PermissionConfig.Target) datasource.getItem(itemId);
                        WebHBoxLayout hLayout = new WebHBoxLayout();
                        WebLabel labelCaption = new WebLabel();
                        labelCaption.setValue(target.getCaption());
                        WebCheckBox checkBox = null;
                        if (target.getValue() != null) {
                            checkBox = new WebCheckBox();
                            final com.vaadin.ui.CheckBox vCheckBox = (com.vaadin.ui.CheckBox) WebComponentsHelper.unwrap(checkBox);
                            if (targets.contains(target)) {
                                checkBox.setValue(true);
                            }

                            vCheckBox.addListener(new com.vaadin.ui.CheckBox.ClickListener() {
                                public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                                    if ((Boolean) vCheckBox.getValue()) {
                                        targets.add(target);
                                    } else {
                                        targets.remove(target);
                                    }
                                }
                            });
                        }
                        hLayout.add(labelCaption);
                        if (checkBox != null)
                            hLayout.add(checkBox);
                        hLayout.setSpacing(true);
                        return hLayout;
                    }
                }
        );
    }
}
