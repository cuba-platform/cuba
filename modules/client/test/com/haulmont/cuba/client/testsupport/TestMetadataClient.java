/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AbstractMetadata;
import com.haulmont.cuba.core.sys.PersistentClassesMetadataLoader;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class TestMetadataClient extends AbstractMetadata {

    private List<String> packages;

    private String viewsConfig;

    public TestMetadataClient(List<String> packages, String viewsConfig) {
        this.packages = packages;
        this.viewsConfig = viewsConfig;
    }

    @Override
    protected void initMetadata() {
        MetadataLoader metadataLoader = new PersistentClassesMetadataLoader();
        for (String p : packages) {
            metadataLoader.loadPackage(p, p);
        }
        metadataLoader.postProcess();

        Session session = metadataLoader.getSession();

        metadataLoader = new ChileMetadataLoader(session);
        for (String p : packages) {
            metadataLoader.loadPackage(p, p);
        }
        metadataLoader.postProcess();

        this.session = session;
        this.replacedEntities = new HashMap<Class, Class>();
    }

    @Override
    protected void initViews() {
        ViewRepository viewRepository = new ViewRepository();
        if (!StringUtils.isEmpty(viewsConfig))
            viewRepository.deployViews(viewsConfig);
        this.viewRepository = viewRepository;
    }
}
