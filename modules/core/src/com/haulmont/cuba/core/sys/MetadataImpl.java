/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Metadata.NAME)
public class MetadataImpl extends AbstractMetadata {

    @Inject
    private MetadataBuildSupport metadataBuildSupport;

    protected MetadataBuildInfo getMetadataBuildInfo() {
        return new MetadataBuildInfo(
                metadataBuildSupport.getPersistentEntitiesPackages(),
                metadataBuildSupport.getTransientEntitiesPackages(),
                metadataBuildSupport.getEntityAnnotations()
        );
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
        return new ViewRepository(this);
    }
}
