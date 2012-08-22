/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.AbstractMetadata;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Metadata.NAME)
public class MetadataClientImpl extends AbstractMetadata {

    @Inject
    private ServerInfoService serverInfoService;

    @Inject
    private Configuration configuration;

    protected MetadataBuildInfo getMetadataBuildInfo() {
        return serverInfoService.getMetadataBuildInfo();
    }

    @Override
    protected void initViews() {
        log.info("Initializing views");

        boolean lazyLoadServerViews = configuration.getConfig(ClientConfig.class).getLazyLoadServerViews();

        ViewRepositoryClient viewRepository = createViewRepository(lazyLoadServerViews);

        if (!lazyLoadServerViews) {
            List<View> views = serverInfoService.getViews();
            for (View view : views) {
                MetaClass metaClass = getSession().getClass(view.getEntityClass());
                viewRepository.storeView(metaClass, view);
            }
        }

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                viewRepository.deployViews(fileName);
            }
        }

        this.viewRepository = viewRepository;
    }

    protected ViewRepositoryClient createViewRepository(boolean lazyLoadServerViews) {
        return new ViewRepositoryClient(this, lazyLoadServerViews, serverInfoService);
    }
}
