/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.browse;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.app.ui.jmxcontrol.ds.ManagedBeanInfoDatasource;
import com.haulmont.cuba.web.app.ui.jmxinstance.edit.JmxInstanceEditor;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.*;

/**
 * @author budarov
 * @version $Id$
 */
public class MbeansDisplayWindow extends AbstractWindow {

    @Inject
    protected ManagedBeanInfoDatasource mbeanDs;

    @Inject
    protected CollectionDatasource<JmxInstance, UUID> jmxInstancesDs;

    @Inject
    protected TextField objectNameField;

    @Inject
    protected Label localJmxField;

    @Resource(name = "mbeans")
    protected TreeTable mbeansTable;

    @Inject
    protected LookupPickerField jmxConnectionField;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    protected JmxInstance localJmxInstance;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        objectNameField.addListener(new ObjectNameFieldListener());

        mbeansTable.addAction(new RefreshAction(mbeansTable));

        Action inspectAction = new ItemTrackingAction("inspect") {
            @Override
            public void actionPerform(Component component) {
                Set<ManagedBeanInfo> selected = target.getSelected();
                if (!selected.isEmpty()) {
                    ManagedBeanInfo mbi = selected.iterator().next();
                    if (mbi.getObjectName() != null) { // otherwise it's a fake root node
                        Window editor = openEditor("jmxConsoleInspectMbean", mbi, WindowManager.OpenType.THIS_TAB);
                        editor.addListener(new CloseListener() {
                            @Override
                            public void windowClosed(String actionId) {
                                target.requestFocus();
                            }
                        });
                    } else { // expand / collapse fake root node
                        TreeTable treeTable = (TreeTable) target;
                        UUID itemId = mbi.getId();
                        if (treeTable.isExpanded(itemId)) {
                            treeTable.collapse(itemId);
                        } else {
                            treeTable.expand(itemId);
                        }
                    }
                }
            }

            @Override
            public String getCaption() {
                return getMessage("action.inspect");
            }
        };

        mbeansTable.addAction(inspectAction);
        mbeansTable.setItemClickAction(inspectAction);

        localJmxInstance = jmxControlAPI.getLocalInstance();

        jmxInstancesDs.refresh();
        jmxConnectionField.setValue(localJmxInstance);
        jmxConnectionField.setRequired(true);
        jmxConnectionField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                JmxInstance jmxInstance = jmxConnectionField.getValue();
                try {
                    mbeanDs.setJmxInstance(jmxInstance);
                    mbeanDs.refresh();
                } catch (JmxControlException e) {
                    showNotification(getMessage("unableToConnectToInterface"), NotificationType.WARNING);
                    if (jmxInstance != localJmxInstance)
                        jmxConnectionField.setValue(localJmxInstance);
                }
            }
        });

        jmxConnectionField.removeAllActions();

        jmxConnectionField.addAction(new PickerField.LookupAction(jmxConnectionField) {
            @Override
            public void afterCloseLookup(String actionId) {
                jmxInstancesDs.refresh();
            }
        });

        jmxConnectionField.addAction(new AbstractAction("actions.Add") {
            @Override
            public void actionPerform(Component component) {
                final JmxInstanceEditor instanceEditor = openEditor("sys$JmxInstance.edit", new JmxInstance(), WindowManager.OpenType.DIALOG);
                instanceEditor.addListener(new CloseListener() {
                    @Override
                    public void windowClosed(String actionId) {
                        if (COMMIT_ACTION_ID.equals(actionId)) {
                            jmxInstancesDs.refresh();
                            jmxConnectionField.setValue(instanceEditor.getItem());
                        }
                    }
                });
            }

            @Override
            public String getIcon() {
                return "icons/plus-btn.png";
            }
        });

        mbeanDs.setJmxInstance(localJmxInstance);
        mbeanDs.refresh();

        localJmxField.setValue(jmxControlAPI.getLocalNodeName());
        localJmxField.setEditable(false);

        mbeansTable.setStyleProvider(new Table.StyleProvider() {
            @Nullable
            @Override
            public String getStyleName(Entity entity, @Nullable String property) {
                if (entity instanceof ManagedBeanInfo && ((ManagedBeanInfo) entity).getObjectName() == null) {
                    return "cuba-jmx-tree-table-domain";
                }
                return null;
            }
        });
    }

    private class ObjectNameFieldListener implements ValueListener {
        @Override
        public void valueChanged(Object source, String property, Object prevValue, Object value) {
            Map<String, Object> params = new HashMap<>();
            params.put("objectName", value);
            mbeanDs.refresh(params);
            if (StringUtils.isNotEmpty((String) value)) {
                mbeansTable.expandAll();
            }
        }
    }
}