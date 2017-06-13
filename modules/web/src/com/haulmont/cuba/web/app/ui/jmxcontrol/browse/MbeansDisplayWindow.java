/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.browse;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.app.ui.jmxcontrol.ds.ManagedBeanInfoDatasource;
import com.haulmont.cuba.web.app.ui.jmxinstance.edit.JmxInstanceEditor;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.JmxControlException;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    protected TreeTable<ManagedBeanInfo> mbeansTable;

    @Inject
    protected LookupPickerField jmxConnectionField;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected Metadata metadata;

    protected JmxInstance localJmxInstance;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        objectNameField.addValueChangeListener(new ObjectNameFieldListener());

        mbeansTable.addAction(new RefreshAction(mbeansTable));

        Action inspectAction = new ItemTrackingAction("inspect")
                .withCaption(getMessage("action.inspect"))
                .withHandler(event -> {
                    Set<ManagedBeanInfo> selected = mbeansTable.getSelected();
                    if (!selected.isEmpty()) {
                        ManagedBeanInfo mbi = selected.iterator().next();
                        if (mbi.getObjectName() != null) { // otherwise it's a fake root node
                            Window editor = openEditor("jmxConsoleInspectMbean", mbi, OpenType.THIS_TAB);
                            editor.addCloseListener(actionId -> {
                                mbeansTable.requestFocus();
                            });
                        } else {
                            // expand / collapse fake root node
                            UUID itemId = mbi.getId();
                            if (mbeansTable.isExpanded(itemId)) {
                                mbeansTable.collapse(itemId);
                            } else {
                                mbeansTable.expand(itemId);
                            }
                        }
                    }
                });

        mbeansTable.addAction(inspectAction);
        mbeansTable.setItemClickAction(inspectAction);

        localJmxInstance = jmxControlAPI.getLocalInstance();

        jmxInstancesDs.refresh();
        jmxConnectionField.setValue(localJmxInstance);
        jmxConnectionField.setRequired(true);
        jmxConnectionField.addValueChangeListener(e -> {
            JmxInstance jmxInstance = (JmxInstance) e.getValue();
            try {
                mbeanDs.setJmxInstance(jmxInstance);
                mbeanDs.refresh();
            } catch (JmxControlException ex) {
                showNotification(getMessage("unableToConnectToInterface"), NotificationType.WARNING);
                if (jmxInstance != localJmxInstance) {
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

        jmxConnectionField.addAction(new BaseAction("actions.Add")
                .withIcon("icons/plus-btn.png")
                .withHandler(event -> {
                    JmxInstanceEditor instanceEditor = (JmxInstanceEditor) openEditor(
                            metadata.create(JmxInstance.class), OpenType.DIALOG);
                    instanceEditor.addCloseListener(actionId -> {
                        if (COMMIT_ACTION_ID.equals(actionId)) {
                            jmxInstancesDs.refresh();
                            jmxConnectionField.setValue(instanceEditor.getItem());
                        }
                    });
                }));

        mbeanDs.setJmxInstance(localJmxInstance);
        mbeanDs.refresh();

        localJmxField.setValue(jmxControlAPI.getLocalNodeName());
        localJmxField.setEditable(false);

        mbeansTable.setStyleProvider((entity, property) -> {
            if (entity != null && entity.getObjectName() == null) {
                return "c-jmx-tree-table-domain";
            }
            return null;
        });
    }

    private class ObjectNameFieldListener implements ValueChangeListener {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            mbeanDs.refresh(ParamsMap.of("objectName", e.getValue()));

            if (StringUtils.isNotEmpty((String) e.getValue())) {
                mbeansTable.expandAll();
            }
        }
    }
}