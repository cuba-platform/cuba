/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.core.restore;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.app.core.restore.EntityRestore;
import com.haulmont.cuba.web.WebConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntityRestoreCompanion implements EntityRestore.Companion {

    @Override
    public List<String> getClientSpecificRestoreEntities() {
        String restoreEntitiesProp = AppBeans.get(Configuration.class).getConfig(WebConfig.class).getRestoreEntityId();
        if (StringUtils.isNotBlank(restoreEntitiesProp)) {
            return Arrays.asList(StringUtils.split(restoreEntitiesProp, ','));
        }

        return Collections.emptyList();
    }
}