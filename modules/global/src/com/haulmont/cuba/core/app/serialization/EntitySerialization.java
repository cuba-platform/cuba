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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;

@Component(EntitySerializationAPI.NAME)
public class EntitySerialization implements EntitySerializationAPI {

    protected Logger log = LoggerFactory.getLogger(EntitySerialization.class);

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
    public String toJson(Collection<? extends Entity> entities) {
        return toJson(entities, null);
    }

    @Override
    public String toJson(Entity entity, View view) {
        context.remove();
        return toJson(Collections.singletonList(entity), view);
    }

    @Override
    public String toJson(Collection<? extends Entity> entities, View view) {
        context.remove();
        return createGson(view).toJson(entities);
    }

    @Override
    public <T extends Entity> Collection<T> fromJson(String json) {
        context.remove();
        Type collectionType = new TypeToken<Collection<? extends Entity>>(){}.getType();
        return createGson(null).fromJson(json, collectionType);
    }

    public Gson createGson(@Nullable View view) {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(Entity.class, new TypeAdapter<Entity>() {
                    @Override
                    public void write(JsonWriter out, Entity entity) throws IOException {
                        writeEntity(out, entity, view);
                    }

                    @Override
                    public Entity read(JsonReader in) throws IOException {
                        return readEntity(in);
                    }
                })
                .create();
    }

    protected void writeEntity(JsonWriter out, Entity entity, @Nullable View view) throws IOException {
        out.beginObject();
        MetaClass metaClass = entity.getMetaClass();
        if (!metadataTools.isEmbeddable(metaClass)) {
            MetaProperty primaryKeyProperty = metadataTools.getPrimaryKeyProperty(entity.getMetaClass());
            if (primaryKeyProperty == null)
                throw new EntitySerializationException("Primary key property not found for entity " + metaClass.getName());
            Datatype id = Datatypes.getNN(primaryKeyProperty.getJavaType());
            out.name("id");
            out.value(entity.getMetaClass().getName() + "-" + id.format(entity.getId()));
            Map<Object, Entity> processedObjects = context.get().getProcessedEntities();
            if (!processedObjects.containsKey(entity.getId())) {
                processedObjects.put(entity.getId(), entity);
                writeFields(out, entity, view);
            }
        } else {
            writeFields(out, entity, view);
        }
        out.endObject();
    }

    protected boolean propertyWritingAllowed(MetaProperty metaProperty, Entity entity) {
        return !"id".equalsIgnoreCase(metaProperty.getName())
                && PersistenceHelper.isLoaded(entity, metaProperty.getName());
    }

    protected void writeFields(JsonWriter out, Entity entity, @Nullable View view) throws IOException {
        for (MetaProperty metaProperty : entity.getMetaClass().getProperties()) {
            if (propertyWritingAllowed(metaProperty, entity)) {
                ViewProperty viewProperty = null;
                if (view != null) {
                    viewProperty = view.getProperty(metaProperty.getName());
                    if (viewProperty == null) continue;
                }
                Field field = getField(entity.getClass(), metaProperty.getName());
                if (field == null) {
                    log.error("Field {} for class {} not found", metaProperty.getName(), entity.getClass().getName());
                    continue;
                }
                makeFieldAccessible(field);

                Object fieldValue;
                try {
                    fieldValue = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new EntitySerializationException("Error reading a value of field " + field.getName(), e);
                }
                if (fieldValue == null) continue;

                Range propertyRange = metaProperty.getRange();
                if (propertyRange.isDatatype()) {
                    writeSimpleProperty(out, fieldValue, metaProperty);
                } else if (propertyRange.isEnum()) {
                    out.name(metaProperty.getName());
                    out.value(fieldValue.toString());
                } else if (propertyRange.isClass()) {
                    if (fieldValue instanceof Entity) {
                        out.name(metaProperty.getName());
                        writeEntity(out, (Entity) fieldValue, viewProperty != null ? viewProperty.getView() : null);
                    } else if (fieldValue instanceof Collection) {
                        out.name(metaProperty.getName());
                        writeCollection(out, (Collection) fieldValue, viewProperty != null ? viewProperty.getView() : null);
                    }
                }
            }
        }
    }

    protected void writeSimpleProperty(JsonWriter out, Object value, MetaProperty property) throws IOException {
        if (value != null) {
            out.name(property.getName());
            Datatype datatype = Datatypes.get(property.getJavaType());
            if (datatype != null) {
                out.value(datatype.format(value));
            } else {
                out.value(String.valueOf(value));
            }
        }
    }

