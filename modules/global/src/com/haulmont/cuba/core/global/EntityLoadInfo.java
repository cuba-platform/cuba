/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.01.2010 15:01:53
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

public class EntityLoadInfo {

    private MetaClass metaClass;
    private UUID id;
    private String viewName;

    public EntityLoadInfo(UUID id, MetaClass metaClass, String viewName) {
        this.id = id;
        this.metaClass = metaClass;
        this.viewName = viewName;
    }

    public UUID getId() {
        return id;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public String getViewName() {
        return viewName;
    }

    public static EntityLoadInfo parse(String str) {
        int dashCount = StringUtils.countMatches(str, "-");
        if (dashCount < 5) {
            return null;
        }

        int idDashPos = str.indexOf('-');
        String entityName = str.substring(0, idDashPos);
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityName);
        if (metaClass == null) {
            return null;
        }

        int viewDashPos;
        String viewName;
        if (dashCount == 6) {
            viewDashPos = str.lastIndexOf("-");
            viewName = str.substring(viewDashPos + 1);
        } else {
            viewDashPos = str.length();
            viewName = null;
        }

        String entityIdStr = str.substring(idDashPos + 1, viewDashPos);
        UUID id;
        try {
            id = UUID.fromString(entityIdStr);
        } catch (Exception e) {
            return null;
        }

        return new EntityLoadInfo(id, metaClass, viewName);
    }

    @Override
    public String toString() {
        return metaClass.getName() + "-" + id + (viewName == null ? "" : "-" + viewName);
    }
}
