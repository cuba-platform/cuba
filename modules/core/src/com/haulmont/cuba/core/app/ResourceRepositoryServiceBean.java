/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:45:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * DEPRECATED!<br/>
 * Use {@link com.haulmont.cuba.core.global.ScriptingProvider} to load resources and cache them only when necessary.
 */
@Service(ResourceRepositoryService.NAME)
@Deprecated
public class ResourceRepositoryServiceBean implements ResourceRepositoryService
{
    @Inject
    private ResourceRepositoryAPI repository;

    public boolean resourceExists(String name) {
        return repository.resourceExists(name);
    }

    public String getResAsString(String name) {
        return repository.getResAsString(name);
    }
}
