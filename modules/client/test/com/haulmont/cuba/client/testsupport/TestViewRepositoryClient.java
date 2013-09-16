/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.core.sys.ResourcesImpl;
import org.apache.commons.lang.StringUtils;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TestViewRepositoryClient extends AbstractViewRepository {

    private String viewsConfig;

    public TestViewRepositoryClient(String viewsConfig) {
        this.viewsConfig = viewsConfig;
        this.resources = new ResourcesImpl(getClass().getClassLoader());
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    protected void init() {
        if (!StringUtils.isEmpty(viewsConfig))
            deployViews(viewsConfig);
    }
}
