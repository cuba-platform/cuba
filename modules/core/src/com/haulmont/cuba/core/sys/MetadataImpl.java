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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.entity.annotation.EnableRestore;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
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
    protected void initMetadata() {
        log.info("Initializing metadata");

        Collection<String> packages;

        MetadataLoader metadataLoader = new PersistentClassesMetadataLoader();
        packages = MetadataBuildHelper.getPersistentEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        Session session = metadataLoader.getSession();

        metadataLoader = new ChileMetadataLoader(session);
        packages = MetadataBuildHelper.getTransientEntitiesPackages();
        loadMetadata(metadataLoader, packages);
        metadataLoader.postProcess();

        for (MetaClass metaClass : session.getClasses()) {
            initMetaAnnotations(metaClass);
        }

        Map<Class, Class> replacedEntities = new HashMap<Class, Class>();

        processMetadataXml(session, replacedEntities);

        this.session = session;
        this.replacedEntities = replacedEntities;
    }

    /**
     * Initialize entity annotations from class-level Java annotations.
     * <p>Should be overridden in application projects to handle application-specific annotations.</p>
     *
     * @param metaClass MetaClass instance to assign annotations
     */
    protected void initMetaAnnotations(MetaClass metaClass) {
        Class<?> javaClass = metaClass.getJavaClass();

        SystemLevel systemLevel = javaClass.getAnnotation(SystemLevel.class);
        if (systemLevel != null)
            metaClass.getAnnotations().put(SystemLevel.class.getName(), systemLevel.value());

        EnableRestore enableRestore = javaClass.getAnnotation(EnableRestore.class);
        if (enableRestore != null)
            metaClass.getAnnotations().put(EnableRestore.class.getName(), enableRestore.value());

        TrackEditScreenHistory trackEditScreenHistory = javaClass.getAnnotation(TrackEditScreenHistory.class);
        if (trackEditScreenHistory != null)
            metaClass.getAnnotations().put(TrackEditScreenHistory.class.getName(), trackEditScreenHistory.value());
    }

    protected void processMetadataXml(Session session, Map<Class, Class> replacedEntities) {
        String config = MetadataBuildHelper.getMetadataConfig();
        StrTokenizer tokenizer = new StrTokenizer(config);
        for (String fileName : tokenizer.getTokenArray()) {
            processMetadataXmlFile(session, replacedEntities, fileName);
        }
    }

    protected void processMetadataXmlFile(Session session, Map<Class, Class> replacedEntities, String path) {
        Element root = MetadataBuildHelper.readXml(path);

        for (Element element : Dom4j.elements(root, "include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                processMetadataXmlFile(session, replacedEntities, fileName);
            }
        }

        Element entityFactoryEl = root.element("entityFactory");
        if (entityFactoryEl != null) {
            for (Element replaceElem : Dom4j.elements(entityFactoryEl, "replace")) {
                String className = replaceElem.attributeValue("class");
                String withClassName = replaceElem.attributeValue("with");
                replacedEntities.put(ReflectionHelper.getClass(className), ReflectionHelper.getClass(withClassName));
            }
        }

        Element annotationsEl = root.element("annotations");
        if (annotationsEl != null) {
            for (Element entityEl : Dom4j.elements(annotationsEl, "entity")) {
                String className = entityEl.attributeValue("class");
                MetaClass metaClass = session.getClass(ReflectionHelper.getClass(className));
                if (metaClass != null) {
                    for (Element annotEl : Dom4j.elements(entityEl, "annotation")) {
                        String str = annotEl.attributeValue("value");
                        Object val;
                        if (str != null && (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")))
                            val = Boolean.valueOf(str);
                        else
                            val = str;
                        metaClass.getAnnotations().put(annotEl.attributeValue("name"), val);
                    }
                }
            }
        }
    }

    @Override
    protected void initViews() {
        log.info("Initializing views");
        ViewRepository viewRepository = new ViewRepository();

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                viewRepository.deployViews(fileName);
            }
        }

        this.viewRepository = viewRepository;
    }
}
