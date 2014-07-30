/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.addcondition;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.security.entity.EntityAttrAccess;

/**
 * @author artamonov
 * @version $Id$
 */
public class ModelPropertiesFilter {

    private final MessageTools messageTools;
    private final MetadataTools metadataTools;
    private final Security security;

    public ModelPropertiesFilter() {
        security = AppBeans.get(Security.NAME);
        messageTools = AppBeans.get(MessageTools.class);
        metadataTools = AppBeans.get(MetadataTools.class);
    }

    public boolean isPropertyFilterAllowed(MetaClass metaClass, MetaProperty property) {
        return security.isEntityAttrPermitted(metaClass, property.getName(), EntityAttrAccess.VIEW)
                && !metadataTools.isSystemLevel(property)           // exclude system level attributes
                && metadataTools.isPersistent(property)             // exclude transient properties
                && messageTools.hasPropertyCaption(property)        // exclude not localized properties (they are usually not for end user)
                && !property.getRange().getCardinality().isMany();  // exclude ToMany
    }
}