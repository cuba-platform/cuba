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

import com.google.common.base.Strings;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.MetadataBuildSupport.XmlAnnotation;
import com.haulmont.cuba.core.sys.MetadataBuildSupport.XmlAnnotations;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * INTERNAL.
 * Creates metadata session and loads metadata from annotated Java classes.
 */
@Component(MetadataLoader.NAME)
@Scope("prototype")
public class MetadataLoader {

    public static final String NAME = "cuba_MetadataLoader";

    private final Logger log = LoggerFactory.getLogger(MetadataLoader.class);

    @Inject
    protected MetadataBuildSupport metadataBuildSupport;

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected DatatypeRegistry datatypeRegistry;

    @Inject
    protected ApplicationContext applicationContext;

    protected Session session;
    protected List<String> rootPackages = new ArrayList<>();

    public MetadataLoader() {
        this.session = new SessionImpl();
    }

    protected MetaModelLoader createModelLoader(Session session) {
        return (MetaModelLoader) applicationContext.getBean(MetaModelLoader.NAME, session);
    }

    /**
     * Loads metadata session.
     */
    public void loadMetadata() {
        List<MetadataBuildSupport.XmlFile> metadataXmlList = metadataBuildSupport.init();

        initRootPackages(metadataXmlList);

        initDatatypes(metadataBuildSupport.getDatatypeElements(metadataXmlList));

        MetaModelLoader modelLoader = createModelLoader(session);

        Map<String, List<EntityClassInfo>> entityPackages = metadataBuildSupport.getEntityPackages(metadataXmlList);
        for (Map.Entry<String, List<EntityClassInfo>> entry : entityPackages.entrySet()) {
            modelLoader.loadModel(entry.getKey(), entry.getValue());
        }

        for (MetaClass metaClass : session.getClasses()) {
            postProcessClass(metaClass);
            initMetaAnnotations(metaClass);
        }

        initStoreMetaAnnotations(entityPackages);
        initExtensionMetaAnnotations();

        List<XmlAnnotations> xmlAnnotations = metadataBuildSupport.getEntityAnnotations(metadataXmlList);
        for (MetaClass metaClass : session.getClasses()) {
            addMetaAnnotationsFromXml(xmlAnnotations, metaClass);
        }

        replaceExtendedMetaClasses();
    }

    /**
     * @return loaded session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @return list of root packages of all loaded meta-models
     */
    public List<String> getRootPackages() {
        return rootPackages;
    }

    protected void initRootPackages(List<MetadataBuildSupport.XmlFile> metadataXmlList) {
        for (MetadataBuildSupport.XmlFile xmlFile : metadataXmlList) {
            for (Element element : Dom4j.elements(xmlFile.root, "metadata-model")) {
                String rootPackage = element.attributeValue("root-package");
                if (!StringUtils.isBlank(rootPackage) && !rootPackages.contains(rootPackage)) {
                    rootPackages.add(rootPackage);
                }
            }
        }
    }

    protected void initDatatypes(List<Element> datatypeElements) {
        loadDatatypesFromClasspathResource();

        for (Element datatypeEl : datatypeElements) {
            String id = datatypeEl.attributeValue("id");
            String className = datatypeEl.attributeValue("class");

            if (Strings.isNullOrEmpty(className))
                throw new IllegalStateException("Missing required 'class' attribute for datatype " + id + ". Check your metadata.xml file.");

            if (Strings.isNullOrEmpty(id))
                throw new IllegalStateException("Missing required 'id' attribute for datatype " + className + ". Check your metadata.xml file.");

            try {
                Datatype datatype;
                Class<Datatype> datatypeClass = ReflectionHelper.getClass(className);
                try {
                    Constructor<Datatype> constructor = datatypeClass.getConstructor(Element.class);
                    datatype = constructor.newInstance(datatypeEl);
                } catch (Throwable e) {
                    datatype = datatypeClass.newInstance();
                }
                datatypeRegistry.register(datatype, id, Boolean.valueOf(datatypeEl.attributeValue("default")));
            } catch (Throwable e) {
                log.error("Fail to load datatype '{}'", className, e);
            }
        }
    }

