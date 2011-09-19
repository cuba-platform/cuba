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
import java.util.Map;

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
    protected Session initMetadata() {
        MetadataLoader metadataLoader = new PersistentClassesMetadataLoader();
        for (String p : packages) {
            metadataLoader.loadPackage(p, p);
        }
        metadataLoader.postProcess();

        Session metadataSession = metadataLoader.getSession();

        metadataLoader = new ChileMetadataLoader(metadataSession);
        for (String p : packages) {
            metadataLoader.loadPackage(p, p);
        }
        metadataLoader.postProcess();

        return metadataSession;
    }

    @Override
    protected ViewRepository initViews() {
        ViewRepository vr = new ViewRepository();
        if (!StringUtils.isEmpty(viewsConfig))
            vr.deployViews(viewsConfig);
        return vr;
    }

    @Override
    protected Map<Class, Class> initReplacedEntities() {
        return new HashMap<Class, Class>();
    }
}
