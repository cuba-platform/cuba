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

package com.haulmont.cuba.web.app.ui.jmxcontrol.inspect;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.backgroundwork.BackgroundWorkWindow;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.ui.jmxcontrol.util.AttributeEditor;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanAttribute;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanInfo;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanOperation;
import com.haulmont.cuba.web.jmx.entity.ManagedBeanOperationParameter;
import com.haulmont.cuba.web.widgets.CubaTable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.haulmont.cuba.web.jmx.entity.AttributeHelper.convertTypeToReadableName;

public class MbeanInspectWindow extends AbstractEditor {

    private final Logger log = LoggerFactory.getLogger(MbeanInspectWindow.class);

    @Inject
    protected Table<ManagedBeanAttribute> attributesTable;

    @Named("attributesTable.edit")
    protected Action editAttributeAction;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected BoxLayout operations;

    @Inject
    protected CollectionDatasource<ManagedBeanAttribute, UUID> attrDs;

    @Inject
    protected UiComponents uiComponents;

    @Inject
    protected WebConfig webConfig;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        attributesTable.unwrap(CubaTable.class)
                .setTextSelectionEnabled(true);

        attributesTable.setItemClickAction(editAttributeAction);
        attributesTable.addGeneratedColumn("type", entity -> {
            Label<String> label = uiComponents.create(Label.NAME);
            label.setValue(convertTypeToReadableName(entity.getType()));
            return label;
        });

        attrDs.addCollectionChangeListener(e -> {
            if (e.getDs().getItemIds().isEmpty()) {
                attributesTable.setHeight("80px"); // reduce its height if no attributes
            }
        });

