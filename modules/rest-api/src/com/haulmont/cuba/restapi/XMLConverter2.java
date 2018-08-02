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

package com.haulmont.cuba.restapi;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SecurityState;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.activation.MimeType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.*;

/**
 * XML Converter that works with new xml schema defined in platform v5.4
 *
 */
@Component
public class XMLConverter2 implements Converter {

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

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Configuration configuration;

    protected RestConfig restConfig;

    @PostConstruct
    public void init() {
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


    protected Document _process(Entity entity, View view) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element rootEl = document.addElement("instances");
        encodeEntity(entity, new HashSet<Entity>(), view, rootEl);
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
            encodeEntity(entity, new HashSet<Entity>(), view, rootEl);
        }
        return document;
    }

    @Override
    public String process(Set<Entity> entities) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element rootEl = document.addElement("instances");
        for (Entity entity : entities) {
            encodeEntity(entity, new HashSet<Entity>(), null, rootEl);
        }
        return documentToString(document);
    }

    @Override
    @Nonnull
    public String processServiceMethodResult(Object result, Class resultType) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element resultEl = document.addElement("result");
        if (result instanceof Entity) {
            Entity entity = (Entity) result;
            Document convertedEntity = _process(entity, null);
            resultEl.add(convertedEntity.getRootElement());
        } else if (result instanceof Collection) {
            if (!checkCollectionItemTypes((Collection) result, Entity.class))
                throw new IllegalArgumentException("Items that are not instances of Entity class found in service method result");
            ArrayList list = new ArrayList((Collection) result);
            MetaClass metaClass = null;
            if (!list.isEmpty())
                metaClass = ((Entity) list.get(0)).getMetaClass();

            Document processed = _process(list, metaClass, null);
            resultEl.add(processed.getRootElement());
        } else {
            if (result != null && resultType != Void.TYPE) {
                Datatype datatype = getDatatype(resultType);
                resultEl.setText(datatype != null ? datatype.format(result) : result.toString());
            } else {
                encodeNull(resultEl);
            }
        }
        return documentToString(document);
    }

    protected Datatype getDatatype(Class clazz) {
        if (clazz == Integer.TYPE || clazz == Byte.TYPE || clazz == Short.TYPE) return Datatypes.get(Integer.class);
        if (clazz == Long.TYPE) return Datatypes.get(Long.class);
        if (clazz == Boolean.TYPE) return Datatypes.get(Boolean.class);

        return Datatypes.get(clazz);
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
                //first find and register ids of all entities to be committed
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
    public QueryRequest parseQueryRequest(String content) throws IllegalArgumentException, ClassNotFoundException, ParseException {
        Document document = Dom4j.readDocument(content);
        Element rootElement = document.getRootElement();
        QueryRequest queryRequest = new QueryRequest();

        Element entityElem = rootElement.element("entity");
        String entity = entityElem.getTextTrim();
        queryRequest.setEntity(entity);

        Element queryElem = rootElement.element("query");
        String query = queryElem.getTextTrim();
        queryRequest.setQuery(query);

        if (rootElement.element("view") != null) {
            Element viewElem = rootElement.element("view");
            String view = viewElem.getTextTrim();
            queryRequest.setViewName(view);
        }

        if (rootElement.element("max") != null) {
            Element maxElem = rootElement.element("max");
            String maxString = maxElem.getTextTrim();
            int max = Integer.parseInt(maxString);
            queryRequest.setMax(max);
        }

        if (rootElement.element("first") != null) {
            Element firstElem = rootElement.element("first");
            String firstString = firstElem.getTextTrim();
            int first = Integer.parseInt(firstString);
            queryRequest.setFirst(first);
        }

        if (rootElement.element("dynamicAttributes") != null) {
            Element dynamicAttributesElem = rootElement.element("dynamicAttributes");
            String dynamicAttributesString = dynamicAttributesElem.getTextTrim();
            Boolean dynamicAttributes = Boolean.valueOf(dynamicAttributesString);
            queryRequest.setDynamicAttributes(dynamicAttributes);
        }

        if (rootElement.element("params") != null) {
            Element paramsElem = rootElement.element("params");
            List paramList = paramsElem.elements("param");
            for (Object obj : paramList) {
                if (obj instanceof Element) {
                    Element paramElem = (Element) obj;

                    Element nameElem = paramElem.element("name");
                    String paramName = nameElem.getStringValue();

                    Element valueElem = paramElem.element("value");
                    String paramValue = valueElem.getStringValue();

                    Object value = null;
                    if (paramElem.element("type") != null) {
                        Element typeElem = paramElem.element("type");
                        String typeString = typeElem.getStringValue();
                        Class type = ClassUtils.forName(typeString, null);
                        value = ParseUtils.toObject(type, paramValue, this);
                    } else {
                        value = ParseUtils.tryParse(paramValue);
                    }

                    queryRequest.getParams().put(paramName, value != null ? value : paramValue);
                }
            }
        }

        return queryRequest;
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
     * @param instanceEl    element that contains entity description
     * @param entity        if this parameter is not null then its fields will be filled,
     *                      if it is null then new entity will be created.
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

                if (entity instanceof BaseGenericIdEntity && "__securityToken".equals(propertyEl.getName())) {
                    byte[] securityToken = Base64.getDecoder().decode(propertyEl.getText());
                    SecurityState securityState = BaseEntityInternalAccess.getOrCreateSecurityState((BaseGenericIdEntity) entity);
                    BaseEntityInternalAccess.setSecurityToken(securityState, securityToken);
                    continue;
                }

                String propertyName = propertyEl.attributeValue("name");

                MetaPropertyPath metaPropertyPath = metadata.getTools().resolveMetaPropertyPath(metaClass, propertyName);
                Preconditions.checkNotNullArgument(metaPropertyPath, "Could not resolve property '%s' in '%s'", propertyName, metaClass);
                MetaProperty property = metaPropertyPath.getMetaProperty();

                if (commitRequest != null && !attrModifyPermitted(metaClass, propertyName))
                    continue;

                if (commitRequest != null
                        && metadataTools.isNotPersistent(property)
                        && !DynamicAttributesUtils.isDynamicAttribute(propertyName))
                    continue;

                if (Boolean.parseBoolean(propertyEl.attributeValue("null"))) {
                    entity.setValue(propertyName, null);
                    continue;
                }

                if (entity instanceof BaseGenericIdEntity
                        && DynamicAttributesUtils.isDynamicAttribute(propertyName)
                        && ((BaseGenericIdEntity) entity).getDynamicAttributes() == null) {
                    ConverterHelper.fetchDynamicAttributes(entity);
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
                                Entity embeddedEntity = metadata.create(embeddedMetaClass);
                                value = parseEntity(refInstanceEl, embeddedEntity, commitRequest);
                            } else {
                                String id = refInstanceEl.attributeValue("id");

                                //reference to an entity that also a commit instance
                                //will be registered later
                                if (commitRequest != null && commitRequest.getCommitIds().contains(id)) {
                                    EntityLoadInfo loadInfo = EntityLoadInfo.parse(id);
                                    Entity ref = metadata.create(loadInfo.getMetaClass());
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

                            for (Object childInstanceEl : propertyEl.elements("instance")) {
                                Entity childEntity = parseEntity((Element) childInstanceEl, null, commitRequest);
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
        Entity instance = metadata.create(metaClass);
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (!metaProperty.isReadOnly())
                instance.setValue(metaProperty.getName(), null);
        }
        return instance;
    }

    protected void encodeEntity(Entity entity, HashSet<Entity> visited, View view, Element parentEl) throws Exception {
        if (entity == null) {
            parentEl.addAttribute("null", "true");
            return;
        }

        if (!readPermitted(entity.getMetaClass()))
            throw new IllegalAccessException();

        Element instanceEl = parentEl.addElement("instance");
        instanceEl.addAttribute("id", EntityLoadInfo.create(entity).toString());

        boolean entityAlreadyVisited = !visited.add(entity);
        if (entityAlreadyVisited) {
            return;
        }

        if (entity instanceof BaseGenericIdEntity) {
            byte[] securityToken = BaseEntityInternalAccess.getSecurityToken((BaseGenericIdEntity) entity);

            if (securityToken != null) {
                BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;
                instanceEl.addElement("__securityToken").setText(Base64.getEncoder().encodeToString(securityToken));

                String[] filteredAttributes = BaseEntityInternalAccess.getFilteredAttributes(baseGenericIdEntity);

                if (filteredAttributes != null) {
                    Element filteredAttributesElement = instanceEl.addElement("__filteredAttributes");
                    Arrays.stream(filteredAttributes)
                            .forEach(obj -> filteredAttributesElement.addElement("a").setText(obj));
                }
            }
        }

        MetaClass metaClass = entity.getMetaClass();
        List<MetaProperty> orderedProperties = ConverterHelper.getActualMetaProperties(metaClass, entity);
        for (MetaProperty property : orderedProperties) {
            if (metadataTools.isPersistent(property) && !PersistenceHelper.isLoaded(entity, property.getName())) {
                continue;
            }

            if (!attrViewPermitted(metaClass, property.getName()))
                continue;

            if (!isPropertyIncluded(view, property)
                    && !DynamicAttributesUtils.isDynamicAttribute(property.getName())) {
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
                    if (value != null) {
                        fieldEl.setText(property.getRange().asDatatype().format(value));
                    } else if (!DynamicAttributesUtils.isDynamicAttribute(property.getName())) {
                        encodeNull(fieldEl);
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
                        encodeEntity((Entity) value, visited, propertyView, referenceEl);
                    } else {
                        Element collectionEl = instanceEl.addElement("collection");
                        collectionEl.addAttribute("name", property.getName());

                        if (value == null) {
                            encodeNull(collectionEl);
                            break;
                        }

                        for (Object childEntity : (Collection) value) {
                            encodeEntity((Entity) childEntity, visited, propertyView, collectionEl);
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
        return (viewProperty != null);
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