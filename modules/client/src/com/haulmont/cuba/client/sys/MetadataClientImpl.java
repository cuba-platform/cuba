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
    protected Session initMetadata() {
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

    @Override
    protected ViewRepository initViews() {
        log.info("Initializing views");

        ViewRepository vr = new ViewRepository();

        List<View> views = deployerService.getViews();
        for (View view : views) {
            MetaClass metaClass = getSession().getClass(view.getEntityClass());
            vr.storeView(metaClass, view);
        }

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                vr.deployViews(fileName);
            }
        }

        return vr;
    }

    @Override
    protected Map<Class, Class> initReplacedEntities() {
        Map<Class, Class> map = new HashMap<Class, Class>();

        Map<String, String> names = deployerService.getReplacedEntities();
        for (Map.Entry<String, String> entry : names.entrySet()) {
            Class from = ReflectionHelper.getClass(entry.getKey());
            Class to = ReflectionHelper.getClass(entry.getValue());
            map.put(from, to);
        }

        return map;
    }
}