    protected void loadDatatypesFromClasspathResource() {
        SAXReader reader = new SAXReader();
        URL resource = Datatypes.class.getResource(getGetDatatypesResourcePath());
        if (resource != null) {
            log.info("Loading datatypes from " + resource);
            try {
                Document document = reader.read(resource);
                Element element = document.getRootElement();

                List<Element> datatypeElements = Dom4j.elements(element, "datatype");
                for (Element datatypeElement : datatypeElements) {
                    String datatypeClassName = datatypeElement.attributeValue("class");
                    try {
                        Datatype datatype;
                        Class<Datatype> datatypeClass = ReflectionHelper.getClass(datatypeClassName);
                        try {
                            final Constructor<Datatype> constructor = datatypeClass.getConstructor(Element.class);
                            datatype = constructor.newInstance(datatypeElement);
                        } catch (Throwable e) {
                            datatype = datatypeClass.newInstance();
                        }

                        String id = datatypeElement.attributeValue("id");
                        if (Strings.isNullOrEmpty(id))
                            id = guessDatatypeId(datatype);
                        datatypeRegistry.register(datatype, id, true);
                    } catch (Throwable e) {
                        log.error(String.format("Fail to load datatype '%s'", datatypeClassName), e);
                    }
                }
            } catch (DocumentException e) {
                log.error("Fail to load datatype settings", e);
            }
        }
    }

    /**
     * Guesses id for a datatype registered in legacy datatypes.xml file.
     * For backward compatibility only.
     */
    protected String guessDatatypeId(Datatype datatype) {
        if (datatype instanceof BigDecimalDatatype)
            return "decimal";
        if (datatype instanceof BooleanDatatype)
            return "boolean";
        if (datatype instanceof ByteArrayDatatype)
            return "byteArray";
        if (datatype instanceof DateDatatype)
            return "date";
        if (datatype instanceof DateTimeDatatype)
            return "dateTime";
        if (datatype instanceof DoubleDatatype)
            return "double";
        if (datatype instanceof IntegerDatatype)
            return "int";
        if (datatype instanceof LongDatatype)
            return "long";
        if (datatype instanceof StringDatatype)
            return "string";
        if (datatype instanceof TimeDatatype)
            return "time";
        if (datatype instanceof UUIDDatatype)
            return "uuid";
        try {
            Field nameField = datatype.getClass().getField("NAME");
            if (Modifier.isStatic(nameField.getModifiers()) && nameField.isAccessible()) {
                return (String) nameField.get(null);
            }
        } catch (Exception e) {
            log.trace("Cannot get NAME static field value: {}", e);
        }
        throw new IllegalStateException("Cannot guess id for datatype " + datatype);
    }

    protected String getGetDatatypesResourcePath() {
        return "/datatypes.xml";
    }

    protected void replaceExtendedMetaClasses() {
        StopWatch sw = new Slf4JStopWatch("Metadata.replaceExtendedMetaClasses");

        for (MetaModel model : session.getModels()) {
            MetaModelImpl modelImpl = (MetaModelImpl) model;

            List<Pair<MetaClass, MetaClass>> replaceMap = new ArrayList<>();
            for (MetaClass metaClass : modelImpl.getClasses()) {
                MetaClass effectiveMetaClass = session.getClass(extendedEntities.getEffectiveClass(metaClass));

                if (effectiveMetaClass != metaClass) {
                    replaceMap.add(new Pair<>(metaClass, effectiveMetaClass));
                }

                for (MetaProperty metaProperty : metaClass.getOwnProperties()) {
                    MetaPropertyImpl propertyImpl = (MetaPropertyImpl) metaProperty;

                    // replace domain
                    Class effectiveDomainClass = extendedEntities.getEffectiveClass(metaProperty.getDomain());
                    MetaClass effectiveDomainMeta = session.getClass(effectiveDomainClass);
                    if (metaProperty.getDomain() != effectiveDomainMeta) {
                        propertyImpl.setDomain(effectiveDomainMeta);
                    }

                    if (metaProperty.getRange().isClass()) {
                        // replace range class
                        ClassRange range = (ClassRange) metaProperty.getRange();

                        Class effectiveRangeClass = extendedEntities.getEffectiveClass(range.asClass());
                        MetaClass effectiveRangeMeta = session.getClass(effectiveRangeClass);
                        if (effectiveRangeMeta != range.asClass()) {
                            ClassRange newRange = new ClassRange(effectiveRangeMeta);
                            newRange.setCardinality(range.getCardinality());
                            newRange.setOrdered(range.isOrdered());

                            ((MetaPropertyImpl) metaProperty).setRange(newRange);
                        }
                    }
                }
            }

            for (Pair<MetaClass, MetaClass> replace : replaceMap) {
                MetaClass replacedMetaClass = replace.getFirst();
                extendedEntities.registerReplacedMetaClass(replacedMetaClass);

                MetaClassImpl effectiveMetaClass = (MetaClassImpl) replace.getSecond();
                modelImpl.registerClass(replacedMetaClass.getName(), replacedMetaClass.getJavaClass(), effectiveMetaClass);
            }
        }

        sw.stop();
    }

