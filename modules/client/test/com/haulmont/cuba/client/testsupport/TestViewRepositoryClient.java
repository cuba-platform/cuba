/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
