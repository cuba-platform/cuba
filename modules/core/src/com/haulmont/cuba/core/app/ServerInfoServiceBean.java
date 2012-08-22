/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewNotFoundException;
import com.haulmont.cuba.core.sys.MetadataBuildSupport;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;
import java.util.TimeZone;

/**
 * Standard implementation of {@link ServerInfoService} interface.
 *
 * <p>Annotated with <code>@ManagedBean</code> instead of <code>@Service</code> to be available before user login.</p>
 */
@ManagedBean(ServerInfoService.NAME)
public class ServerInfoServiceBean implements ServerInfoService {

    @Inject
    protected Metadata metadata;

    @Inject
    protected ServerInfoAPI serverInfo;

    @Inject
    private MetadataBuildSupport metadataBuildSupport;

    @Override
    public String getReleaseNumber() {
        return serverInfo.getReleaseNumber();
    }

    @Override
    public String getReleaseTimestamp() {
        return serverInfo.getReleaseTimestamp();
    }

    @Override
    public MetadataBuildInfo getMetadataBuildInfo() {
        return new MetadataBuildInfo(
                metadataBuildSupport.getPersistentEntitiesPackages(),
                metadataBuildSupport.getTransientEntitiesPackages(),
                metadataBuildSupport.getEntityAnnotations()
        );
    }

    @Override
    public List<View> getViews() {
        return metadata.getViewRepository().getAll();
    }

    @Override
    public View getView(Class<? extends Entity> entityClass, String name) {
        try {
            return metadata.getViewRepository().getView(entityClass, name);
        } catch (ViewNotFoundException e) {
            return null;
        }
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
}