        removeAction("windowCommit");
    }

    public void editAttribute() {
        Set<ManagedBeanAttribute> selected = attributesTable.getSelected();
        if (selected.isEmpty()) {
            return;
        }

        ManagedBeanAttribute mba = selected.iterator().next();
        if (!mba.getWriteable()) {
            return;
        }

        AbstractEditor w = openEditor("jmxConsoleEditAttribute", mba, OpenType.DIALOG);
        w.addCloseListener(actionId -> {
            if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                Object item = w.getItem();
                reloadAttribute((ManagedBeanAttribute) item);
            }
        });
    }

    protected void reloadAttribute(ManagedBeanAttribute attribute) {
        jmxControlAPI.loadAttributeValue(attribute);

        attrDs.updateItem(attribute);
    }

    public void reloadAttributes() {
        attributesTable.getDatasource().refresh();
    }

    @Override
    protected void postInit() {
        initOperationsLayout((ManagedBeanInfo) getItem());

        ManagedBeanInfo mbean = (ManagedBeanInfo) getItem();
        if (mbean.getObjectName() != null) {
            setCaption(formatMessage("inspectMbean.title.format", mbean.getObjectName()));
        }
    }

    protected void initOperationsLayout(ManagedBeanInfo mbean) {
        BoxLayout container = operations;

        for (ManagedBeanOperation op : mbean.getOperations()) {
            BoxLayout vl = uiComponents.create(VBoxLayout.class);
            vl.setMargin(false, false, true, false);
            vl.setSpacing(true);
            vl.setStyleName("c-mbeans-operation-container");

            Label<String> nameLbl = uiComponents.create(Label.TYPE_DEFAULT);
            nameLbl.setValue(convertTypeToReadableName(op.getReturnType()) + " " + op.getName() + "()");
            nameLbl.setStyleName("h2");
            vl.add(nameLbl);

            if (StringUtils.isNotEmpty(op.getDescription())) {
                Label<String> descrLbl = uiComponents.create(Label.TYPE_DEFAULT);
                descrLbl.setValue(op.getDescription());
                vl.add(descrLbl);
            }

            List<AttributeEditor> attrProviders = new ArrayList<>();

            if (!op.getParameters().isEmpty()) {
                GridLayout grid = uiComponents.create(GridLayout.class);
                grid.setSpacing(true);
                grid.setColumns(3);
                grid.setRows(op.getParameters().size());
                int row = 0;
                for (ManagedBeanOperationParameter param : op.getParameters()) {
                    Label<String> pnameLbl = uiComponents.create(Label.TYPE_DEFAULT);
                    pnameLbl.setValue(param.getName());

                    Label<String> ptypeLbl = uiComponents.create(Label.TYPE_DEFAULT);
                    ptypeLbl.setValue(convertTypeToReadableName(param.getType()));

                    AttributeEditor prov = new AttributeEditor(frame, param.getType());
                    attrProviders.add(prov);
                    Component editField = prov.getComponent();

                    Component editComposition = editField;

                    if (StringUtils.isNotBlank(param.getDescription())) {
                        Label<String> pdescrLbl = uiComponents.create(Label.TYPE_DEFAULT);
                        pdescrLbl.setValue(param.getDescription());

                        BoxLayout editorLayout = uiComponents.create(VBoxLayout.class);
                        editorLayout.add(editField);
                        editorLayout.add(pdescrLbl);

                        editComposition = editorLayout;
                    }

                    grid.add(pnameLbl, 0, row);
                    grid.add(ptypeLbl, 1, row);
                    grid.add(editComposition, 2, row);
                    row++;
                }
                vl.add(grid);
            }

            Button invokeBtn = uiComponents.create(Button.class);
            invokeBtn.setAction(new BaseAction("invoke")
                    .withCaption(getMessage("mbean.operation.invoke"))
                    .withHandler(event ->
                            invokeOperation(op, attrProviders)
                    ));

            vl.add(invokeBtn);
            container.add(vl);
        }
        if (mbean.getOperations().isEmpty()) {
            Label<String> lbl = uiComponents.create(Label.TYPE_DEFAULT);
            lbl.setValue(getMessage("mbean.operations.none"));
            container.add(lbl);
        }
    }

    protected void invokeOperation(ManagedBeanOperation op, List<AttributeEditor> attrProviders) {
        Object[] paramValues = new Object[attrProviders.size()];
        try {
            for (int i = 0; i < attrProviders.size(); i++) {
                paramValues[i] = attrProviders.get(i).getAttributeValue(true);
            }
        } catch (Exception e) {
            log.error("Conversion error", e);
            showNotification(getMessage("invokeOperation.conversionError"), NotificationType.HUMANIZED);
            return;
        }

        if (op.getRunAsync() && webConfig.getPushEnabled()) {
            runAsynchronously(op, paramValues, op.getTimeout());
        } else {
            runSynchronously(op, paramValues);
        }
    }

    protected void runSynchronously(ManagedBeanOperation operation, Object[] paramValues) {
        Map<String, Object> resultMap = Collections.emptyMap();
        try {
            Object res = jmxControlAPI.invokeOperation(operation, paramValues);
            if (res != null) {
                resultMap = ParamsMap.of(
                        "result", res,
                        "beanName", operation.getMbean().getClassName(),
                        "methodName", operation.getName());
            }
        } catch (Exception ex) {
            log.error("Error occurs while performing JMX operation {}", operation.getName(), ex);

            resultMap = ParamsMap.of(
                    "exception", ex,
                    "beanName", operation.getMbean().getClassName(),
                    "methodName", operation.getName());
        }

        Window w = openWindow("jmxConsoleOperationResult", OpenType.DIALOG, resultMap);
        w.addCloseListener(actionId -> reloadAttributes());
    }

    protected void runAsynchronously(ManagedBeanOperation operation, Object[] paramValues, Long timeout) {
        BackgroundTask<Long, Object> task = new BackgroundTask<Long, Object>(timeout, TimeUnit.MILLISECONDS, this) {
            @Override
            public Object run(TaskLifeCycle<Long> taskLifeCycle) {
                return jmxControlAPI.invokeOperation(operation, paramValues);
            }

            @Override
            public void done(Object result) {
                Window w = openWindow("jmxConsoleOperationResult", OpenType.DIALOG, ParamsMap.of(
                        "result", result,
                        "beanName", operation.getMbean().getClassName(),
                        "methodName", operation.getName()));
                w.addCloseListener(actionId -> reloadAttributes());
            }

            @Override
            public boolean handleException(Exception ex) {
                log.error("Error occurs while performing JMX operation {}", operation.getName(), ex);

                Window w = openWindow("jmxConsoleOperationResult", OpenType.DIALOG, ParamsMap.of(
                        "exception", ex,
                        "beanName", operation.getMbean().getClassName(),
                        "methodName", operation.getName()));

                w.addCloseListener(actionId -> reloadAttributes());
                return true;
            }
        };

        BackgroundWorkWindow.show(task, true);
    }

    public void close() {
        super.close(Window.CLOSE_ACTION_ID, true);
    }
}