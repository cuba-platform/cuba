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

package com.haulmont.cuba.core.app.serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;

@Component(EntitySerializationAPI.NAME)
public class EntitySerialization implements EntitySerializationAPI {

    protected Logger log = LoggerFactory.getLogger(EntitySerialization.class);

    protected static final String ENTITY_NAME_PROP = "_entityName";
    protected static final String INSTANCE_NAME_PROP = "_instanceName";

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Metadata metadata;

    protected ThreadLocal<EntitySerializationContext> context = new ThreadLocal<EntitySerializationContext>() {
        @Override
        protected EntitySerializationContext initialValue() {
            return new EntitySerializationContext();
        }
    };

    /**
     * Class is used for storing a collection of entities already processed during the serialization.
     */
    protected class EntitySerializationContext {
        protected Map<Object, Entity> processedEntities = new HashMap<>();

        protected Map<Object, Entity> getProcessedEntities() {
            return processedEntities;
        }
    }

    @Override
    public String toJson(Entity entity) {
        return toJson(entity, null);
    }

    @Override
    public String toJson(Entity entity,
                         @Nullable View view,
                         EntitySerializationOption... options) {
        context.remove();
        return createGsonForSerialization(view, options).toJson(entity);
    }

    @Override
    public String toJson(Collection<? extends Entity> entities) {
        return toJson(entities, null);
    }

    @Override
    public String toJson(Collection<? extends Entity> entities,
                         @Nullable View view,
                         EntitySerializationOption... options) {
        context.remove();
        return createGsonForSerialization(view, options).toJson(entities);
    }

    @Override
    public Entity entityFromJson(String json,
                                 @Nullable MetaClass metaClass,
                                 EntitySerializationOption... options) {
        context.remove();
        return createGsonForDeserialization(metaClass, options).fromJson(json, Entity.class);
    }

    @Override
    public <T extends Entity> Collection<T> entitiesCollectionFromJson(String json,
                                                                       @Nullable MetaClass metaClass,
                                                                       EntitySerializationOption... options) {
        context.remove();
        Type collectionType = new TypeToken<Collection<Entity>>(){}.getType();
        return createGsonForDeserialization(metaClass, options).fromJson(json, collectionType);
    }

