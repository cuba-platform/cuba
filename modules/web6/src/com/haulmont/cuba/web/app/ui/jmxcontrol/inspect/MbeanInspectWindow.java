/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.jmxcontrol.inspect;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.app.ui.jmxcontrol.util.AttributeEditor;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.jmx.JmxControlAPI;
import com.haulmont.cuba.web.jmx.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * @author budarov
 * @version $Id$
 */
public class MbeanInspectWindow extends AbstractEditor {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    protected Table attributesTable;

    @Named("attributesTable.edit")
    protected Action editAttributeAction;

    @Inject
    protected JmxControlAPI jmxControlAPI;

    @Inject
    protected BoxLayout operations;

    @Inject
    protected CollectionDatasource<ManagedBeanAttribute, UUID> attrDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        com.haulmont.cuba.web.toolkit.ui.Table vaadinAttrTable =
                (com.haulmont.cuba.web.toolkit.ui.Table) WebComponentsHelper.unwrap(attributesTable);
        vaadinAttrTable.setTextSelectionEnabled(true);

        attributesTable.setItemClickAction(editAttributeAction);
        attrDs.addListener(new CollectionDsListenerAdapter<ManagedBeanAttribute>() {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<ManagedBeanAttribute> items) {
                if (ds.getItemIds().isEmpty()) {
                    attributesTable.setHeight("80px"); // reduce its height if no attributes
                }
            }
        });

        Action windowCommit = getAction("windowCommit");
        if (windowCommit != null) {
            removeAction(windowCommit);
        }
    }

    public void editAttribute() {
        Set<ManagedBeanAttribute> selected = attributesTable.getSelected();
        if (selected.isEmpty()) {
            return;
        }

        ManagedBeanAttribute mba = selected.iterator().next();
        if (!mba.getWriteable() || AttributeHelper.isArray(mba.getType())) {
            return;
        }

        final Window.Editor w = openEditor("jmxConsoleEditAttribute", mba, WindowManager.OpenType.DIALOG);
        w.addListener(new CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                    Object item = w.getItem();
                    reloadAttribute((ManagedBeanAttribute) item);
                }
            }
        });
    }

    protected void reloadAttribute(ManagedBeanAttribute attribute) {
        attribute = jmxControlAPI.loadAttributeValue(attribute);

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
        ComponentsFactory componentsFactory = AppConfig.getFactory();
        BoxLayout container = operations;
        for (final ManagedBeanOperation op : mbean.getOperations()) {
            BoxLayout vl = componentsFactory.createComponent(BoxLayout.VBOX);
            vl.setMargin(false, false, true, false);
            vl.setSpacing(true);
            vl.setStyleName("operationContainer");

            Label nameLbl = componentsFactory.createComponent(Label.NAME);
            nameLbl.setValue(op.getReturnType() + " " + op.getName() + "()");
            nameLbl.setStyleName("h2");
            vl.add(nameLbl);

            if (StringUtils.isNotEmpty(op.getDescription())) {
                Label descrLbl = componentsFactory.createComponent(Label.NAME);
                descrLbl.setValue(op.getDescription());
                vl.add(descrLbl);
            }

            final List<AttributeEditor> attrProviders = new ArrayList<>();

            if (!op.getParameters().isEmpty()) {
                GridLayout grid = componentsFactory.createComponent(GridLayout.NAME);
                grid.setSpacing(true);
                grid.setColumns(3);
                grid.setRows(op.getParameters().size());
                int row = 0;
                for (ManagedBeanOperationParameter param : op.getParameters()) {
                    Label pnameLbl = componentsFactory.createComponent(Label.NAME);
                    pnameLbl.setValue(param.getName());

                    Label ptypeLbl = componentsFactory.createComponent(Label.NAME);
                    ptypeLbl.setValue(param.getType());

                    AttributeEditor prov = new AttributeEditor(this, param.getType());
                    attrProviders.add(prov);
                    Component editField = prov.getComponent();

                    Component editComposition = editField;

                    if (StringUtils.isNotBlank(param.getDescription())) {
                        Label pdescrLbl = componentsFactory.createComponent(Label.NAME);
                        pdescrLbl.setValue(param.getDescription());

                        BoxLayout editorLayout = componentsFactory.createComponent(BoxLayout.VBOX);
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

            Button invokeBtn = componentsFactory.createComponent(Button.NAME);
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
            Label lbl = componentsFactory.createComponent(Label.NAME);
            lbl.setValue(getMessage("mbean.operations.none"));
            container.add(lbl);
        }
    }

    protected void invokeOperation(ManagedBeanOperation op, List<AttributeEditor> attrProviders) {
        Map<String, Object> params = new HashMap<>();
        Object[] paramValues = new Object[attrProviders.size()];
        try {
            for (int i = 0; i < attrProviders.size(); i++) {
                paramValues[i] = attrProviders.get(i).getAttributeValue();
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

        Window w = openWindow("jmxConsoleOperationResult", WindowManager.OpenType.DIALOG, params);
        w.addListener(new CloseListener() {
            @Override
            public void windowClosed(String actionId) {
                reloadAttributes();
            }
        });
    }

    public void close() {
        super.close("close", true);
    }
}