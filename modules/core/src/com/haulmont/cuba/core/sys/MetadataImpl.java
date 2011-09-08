/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.loader.ChileMetadataLoader;
import com.haulmont.chile.core.loader.MetadataLoader;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import javax.annotation.ManagedBean;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Metadata.NAME)
public class MetadataImpl extends AbstractMetadata {

    private Log log = LogFactory.getLog(getClass());

    @Override
    protected Session initMetadata() {
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

    @Override
    protected ViewRepository initViews() {
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

    @Override
    protected Map<Class, Class> initReplacedEntities() {
        Map<Class, Class> map = new HashMap<Class, Class>();
        String config = MetadataBuildHelper.getMetadataConfig();
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
}
