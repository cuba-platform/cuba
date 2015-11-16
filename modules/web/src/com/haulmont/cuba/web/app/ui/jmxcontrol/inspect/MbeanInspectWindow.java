/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.inspect;

import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.app.ui.jmxcontrol.util.AttributeEditor;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.entity.*;
import com.haulmont.cuba.web.toolkit.ui.CubaTable;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * @author budarov
 * @version $Id$
 */
public class MbeanInspectWindow extends AbstractEditor {

    private Logger log = LoggerFactory.getLogger(getClass());

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
    protected ComponentsFactory componentsFactory;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        CubaTable vaadinAttrTable = (CubaTable) WebComponentsHelper.unwrap(attributesTable);
        vaadinAttrTable.setTextSelectionEnabled(true);

        attributesTable.setItemClickAction(editAttributeAction);
        attributesTable.addGeneratedColumn("type", entity -> {
            Label label = componentsFactory.createComponent(Label.class);
            label.setValue(AttributeHelper.convertTypeToReadableName(((ManagedBeanAttribute) entity).getType()));
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

        Editor w = openEditor("jmxConsoleEditAttribute", mba, OpenType.DIALOG);
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
        for (final ManagedBeanOperation op : mbean.getOperations()) {
            BoxLayout vl = componentsFactory.createComponent(VBoxLayout.class);
            vl.setMargin(false, false, true, false);
            vl.setSpacing(true);
            vl.setStyleName("cuba-mbeans-operation-container");

            Label nameLbl = componentsFactory.createComponent(Label.class);
            nameLbl.setValue(AttributeHelper.convertTypeToReadableName(op.getReturnType()) + " " + op.getName() + "()");
            nameLbl.setStyleName("h2");
            vl.add(nameLbl);

            if (StringUtils.isNotEmpty(op.getDescription())) {
                Label descrLbl = componentsFactory.createComponent(Label.class);
                descrLbl.setValue(op.getDescription());
                vl.add(descrLbl);
            }

            final List<AttributeEditor> attrProviders = new ArrayList<>();

            if (!op.getParameters().isEmpty()) {
                GridLayout grid = componentsFactory.createComponent(GridLayout.class);
                grid.setSpacing(true);
                grid.setColumns(3);
                grid.setRows(op.getParameters().size());
                int row = 0;
                for (ManagedBeanOperationParameter param : op.getParameters()) {
                    Label pnameLbl = componentsFactory.createComponent(Label.class);
                    pnameLbl.setValue(param.getName());

                    Label ptypeLbl = componentsFactory.createComponent(Label.class);
                    ptypeLbl.setValue(AttributeHelper.convertTypeToReadableName(param.getType()));

                    AttributeEditor prov = new AttributeEditor(this, param.getType());
                    attrProviders.add(prov);
                    Component editField = prov.getComponent();

                    Component editComposition = editField;

                    if (StringUtils.isNotBlank(param.getDescription())) {
                        Label pdescrLbl = componentsFactory.createComponent(Label.class);
                        pdescrLbl.setValue(param.getDescription());

                        BoxLayout editorLayout = componentsFactory.createComponent(VBoxLayout.class);
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

            Button invokeBtn = componentsFactory.createComponent(Button.class);
            invokeBtn.setAction(new AbstractAction("invoke") {
                @Override
                public void actionPerform(Component component) {
                    invokeOperation(op, attrProviders);
                }

                @Override
                public String getCaption() {
                    return getMessage("mbean.operation.invoke");
                }
            });

            vl.add(invokeBtn);
            container.add(vl);
        }
        if (mbean.getOperations().isEmpty()) {
            Label lbl = componentsFactory.createComponent(Label.class);
            lbl.setValue(getMessage("mbean.operations.none"));
            container.add(lbl);
        }
    }

    protected void invokeOperation(ManagedBeanOperation op, List<AttributeEditor> attrProviders) {
        Map<String, Object> params = new HashMap<>();
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

        try {
            Object res = jmxControlAPI.invokeOperation(op, paramValues);
            if (res != null) {
                params.put("result", res);
            }
        } catch (Exception e) {
            params.put("exception", e);
        }

        Window w = openWindow("jmxConsoleOperationResult", OpenType.DIALOG, params);
        w.addCloseListener(actionId -> reloadAttributes());
    }

    public void close() {
        super.close(Window.CLOSE_ACTION_ID, true);
    }
}