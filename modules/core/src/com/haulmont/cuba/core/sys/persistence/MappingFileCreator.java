/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.annotation.Extends;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.persistence.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Generates an orm.xml file containing mapping overrides to support extended entities in associations.
 * Works together with {@link PersistenceConfigProcessor}.
 *
 * @author krivopustov
 * @version $Id$
*/
class MappingFileCreator {

    private static final String XMLNS = "http://java.sun.com/xml/ns/persistence/orm";
    private static final String PERSISTENCE_VER = "1.0";

    private Collection<String> classNames;
    private Map<String, String> properties;
    private File dir;

    private Log log = LogFactory.getLog(getClass());

    private static class Attr {

        private enum Type {
            ONE_TO_ONE("one-to-one"),
            ONE_TO_MANY("one-to-many"),
            MANY_TO_ONE("many-to-one"),
            MANY_TO_MANY("many-to-many");

            private String xml;

            private Type(String xml) {
                this.xml = xml;
            }
        }
        private final Type type;
        private final String name;
        private final String targetEntity;

        private Attr(Type type, String name, String targetEntity) {
            this.type = type;
            this.name = name;
            this.targetEntity = targetEntity;
        }
    }

    MappingFileCreator(Collection<String> classNames, Map<String, String> properties, File dir) {
        this.classNames = classNames;
        this.properties = properties;
        this.dir = dir;
    }

    public void create() {
        Map<Class, Class> extendedClasses = new HashMap<>();
        List<Class> persistentClasses = new ArrayList<>();
        for (String className : classNames) {
            Class<Object> aClass = ReflectionHelper.getClass(className);
            persistentClasses.add(aClass);
            Extends annotation = aClass.getAnnotation(Extends.class);
            if (annotation != null) {
                Class originalClass = annotation.value();
                extendedClasses.put(originalClass, aClass);
            }
        }

        if (extendedClasses.isEmpty())
            return;

        Map<String, List<Attr>> mappings = new LinkedHashMap<>();
        for (Class aClass : persistentClasses) {
            List<Attr> attrList = processClass(aClass, extendedClasses);
            if (!attrList.isEmpty())
                mappings.put(aClass.getName(), attrList);
        }

        if (mappings.isEmpty())
            return;
        log.debug("Found " + mappings.size() + " entities containing extended associations");

        Document doc = createDocument(mappings);
        File file = writeDocument(doc);

        String filePath = file.getAbsolutePath().replace("\\", "/");
        String prop = properties.get("openjpa.MetaDataFactory");
        if (prop != null)
            log.warn("Please don't set openjpa.MetaDataFactory in your persistence.xml, it is overridden anyway");
        properties.put("openjpa.MetaDataFactory", "jpa(URLs=file://" + filePath + ")");
    }

    private List<Attr> processClass(Class aClass, Map<Class, Class> extendedClasses) {
        List<Attr> list = new ArrayList<>();

        List<Class> classes = ClassUtils.getAllSuperclasses(aClass);
        classes.add(0, aClass);
        for (Class<?> c : classes) {
            if (c.getAnnotation(Entity.class) != null || c.getAnnotation(MappedSuperclass.class) != null) {
                for (Field field : c.getDeclaredFields()) {
                    Attr.Type type = getAttrType(field);
                    if (type != null) {
                        Class extClass = extendedClasses.get(field.getType());
                        if (extClass != null) {
                            Attr attr = new Attr(type, field.getName(), extClass.getName());
                            list.add(attr);
                        }
                    }
                }
            }
        }

        return list;
    }

    private Attr.Type getAttrType(Field field) {
        if (field.getAnnotation(OneToOne.class) != null)
            return Attr.Type.ONE_TO_ONE;
        else if (field.getAnnotation(OneToMany.class) != null)
            return Attr.Type.ONE_TO_MANY;
        else if (field.getAnnotation(ManyToOne.class) != null)
            return Attr.Type.MANY_TO_ONE;
        else if (field.getAnnotation(ManyToMany.class) != null)
            return Attr.Type.MANY_TO_MANY;
        else
            return null;
    }

    private Document createDocument(Map<String, List<Attr>> mappings) {
        Document doc = DocumentHelper.createDocument();
        Element rootEl = doc.addElement("entity-mappings", XMLNS);
        rootEl.addAttribute("version", PERSISTENCE_VER);
        for (Map.Entry<String, List<Attr>> entry : mappings.entrySet()) {
            Element entityEl = rootEl.addElement("entity", XMLNS);
            entityEl.addAttribute("class", entry.getKey());
            Element attributesEl = entityEl.addElement("attributes", XMLNS);
            for (Attr attr : entry.getValue()) {
                Element attrEl = attributesEl.addElement(attr.type.xml, XMLNS);
                attrEl.addAttribute("name", attr.name);
                attrEl.addAttribute("target-entity", attr.targetEntity);
            }
        }
        return doc;
    }

    private File writeDocument(Document doc) {
        File file = new File(dir, "orm.xml");
        log.info("Creating file " + file);

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            Dom4j.writeDocument(doc, true, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(os);
        }
        return file;
    }
}
