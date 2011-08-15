/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.ScriptingProvider;
import org.springframework.stereotype.Service;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Service(ResourceService.NAME)
public class ResourceServiceBean implements ResourceService {

    @Override
    public String getResourceAsString(String name) {
        return ScriptingProvider.getResourceAsString(name);
    }
}
