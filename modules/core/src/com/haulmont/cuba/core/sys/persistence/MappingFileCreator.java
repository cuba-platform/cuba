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

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.annotation.Extends;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.*;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.persistence.Entity;
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
 *
*/
class MappingFileCreator {

    private static final String XMLNS = "http://xmlns.jcp.org/xml/ns/persistence/orm";
    private static final String SCHEMA_LOCATION = XMLNS + " http://xmlns.jcp.org/xml/ns/persistence/orm_2_1.xsd";
    private static final String PERSISTENCE_VER = "2.1";

    private Collection<String> classNames;
    private Map<String, String> properties;
    private File dir;

    private Logger log = LoggerFactory.getLogger(getClass());

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
                if (!originalClass.isAnnotationPresent(DiscriminatorValue.class)) { // entities with discriminator don't need to be re-mapped
                    extendedClasses.put(originalClass, aClass);
                }
            }
        }

        if (extendedClasses.isEmpty())
            return false;

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

        Map<Class<?>, List<Attr>> mappings = new LinkedHashMap<>();
        for (Class aClass : persistentClasses) {
            List<Attr> attrList = processClass(aClass, classes);
            if (!attrList.isEmpty())
                mappings.put(aClass, attrList);
        }

        if (mappings.isEmpty())
            return false;
        log.debug("Found " + mappings.size() + " entities containing extended associations");

        Document doc = createDocument(mappings);
        writeDocument(doc);
        return true;
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

    private Document createDocument(Map<Class<?>, List<Attr>> mappings) {
        Document doc = DocumentHelper.createDocument();
        Element rootEl = doc.addElement("entity-mappings", XMLNS);
        Namespace xsi = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootEl.add(xsi);
        rootEl.addAttribute(new QName("schemaLocation", xsi), SCHEMA_LOCATION);
        rootEl.addAttribute("version", PERSISTENCE_VER);

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

        return doc;
    }

    private void createAttributes(Map.Entry<Class<?>, List<Attr>> entry, Element entityEl) {
        Element attributesEl = entityEl.addElement("attributes", XMLNS);
        Collections.sort(entry.getValue(), new Comparator<Attr>() {
            @Override
            public int compare(Attr a1, Attr a2) {
                return a1.type.order - a2.type.order;
            }
        });
        for (Attr attr : entry.getValue()) {
            attr.toXml(attributesEl);
        }
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

    private class ClassDef {

        private Class<?> entityClass;

        private ClassDef(Class<?> entityClass) {
            this.entityClass = entityClass;
        }

        @Nullable
        private Element toXml(Element parentEl) {
            Element el;
            if (entityClass.getAnnotation(Entity.class) != null) {
                el = parentEl.addElement("entity", XMLNS);
            } else if (entityClass.getAnnotation(MappedSuperclass.class) != null) {
                el = parentEl.addElement("mapped-superclass", XMLNS);
            } else {
                log.warn(entityClass + " has neither @Entity nor @MappedSuperclass annotation, ignoring it");
                return null;
            }
            el.addAttribute("class", entityClass.getName());
            return el;
        }
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
            };

            private int order;
            private String xml;

            private Type(int order, String xml) {
                this.order = order;
                this.xml = xml;
            }

            protected abstract String getFetch(Field field);
            protected abstract String getMappedBy(Field field);
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

            // either
            new JoinColumnHandler(field.getAnnotation(JoinColumn.class)).toXml(el);
            // or
            new OrderByHandler(field.getAnnotation(OrderBy.class)).toXml(el);
            new JoinTableHandler(field.getAnnotation(JoinTable.class)).toXml(el);

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