    protected void initStoreMetaAnnotations(Map<String, List<EntityClassInfo>> entityPackages) {
        if (Stores.getAdditional().isEmpty())
            return;

        Map<String, String> nameToStoreMap = new HashMap<>();
        for (List<EntityClassInfo> list : entityPackages.values()) {
            for (EntityClassInfo entityClassInfo : list) {
                if (nameToStoreMap.containsKey(entityClassInfo.name)) {
                    throw new IllegalStateException("Entity cannot belong to more than one store: " + entityClassInfo.name);
                }
                nameToStoreMap.put(entityClassInfo.name, entityClassInfo.store);
            }
        }

        for (MetaClass metaClass : session.getClasses()) {
            String className = metaClass.getJavaClass().getName();
            String store = nameToStoreMap.get(className);
            if (store != null)
                metaClass.getAnnotations().put(MetadataTools.STORE_ANN_NAME, store);
        }
    }

    /**
     * Initialize connections between extended and base entities.
     */
    protected void initExtensionMetaAnnotations() {
        for (MetaClass metaClass : session.getClasses()) {
            Class<?> javaClass = metaClass.getJavaClass();

            List<Class> superClasses = new ArrayList<>();
            Extends extendsAnnotation = javaClass.getAnnotation(Extends.class);
            while (extendsAnnotation != null) {
                Class<? extends Entity> superClass = extendsAnnotation.value();
                superClasses.add(superClass);
                extendsAnnotation = superClass.getAnnotation(Extends.class);
            }

            for (Class superClass : superClasses) {
                metaClass.getAnnotations().put(Extends.class.getName(), superClass);

                MetaClass superMetaClass = session.getClassNN(superClass);

                Class<?> extendedByClass = (Class) superMetaClass.getAnnotations().get(ExtendedBy.class.getName());
                if (extendedByClass != null && !javaClass.equals(extendedByClass)) {
                    if (javaClass.isAssignableFrom(extendedByClass))
                        continue;
                    else if (!extendedByClass.isAssignableFrom(javaClass))
                        throw new IllegalStateException(superClass + " is already extended by " + extendedByClass);
                }

                superMetaClass.getAnnotations().put(ExtendedBy.class.getName(), javaClass);
            }
        }
    }

    /**
     * Initialize entity annotations from definition in <code>metadata.xml</code>.
     * <p>Can be overridden in application projects to handle application-specific annotations.</p>
     *
     * @param xmlAnnotations map of class name to annotations map
     * @param metaClass      MetaClass instance to assign annotations
     */
    protected void addMetaAnnotationsFromXml(List<XmlAnnotations> xmlAnnotations, MetaClass metaClass) {
        for (XmlAnnotations xmlAnnotation : xmlAnnotations) {
            MetaClass metaClassFromXml = session.getClassNN(ReflectionHelper.getClass(xmlAnnotation.entityClass));
            Class extendedClass = extendedEntities.getExtendedClass(metaClassFromXml);
            MetaClass effectiveMetaClass = extendedClass != null ? session.getClassNN(extendedClass) : metaClassFromXml;
            if (effectiveMetaClass.equals(metaClass)) {
                for (Map.Entry<String, XmlAnnotation> entry : xmlAnnotation.annotations.entrySet()) {
                    assignMetaAnnotationValueFromXml(entry.getKey(), entry.getValue(), metaClass.getAnnotations());
                }
                for (XmlAnnotations attributeAnnotation : xmlAnnotation.attributeAnnotations) {
                    MetaProperty property = metaClass.getPropertyNN(attributeAnnotation.entityClass);
                    for (Map.Entry<String, XmlAnnotation> entry : attributeAnnotation.annotations.entrySet()) {
                        assignMetaAnnotationValueFromXml(entry.getKey(), entry.getValue(), property.getAnnotations());
                    }
                }
                break;
            }
        }
    }

