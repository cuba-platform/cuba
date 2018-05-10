/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components.table;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class LinkCellClickListener implements Table.CellClickListener {

    protected Table table;
    protected ApplicationContext applicationContext;

    public LinkCellClickListener(Table table, ApplicationContext applicationContext) {
        this.table = table;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onClick(final Entity rowItem, final String columnId) {
        Table.Column column = table.getColumn(columnId);
        if (column.getXmlDescriptor() != null) {
            String invokeMethodName = column.getXmlDescriptor().attributeValue("linkInvoke");
            if (StringUtils.isNotEmpty(invokeMethodName)) {
                callControllerInvoke(rowItem, columnId, invokeMethodName);

                return;
            }
        }

        Entity entity;
        Object value = rowItem.getValueEx(columnId);

        if (value instanceof Entity) {
            entity = (Entity) value;
        } else {
            entity = rowItem;
        }

        WindowManager wm;
        Window window = ComponentsHelper.getWindow(table);
        if (window == null) {
            throw new IllegalStateException("Please specify Frame for Table");
        } else {
            wm = window.getWindowManager();
        }

        Messages messages = applicationContext.getBean(Messages.NAME, Messages.class);

        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
            wm.showNotification(messages.getMainMessage("OpenAction.objectIsDeleted"),
                    Frame.NotificationType.HUMANIZED);
            return;
        }

        DataSupplier dataSupplier = window.getDsContext().getDataSupplier();
        entity = dataSupplier.reload(entity, View.MINIMAL);

        WindowConfig windowConfig = applicationContext.getBean(WindowConfig.NAME, WindowConfig.class);

        String windowAlias = null;
        if (column.getXmlDescriptor() != null) {
            windowAlias = column.getXmlDescriptor().attributeValue("linkScreen");
        }
        if (StringUtils.isEmpty(windowAlias)) {
            windowAlias = windowConfig.getEditorScreenId(entity.getMetaClass());
        }

        WindowManager.OpenType screenOpenType = WindowManager.OpenType.THIS_TAB;
        if (column.getXmlDescriptor() != null) {
            String openTypeAttribute = column.getXmlDescriptor().attributeValue("linkScreenOpenType");
            if (StringUtils.isNotEmpty(openTypeAttribute)) {
                screenOpenType = WindowManager.OpenType.valueOf(openTypeAttribute);
            }
        }

        Window.Editor editor = wm.openEditor(
                windowConfig.getWindowInfo(windowAlias),
                entity,
                screenOpenType
        );
        editor.addCloseListener(actionId -> {
            // move focus to component
            table.requestFocus();

            if (Window.COMMIT_ACTION_ID.equals(actionId)) {
                Entity editorItem = editor.getItem();

                handleEditorCommit(editorItem, rowItem, columnId);
            }
        });
    }

    protected void handleEditorCommit(Entity editorItem, Entity rowItem, String columnId) {
        MetaPropertyPath mpp = rowItem.getMetaClass().getPropertyPath(columnId);
        if (mpp == null) {
            throw new IllegalStateException(String.format("Unable to find metaproperty %s for class %s",
                    columnId, rowItem.getMetaClass()));
        }

        if (mpp.getRange().isClass()) {
            DatasourceImplementation ds = ((DatasourceImplementation) table.getDatasource());
            boolean modifiedInTable = ds.getItemsToUpdate().contains(rowItem);
            boolean ownerDsModified = ds.isModified();

            rowItem.setValueEx(columnId, null);
            rowItem.setValueEx(columnId, editorItem);

            // restore modified for owner datasource
            // remove from items to update if it was not modified before setValue
            if (!modifiedInTable) {
                ds.getItemsToUpdate().remove(rowItem);
            }
            ds.setModified(ownerDsModified);
        } else {
            //noinspection unchecked
            table.getDatasource().updateItem(editorItem);
        }
    }

    protected void callControllerInvoke(Entity rowItem, String columnId, String invokeMethodName) {
        Object controller = ComponentsHelper.getFrameController(table.getFrame());
        Method method;
        method = findLinkInvokeMethod(controller.getClass(), invokeMethodName);
        if (method != null) {
            try {
                method.invoke(controller, rowItem, columnId);
            } catch (Exception e) {
                throw new RuntimeException("Unable to cal linkInvoke method for table column", e);
            }
        } else {
            try {
                method = controller.getClass().getMethod(invokeMethodName);
                try {
                    method.invoke(controller);
                } catch (Exception e1) {
                    throw new RuntimeException("Unable to call linkInvoke method for table column", e1);
                }
            } catch (NoSuchMethodException e1) {
                throw new IllegalStateException(String.format("No suitable methods named %s for invoke", invokeMethodName));
            }
        }
    }

    protected Method findLinkInvokeMethod(Class cls, String methodName) {
        Method exactMethod = MethodUtils.getAccessibleMethod(cls, methodName, new Class[]{Entity.class, String.class});
        if (exactMethod != null) {
            return exactMethod;
        }

        // search through all methods
        Method[] methods = cls.getMethods();
        for (Method availableMethod : methods) {
            if (availableMethod.getName().equals(methodName)) {
                if (availableMethod.getParameterCount() == 2
                        && Void.TYPE.equals(availableMethod.getReturnType())) {
                    if (Entity.class.isAssignableFrom(availableMethod.getParameterTypes()[0]) &&
                            String.class == availableMethod.getParameterTypes()[1]) {
                        // get accessible version of method
                        return MethodUtils.getAccessibleMethod(availableMethod);
                    }
                }
            }
        }
        return null;
    }
}