/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.impl.SessionImpl;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Metadata.NAME)
public class MetadataImpl extends AbstractMetadata {

    @Inject
    private MetadataBuildSupport metadataBuildSupport;

    @Inject
    private Resources resources;

    protected MetadataBuildInfo getMetadataBuildInfo() {
        return new MetadataBuildInfo(
                metadataBuildSupport.getPersistentEntitiesPackages(),
                metadataBuildSupport.getTransientEntitiesPackages(),
                metadataBuildSupport.getEntityAnnotations()
        );
    }

    @Override
    protected void initMetadata() {
        super.initMetadata();
        SessionImpl.setSerializationSupportSession(this.session);
    }

    @Override
    protected void initViews() {
        log.info("Initializing views");
        ViewRepository viewRepository = createViewRepository();

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                viewRepository.deployViews(fileName);
            }
        }

        this.viewRepository = viewRepository;
    }

    protected ViewRepository createViewRepository() {
        return new ViewRepository(this, resources);
    }
}
