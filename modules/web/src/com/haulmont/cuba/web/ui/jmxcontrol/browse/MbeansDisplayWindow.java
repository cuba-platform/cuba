/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 17.08.2010 11:23:40
 * $Id$
 */

package com.haulmont.cuba.web.ui.jmxcontrol.browse;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.jmxcontrol.entity.ManagedBeanInfo;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MbeansDisplayWindow extends AbstractWindow {
    private static final long serialVersionUID = 360466485361065470L;

    @Inject
    private TextField objectNameField;

    @Inject
    private CollectionDatasource<ManagedBeanInfo, UUID> mbeanDs;

    @Resource(name = "mbeans")
    private TreeTable mbeansTable;


    public MbeansDisplayWindow(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        objectNameField.addListener(new ObjectNameFieldListener());

        mbeansTable.addAction(new RefreshAction(mbeansTable));

        Action inspectAction = new AbstractAction("inspect") {
            private static final long serialVersionUID = 3804486579147680485L;

            public void actionPerform(Component component) {
                Set selected = mbeansTable.getSelected();
                if (!selected.isEmpty()) {
                    ManagedBeanInfo mbi = (ManagedBeanInfo) selected.iterator().next();
                    if (mbi.getObjectName() != null) { // otherwise it's a fake root node
                        openEditor("jmxcontrol$InspectMbean", mbi, WindowManager.OpenType.THIS_TAB);
                    } else { // expand / collapse fake root node
                        TreeTable treeTable = mbeansTable;
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

        mbeansTable.getDatasource().refresh();
    }

    private class ObjectNameFieldListener implements ValueListener {
        @Override
        public void valueChanged(Object source, String property, Object prevValue, Object value) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("objectName", value);
            mbeanDs.refresh(params);
            if (StringUtils.isNotEmpty((String) value)) {
                mbeansTable.expandAll();
            }
        }
    }
}