    protected Gson createGsonForSerialization(@Nullable View view, EntitySerializationOption... options) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                .registerTypeHierarchyAdapter(Entity.class, new EntitySerializer(view, options))
                .create();
        if (ArrayUtils.contains(options, EntitySerializationOption.SERIALIZE_NULLS)) {
            gsonBuilder.serializeNulls();
        }
        return gsonBuilder.create();
    }

    protected Gson createGsonForDeserialization(@Nullable MetaClass metaClass, EntitySerializationOption... options) {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(Entity.class, new EntityDeserializer(metaClass, options))
                .create();
    }

    @Nullable
    protected Field getField(@Nullable Class clazz, String fieldName) {
        try {
            if (clazz != null) {
                return clazz.getDeclaredField(fieldName);
            }
        } catch (NoSuchFieldException ex) {
            return getField(clazz.getSuperclass(), fieldName);
        }
        return null;
    }

    protected void makeFieldAccessible(Field field) {
        if (field != null && !Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    protected class EntitySerializer implements JsonSerializer<Entity> {

        protected boolean complexIdFormat;
        protected boolean compactRepeatedEntities = false;
        protected boolean serializeInstanceName;
        protected View view;

        public EntitySerializer(@Nullable View view, EntitySerializationOption... options) {
            this.view = view;
            if (options != null) {
                for (EntitySerializationOption option : options) {
                    if (option == EntitySerializationOption.COMPLEX_ID_FORMAT)
                        complexIdFormat = true;
                    if (option == EntitySerializationOption.COMPACT_REPEATED_ENTITIES)
                        compactRepeatedEntities = true;
                    if (option == EntitySerializationOption.SERIALIZE_INSTANCE_NAME)
                        serializeInstanceName = true;
                }
            }
        }

        @Override
        public JsonElement serialize(Entity entity, Type typeOfSrc, JsonSerializationContext context) {
            return serializeEntity(entity, view, new HashSet<>());
        }

        protected JsonObject serializeEntity(Entity entity, @Nullable View view, Set<Entity> cyclicReferences) {
            JsonObject jsonObject = new JsonObject();
            MetaClass metaClass = entity.getMetaClass();
            if (!metadataTools.isEmbeddable(metaClass)) {
                if (!complexIdFormat) {
                    jsonObject.addProperty(ENTITY_NAME_PROP, metaClass.getName());
                }
                if (serializeInstanceName) {
                    jsonObject.addProperty(INSTANCE_NAME_PROP, entity.getInstanceName());
                }
                writeIdField(entity, jsonObject);
                if (compactRepeatedEntities) {
                    Map<Object, Entity> processedObjects = context.get().getProcessedEntities();
                    if (!processedObjects.containsKey(entity.getId())) {
                        processedObjects.put(entity.getId(), entity);
                        writeFields(entity, jsonObject, view, cyclicReferences);
                    }
                } else {
                    if (!cyclicReferences.contains(entity)) {
                        cyclicReferences.add(entity);
                        writeFields(entity, jsonObject, view, cyclicReferences);
                    }
                }
            } else {
                writeFields(entity, jsonObject, view, cyclicReferences);
            }
            return jsonObject;
        }

        protected void writeIdField(Entity entity, JsonObject jsonObject) {
            MetaProperty primaryKeyProperty = metadataTools.getPrimaryKeyProperty(entity.getMetaClass());
            if (primaryKeyProperty == null)
                throw new EntitySerializationException("Primary key property not found for entity " + entity.getMetaClass());
            Datatype idDatatype = Datatypes.getNN(primaryKeyProperty.getJavaType());
            String idValue = complexIdFormat ? entity.getMetaClass().getName() + "-" + idDatatype.format(entity.getId()) : idDatatype.format(entity.getId());
            jsonObject.addProperty("id", idValue);
        }

        protected boolean propertyWritingAllowed(MetaProperty metaProperty, Entity entity) {
            return !"id".equals(metaProperty.getName()) && PersistenceHelper.isLoaded(entity, metaProperty.getName());
        }

        protected void writeFields(Entity entity, JsonObject jsonObject, @Nullable View view, Set<Entity> cyclicReferences) {
            for (MetaProperty metaProperty : entity.getMetaClass().getProperties()) {
                if (propertyWritingAllowed(metaProperty, entity)) {
                    ViewProperty viewProperty = null;
                    if (view != null) {
                        viewProperty = view.getProperty(metaProperty.getName());
                        if (viewProperty == null) continue;
                    }

                    if (!PersistenceHelper.isNew(entity)
                            && !PersistenceHelper.isLoaded(entity, metaProperty.getName())) {
                        continue;
                    }

                    Object fieldValue = entity.getValue(metaProperty.getName());

                    //always write nulls here. GSON will not serialize them to the result if
                    //EntitySerializationOptions.SERIALIZE_NULLS was not set.
                    if (fieldValue == null) {
                        jsonObject.add(metaProperty.getName(), null);
                        continue;
                    }

                    Range propertyRange = metaProperty.getRange();
                    if (propertyRange.isDatatype()) {
                        writeSimpleProperty(jsonObject, fieldValue, metaProperty);
                    } else if (propertyRange.isEnum()) {
                        jsonObject.addProperty(metaProperty.getName(), fieldValue.toString());
                    } else if (propertyRange.isClass()) {
                        if (fieldValue instanceof Entity) {
                            JsonObject propertyJsonObject = serializeEntity((Entity) fieldValue,
                                    viewProperty != null ? viewProperty.getView() : null,
                                    new HashSet<>(cyclicReferences));
                            jsonObject.add(metaProperty.getName(), propertyJsonObject);
                        } else if (fieldValue instanceof Collection) {
                            JsonArray jsonArray = serializeCollection((Collection) fieldValue,
                                    viewProperty != null ? viewProperty.getView() : null,
                                    new HashSet<>(cyclicReferences));
                            jsonObject.add(metaProperty.getName(), jsonArray);
                        }
                    }
                }
            }
        }

        protected void writeSimpleProperty(JsonObject jsonObject, @NotNull Object fieldValue, MetaProperty property) {
            String propertyName = property.getName();
            if (fieldValue instanceof Number) {
                jsonObject.addProperty(propertyName, (Number) fieldValue);
            } else if (fieldValue instanceof Boolean) {
                jsonObject.addProperty(propertyName, (Boolean) fieldValue);
            } else {
                Datatype datatype = Datatypes.get(property.getJavaType());
                if (datatype != null) {
                    jsonObject.addProperty(propertyName, datatype.format(fieldValue));
                } else {
                    jsonObject.addProperty(propertyName, String.valueOf(fieldValue));
                }
            }
        }

        protected JsonArray serializeCollection(Collection value, @Nullable View view, Set<Entity> cyclicReferences) {
            JsonArray jsonArray = new JsonArray();
            value.stream()
                    .filter(e -> e instanceof Entity)
                    .forEach(e -> {
                        JsonObject jsonObject = serializeEntity((Entity) e, view, cyclicReferences);
                        jsonArray.add(jsonObject);
                    });
            return jsonArray;
        }
    }

    protected class EntityDeserializer implements JsonDeserializer<Entity> {

        protected boolean complexIdFormat = false;
        protected MetaClass metaClass;

        public EntityDeserializer(MetaClass metaClass, EntitySerializationOption... options) {
            this.metaClass = metaClass;
            if (options != null) {
                for (EntitySerializationOption option : options) {
                    if (option == EntitySerializationOption.COMPLEX_ID_FORMAT)
                        complexIdFormat = true;
                }
            }
        }

        @Override
        public Entity deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return readEntity(jsonElement.getAsJsonObject(), metaClass);
        }

        protected Entity readEntity(JsonObject jsonObject, @Nullable MetaClass metaClass) {
            Entity entity;
            JsonPrimitive idPrimitive = jsonObject.getAsJsonPrimitive("id");
            if (complexIdFormat) {
                if (idPrimitive == null) {
                    throw new EntitySerializationException("id property not found in json");
                }
                String idValue = idPrimitive.getAsString();
                EntityLoadInfo entityLoadInfo = EntityLoadInfo.parse(idValue);
                if (entityLoadInfo == null) {
                    throw new EntitySerializationException("Entity info " + idValue + " cannot be parsed");
                }
                entity = metadata.create(entityLoadInfo.getMetaClass());
                clearFields(entity);
                entity.setValue("id", entityLoadInfo.getId());
            } else {
                MetaClass resultMetaClass = metaClass;
                JsonPrimitive entityNameJsonPrimitive = jsonObject.getAsJsonPrimitive(ENTITY_NAME_PROP);
                if (entityNameJsonPrimitive != null) {
                    String entityName = entityNameJsonPrimitive.getAsString();
                    resultMetaClass = metadata.getClass(entityName);
                }

                if (resultMetaClass == null) {
                    throw new EntitySerializationException("Cannot deserialize an entity. MetaClass is not defined");
                }

                entity = metadata.create(resultMetaClass);
                clearFields(entity);
                if (idPrimitive != null) {
                    String idString = idPrimitive.getAsString();
                    MetaProperty primaryKeyProperty = metadataTools.getPrimaryKeyProperty(resultMetaClass);
                    if (primaryKeyProperty == null)
                        throw new EntitySerializationException("Primary key property not found for entity " + resultMetaClass);
                    Datatype idDatatype = Datatypes.getNN(primaryKeyProperty.getJavaType());
                    try {
                        entity.setValue("id", idDatatype.parse(idString));
                    } catch (ParseException e) {
                        throw new EntitySerializationException(e);
                    }
                }
            }

            readFields(jsonObject, entity);
            return entity;
        }

        protected boolean propertyReadRequired(String propertyName) {
            return !"id".equals(propertyName) && !ENTITY_NAME_PROP.equals(propertyName);
        }

        protected void readFields(JsonObject jsonObject, Entity entity) {
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String propertyName = entry.getKey();
                if (!propertyReadRequired(propertyName)) continue;
                JsonElement propertyValue = entry.getValue();
                MetaProperty metaProperty = entity.getMetaClass().getProperty(propertyName);
                if (metaProperty != null) {
                    if (propertyValue.isJsonNull()) {
                        entity.setValue(propertyName, null);
                        continue;
                    }
                    Class<?> propertyType = metaProperty.getJavaType();
                    Range propertyRange = metaProperty.getRange();
                    if (propertyRange.isDatatype()) {
                        Object value = readSimpleProperty(propertyValue, propertyType);
                        entity.setValue(propertyName, value);
                    } else if (propertyRange.isEnum()) {
                        String stringValue = propertyValue.getAsString();
                        try {
                            Enum enumValue = Enum.valueOf((Class<Enum>) propertyType, stringValue);
                            entity.setValue(propertyName, enumValue);
                        } catch (Exception e) {
                            log.error(String.format("An error occurred while parsing enum. Class [%s]. Value [%s].", propertyType, stringValue), e);
                        }
                    } else if (propertyRange.isClass()) {
                        if (Entity.class.isAssignableFrom(propertyType)) {
                            if (metadataTools.isEmbedded(metaProperty)) {
                                entity.setValue(propertyName, readEmbeddedEntity(propertyValue.getAsJsonObject(), metaProperty));
                            } else {
                                entity.setValue(propertyName, readEntity(propertyValue.getAsJsonObject(), propertyRange.asClass()));
                            }
                        } else if (Collection.class.isAssignableFrom(propertyType)) {
                            Collection entities = readCollection(propertyValue.getAsJsonArray(), metaProperty);
                            entity.setValue(propertyName, entities);
                        }
                    }
                } else {
                    log.warn("Entity {} doesn't contain a '{}' property", entity.getMetaClass().getName(), propertyName);
                }
            }

        }

        protected Object readSimpleProperty(JsonElement valueElement, Class<?> propertyType) {
            String value = valueElement.getAsString();
            Object parsedValue = null;
            try {
                Datatype<?> datatype = Datatypes.get(propertyType);
                if (datatype != null) {
                    parsedValue = datatype.parse(value);
                }
                return parsedValue;
            } catch (ParseException e) {
                throw new EntitySerializationException(String.format("An error occurred while parsing property. Class [%s]. Value [%s].", propertyType, value), e);
            }
        }

        protected Entity readEmbeddedEntity(JsonObject jsonObject, MetaProperty metaProperty) {
            MetaClass metaClass = metaProperty.getRange().asClass();
            Entity entity = metadata.create(metaClass);
            clearFields(entity);
            readFields(jsonObject, entity);
            return entity;
        }

        protected Collection readCollection(JsonArray jsonArray, MetaProperty metaProperty) {
            Collection<Entity> entities;
            Class<?> propertyType = metaProperty.getJavaType();
            if (List.class.isAssignableFrom(propertyType)) {
                entities = new ArrayList<>();
            } else if (Set.class.isAssignableFrom(propertyType)) {
                entities = new LinkedHashSet<>();
            } else {
                throw new EntitySerializationException(String.format("Could not instantiate collection with class [%s].", propertyType));
            }

            jsonArray.forEach(jsonElement -> {
                Entity entityForList = readEntity(jsonElement.getAsJsonObject(), metaProperty.getRange().asClass());
                entities.add(entityForList);
            });
            return entities;
        }

        protected void clearFields(Entity entity) {
            for (MetaProperty metaProperty : entity.getMetaClass().getProperties()) {
                if ("id".equals(metaProperty.getName())) continue;
                Field field = getField(entity.getClass(), metaProperty.getName());
                if (field != null) {
                    makeFieldAccessible(field);
                    try {
                        field.set(entity, null);
                    } catch (IllegalAccessException e) {
                        throw new EntitySerializationException("Can't get access to field " + field.getName() + " of class " + entity.getClass().getName(), e);
                    }
                }
            }
        }
    }

}