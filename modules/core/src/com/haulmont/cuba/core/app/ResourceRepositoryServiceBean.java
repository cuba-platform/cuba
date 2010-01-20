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
 * Service facade for {@link com.haulmont.cuba.core.app.ResourceRepository} MBean
 */
@Service(ResourceRepositoryService.NAME)
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
