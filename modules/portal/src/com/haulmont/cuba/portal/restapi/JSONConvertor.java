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

import com.haulmont.chile.core.datatypes.impl.StringDatatype;
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

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class JSONConvertor implements Convertor {

    public static final String MIME_STR = "application/json;charset=UTF-8";
    public static final MimeType MIME_TYPE_JSON;

    static {
        try {
            MIME_TYPE_JSON = new MimeType(MIME_STR);
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
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
    public MyJSONObject process(Entity entity, MetaClass metaclass, String requestURI, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return encodeInstance(entity, new HashSet<Entity>(), metaclass, view);
    }

    @Override
    public MyJSONObject.Array process(List<Entity> entities, MetaClass metaClass, String requestURI, View view)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        MyJSONObject.Array result = new MyJSONObject.Array();
        for (Entity entity : entities) {
            MyJSON item = encodeInstance(entity, new HashSet<Entity>(), metaClass, view);
            result.add(item);
        }
        return result;
    }

    @Override
    public MyJSONObject.Array process(Set<Entity> entities, String requestURI)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        MyJSONObject.Array result = new MyJSONObject.Array();
        for (Entity entity : entities) {
            MyJSONObject entityJson = encodeInstance(entity, new HashSet<Entity>(), getMetaClass(entity), null);

            if (restConfig.getRestApiCommitReturnsMaps()) {
                MyJSONObject valueJson = encodeInstance(entity, new HashSet<Entity>(), getMetaClass(entity), null);
                MyJSONObject.Array mapping = new MyJSONObject.Array();
                mapping.add(entityJson);
                mapping.add(valueJson);
                result.add(mapping);
            } else {
                result.add(entityJson);
            }

        }
        return result;
    }

    private MetaClass getMetaClass(Entity entity) {
        return metadata.getSession().getClassNN(entity.getClass());
    }

    @Override
    public void write(HttpServletResponse response, Object o) {
        response.setContentType(MIME_STR);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(o.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
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
                    String value = json.getString(key);
                    String typeName = property.getRange().asDatatype().getName();
                    if (json.isNull(key) || (!StringDatatype.NAME.equals(typeName) && "null".equals(value)))
                            value = null;
                    setField(bean, key, property.getRange().asDatatype().parse(value));
                    break;
                case ENUM:
                    setField(bean, key, property.getRange().asEnumeration().parse(json.getString(key)));
                    break;
                case COMPOSITION:
                case ASSOCIATION:
                    if ("null".equals(json.getString(key))) {
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

    protected void setField(Object bean, String field, Object value)
            throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        new PropertyDescriptor(field, bean.getClass()).getWriteMethod().invoke(bean, value);
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

        MyJSONObject root = new MyJSONObject(idof(entity), ref);

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
}