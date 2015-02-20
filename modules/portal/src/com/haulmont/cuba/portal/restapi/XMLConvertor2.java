/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.portal.config.RestConfig;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.util.ClassUtils;

import javax.activation.MimeType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.StringWriter;
import java.util.*;

/**
 * XML Convertor that works with new xml schema defined in platform v5.4
 *
 * @author krivopustov
 * @version $Id$
 */
public class XMLConvertor2 implements Convertor {

    public static final MimeType MIME_TYPE_XML;
    public static final String MIME_STR = "text/xml;charset=UTF-8";
    public static final String TYPE_XML = "xml";

    static {
        try {
            MIME_TYPE_XML = new MimeType(MIME_STR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final Metadata metadata;

    protected final RestConfig restConfig;
    protected final MetadataTools metadataTools;

    public XMLConvertor2() {
        metadata = AppBeans.get(Metadata.NAME);
        metadataTools = metadata.getTools();
        Configuration configuration = AppBeans.get(Configuration.NAME);
        restConfig = configuration.getConfig(RestConfig.class);
    }

    @Override
    public MimeType getMimeType() {
        return MIME_TYPE_XML;
    }

    @Override
    public String getType() {
        return TYPE_XML;
    }

    @Override
    public List<Integer> getApiVersions() {
        return Arrays.asList(1, 2);
    }

    @Override
    public String process(Entity entity, MetaClass metaclass, View view) throws Exception {
        Document document = _process(entity, view);
        return documentToString(document);
    }


    protected Document _process(Entity entity,  View view) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element rootEl = document.addElement("instances");
        encodeEntity(entity, view, rootEl);
        return document;
    }

    @Override
    public String process(List<Entity> entities, MetaClass metaClass, View view) throws Exception {
        Document document = _process(entities, metaClass, view);
        return documentToString(document);
    }

    protected Document _process(List<Entity> entities, MetaClass metaClass, View view) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element rootEl = document.addElement("instances");
        for (Entity entity : entities) {
            encodeEntity(entity, view, rootEl);
        }
        return document;
    }

    @Override
    public String process(Set<Entity> entities) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element rootEl = document.addElement("instances");
        for (Entity entity : entities) {
            encodeEntity(entity, null, rootEl);
        }
        return documentToString(document);
    }

