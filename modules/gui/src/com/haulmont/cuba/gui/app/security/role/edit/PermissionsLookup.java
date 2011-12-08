/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.PermissionTarget;
import org.apache.commons.lang.BooleanUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class PermissionsLookup extends AbstractLookup {

    protected Tree permissionsTree;
    protected String type;
    @SuppressWarnings({"unchecked"})
    protected LinkedList<PermissionTarget> targets;
    protected CollectionDatasource<PermissionTarget, String> entityPermissionsDs;
    private Companion companion;

    public interface Companion {
        void initPermissionsTree(WidgetsTree tree);

        void initPermissionsTreeComponents(BoxLayout box, Label label, CheckBox checkBox);
    }

    public PermissionsLookup(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        companion = getCompanion();

        permissionsTree = getComponent("permissions-tree");
        entityPermissionsDs = permissionsTree.getDatasource();

        entityPermissionsDs.refresh();
        permissionsTree.expandTree();

        targets = new LinkedList<PermissionTarget>();

        addListener(new CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if ("select".equals(actionId))
                    getLookupHandler().handleLookup(targets);
            }
        });

        Button checkAll = getComponent("checkAll");
        if (checkAll != null)
            checkAll.setAction(new AbstractAction("checkAll") {
                @Override
                public void actionPerform(Component component) {
                    HierarchicalDatasource datasource = (HierarchicalDatasource) permissionsTree.getDatasource();
                    if (datasource != null) {
                        for (String uuid : (Collection<String>) datasource.getItemIds()) {
                            PermissionTarget target = (PermissionTarget) datasource.getItem(uuid);
                            if (!targets.contains(target) && target.getPermissionValue() != null)
                                targets.add(target);
                        }
                        permissionsTree.refresh();
                    }
                }

                @Override
                public String getCaption() {
                    return "";
                }
            });

        Button uncheckAll = getComponent("uncheckAll");
        if (uncheckAll != null)
            uncheckAll.setAction(new AbstractAction("uncheckAll") {
                @Override
                public void actionPerform(Component component) {
                    targets.clear();
                    permissionsTree.refresh();
                }

                @Override
                public String getCaption() {
                    return "";
                }
            });

        if (permissionsTree instanceof WidgetsTree) {
            if (companion != null)
                companion.initPermissionsTree((WidgetsTree) permissionsTree);

            ((WidgetsTree) permissionsTree).setWidgetBuilder(
                    new WidgetsTree.WidgetBuilder() {
                        @Override
                        public Component build(HierarchicalDatasource datasource, Object itemId, boolean leaf) {
                            final PermissionTarget target = (PermissionTarget) datasource.getItem(itemId);

                            ComponentsFactory factory = AppConfig.getFactory();
                            BoxLayout box = factory.createComponent(BoxLayout.HBOX);
                            box.setMargin(false);
                            box.setSpacing(false);

                            Label label = factory.createComponent(Label.NAME);
                            label.setValue(target.getCaption());

                            box.add(label);

                            CheckBox checkBox = null;
                            if (target.getPermissionValue() != null) {
                                final CheckBox cb = factory.createComponent(CheckBox.NAME);
                                if (targets.contains(target)) {
                                    cb.setValue(true);
                                }
                                cb.addListener(
                                        new ValueListener() {
                                            @Override
                                            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                                                if (BooleanUtils.isTrue((Boolean) cb.getValue())) {
                                                    targets.add(target);
                                                } else {
                                                    targets.remove(target);
                                                }
                                            }
                                        }
                                );
                                box.add(cb);
                                checkBox = cb;
                            }

                            if (companion != null)
                                companion.initPermissionsTreeComponents(box, label, checkBox);

                            return box;
                        }
                    }
            );
        }

        Label permissionsType = getComponent("permissionsType");
        type = (String) params.get("param$PermissionValue");
        if (PermissionValue.ALLOW.name().equals(type)) {
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class, "PermissionValue.ALLOW"));
        } else if (PermissionValue.DENY.name().equals(type)) {
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class, "PermissionValue.DENY"));
        } else if (PropertyPermissionValue.MODIFY.name().equals(type)) {
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class, "PropertyPermissionValue.MODIFY"));
        } else if (PropertyPermissionValue.VIEW.name().equals(type)) {
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class, "PropertyPermissionValue.VIEW"));
        } else if ("FORBID".equals(type)) {
            permissionsType.setValue(MessageProvider.getMessage(ScreenPermissionsLookup.class, "PropertyPermissionValue.DENY"));
        } else {
            permissionsType.setVisible(false);
        }
    }
}