    protected void writeCollection(JsonWriter out, Collection value, @Nullable View view) throws IOException {
        out.beginArray();
        for (Object o : value) {
            if (o instanceof Entity) {
                writeEntity(out, (Entity) o, view);
            }
        }
        out.endArray();
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

    protected Entity readEntity(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        String idValue = in.nextString();
        EntityLoadInfo entityLoadInfo = EntityLoadInfo.parse(idValue);
        if (entityLoadInfo == null) {
            throw new EntitySerializationException("Entity info " + idValue + " cannot be parsed");
        }

        MetaClass metaClass = entityLoadInfo.getMetaClass();
        Entity entity = metadata.create(metaClass);
        clearFields(entity);
        entity.setValue("id", entityLoadInfo.getId());

        Map<Object, Entity> processedEntities = context.get().getProcessedEntities();
        Entity processedEntity = processedEntities.get(entity.getId());
        if (processedEntity != null) {
            entity = processedEntity;
        } else {
            processedEntities.put(entity.getId(), entity);
            readFields(in, metaClass, entity);
        }
        in.endObject();
        return entity;
    }

    protected Entity readEmbeddedEntity(JsonReader in, Class<?> entityClass) throws IOException {
        in.beginObject();
        MetaClass metaClass = metadata.getClassNN(entityClass);
        Entity entity = metadata.create(metaClass);
        clearFields(entity);
        Map<Object, Entity> processedEntities = context.get().getProcessedEntities();
        Entity processedEntity = processedEntities.get(entity.getId());
        if (processedEntity != null) {
            entity = processedEntity;
        } else {
            processedEntities.put(entity.getId(), entity);
            readFields(in, metaClass, entity);
        }
        in.endObject();
        return entity;
    }

    private void clearFields(Entity entity) {
        for (MetaProperty metaProperty : entity.getMetaClass().getProperties()) {
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

    protected void readFields(JsonReader in, MetaClass metaClass, Entity entity) throws IOException {
        while (in.hasNext()) {
            String propertyName = in.nextName();
            MetaProperty metaProperty = metaClass.getProperty(propertyName);
            if (metaProperty != null) {
                Class<?> propertyType = metaProperty.getJavaType();
                Range propertyRange = metaProperty.getRange();
                if (propertyRange.isDatatype()) {
                    Object value = readSimpleProperty(in, propertyType);
                    entity.setValue(propertyName, value);
                } else if (propertyRange.isEnum()) {
                    String stringValue = in.nextString();
                    try {
                        Object value = propertyRange.asEnumeration().parse(stringValue);
                        entity.setValue(propertyName, value);
                    } catch (ParseException e) {
                        throw new EntitySerializationException(String.format("An error occurred while parsing enum. Class [%s]. Value [%s].", propertyType, stringValue), e);
                    }
                } else if (propertyRange.isClass()) {
                    if (Entity.class.isAssignableFrom(propertyType)) {
                        if (metadataTools.isEmbedded(metaProperty)) {
                            entity.setValue(propertyName, readEmbeddedEntity(in, propertyType));
                        } else {
                            entity.setValue(propertyName, readEntity(in));
                        }
                    } else if (Collection.class.isAssignableFrom(propertyType)) {
                        Collection entities = readCollection(in, propertyType);
                        entity.setValue(propertyName, entities);
                    } else {
                        in.skipValue();
                    }
                }
            } else {
                in.skipValue();
            }
        }
    }

    protected Object readSimpleProperty(JsonReader in, Class<?> propertyType) throws IOException {
        String value = in.nextString();
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

    @SuppressWarnings("unchecked")
    protected Collection readCollection(JsonReader in, Class<?> propertyType) throws IOException {
        Collection entities;
        if (List.class.isAssignableFrom(propertyType)) {
            entities = new ArrayList<>();
        } else if (Set.class.isAssignableFrom(propertyType)) {
            entities = new LinkedHashSet<>();
        } else {
            throw new EntitySerializationException(String.format("Could not instantiate collection with class [%s].", propertyType));
        }
        in.beginArray();
        while (in.hasNext()) {
            Entity entityForList = readEntity(in);
            entities.add(entityForList);
        }
        in.endArray();
        return entities;
    }
}