    @Override
    @Nonnull
    public String processServiceMethodResult(Object result, @Nullable String viewName) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element resultEl = document.addElement("result");
        if (result instanceof Entity) {
            Entity entity = (Entity) result;
            if (Strings.isNullOrEmpty(viewName)) viewName = View.LOCAL;
            ViewRepository viewRepository = AppBeans.get(ViewRepository.class);
            View view = viewRepository.getView(entity.getMetaClass(), viewName);
            Document convertedEntity = _process(entity, view);
            resultEl.add(convertedEntity.getRootElement());
        } else if (result instanceof Collection) {
            if (!checkCollectionItemTypes((Collection) result, Entity.class))
                throw new IllegalArgumentException("Items that are not instances of Entity class found in service method result");
            ArrayList list = new ArrayList((Collection) result);
            MetaClass metaClass = null;
            if (!list.isEmpty())
                metaClass = ((Entity) list.get(0)).getMetaClass();

            View view = null;
            if (metaClass != null) {
                ViewRepository viewRepository = AppBeans.get(ViewRepository.class);
                if (Strings.isNullOrEmpty(viewName)) viewName = View.LOCAL;
                view = viewRepository.getView(metaClass, viewName);
            }
            Document processed = _process(list, metaClass, view);
            resultEl.add(processed.getRootElement());
        } else {
            if (result != null) {
                resultEl.setText(result.toString());
            } else {
                encodeNull(resultEl);
            }
        }
        return documentToString(document);
    }

    protected boolean checkCollectionItemTypes(Collection collection, Class<?> itemClass) {
        for (Object collectionItem : collection) {
            if (!itemClass.isAssignableFrom(collectionItem.getClass()))
                return false;
        }
        return true;
    }

    @Override
    public CommitRequest parseCommitRequest(String content) {
        try {
            CommitRequest commitRequest = new CommitRequest();
            Document document = Dom4j.readDocument(content);
            Element rootElement = document.getRootElement();

            //commit instances
            Element commitInstancesEl = rootElement.element("commitInstances");
            if (commitInstancesEl != null) {
                //first find and register ids of all entities to be commited
                Set<String> commitIds = new HashSet<>();
                for (Object instance : commitInstancesEl.elements("instance")) {
                    Element instanceEl = (Element) instance;
                    String id = instanceEl.attributeValue("id");
                    if (id.startsWith("NEW-"))
                        id = id.substring(id.indexOf('-') + 1);
                    commitIds.add(id);
                }
                commitRequest.setCommitIds(commitIds);

                List commitInstanceElements = commitInstancesEl.elements("instance");
                List<Entity> commitInstances = new ArrayList<>();
                for (Object el : commitInstanceElements) {
                    Element commitInstanceEl = (Element) el;
                    String id = commitInstanceEl.attributeValue("id");
                    InstanceRef ref = commitRequest.parseInstanceRefAndRegister(id);
                    Entity instance = ref.getInstance();
                    parseEntity(commitInstanceEl, instance, commitRequest);
                    commitInstances.add(instance);
                }
                commitRequest.setCommitInstances(commitInstances);
            }

            //remove instances
            Element removeInstancesEl = rootElement.element("removeInstances");
            if (removeInstancesEl != null) {
                List removeInstanceElements = removeInstancesEl.elements("instance");
                List<Entity> removeInstances = new ArrayList<>();
                for (Object el : removeInstanceElements) {
                    Element removeInstance = (Element) el;
                    String id = removeInstance.attributeValue("id");
                    InstanceRef ref = commitRequest.parseInstanceRefAndRegister(id);
                    Entity instance = ref.getInstance();
                    removeInstances.add(instance);
                }
                commitRequest.setRemoveInstances(removeInstances);
            }

            //soft deletion
            Element softDeletionEl = rootElement.element("softDeletion");
            if (softDeletionEl != null) {
                commitRequest.setSoftDeletion(Boolean.parseBoolean(softDeletionEl.getText()));
            }

            return commitRequest;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServiceRequest parseServiceRequest(String content) throws Exception {
        Document document = Dom4j.readDocument(content);
        Element rootElement = document.getRootElement();
        Element serviceEl = rootElement.element("service");
        if (serviceEl == null) {
            throw new IllegalArgumentException("Service name not specified in request");
        }
        String serviceName = serviceEl.getTextTrim();
        Element methodEl = rootElement.element("method");
        if (methodEl == null) {
            throw new IllegalArgumentException("Method name not specified in request");
        }
        String methodName = methodEl.getTextTrim();
        String viewName = null;
        Element viewEl = rootElement.element("view");
        if (viewEl != null)
            viewName = viewEl.getTextTrim();

        ServiceRequest serviceRequest = new ServiceRequest(serviceName, methodName, this);
        serviceRequest.setViewName(viewName);

        Element paramsEl = rootElement.element("params");
        if (paramsEl != null) {
            int idx = 0;
            while (true) {
                String paramName = "param" + idx;
                Element paramEl = findParamByName(paramsEl, paramName);
                if (paramEl == null) break;
                serviceRequest.getParamValuesString().add(paramElementContentAsString(paramEl));

                Element paramTypeEl = findParamByName(paramsEl, paramName + "_type");
                if (paramTypeEl != null) {
                    String type = paramTypeEl.getText();
                    serviceRequest.getParamTypes().add(ClassUtils.forName(type, null));
                } else {
                    if (!serviceRequest.getParamTypes().isEmpty()) {
                        //types should be defined for all parameters or for none of them
                        throw new RestServiceException("Parameter type for param" + idx + " is not defined");
                    }
                }
                idx++;
            }
        }

        return serviceRequest;
    }

    protected Element findParamByName(Element paramsEl, String paramName) {
        List params = paramsEl.elements("param");
        for (Object param : params) {
            String curName = ((Element) param).attributeValue("name");
            if (paramName.equals(curName)) return (Element) param;
        }
        return null;
    }

    protected String paramElementContentAsString(Element paramEl) {
        Element nestedEl = paramEl.element("instance");
        if (nestedEl == null) {
            nestedEl = paramEl.element("instances");
        }

        if (nestedEl == null) {
            return paramEl.getText();
        } else {
            Document doc = DocumentHelper.createDocument(nestedEl.createCopy());
            return doc.asXML();
        }
    }

    @Override
    public Entity parseEntity(String content) {
        Document document = Dom4j.readDocument(content);
        Element instanceEl = document.getRootElement();
        return parseEntity(instanceEl, null, null);
    }

    @Override
    public Collection<? extends Entity> parseEntitiesCollection(String content, Class<? extends Collection> collectionClass) {
        try {
            Collection collection = newCollectionInstance(collectionClass);
            Document document = Dom4j.readDocument(content);
            List instances = document.getRootElement().elements("instance");
            for (Object instance : instances) {
                Entity entity = parseEntity((Element) instance, null, null);
                collection.add(entity);
            }
            return collection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Collection newCollectionInstance(Class<? extends Collection> clazz) throws IllegalAccessException, InstantiationException {
        if (!clazz.isInterface()) {
            return clazz.newInstance();
        } else {
            if (List.class.isAssignableFrom(clazz)) return new ArrayList();
            if (Set.class.isAssignableFrom(clazz)) return new HashSet();
        }
        throw new IllegalArgumentException("Collections of type" + clazz.getName() + " not supported");
    }

    /**
     * Converts a content of XML element to an entity.
     *
     * @param instanceEl element that contains entity description
     * @param entity if this parameter is not null then its fields will be filled,
     *               if it is null then new entity will be created.
     * @param commitRequest must not be null if method is called when parsing a {@code CommitRequest}.
     *                      Security permissions checks are performed based on existing/absence of this
     *                      parameter.
     */
    protected Entity parseEntity(Element instanceEl, @Nullable Entity entity, @Nullable CommitRequest commitRequest) {
        try {
            if (entity == null) {
                String id = instanceEl.attributeValue("id");
                EntityLoadInfo loadInfo = EntityLoadInfo.parse(id);

                if (loadInfo == null)
                    throw new IllegalArgumentException("XML description of entity doesn't contain valid 'id' attribute");

                entity = createEmptyInstance(loadInfo);
                entity.setValue("id", loadInfo.getId());
            }
            MetaClass metaClass = entity.getMetaClass();

            List propertyEls = instanceEl.elements();
            for (Object el : propertyEls) {
                Element propertyEl = (Element) el;
                String propertyName = propertyEl.attributeValue("name");
                MetaProperty property = metaClass.getPropertyNN(propertyName);

                if (commitRequest != null && !attrModifyPermitted(metaClass, propertyName))
                    continue;

                if (commitRequest != null && metadataTools.isTransient(property))
                    continue;

                if (Boolean.parseBoolean(propertyEl.attributeValue("null"))) {
                    entity.setValue(propertyName, null);
                    continue;
                }

                String stringValue = propertyEl.getText();
                Object value;
                switch (property.getType()) {
                    case DATATYPE:
                        value = property.getRange().asDatatype().parse(stringValue);
                        entity.setValue(propertyName, value);
                        break;
                    case ENUM:
                        value = property.getRange().asEnumeration().parse(stringValue);
                        entity.setValue(propertyName, value);
                        break;
                    case COMPOSITION:
                    case ASSOCIATION:
                        MetaClass propertyMetaClass = propertyMetaClass(property);
                        //checks if the user permitted to read and update a property
                        if (commitRequest != null && !updatePermitted(propertyMetaClass) && !readPermitted(propertyMetaClass))
                            break;

                        if (!property.getRange().getCardinality().isMany()) {
                            Element refInstanceEl = propertyEl.element("instance");
                            if (metadataTools.isEmbedded(property)) {
                                MetaClass embeddedMetaClass = property.getRange().asClass();
                                Entity embeddedEntity = embeddedMetaClass.createInstance();
                                value = parseEntity(refInstanceEl, embeddedEntity, commitRequest);
                            } else {
                                String id = refInstanceEl.attributeValue("id");

                                //reference to an entity that also a commit instance
                                //will be registered later
                                if (commitRequest != null && commitRequest.getCommitIds().contains(id)) {
                                    EntityLoadInfo loadInfo = EntityLoadInfo.parse(id);
                                    BaseUuidEntity ref = loadInfo.getMetaClass().createInstance();
                                    ref.setValue("id", loadInfo.getId());
                                    entity.setValue(propertyName, ref);
                                    break;
                                }

                                value = parseEntity(refInstanceEl, null, commitRequest);
                            }
                            entity.setValue(propertyName, value);

                        } else {
                            Class<?> propertyJavaType = property.getJavaType();
                            Collection<Object> coll;
                            if (List.class.isAssignableFrom(propertyJavaType))
                                coll = new ArrayList<>();
                            else if (Set.class.isAssignableFrom(propertyJavaType))
                                coll = new HashSet<>();
                            else
                                throw new RuntimeException("Datatype " + propertyJavaType.getName() + " of "
                                        + metaClass.getName() + "#" + property.getName() + " is not supported");
                            entity.setValue(propertyName, coll);

                            for (Object childInstenceEl : propertyEl.elements("instance")) {
                                Entity childEntity = parseEntity((Element) childInstenceEl, null, commitRequest);
                                coll.add(childEntity);
                            }
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unknown property type");
                }
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates new entity instance from {@link com.haulmont.cuba.core.global.EntityLoadInfo}
     * and reset fields values
     */
    protected Entity createEmptyInstance(EntityLoadInfo loadInfo) throws IllegalAccessException, InstantiationException {
        MetaClass metaClass = loadInfo.getMetaClass();
        Entity instance = metaClass.createInstance();
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            instance.setValue(metaProperty.getName(), null);
        }
        return instance;
    }

    protected void encodeEntity(Entity entity, View view, Element parentEl) throws Exception {
            if (entity == null) {
                parentEl.addAttribute("null", "true");
                return;
            }

            if (!readPermitted(entity.getMetaClass()))
                throw new IllegalAccessException();

            Element instanceEl = parentEl.addElement("instance");
            instanceEl.addAttribute("id", EntityLoadInfo.create(entity).toString());

            MetaClass metaClass = entity.getMetaClass();
            List<MetaProperty> orderedProperties = ConvertorHelper.getOrderedProperties(metaClass);
            for (MetaProperty property : orderedProperties) {
                if (!attrViewPermitted(metaClass, property.getName()))
                    continue;

                if (!isPropertyIncluded(view, property)) {
                    continue;
                }

                Object value = entity.getValue(property.getName());

                switch (property.getType()) {
                    case DATATYPE:
                        if (property.equals(metadataTools.getPrimaryKeyProperty(metaClass))
                                && !property.getJavaType().equals(String.class)) {
                            // skipping id for non-String-key entities
                            continue;
                        }
                        Element fieldEl = instanceEl.addElement("field");
                        fieldEl.addAttribute("name", property.getName());
                        if (value == null) {
                            encodeNull(fieldEl);
                        } else {
                            fieldEl.setText(property.getRange().asDatatype().format(value));
                        }
                        break;
                    case ENUM:
                        fieldEl = instanceEl.addElement("field");
                        fieldEl.addAttribute("name", property.getName());
                        if (value == null) {
                            encodeNull(fieldEl);
                        } else {
                            fieldEl.setText(property.getRange().asEnumeration().format(value));
                        }
                        break;
                    case COMPOSITION:
                    case ASSOCIATION:
                        MetaClass meta = propertyMetaClass(property);
                        //checks if the user permitted to read a property
                        if (!readPermitted(meta)) {
                            break;
                        }

                        View propertyView = null;
                        if (view != null) {
                            ViewProperty vp = view.getProperty(property.getName());
                            if (vp != null) propertyView = vp.getView();
                        }

                        if (!property.getRange().getCardinality().isMany()) {
                            Element referenceEl = instanceEl.addElement("reference");
                            referenceEl.addAttribute("name", property.getName());
                            encodeEntity((Entity) value, propertyView, referenceEl);
                        } else {
                            Element collectionEl = instanceEl.addElement("collection");
                            collectionEl.addAttribute("name", property.getName());

                            if (value == null) {
                                encodeNull(collectionEl);
                                break;
                            }

                            for (Object childEntity : (Collection) value) {
                                encodeEntity((Entity) childEntity, propertyView, collectionEl);
                            }
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unknown property type");
                }
            }
    }

    protected boolean attrViewPermitted(MetaClass metaClass, String property) {
        return attrPermitted(metaClass, property, EntityAttrAccess.VIEW);
    }

    protected boolean attrModifyPermitted(MetaClass metaClass, String property) {
        return attrPermitted(metaClass, property, EntityAttrAccess.MODIFY);
    }

    protected boolean attrPermitted(MetaClass metaClass, String property, EntityAttrAccess entityAttrAccess) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityAttrPermitted(metaClass, property, entityAttrAccess);
    }

    protected boolean readPermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.READ);
    }

    protected boolean updatePermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.UPDATE);
    }

    protected boolean entityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityOpPermitted(metaClass, entityOp);
    }

    protected MetaClass propertyMetaClass(MetaProperty property) {
        return property.getRange().asClass();
    }

    protected boolean isPropertyIncluded(View view, MetaProperty metaProperty) {
        if (view == null) {
            return true;
        }

        ViewProperty viewProperty = view.getProperty(metaProperty.getName());
        return (viewProperty != null || (view.isIncludeSystemProperties() && metadataTools.isSystem(metaProperty)));
    }


    protected void encodeNull(Element element) {
        element.addAttribute("null", "true");
    }

    protected String documentToString(Document document) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            StringWriter sw = new StringWriter();
            XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}