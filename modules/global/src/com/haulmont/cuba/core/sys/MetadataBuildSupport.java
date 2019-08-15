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

package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.xmlparsing.Dom4jTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

@Component("cuba_MetadataBuildSupport")
public class MetadataBuildSupport {

    @Inject
    protected Dom4jTools dom4JTools;

    public static class XmlAnnotation {
        public final Object value;
        public final Map<String, Object> attributes = new HashMap<>();

        public XmlAnnotation(Object value) {
            this.value = value;
        }
    }

    public static class XmlAnnotations {
        public final String entityClass;
        public final Map<String, XmlAnnotation> annotations = new HashMap<>();
        public final List<XmlAnnotations> attributeAnnotations = new ArrayList<>();

        public XmlAnnotations(String entityClass) {
            this.entityClass = entityClass;
        }
    }

    public static class XmlFile {
        public final String name;
        public final Element root;

        public XmlFile(String name, Element root) {
            this.name = name;
            this.root = root;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(MetadataBuildSupport.class);

    public static final String PERSISTENCE_CONFIG = "cuba.persistenceConfig";
    public static final String METADATA_CONFIG = "cuba.metadataConfig";

    @Inject
    protected Resources resources;

    @Inject
    protected DatatypeRegistry datatypes;

    private static final Pattern JAVA_CLASS_PATTERN = Pattern.compile("([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*");

    public List<XmlFile> init() {
        List<XmlFile> metadataXmlList = new ArrayList<>();
        StringTokenizer metadataFilesTokenizer = new StringTokenizer(getMetadataConfig());
        for (String fileName : metadataFilesTokenizer.getTokenArray()) {
            metadataXmlList.add(new XmlFile(fileName, readXml(fileName)));
        }
        return metadataXmlList;
    }

    /**
     * @param storeName data store name
     * @return location of persistent entities descriptor or null if not defined
     */
    @Nullable
    public String getPersistenceConfig(String storeName) {
        String propName = PERSISTENCE_CONFIG;
        if (!Stores.isMain(storeName))
            propName = propName + "_" + storeName;

        String config = AppContext.getProperty(propName);
        if (StringUtils.isBlank(config)) {
            log.trace("Property {} is not set, assuming {} is not a RdbmsStore", propName, storeName);
            return null;
        } else
            return config;
    }

    /**
     * @return location of metadata descriptor
     */
    public String getMetadataConfig() {
        String config = AppContext.getProperty(METADATA_CONFIG);
        if (StringUtils.isBlank(config))
            throw new IllegalStateException(METADATA_CONFIG + " application property is not defined");
        return config;
    }

    public List<Element> getDatatypeElements(List<XmlFile> metadataXmlList) {
        List<Element> list = new ArrayList<>();
        for (XmlFile xmlFile : metadataXmlList) {
            Element datatypesEl = xmlFile.root.element("datatypes");
            if (datatypesEl != null) {
                list.addAll(datatypesEl.elements("datatype"));
            }
        }
        return list;
    }

    public Map<String, List<EntityClassInfo>> getEntityPackages(List<XmlFile> metadataXmlList) {
        Map<String, List<EntityClassInfo>> packages = new LinkedHashMap<>();

        loadFromMetadataConfig(packages, metadataXmlList);
        Stores.getAll().forEach(db -> loadFromPersistenceConfig(packages, db));

        return packages;
    }

    protected void loadFromMetadataConfig(Map<String, List<EntityClassInfo>> packages, List<XmlFile> metadataXmlList) {
        for (XmlFile xmlFile : metadataXmlList) {
            for (Element element : xmlFile.root.elements("metadata-model")) {
                String rootPackage = element.attributeValue("root-package");
                if (StringUtils.isBlank(rootPackage)) {
                    throw new IllegalStateException("metadata-model/@root-package is empty in " + xmlFile.name);
                }

                List<EntityClassInfo> classNames = packages.computeIfAbsent(rootPackage, k -> new ArrayList<>());

                for (Element classEl : element.elements("class")) {
                    classNames.add(new EntityClassInfo(classEl.attributeValue("store"), classEl.getText().trim(), false));
                }
            }
        }
    }

    protected void loadFromPersistenceConfig(Map<String, List<EntityClassInfo>> packages, String db) {
        String persistenceConfig = getPersistenceConfig(db);
        if (persistenceConfig == null) {
            return;
        }
        StringTokenizer persistenceFilesTokenizer = new StringTokenizer(persistenceConfig);
        for (String fileName : persistenceFilesTokenizer.getTokenArray()) {
            Element root = readXml(fileName);
            Element puEl = root.element("persistence-unit");
            if (puEl == null) {
                throw new IllegalStateException(String.format("File %s has no persistence-unit element", fileName));
            }

            for (Element classEl : puEl.elements("class")) {
                String className = classEl.getText().trim();
                boolean included = false;

                for (Map.Entry<String, List<EntityClassInfo>> entry : packages.entrySet()) {
                    if (className.startsWith(entry.getKey() + ".")) {
                        List<EntityClassInfo> classNames = entry.getValue();
                        if (classNames == null) {
                            classNames = new ArrayList<>();
                            packages.put(entry.getKey(), classNames);
                        }
                        classNames.add(new EntityClassInfo(db, className, true));
                        included = true;
                        break;
                    }
                }

                if (!included) {
                    String rootPackages = String.join(",\n", packages.keySet());
                    throw new IllegalStateException(
                            String.format("Can not find a model for class %s. The class's package must be inside of some model's root package. " +
                                            "Move the class to one of the following model's root packages: \n%s.", className, rootPackages));
                }
            }
        }
    }

    protected Element readXml(String path) {
        try (InputStream stream = resources.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Resource not found: " + path);
            }

            Document document = dom4JTools.readDocument(stream);
            return document.getRootElement();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read resource: " + path);
        }
    }

