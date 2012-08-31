/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AbstractMetadata;
import com.haulmont.cuba.core.sys.PersistentEntitiesMetadataLoader;
import com.haulmont.cuba.core.sys.ResourcesImpl;
import com.haulmont.cuba.core.sys.TransientEntitiesMetadataLoader;
import org.apache.commons.lang.StringUtils;

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

        extendedEntities = new ExtendedEntities(this);
        tools = new MetadataTools(this, null, null);
    }

    @Override
    protected void initMetadata() {
        MetadataLoader persistentEntitiesMetadataLoader = new PersistentEntitiesMetadataLoader();
        for (String p : packages) {
            persistentEntitiesMetadataLoader.loadPackage(p, p);
        }
        persistentEntitiesMetadataLoader.postProcess();

        Session session = persistentEntitiesMetadataLoader.getSession();

        TransientEntitiesMetadataLoader transientEntitiesMetadataLoader = new TransientEntitiesMetadataLoader();
        transientEntitiesMetadataLoader.setSession(session);
        for (String p : packages) {
            transientEntitiesMetadataLoader.loadPackage(p, p);
        }
        transientEntitiesMetadataLoader.postProcess();

        this.session = session;
    }

    @Override
    protected MetadataBuildInfo getMetadataBuildInfo() {
        return null;
    }

    @Override
    protected void initViews() {
        ViewRepository viewRepository = new ViewRepository(this, new ResourcesImpl(getClass().getClassLoader()));
        if (!StringUtils.isEmpty(viewsConfig))
            viewRepository.deployViews(viewsConfig);
        this.viewRepository = viewRepository;
    }
}
