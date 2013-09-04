/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.ScriptingProvider;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(ResourceService.NAME)
public class ResourceServiceBean implements ResourceService {

    @Inject
    protected Resources resources;

    @Override
    public String getResourceAsString(String name) {
        return resources.getResourceAsString(name);
    }
}
