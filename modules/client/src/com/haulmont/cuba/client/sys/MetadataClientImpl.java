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
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataBuildInfo;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.sys.AbstractMetadata;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.PersistentClassesMetadataLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Metadata.NAME)
public class MetadataClientImpl extends AbstractMetadata {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    private CubaDeployerService deployerService;

    @Override
    protected void initMetadata() {
        log.info("Initializing metadata");

        MetadataBuildInfo metadataBuildInfo = deployerService.getMetadataBuildInfo();

        Collection<String> packages;

        MetadataLoader metadataLoader = new PersistentClassesMetadataLoader();
        packages = metadataBuildInfo.getPersistentEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        session = metadataLoader.getSession();

        metadataLoader = new ChileMetadataLoader(session);
        packages = metadataBuildInfo.getTransientEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        for (Map.Entry<String, Map<String, Object>> classEntry : metadataBuildInfo.getEntityAnnotations().entrySet()) {
            MetaClass metaClass = session.getClass(ReflectionHelper.getClass(classEntry.getKey()));
            for (Map.Entry<String, Object> entry : classEntry.getValue().entrySet()) {
                metaClass.getAnnotations().put(entry.getKey(), entry.getValue());
            }
        }

        replacedEntities = new HashMap<Class, Class>();
        for (Map.Entry<String, String> entry : metadataBuildInfo.getReplacedEntities().entrySet()) {
            Class from = ReflectionHelper.getClass(entry.getKey());
            Class to = ReflectionHelper.getClass(entry.getValue());
            replacedEntities.put(from, to);
        }
    }

    @Override
    protected void initViews() {
        log.info("Initializing views");

        viewRepository = new ViewRepository();

        List<View> views = deployerService.getViews();
        for (View view : views) {
            MetaClass metaClass = getSession().getClass(view.getEntityClass());
            viewRepository.storeView(metaClass, view);
        }

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                viewRepository.deployViews(fileName);
            }
        }
    }
}
