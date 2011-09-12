/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 19.08.2010 15:35:24
 * $Id$
 */

package com.haulmont.cuba.web.ui.jmxcontrol.inspect;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.jmxcontrol.app.JmxControlService;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanAttribute;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanInfo;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanOperation;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanOperationParameter;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.ui.jmxcontrol.util.AttributeEditor;
import com.haulmont.cuba.jmxcontrol.util.AttributeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class MbeanInspectWindow extends AbstractEditor {
    private static final long serialVersionUID = -7513743050483174295L;

    public MbeanInspectWindow(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        final Table attrTable = (Table) getComponent("attributes");
        com.haulmont.cuba.web.toolkit.ui.Table vaadinAttrTable = (com.haulmont.cuba.web.toolkit.ui.Table) WebComponentsHelper.unwrap(attrTable);
        vaadinAttrTable.setTextSelectionEnabled(true);

        Action refreshAction = new AbstractAction("refresh")  {
            private static final long serialVersionUID = -603235110641508028L;

            public void actionPerform(Component component) {
                reloadAttributes();
            }
        };
        attrTable.addAction(refreshAction);

        Action editAction = new AbstractAction("edit") {
            private static final long serialVersionUID = -603235110641508028L;

            public void actionPerform(Component component) {
                Set selected = attrTable.getSelected();
                if (!selected.isEmpty()) {
                    ManagedBeanAttribute mba = (ManagedBeanAttribute) selected.iterator().next();
                    if (mba.getWriteable() && !AttributeHelper.isArray(mba.getType())) {
                        final Window w = openEditor("jmxcontrol$EditAttribute", mba, WindowManager.OpenType.THIS_TAB);
                        w.addListener(new Window.CloseListener() {
                            private static final long serialVersionUID = -5430526383879890076L;

                            public void windowClosed(String actionId) {
                            if (Window.COMMIT_ACTION_ID.equals(actionId) && w instanceof Window.Editor) {
                                Object item = ((Window.Editor) w).getItem();
                                reloadAttribute((ManagedBeanAttribute) item);
                                showNotification(getMessage("editAttribute.success"), IFrame.NotificationType.HUMANIZED);
                            }
                        }
                    });

                    }
                }
            }

            @Override
            public String getCaption() {
                return getMessage("inspectmbean.attribute.edit");
            }
        };

        attrTable.setItemClickAction(editAction);

        Action closeAction = new AbstractAction("close") {
            private static final long serialVersionUID = 3530488601672890970L;

            public void actionPerform(Component component) {
                commitAndClose();
            }

            @Override
            public String getCaption() {
                return getMessage("close");
            }
        };
        ((Button) getComponent("close")).setAction(closeAction);
    }

    private void reloadAttribute(ManagedBeanAttribute attribute) {
        Table attrTable = (Table) getComponent("attributes");

        JmxControlService service = ServiceLocator.lookup(JmxControlService.NAME);
        attribute = service.loadAttributeValue(attribute);
        attrTable.getDatasource().updateItem(attribute);
    }

    private void reloadAttributes() {
        Table attrTable = (Table) getComponent("attributes");
        attrTable.getDatasource().refresh();
    }

    @Override
     public void setItem(Entity item) {
        super.setItem(item);

        initOperationsLayout((ManagedBeanInfo) getItem());
    }

    private void initOperationsLayout(ManagedBeanInfo mbean) {
        BoxLayout container = getComponent("operations");
        for (final ManagedBeanOperation op: mbean.getOperations()) {
            BoxLayout vl = new WebVBoxLayout();
            vl.setMargin(false, false, true, false);
            vl.setSpacing(true);
            vl.setStyleName("operationContainer");

            Label nameLbl = new WebLabel();
            nameLbl.setValue(op.getReturnType() + " " + op.getName() + "()");
            nameLbl.setStyleName("h2");

            Label descrLbl = new WebLabel();
            descrLbl.setValue(op.getDescription());

            vl.add(nameLbl);
            vl.add(descrLbl);

            final List<AttributeEditor> attrProviders = new ArrayList<AttributeEditor>();

            if (!op.getParameters().isEmpty()) {
                GridLayout grid = new WebGridLayout();
                grid.setSpacing(true);
                grid.setColumns(4);
                grid.setRows(op.getParameters().size());
                int row = 0;
                for (ManagedBeanOperationParameter param: op.getParameters()) {
                    Label pnameLbl = new WebLabel();
                    pnameLbl.setValue(param.getName());

                    Label ptypeLbl = new WebLabel();
                    ptypeLbl.setValue(param.getType());

                    AttributeEditor prov = new AttributeEditor(this, param.getType());
                    attrProviders.add(prov);
                    Component editField = prov.getComponent();

                    Label pdescrLbl = new WebLabel();
                    pdescrLbl.setValue(param.getDescription());

                    grid.add(pnameLbl, 0, row);
                    grid.add(ptypeLbl, 1, row);
                    grid.add(editField, 2, row);
                    grid.add(pdescrLbl, 3, row);
                    row++;
                }
                vl.add(grid);
            }

            Button invokeBtn = new WebButton();
            invokeBtn.setAction(new AbstractAction("invoke") {
                private static final long serialVersionUID = 2053416651466641516L;

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
            Label lbl = new WebLabel();
            lbl.setValue(getMessage("mbean.operations.none"));
            container.add(lbl);
        }
    }                                                                                                   

    private void invokeOperation(ManagedBeanOperation op, List<AttributeEditor> attrProviders) {
        JmxControlService jcs = ServiceLocator.lookup(JmxControlService.NAME);
        Map<String, Object> params = new HashMap<String, Object>();
        Object[] paramValues = new Object[attrProviders.size()];
        try {
            for (int i = 0; i < attrProviders.size(); i++) {
                paramValues[i] = attrProviders.get(i).getAttributeValue();
            }
        }
        catch (Exception e) {
            showNotification(getMessage("invokeOperation.conversionError"), IFrame.NotificationType.HUMANIZED);
            return;
        }

        try {
            Object res = jcs.invokeOperation(op, paramValues);
            if (res != null) {
                params.put("result", res);
            }
        }
        catch (Exception e) {
            params.put("exception", e);
        }
        Window w = openWindow("jmxcontrol$OperationResult", WindowManager.OpenType.DIALOG, params);
        w.addListener(new CloseListener() {
            private static final long serialVersionUID = 7535255471687674384L;

            public void windowClosed(String actionId) {
                reloadAttributes();
            }
        });
    }
}
