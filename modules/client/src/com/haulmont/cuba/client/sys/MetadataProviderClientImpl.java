/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.client.sys;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.app.CubaDeployerService;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.PersistentClassesMetadataLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MetadataProviderClientImpl extends MetadataProvider {

    private Log log = LogFactory.getLog(MetadataProviderClientImpl.class);

    private volatile Session session;

    private volatile ViewRepository viewRepository;

    private volatile Map<Class, Class> replacedEntities;

    private CubaDeployerService deployerService;

    public void setDeployerService(CubaDeployerService deployerService) {
        this.deployerService = deployerService;
    }

    @Override
    protected Session __getSession() {
        if (session == null) {
            synchronized (this) {
                if (session == null) {
                    session = initMetadata();
                }
            }
        }
        return session;
    }

    @Override
    protected ViewRepository __getViewRepository() {
        if (viewRepository == null) {
            synchronized (this) {
                if (viewRepository == null) {
                    viewRepository = initViews();
                }
            }
        }
        return viewRepository;
    }

    @Override
    protected Map<Class, Class> __getReplacedEntities() {
        if (replacedEntities == null) {
            synchronized (this) {
                if (replacedEntities == null) {
                    replacedEntities = initReplacedEntities();
                }
            }
        }
        return replacedEntities;
    }

    private Session initMetadata() {
        log.info("Initializing metadata");

        MetadataBuildInfo metadataBuildInfo = deployerService.getMetadataBuildInfo();

        Collection<String> packages;

        MetadataLoader metadataLoader = new PersistentClassesMetadataLoader();
        packages = metadataBuildInfo.getPersistentEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        Session metadataSession = metadataLoader.getSession();

        metadataLoader = new ChileMetadataLoader(metadataSession);
        packages = metadataBuildInfo.getTransientEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        return metadataSession;
    }

    private ViewRepository initViews() {
        log.info("Initializing views");

        ViewRepository vr = new ViewRepository();

        List<View> views = deployerService.getViews();
        for (View view : views) {
            MetaClass metaClass = __getSession().getClass(view.getEntityClass());
            vr.storeView(metaClass, view);
        }

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName))
            vr.deployViews(configName);

        return vr;
    }

    private Map<Class, Class> initReplacedEntities() {
        Map<Class, Class> map = new HashMap<Class, Class>();

        Map<String, String> names = deployerService.getReplacedEntities();
        for (Map.Entry<String, String> entry : names.entrySet()) {
            Class from = ReflectionHelper.getClass(entry.getKey());
            Class to = ReflectionHelper.getClass(entry.getValue());
            map.put(from, to);
        }

        return map;
    }

    private void loadMetadata(MetadataLoader loader, Collection<String> packages) {
        for (String p : packages) {
            loader.loadPackage(p, p);
        }
    }
}
