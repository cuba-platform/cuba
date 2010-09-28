/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.09.2010 17:07:29
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.App;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowInfoAction extends AbstractAction {

    public static final String ACTION_ID = "showSystemInfo";
    public static final String ACTION_PERMISSION = "cuba.gui.showInfo";

    private CollectionDatasource ds;
    protected String mp;

    public ShowInfoAction() {
        super(ACTION_ID);
        mp = AppConfig.getInstance().getMessagesPack();
    }

    public CollectionDatasource getDatasource() {
        return ds;
    }

    public void setDatasource(CollectionDatasource ds) {
        this.ds = ds;
    }

    @Override
    public String getCaption() {
        return MessageProvider.getMessage(mp, "table.showInfoAction");
    }

    public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
        if (ds == null)
            return;

        App.getInstance().getWindowManager().showMessageDialog(
                MessageProvider.getMessage(mp, "table.showInfoAction"),
                compileInfo(ds),
                IFrame.MessageType.CONFIRMATION
        );
    }

    private String compileInfo(CollectionDatasource ds) {
        StringBuilder sb = new StringBuilder();

        MetaClass metaClass = ds.getMetaClass();
        sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.entityName", metaClass.getName())).append("<br/>");
        sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.entityClass", metaClass.getJavaClass().getName())).append("<br/>");

        javax.persistence.Table annotation = (javax.persistence.Table) metaClass.getJavaClass().getAnnotation(javax.persistence.Table.class);
        if (annotation != null)
            sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.entityTable", annotation.name())).append("<br/>");

        Entity instance = ds.getItem();
        if (instance != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            sb.append("<hr/>");
            sb.append(MessageProvider.getMessage(mp, "table.showInfoAction.row")).append("<br/>");
            sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.id", instance.getId())).append("<br/>");
            if (instance instanceof Versioned && ((Versioned) instance).getVersion() != null) {
                sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.version", ((Versioned) instance).getVersion())).append("<br/>");
            }
            if (instance instanceof BaseEntity) {
                if (((BaseEntity) instance).getCreateTs() != null)
                    sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.createTs", df.format(((BaseEntity) instance).getCreateTs()))).append("<br/>");
                if (((BaseEntity) instance).getCreatedBy() != null)
                    sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.createdBy", ((BaseEntity) instance).getCreatedBy())).append("<br/>");
            }
            if (instance instanceof Updatable) {
                Date updateTs = ((Updatable) instance).getUpdateTs();
                if (updateTs != null)
                    sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.updateTs", df.format(updateTs))).append("<br/>");
                String updatedBy = ((Updatable) instance).getUpdatedBy();
                if (updatedBy != null)
                    sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.updatedBy", updatedBy)).append("<br/>");
            }
            if (instance instanceof SoftDelete) {
                Date deleteTs = ((SoftDelete) instance).getDeleteTs();
                if (deleteTs != null)
                    sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.deleteTs", df.format(deleteTs))).append("<br/>");
                String deletedBy = ((SoftDelete) instance).getDeletedBy();
                if (deletedBy != null)
                    sb.append(MessageProvider.formatMessage(mp, "table.showInfoAction.deletedBy", deletedBy)).append("<br/>");
            }
        }

        return sb.toString();
    }
}
