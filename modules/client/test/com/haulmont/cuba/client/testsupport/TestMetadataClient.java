/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class TestMetadataClient extends AbstractMetadata {

    private List<String> packages;

    public TestMetadataClient(List<String> packages, TestViewRepositoryClient viewRepository) {
        this.packages = packages;

        this.viewRepository = viewRepository;
        viewRepository.setMetadata(this);

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
}
