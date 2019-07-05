/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.persistence;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.app.OrmXmlPostProcessor;
import com.haulmont.cuba.core.entity.annotation.Extends;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Generates an orm.xml file containing mapping overrides to support extended entities in associations.
 * Works together with {@link PersistenceConfigProcessor}.
*/
class MappingFileCreator {

    private static final Logger log = LoggerFactory.getLogger(MappingFileCreator.class);

    private static final String XMLNS = "http://xmlns.jcp.org/xml/ns/persistence/orm";
    private static final String SCHEMA_LOCATION = XMLNS + " http://xmlns.jcp.org/xml/ns/persistence/orm_2_1.xsd";
    private static final String PERSISTENCE_VER = "2.1";

    private Collection<String> classNames;
    private Map<String, String> properties;
    private File dir;

    MappingFileCreator(Collection<String> classNames, Map<String, String> properties, File dir) {
        this.classNames = classNames;
        this.properties = properties;
        this.dir = dir;
    }

    public boolean create() {
        Map<Class, Class> extendedClasses = new HashMap<>();
        List<Class> persistentClasses = new ArrayList<>();
        for (String className : classNames) {
            Class<?> aClass = ReflectionHelper.getClass(className);
            persistentClasses.add(aClass);
            Extends annotation = aClass.getAnnotation(Extends.class);
            if (annotation != null) {
                Class originalClass = annotation.value();
                extendedClasses.put(originalClass, aClass);
            }
        }

        Map<Class<?>, List<Attr>> mappings = new LinkedHashMap<>();

        if (!extendedClasses.isEmpty()) {
            // search for higher order extensions
            Map<Class, Class> classes = new HashMap<>();

            for (Map.Entry<Class, Class> mappingEntry : extendedClasses.entrySet()) {
                Class originalClass = mappingEntry.getKey();
                Class extClass = mappingEntry.getValue();
                Class lastExtClass = null;
                Class aClass = extendedClasses.get(extClass);
                while (aClass != null) {
                    lastExtClass = aClass;
                    aClass = extendedClasses.get(aClass);
                }
                if (lastExtClass != null) {
                    classes.put(originalClass, lastExtClass);
                } else {
                    classes.put(originalClass, extClass);
                }
            }

            for (Class aClass : persistentClasses) {
                List<Attr> attrList = processClass(aClass, classes);
                if (!attrList.isEmpty())
                    mappings.put(aClass, attrList);
            }
        }

        Document doc = createDocument(mappings);
        if (doc != null) {
            writeDocument(doc);
            return true;
        } else {
            return false;
        }
    }

