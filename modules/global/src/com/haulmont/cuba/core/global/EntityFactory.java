/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.08.2010 18:41:35
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EntityFactory {

    private static volatile EntityFactory instance;

    private Map<Class, Class> map = new HashMap<Class, Class>();

    private static EntityFactory getInstance() {
        if (instance == null) {
            synchronized (EntityFactory.class) {
                if (instance == null) {
                    instance = new EntityFactory();
                }
            }
        }
        return instance;
    }

    private EntityFactory() {
        load(MetadataProvider.getMetadataXmlPath());
    }

    private void load(String metadataXmlPath) {
        if (!metadataXmlPath.startsWith("/"))
            metadataXmlPath = "/" + metadataXmlPath;

        InputStream stream = EntityFactory.class.getResourceAsStream(metadataXmlPath);

        Document document = Dom4j.readDocument(stream);
        Element root = document.getRootElement();

        for (Element element : Dom4j.elements(root, "include")) {
            String fileName = element.attributeValue("file");
            if (!StringUtils.isBlank(fileName)) {
                load(fileName);
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

    private <T> T __create(Class<T> entityClass) {
        Class<T> replace = map.get(entityClass);
        if (replace == null)
            replace = entityClass;
        try {
            T obj = replace.newInstance();
            return obj;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Class<T> __getReplacedClass(Class<T> clazz) {
        Class replacedClass = map.get(clazz);
        return replacedClass == null ? clazz : replacedClass;
    }

    public static <T> T create(Class<T> entityClass) {
        return getInstance().__create(entityClass);
    }

    public static <T> T create(MetaClass metaClass) {
        return (T) getInstance().__create(metaClass.getJavaClass());
    }

    public static <T> T create(String entityName) {
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityName);
        return (T) getInstance().__create(metaClass.getJavaClass());
    }

    public static <T> Class<T> getReplacedClass(Class<T> clazz) {
        return getInstance().__getReplacedClass(clazz);
    }

    public static <T> Class<T> getReplacedClass(MetaClass metaClass) {
        return getInstance().__getReplacedClass(metaClass.getJavaClass());
    }

    public static <T> Class<T> getReplacedClass(String entityName) {
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityName);
        return getInstance().__getReplacedClass(metaClass.getJavaClass());
    }
}
