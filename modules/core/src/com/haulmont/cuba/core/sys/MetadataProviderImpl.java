/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 12:06:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetadataProviderImpl extends MetadataProvider
{
    private volatile Session session;

    private volatile ViewRepository viewRepository;

    private volatile Map<Class, Class> replacedEntities;

    private static Log log = LogFactory.getLog(MetadataProviderImpl.class);

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

        Collection<String> packages;

        MetadataLoader metadataLoader = new PersistentClassesMetadataLoader();
        packages = MetadataBuildHelper.getPersistentEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        Session metadataSession = metadataLoader.getSession();

        metadataLoader = new ChileMetadataLoader(metadataSession);
        packages = MetadataBuildHelper.getTransientEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        return metadataSession;
    }

    private ViewRepository initViews() {
        log.info("Initializing views");
        ViewRepository vr = new ViewRepository();

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                vr.deployViews(fileName);
            }
        }

        return vr;
    }

    private Map<Class, Class> initReplacedEntities() {
        Map<Class, Class> map = new HashMap<Class, Class>();
        String config = MetadataProvider.getMetadataConfig();
        StrTokenizer tokenizer = new StrTokenizer(config);
        for (String fileName : tokenizer.getTokenArray()) {
            loadReplacedEntities(map, fileName);
        }
        return map;
    }

    private void loadReplacedEntities(Map<Class, Class> map, String path) {
        Element root = MetadataBuildHelper.readXml(path);

        for (Element element : Dom4j.elements(root, "include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                loadReplacedEntities(map, fileName);
            }
        }

        Element element = root.element("entityFactory");
        if (element != null) {
            for (Element replaceElem : Dom4j.elements(element)) {
                String className = replaceElem.attributeValue("class");
                String withClassName = replaceElem.attributeValue("with");
                map.put(ReflectionHelper.getClass(className), ReflectionHelper.getClass(withClassName));
            }
        }
    }

    private void loadMetadata(MetadataLoader loader, Collection<String> packages) {
        for (String p : packages) {
            loader.loadPackage(p, p);
        }
    }
}