    private List<Attr> processClass(Class aClass, Map<Class, Class> extendedClasses) {
        List<Attr> list = new ArrayList<>();

        for (Field field : aClass.getDeclaredFields()) {
            Attr.Type type = getAttrType(field);
            if (type != null) {
                Class<?> fieldType = field.getType();
                Class extClass = null;
                if (Collection.class.isAssignableFrom(fieldType)) {
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                        if (typeArguments.length == 1) {
                            extClass = extendedClasses.get((Class) typeArguments[0]);
                        }
                    }
                } else {
                    extClass = extendedClasses.get(fieldType);
                }
                if (extClass != null) {
                    Attr attr = new Attr(type, field, extClass.getName());
                    list.add(attr);
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

    @Nullable
    private Document createDocument(Map<Class<?>, List<Attr>> mappings) {
        if (mappings.isEmpty()) {
            return postProcess(null);
        }

        Document doc = createEmptyDocument();
        Element rootEl = doc.getRootElement();

        log.debug("Found " + mappings.size() + " entities containing extended associations");

        for (Map.Entry<Class<?>, List<Attr>> entry : mappings.entrySet()) {
            if (entry.getKey().getAnnotation(MappedSuperclass.class) != null) {
                Element entityEl = rootEl.addElement("mapped-superclass", XMLNS);
                entityEl.addAttribute("class", entry.getKey().getName());
                createAttributes(entry, entityEl);
            }
        }
        for (Map.Entry<Class<?>, List<Attr>> entry : mappings.entrySet()) {
            if (entry.getKey().getAnnotation(Entity.class) != null) {
                Element entityEl = rootEl.addElement("entity", XMLNS);
                entityEl.addAttribute("class", entry.getKey().getName());
                entityEl.addAttribute("name", entry.getKey().getAnnotation(Entity.class).name());
                createAttributes(entry, entityEl);
            }
        }
        for (Map.Entry<Class<?>, List<Attr>> entry : mappings.entrySet()) {
            if (entry.getKey().getAnnotation(Embeddable.class) != null) {
                Element entityEl = rootEl.addElement("embeddable", XMLNS);
                entityEl.addAttribute("class", entry.getKey().getName());
                createAttributes(entry, entityEl);
            }
        }

        return postProcess(doc);
    }

    private Document createEmptyDocument() {
        Document doc = DocumentHelper.createDocument();
        Element rootEl = doc.addElement("entity-mappings", XMLNS);
        Namespace xsi = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootEl.add(xsi);
        rootEl.addAttribute(new QName("schemaLocation", xsi), SCHEMA_LOCATION);
        rootEl.addAttribute("version", PERSISTENCE_VER);
        return doc;
    }

    @Nullable
    private Document postProcess(@Nullable Document document) {
        String postProcessorClassName = AppContext.getProperty("cuba.ormXmlPostProcessor");

        if (!Strings.isNullOrEmpty(postProcessorClassName)) {
            log.debug("Running orm.xml post-processor: " + postProcessorClassName);
            if (document == null)
                document = createEmptyDocument();
            try {
                Class processorClass = ReflectionHelper.loadClass(postProcessorClassName);
                OrmXmlPostProcessor processor = (OrmXmlPostProcessor) processorClass.newInstance();
                processor.process(document);
            } catch (Exception e) {
                throw new RuntimeException("Error post-processing orm.xml", e);
            }
        }
        return document;
    }

    private void createAttributes(Map.Entry<Class<?>, List<Attr>> entry, Element entityEl) {
        Element attributesEl = entityEl.addElement("attributes", XMLNS);
        entry.getValue().sort(Comparator.comparingInt(a -> a.type.order));

        for (Attr attr : entry.getValue()) {
            attr.toXml(attributesEl);
        }
    }

    private File writeDocument(Document doc) {
        File file = new File(dir, "orm.xml");
        log.info("Creating file " + file);

        try (OutputStream os = new FileOutputStream(file)) {
            Dom4j.writeDocument(doc, true, os);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write XML document", e);
        }

        return file;
    }

    private static class Attr {

        private enum Type {
            MANY_TO_ONE(1, "many-to-one") {
                @Override
                protected String getFetch(Field field) {
                    return field.getAnnotation(ManyToOne.class).fetch().name();
                }
                @Override
                protected String getMappedBy(Field field) {
                    return null;
                }
                @Override
                protected CascadeType[] getCascade(Field field) {
                    return field.getAnnotation(ManyToOne.class).cascade();
                }
            },
            ONE_TO_MANY(2, "one-to-many") {
                @Override
                protected String getFetch(Field field) {
                    return field.getAnnotation(OneToMany.class).fetch().name();
                }
                @Override
                protected String getMappedBy(Field field) {
                    return field.getAnnotation(OneToMany.class).mappedBy();
                }
                @Override
                protected CascadeType[] getCascade(Field field) {
                    return field.getAnnotation(OneToMany.class).cascade();
                }
            },
            ONE_TO_ONE(3, "one-to-one") {
                @Override
                protected String getFetch(Field field) {
                    return field.getAnnotation(OneToOne.class).fetch().name();
                }
                @Override
                protected String getMappedBy(Field field) {
                    return field.getAnnotation(OneToOne.class).mappedBy();
                }
                @Override
                protected CascadeType[] getCascade(Field field) {
                    return field.getAnnotation(OneToOne.class).cascade();
                }
            },
            MANY_TO_MANY(4, "many-to-many") {
                @Override
                protected String getFetch(Field field) {
                    return field.getAnnotation(ManyToMany.class).fetch().name();
                }
                @Override
                protected String getMappedBy(Field field) {
                    return field.getAnnotation(ManyToMany.class).mappedBy();
                }
                @Override
                protected CascadeType[] getCascade(Field field) {
                    return field.getAnnotation(ManyToMany.class).cascade();
                }
            };

            private int order;
            private String xml;

            private Type(int order, String xml) {
                this.order = order;
                this.xml = xml;
            }

            protected abstract String getFetch(Field field);
            protected abstract String getMappedBy(Field field);
            protected abstract CascadeType[] getCascade(Field field);
        }

        private final Type type;
        private final Field field;
        private final String targetEntity;

        private Attr(Type type, Field field, String targetEntity) {
            this.type = type;
            this.field = field;
            this.targetEntity = targetEntity;
        }

        private Element toXml(Element parentEl) {
            Element el = parentEl.addElement(type.xml, XMLNS);
            el.addAttribute("name", field.getName());
            el.addAttribute("target-entity", targetEntity);
            el.addAttribute("fetch", type.getFetch(field));
            String mappedBy = type.getMappedBy(field);
            if (!StringUtils.isEmpty(mappedBy))
                el.addAttribute("mapped-by", mappedBy);

            CascadeType[] cascadeTypes = type.getCascade(field);
            if (cascadeTypes != null && cascadeTypes.length > 0) {
                Element cascadeTypeEl = el.addElement("cascade", XMLNS);
                for (CascadeType cascadeType : cascadeTypes) {
                    cascadeTypeEl.addElement("cascade-" + cascadeType.name().toLowerCase());
                }
            }
            // either
            new JoinColumnHandler(field.getAnnotation(JoinColumn.class)).toXml(el);
            // or
            new OrderByHandler(field.getAnnotation(OrderBy.class)).toXml(el);
            new JoinTableHandler(field.getAnnotation(JoinTable.class)).toXml(el);
            new MapsIdHandler(field.getAnnotation(MapsId.class)).toXml(el);

            return el;
        }
    }

    private static class JoinColumnHandler {

        private JoinColumn annotation;

        private JoinColumnHandler(JoinColumn annotation) {
            this.annotation = annotation;
        }

        protected void toXml(Element parentEl) {
            if (annotation == null)
                return;

            Element el = parentEl.addElement(getElementName());
            el.addAttribute("name", annotation.name());

            if (!StringUtils.isEmpty(annotation.referencedColumnName()))
                el.addAttribute("referenced-column-name", annotation.referencedColumnName());

            if (annotation.unique())
                el.addAttribute("unique", "true");

            if (!annotation.nullable())
                el.addAttribute("nullable", "false");

            if (!annotation.insertable())
                el.addAttribute("insertable", "false");

            if (!annotation.updatable())
                el.addAttribute("updatable", "false");
        }

        protected String getElementName() {
            return "join-column";
        }
    }

    private static class InverseJoinColumnHandler extends JoinColumnHandler {

        private InverseJoinColumnHandler(JoinColumn annotation) {
            super(annotation);
        }

        @Override
        protected String getElementName() {
            return "inverse-join-column";
        }
    }

    private static class JoinTableHandler {

        private JoinTable annotation;

        private JoinTableHandler(JoinTable annotation) {
            this.annotation = annotation;
        }

        private void toXml(Element parentEl) {
            if (annotation == null)
                return;

            Element el = parentEl.addElement("join-table");
            el.addAttribute("name", annotation.name());

            for (JoinColumn joinColumnAnnot : annotation.joinColumns()) {
                new JoinColumnHandler(joinColumnAnnot).toXml(el);
            }
            for (JoinColumn joinColumnAnnot : annotation.inverseJoinColumns()) {
                new InverseJoinColumnHandler(joinColumnAnnot).toXml(el);
            }
        }
    }

    private static class MapsIdHandler {
        private MapsId annotation;

        private MapsIdHandler(MapsId annotation) {
            this.annotation = annotation;
        }

        private void toXml(Element parentEl) {
            if (annotation == null)
                return;

            parentEl.addAttribute("maps-id", annotation.value());
        }
    }

    private static class OrderByHandler {

        private OrderBy annotation;

        private OrderByHandler(OrderBy annotation) {
            this.annotation = annotation;
        }

        private void toXml(Element parentEl) {
            if (annotation == null)
                return;

            Element el = parentEl.addElement("order-by");
            el.setText(annotation.value());
        }
    }
}