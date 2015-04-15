/*
 * Based on JEST, part of the OpenJPA framework.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.portal.config.RestConfig;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.ClassUtils;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class JSONConvertor implements Convertor {

    public static final String MIME_STR = "application/json;charset=UTF-8";
    public static final String TYPE_JSON = "json";
    public static final MimeType MIME_TYPE_JSON;

    static {
        try {
            MIME_TYPE_JSON = new MimeType(MIME_STR);
        } catch (MimeTypeParseException e) {
            throw new RuntimeException("Unable to initialize JSON mime type", e);
        }
    }

    protected final Metadata metadata;

    protected final RestConfig restConfig;

    public JSONConvertor() {
        metadata = AppBeans.get(Metadata.NAME);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        restConfig = configuration.getConfig(RestConfig.class);
    }

    @Override
    public MimeType getMimeType() {
        return MIME_TYPE_JSON;
    }

    @Override
    public String getType() {
        return TYPE_JSON;
    }

    @Override
    public String process(Entity entity, MetaClass metaclass, View view) throws Exception {
        MyJSONObject jsonObject = encodeInstance(entity, new HashSet<Entity>(), metaclass, view);
        return jsonObject.toString();
    }

    protected MyJSONObject _process(Entity entity) throws Exception {
        return encodeInstance(entity, new HashSet<Entity>(), entity.getMetaClass(), null);
    }

    @Override
    public String process(List<Entity> entities, MetaClass metaClass, View view)  throws Exception {
        MyJSONObject.Array array = _process(entities, metaClass, view);
        return array.toString();
    }

    protected MyJSONObject.Array _process(List<Entity> entities, MetaClass metaClass, View view) throws Exception {
        MyJSONObject.Array result = new MyJSONObject.Array();
        for (Entity entity : entities) {
            MyJSON item = encodeInstance(entity, new HashSet<Entity>(), metaClass, view);
            result.add(item);
        }
        return result;
    }

    @Override
    public String process(Set<Entity> entities) throws Exception {
        MyJSONObject.Array result = new MyJSONObject.Array();
        for (Entity entity : entities) {
            MyJSONObject entityJson = encodeInstance(entity, new HashSet<Entity>(), getMetaClass(entity), null);

            if (restConfig.getRestApiVersion() == 1) {
                //return map for old version of rest api
                MyJSONObject valueJson = encodeInstance(entity, new HashSet<Entity>(), getMetaClass(entity), null);
                MyJSONObject.Array mapping = new MyJSONObject.Array();
                mapping.add(entityJson);
                mapping.add(valueJson);
                result.add(mapping);
            } else {
                result.add(entityJson);
            }

        }
        return result.toString();
    }

    @Override
    public String processServiceMethodResult(Object result, Class resultType) throws Exception {
        MyJSONObject root = new MyJSONObject();
        if (result instanceof Entity) {
            Entity entity = (Entity) result;
            MyJSONObject entityObject = _process(entity);
            root.set("result", entityObject);
        } else if (result instanceof Collection) {
            if (!checkCollectionItemTypes((Collection) result, Entity.class))
                throw new IllegalArgumentException("Items that are not instances of Entity found in service method result");
            //noinspection unchecked
            ArrayList list = new ArrayList((Collection) result);
            MetaClass metaClass;
            if (!list.isEmpty())
                metaClass = ((Entity) list.get(0)).getMetaClass();
            else
                metaClass = AppBeans.get(Metadata.class).getClasses().iterator().next();
            MyJSONObject.Array processed = _process(list, metaClass, null);
            root.set("result", processed);
        } else {
            if (result != null && resultType != Void.TYPE) {
                Datatype datatype = getDatatype(resultType);
                root.set("result", datatype != null ? datatype.format(result) : result.toString());
            } else {
                root.set("result", null);
            }
        }
        return root.toString();
    }

    protected Datatype getDatatype(Class clazz) {
        if (clazz == Integer.TYPE  || clazz == Byte.TYPE || clazz == Short.TYPE) return Datatypes.get(Integer.class);
        if (clazz == Long.TYPE) return Datatypes.get(Long.class);
        if (clazz == Boolean.TYPE) return Datatypes.get(Boolean.class);

        return Datatypes.get(clazz);
    }

    /**
     * Checks that all collection items are instances of given class
     */
    protected boolean checkCollectionItemTypes(Collection collection, Class<?> itemClass) {
        for (Object collectionItem : collection) {
            if (!itemClass.isAssignableFrom(collectionItem.getClass()))
                return false;
        }
        return true;
    }

    private MetaClass getMetaClass(Entity entity) {
        return metadata.getSession().getClassNN(entity.getClass());
    }

    @Override
    public CommitRequest parseCommitRequest(String content) {
        try {
            JSONObject jsonContent = new JSONObject(content);
            CommitRequest result = new CommitRequest();

            if (jsonContent.has("commitInstances")) {
                JSONArray entitiesNodeList = jsonContent.getJSONArray("commitInstances");

                Set<String> commitIds = new HashSet<>(entitiesNodeList.length());
                for (int i = 0; i < entitiesNodeList.length(); i++) {
                    String id = entitiesNodeList.getJSONObject(i).getString("id");
                    if (id.startsWith("NEW-"))
                        id = id.substring(id.indexOf('-') + 1);
                    commitIds.add(id);
                }

                result.setCommitIds(commitIds);
                result.setCommitInstances(parseIntoList(result, entitiesNodeList));
            }

            if (jsonContent.has("removeInstances")) {
                JSONArray entitiesNodeList = jsonContent.getJSONArray("removeInstances");
                result.setRemoveInstances(parseIntoList(result, entitiesNodeList));
            }

            if (jsonContent.has("softDeletion")) {
                result.setSoftDeletion(jsonContent.getBoolean("softDeletion"));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse commit request", e);
        }
    }

    protected List<Entity> parseIntoList(CommitRequest commitRequest, JSONArray nodeList)
            throws JSONException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IntrospectionException, ParseException {
        List<Entity> result = new ArrayList<>(nodeList.length());

        for (int j = 0; j < nodeList.length(); j++) {
            JSONObject jsonObject = nodeList.getJSONObject(j);
            InstanceRef ref = commitRequest.parseInstanceRefAndRegister(jsonObject.getString("id"));
            MetaClass metaClass = ref.getMetaClass();
            Entity instance = ref.getInstance();
            asJavaTree(commitRequest, instance, metaClass, jsonObject);
            result.add(instance);
        }
        return result;
    }

    protected void asJavaTree(CommitRequest commitRequest, Object bean, MetaClass metaClass, JSONObject json)
            throws JSONException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, IntrospectionException, ParseException {
        Iterator iter = json.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();

            if ("id".equals(key)) {
                // id was parsed already
                continue;
            }

            //version is readonly property
            if ("version".equals(key))
                continue;

            MetaProperty property = metaClass.getPropertyNN(key);

            if (!attrModifyPermitted(metaClass, property.getName()))
                continue;

            if (json.get(key) == null) {
                setField(bean, key, new Object[]{null});
                continue;
            }

            switch (property.getType()) {
                case DATATYPE:
                    String value = null;
                    if (!json.isNull(key)) {
                        value = json.get(key).toString();
                    }

                    setField(bean, key, property.getRange().asDatatype().parse(value));
                    break;
                case ENUM:
                    setField(bean, key, property.getRange().asEnumeration().parse(json.getString(key)));
                    break;
                case COMPOSITION:
                case ASSOCIATION:
                    if ("null".equals(json.get(key).toString())) {
                        setField(bean, key, null);
                        break;
                    }
                    MetaClass propertyMetaClass = propertyMetaClass(property);
                    //checks if the user permitted to read and update a property
                    if (!updatePermitted(propertyMetaClass) && !readPermitted(propertyMetaClass))
                        break;

                    if (!property.getRange().getCardinality().isMany()) {
                        JSONObject jsonChild = json.getJSONObject(key);
                        Object child;
                        MetaClass childMetaClass;

                        if (jsonChild.has("id")) {
                            String id = jsonChild.getString("id");

                            //reference to an entity that also a commit instance
                            //will be registered later
                            if (commitRequest.getCommitIds().contains(id)) {
                                EntityLoadInfo loadInfo = EntityLoadInfo.parse(id);
                                if (loadInfo == null)
                                    throw new IllegalArgumentException("Unable to parse ID: " + id);
                                BaseUuidEntity ref = loadInfo.getMetaClass().createInstance();
                                ref.setValue("id", loadInfo.getId());
                                setField(bean, key, ref);
                                break;
                            }

                            InstanceRef ref = commitRequest.parseInstanceRefAndRegister(id);
                            childMetaClass = ref.getMetaClass();
                            child = ref.getInstance();
                        } else {
                            childMetaClass = property.getRange().asClass();
                            child = childMetaClass.createInstance();
                        }
                        asJavaTree(commitRequest, child, childMetaClass, jsonChild);
                        setField(bean, key, child);
                    } else {
                        JSONArray jsonArray = json.getJSONArray(key);
                        Collection<Object> coll = property.getRange().isOrdered()
                                ? new ArrayList<>()
                                : new HashSet<>();
                        setField(bean, key, coll);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Object arrayValue = jsonArray.get(i);
                            if (arrayValue == null)
                                coll.add(null);
                            else {
                                //assuming no simple type here
                                JSONObject jsonChild = (JSONObject) arrayValue;
                                InstanceRef ref = commitRequest.parseInstanceRefAndRegister(jsonChild.getString("id"));
                                Object child = ref.getInstance();
                                coll.add(child);
                                asJavaTree(commitRequest, child, ref.getMetaClass(), jsonChild);
                            }
                        }
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown property type");
            }
        }
    }

    @Override
    public ServiceRequest parseServiceRequest(String content) throws Exception {
        JSONObject jsonObject = new JSONObject(content);
        String serviceName = jsonObject.getString("service");
        String methodName = jsonObject.getString("method");

        ServiceRequest serviceRequest = new ServiceRequest(serviceName, methodName, this);

        JSONObject params = jsonObject.optJSONObject("params");
        if (params != null) {
            int idx = 0;
            while (true) {
                if (!params.has("param" + idx)) break;

                // we need strings for objects
                String param = params.get("param" + idx).toString();
                serviceRequest.getParamValuesString().add(param);

                if (params.has("param" + idx + "_type")) {
                    String type = params.optString("param" + idx + "_type");
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

    @Override
    public Entity parseEntity(String content) {
        try {
            JSONObject json = new JSONObject(content);
            return _parseEntity(json);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse entity", e);
        }
    }

    protected Entity _parseEntity(JSONObject json) {
        try {
            String id = json.getString("id");
            EntityLoadInfo loadInfo = EntityLoadInfo.parse(id);

            if (loadInfo == null)
                throw new IllegalArgumentException("JSON description of entity doesn't contain valid 'id' attribute");

            Entity instance = createEmptyInstance(loadInfo);
            setField(instance, "id", loadInfo.getId());

            MetaClass metaClass = loadInfo.getMetaClass();
            Iterator iter = json.keys();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                if ("id".equals(key)) {
                    // id was parsed already
                    continue;
                }

                MetaProperty property = metaClass.getPropertyNN(key);
                switch (property.getType()) {
                    case DATATYPE:
                        String value = null;
                        if (!json.isNull(key)) {
                            value = json.get(key).toString();
                        }
                        setField(instance, key, property.getRange().asDatatype().parse(value));
                        break;
                    case ENUM:
                        setField(instance, key, property.getRange().asEnumeration().parse(json.getString(key)));
                        break;
                    case COMPOSITION:
                    case ASSOCIATION:
                        if ("null".equals(json.get(key).toString())) {
                            setField(instance, key, null);
                            break;
                        }

                        if (!property.getRange().getCardinality().isMany()) {
                            JSONObject jsonChild = json.getJSONObject(key);
                            Object childInstance = _parseEntity(jsonChild);
                            setField(instance, key, childInstance);
                        } else {
                            JSONArray jsonArray = json.getJSONArray(key);
                            Class<?> propertyJavaType = property.getJavaType();
                            Collection<Object> coll;
                            if (List.class.isAssignableFrom(propertyJavaType))
                                coll = new ArrayList<>();
                            else if (Set.class.isAssignableFrom(propertyJavaType))
                                coll = new HashSet<>();
                            else
                                throw new RuntimeException("Datatype " + propertyJavaType.getName() + " of "
                                        + metaClass.getName() + "#" + property.getName() + " is not supported");
                            setField(instance, key, coll);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Object arrayValue = jsonArray.get(i);
                                if (arrayValue == null)
                                    coll.add(null);
                                else {
                                    //assuming no simple type here
                                    JSONObject jsonChild = (JSONObject) arrayValue;
                                    Object child = _parseEntity(jsonChild);
                                    coll.add(child);
                                }
                            }
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unknown property type");
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse entity", e);
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

    protected void setField(Object bean, String field, Object value)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        new PropertyDescriptor(field, bean.getClass()).getWriteMethod().invoke(bean, value);
    }

    @Override
    public Collection<? extends Entity> parseEntitiesCollection(String content, Class<? extends Collection> collectionClass) {
        try {
            @SuppressWarnings("unchecked")
            Collection<Entity> collection = newCollectionInstance(collectionClass);
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject instanceObj = jsonArray.getJSONObject(i);
                Entity entity = _parseEntity(instanceObj);
                collection.add(entity);
            }
            return collection;
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse entities collection", e);
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
     * Encodes the closure of a persistent instance into JSON.
     *
     * @param entity  the managed instance to be encoded. Can be null.
     * @param visited the persistent instances that had been encoded already. Must not be null or immutable.
     * @param view view on which loaded the entity
     * @return the new element. The element has been appended as a child to the given parent in this method.
     */
    protected MyJSONObject encodeInstance(final Entity entity, final Set<Entity> visited, MetaClass metaClass, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (visited == null) {
            throw new IllegalArgumentException("null closure for encoder");
        }
        if (entity == null) {
            return null;
        }

        boolean ref = !visited.add(entity);

        MyJSONObject root = new MyJSONObject(idof(entity), false);

        if (ref) {
            return root;
        }

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        List<MetaProperty> properties = ConvertorHelper.getOrderedProperties(metaClass);
        for (MetaProperty property : properties) {

            if (!attrViewPermitted(metaClass, property.getName()))
                continue;

            Object value = entity.getValue(property.getName());

            if (property.equals(metadataTools.getPrimaryKeyProperty(metaClass))
                    && !property.getJavaType().equals(String.class)) {
                // skipping id for non-String-key entities
                continue;
            }

            if (!isPropertyIncluded(view, property, metadataTools)){
                continue;
            }

            switch (property.getType()) {
                case DATATYPE:
                    if (value != null) {
                        root.set(property.getName(), property.getRange().asDatatype().format(value));
                    } else {
                        root.set(property.getName(), null);
                    }

                    break;
                case ENUM:
                    if (value != null) {
                        //noinspection unchecked
                        root.set(property.getName(), property.getRange().asEnumeration().format(value));
                    } else {
                        root.set(property.getName(), null);
                    }
                    break;
                case COMPOSITION:
                case ASSOCIATION: {
                    MetaClass meta = propertyMetaClass(property);
                    //checks if the user permitted to read a property's entity
                    if (!readPermitted(meta))
                        break;

                    View propertyView = (view == null ? null : view.getProperty(property.getName()).getView());

                    if (!property.getRange().getCardinality().isMany()) {
                        if (value == null) {
                            root.set(property.getName(), null);
                        } else {
                            root.set(property.getName(), encodeInstance((Entity) value, visited,
                                    property.getRange().asClass(), propertyView));
                        }
                    } else {
                        if (value == null) {
                            root.set(property.getName(), null);
                            break;
                        }

                        MyJSONObject.Array array = new MyJSONObject.Array();
                        root.set(property.getName(), array);

                        Collection<?> members = (Collection<?>) value;
                        for (Object o : members) {
                            if (o == null) {
                                array.add(null);
                            } else {
                                array.add(encodeInstance((Entity) o, visited,
                                        property.getRange().asClass(), propertyView));
                            }
                        }
                    }

                    break;
                }
                default:
                    throw new IllegalStateException("Unknown property type");
            }
        }

        return root;
    }

    protected String idof(Entity entity) {
        return EntityLoadInfo.create(entity).toString();
    }

    protected boolean isPropertyIncluded(View view, MetaProperty metaProperty, MetadataTools metadataTools) {
        if (view == null) {
            return true;
        }

        ViewProperty viewProperty = view.getProperty(metaProperty.getName());
        return (viewProperty != null || (view.isIncludeSystemProperties() && metadataTools.isSystem(metaProperty)));
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

    @Override
    public List<Integer> getApiVersions() {
        return Arrays.asList(1, 2);
    }
}