    public List<XmlAnnotations> getEntityAnnotations(List<XmlFile> metadataXmlList) {
        List<XmlAnnotations> result = new ArrayList<>();

        for (XmlFile xmlFile : metadataXmlList) {
            Element annotationsEl = xmlFile.root.element("annotations");
            if (annotationsEl != null) {
                for (Element entityEl : annotationsEl.elements("entity")) {
                    String className = entityEl.attributeValue("class");
                    XmlAnnotations entityAnnotations = new XmlAnnotations(className);
                    for (Element annotEl : entityEl.elements("annotation")) {
                        XmlAnnotation xmlAnnotation = new XmlAnnotation(inferMetaAnnotationType(annotEl.attributeValue("value")));
                        for (Element attrEl : annotEl.elements("attribute")) {
                            Object value = getXmlAnnotationAttributeValue(attrEl);
                            xmlAnnotation.attributes.put(attrEl.attributeValue("name"), value);
                        }
                        entityAnnotations.annotations.put(annotEl.attributeValue("name"), xmlAnnotation);
                    }
                    for (Element propEl : entityEl.elements("property")) {
                        XmlAnnotations attributeAnnotations = new XmlAnnotations(propEl.attributeValue("name"));
                        for (Element annotEl : propEl.elements("annotation")) {
                            XmlAnnotation xmlAnnotation = new XmlAnnotation(inferMetaAnnotationType(annotEl.attributeValue("value")));
                            for (Element attrEl : annotEl.elements("attribute")) {
                                Object value = getXmlAnnotationAttributeValue(attrEl);
                                xmlAnnotation.attributes.put(attrEl.attributeValue("name"), value);
                            }
                            attributeAnnotations.annotations.put(annotEl.attributeValue("name"), xmlAnnotation);
                        }
                        entityAnnotations.attributeAnnotations.add(attributeAnnotations);
                    }
                    result.add(entityAnnotations);
                }
            }
        }
        return result;
    }

    protected Object getXmlAnnotationAttributeValue(Element attributeEl) {
        String value = attributeEl.attributeValue("value");
        String className = attributeEl.attributeValue("class");
        String datatypeName = attributeEl.attributeValue("datatype");

        List<Element> values = attributeEl.elements("value");
        if (StringUtils.isNotBlank(value)) {
            if (!values.isEmpty())
                throw new IllegalStateException("Both 'value' attribute and 'value' element(s) are specified for attribute " + attributeEl.attributeValue("name"));
            return getXmlAnnotationAttributeValue(value, className, datatypeName);
        }
        if (!values.isEmpty()) {
            Object val0 = getXmlAnnotationAttributeValue(values.get(0).getTextTrim(), className, datatypeName);
            Object array = Array.newInstance(val0.getClass(), values.size());
            Array.set(array, 0, val0);
            for (int i = 1; i < values.size(); i++) {
                Object val = getXmlAnnotationAttributeValue(values.get(i).getTextTrim(), className, datatypeName);
                Array.set(array, i, val);
            }
            return array;
        }
        return null;
    }

    protected Object getXmlAnnotationAttributeValue(String value, String className, String datatypeName) {
        if (className == null && datatypeName == null)
            return inferMetaAnnotationType(value);
        if (className != null) {
            Class aClass = ReflectionHelper.getClass(className);
            if (aClass.isEnum()) {
                //noinspection unchecked
                return Enum.valueOf(aClass, value);
            } else {
                throw new UnsupportedOperationException("Class " + className + "  is not Enum");
            }
        } else {
            try {
                return datatypes.get(datatypeName).parse(value);
            } catch (ParseException e) {
                throw new RuntimeException("Unable to parse XML meta-annotation value", e);
            }
        }
    }

    protected Object inferMetaAnnotationType(String str) {
        Object val;
        if (str != null && (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")))
            val = Boolean.valueOf(str);
        else if (str != null && JAVA_CLASS_PATTERN.matcher(str).matches()) {
            try {
                val = ReflectionHelper.loadClass(str);
            } catch (ClassNotFoundException e) {
                val = str;
            }
        } else if (!"".equals(str) && StringUtils.isNumeric(str)) {
            val = Integer.valueOf(str);
        } else
            val = str;
        return val;
    }
}