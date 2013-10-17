/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.history;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author gorbunkov
 */
public class EntityOpenHistoryFrame extends AbstractFrame {

    CollectionDatasourceImpl openHistoryDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
//        String entityDsName = (String) params.get("entityDs");
//        if (StringUtils.isBlank(entityDsName)) {
//            throw new IllegalArgumentException("entityDs attribute is not set");
//        }
//        if (openHistoryDs == null) {
//            throw new IllegalArgumentException("datasource with name " + entityDsName + " not found in DsContext");
//        }
        Entity item = WindowParams.ITEM.getEntity(params);
        if (!PersistenceHelper.isNew(item)) {
            openHistoryDs = getDsContext().get("openHistoryDs");
            openHistoryDs.setRefreshMode(CollectionDatasource.RefreshMode.ALWAYS);
            openHistoryDs.refresh(Collections.<String, Object>singletonMap("entityId", item.getId()));
        }
    }
}
