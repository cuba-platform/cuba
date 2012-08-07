/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewNotFoundException;
import com.haulmont.cuba.core.sys.MetadataBuildHelper;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                MetadataBuildHelper.getPersistentEntitiesPackages(),
                MetadataBuildHelper.getTransientEntitiesPackages(),
                getEntityAnnotations(),
                getReplacedEntities()
        );
    }

    private Map<String, Map<String, Object>> getEntityAnnotations() {
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        for (MetaClass metaClass : metadata.getSession().getClasses()) {
            if (!metaClass.getAnnotations().isEmpty()) {
                Map<String, Object> annotations = new HashMap<String, Object>();
                for (Map.Entry<String, Object> entry : metaClass.getAnnotations().entrySet()) {
                    // send to the client only annotations with String or Boolean value,
                    // others are not safe for serialization
                    if (entry.getValue() instanceof String || entry.getValue() instanceof Boolean) {
                        annotations.put(entry.getKey(), entry.getValue());
                    }
                }
                result.put(metaClass.getJavaClass().getName(), annotations);
            }
        }
        return result;
    }

    private Map<String, String> getReplacedEntities() {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<Class, Class> entry : metadata.getReplacedEntities().entrySet()) {
            result.put(entry.getKey().getName(), entry.getValue().getName());
        }
        return result;
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