    protected void assignMetaAnnotationValueFromXml(String annName, XmlAnnotation xmlAnn, Map<String, Object> metaAnnotations) {
        if (xmlAnn.value != null) {
            metaAnnotations.put(annName, xmlAnn.value);
            if (!xmlAnn.attributes.isEmpty()) {
                log.warn("Attributes of {} meta-annotation are ignored because a value is set", annName);
            }
        } else {
            Object annValue = metaAnnotations.computeIfAbsent(annName, k -> new LinkedHashMap<>());
            if (annValue instanceof Map) {
                //noinspection unchecked
                ((Map) annValue).putAll(xmlAnn.attributes);
            } else {
                log.warn("Meta-annotation {} has value {} and cannot be re-assigned by annotation attributes", annName, annValue);
            }
        }
    }

    protected void postProcessClass(MetaClass metaClass) {
        for (MetaProperty property : metaClass.getOwnProperties()) {
            postProcessProperty(metaClass, property);
        }

        Collection<MetaClass> missingDescendants = new HashSet<>(1);

        findMissingDescendants(metaClass, missingDescendants);

        if (!missingDescendants.isEmpty()) {
            CollectionUtils.addAll(metaClass.getDescendants(), missingDescendants.iterator());

            MetaClass ancestorMetaClass = metaClass.getAncestor();
            while (ancestorMetaClass != null) {
                CollectionUtils.addAll(ancestorMetaClass.getDescendants(), missingDescendants.iterator());
                ancestorMetaClass = ancestorMetaClass.getAncestor();
            }
        }

        MetaClass ancestorMetaClass = metaClass.getAncestor();
        while (ancestorMetaClass != null) {
            ((MetaClassImpl) metaClass).addAncestor(ancestorMetaClass);
            ancestorMetaClass = ancestorMetaClass.getAncestor();
        }
    }

    protected void postProcessProperty(MetaClass metaClass, MetaProperty metaProperty) {
        // init inverse properties
        MetaProperty inverseProp = metaProperty.getInverse();
        if (inverseProp != null && inverseProp.getInverse() == null) {
            ((MetaPropertyImpl) inverseProp).setInverse(metaProperty);
        }

        if (metaProperty.getRange() == null || !metaProperty.getRange().isClass())
            return;

        AnnotatedElement annotatedElement = metaProperty.getAnnotatedElement();

        OnDelete onDelete = annotatedElement.getAnnotation(OnDelete.class);
        if (onDelete != null) {
            Map<String, Object> metaAnnotations = metaClass.getAnnotations();

            MetaProperty[] properties = (MetaProperty[]) metaAnnotations.get(OnDelete.class.getName());
            properties = ArrayUtils.add(properties, metaProperty);
            metaAnnotations.put(OnDelete.class.getName(), properties);
        }

        OnDeleteInverse onDeleteInverse = annotatedElement.getAnnotation(OnDeleteInverse.class);
        if (onDeleteInverse != null) {
            Map<String, Object> metaAnnotations = metaProperty.getRange().asClass().getAnnotations();

            MetaProperty[] properties = (MetaProperty[]) metaAnnotations.get(OnDeleteInverse.class.getName());
            properties = ArrayUtils.add(properties, metaProperty);
            metaAnnotations.put(OnDeleteInverse.class.getName(), properties);
        }
    }

    protected void findMissingDescendants(MetaClass ancestor, Collection<MetaClass> missingDescendants) {
        Collection<MetaClass> descendants = ancestor.getDescendants();
        for (Object descendant : descendants) {
            missingDescendants.add((MetaClass) descendant);
            findMissingDescendants((MetaClass) descendant, missingDescendants);
        }
    }

    protected void initMetaAnnotations(MetaClass metaClass) {
        for (Annotation annotation : metaClass.getJavaClass().getAnnotations()) {
            MetaAnnotation metaAnnotation = AnnotationUtils.findAnnotation(annotation.getClass(), MetaAnnotation.class);
            if (metaAnnotation != null) {
                String name = annotation.annotationType().getName();

                Map<String, Object> attributes = new LinkedHashMap<>(AnnotationUtils.getAnnotationAttributes(metaClass.getJavaClass(), annotation));
                metaClass.getAnnotations().put(name, attributes);

                Object propagateToSubclasses = attributes.get("propagateToSubclasses");
                if (propagateToSubclasses == null || Boolean.TRUE.equals(propagateToSubclasses)) {
                    for (MetaClass descMetaClass : metaClass.getDescendants()) {
                        Annotation descAnnotation = descMetaClass.getJavaClass().getAnnotation(annotation.annotationType());
                        if (descAnnotation == null) {
                            descMetaClass.getAnnotations().put(name, attributes);
                        }
                    }
                }
            }
        }
    }
}