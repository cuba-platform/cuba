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

import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.SessionImpl;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.global.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component(Metadata.NAME)
public class MetadataImpl implements Metadata {

    private final Logger log = LoggerFactory.getLogger(MetadataImpl.class);

    protected Session session;

    @Inject
    protected DatatypeRegistry datatypeRegistry;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected MetadataTools tools;

    @Inject
    protected Resources resources;

    @Inject
    protected NumberIdSource numberIdSource;

    @Inject
    protected ApplicationContext applicationContext;

    protected List<String> rootPackages = new ArrayList<>();

    @EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
    protected void initMetadata() {
        if (session != null)
            return;

        log.info("Initializing metadata");
        long startTime = System.currentTimeMillis();

        MetadataLoader metadataLoader = (MetadataLoader) applicationContext.getBean(MetadataLoader.NAME);
        metadataLoader.loadMetadata();
        rootPackages = metadataLoader.getRootPackages();
        session = new CachingMetadataSession(metadataLoader.getSession());
        SessionImpl.setSerializationSupportSession(session);

        log.info("Metadata initialized in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public ViewRepository getViewRepository() {
        return viewRepository;
    }

    @Override
    public ExtendedEntities getExtendedEntities() {
        return extendedEntities;
    }

    @Override
    public MetadataTools getTools() {
        return tools;
    }

    @Override
    public DatatypeRegistry getDatatypes() {
        return datatypeRegistry;
    }

    protected <T> T __create(Class<T> entityClass) {
        @SuppressWarnings("unchecked")
        Class<T> extClass = extendedEntities.getEffectiveClass(entityClass);
        try {
            T obj = extClass.newInstance();
            assignIdentifier((Entity) obj);
            assignUuid((Entity) obj);
            createEmbedded((Entity) obj);
            invokePostConstructMethods((Entity) obj);
            return obj;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected void assignIdentifier(Entity entity) {
        if (!(entity instanceof BaseGenericIdEntity))
            return;

        MetaClass metaClass = getClassNN(entity.getClass());

        MetaProperty primaryKeyProperty = tools.getPrimaryKeyProperty(metaClass);
        if (primaryKeyProperty != null && tools.isEmbedded(primaryKeyProperty)) {
            // create an instance of embedded ID
            Entity key = create(primaryKeyProperty.getRange().asClass());
            ((BaseGenericIdEntity) entity).setId(key);
        } else {
            if (entity instanceof BaseLongIdEntity) {
                ((BaseGenericIdEntity<Long>) entity).setId(numberIdSource.createLongId(getEntityNameForIdGeneration(metaClass)));
            } else if (entity instanceof BaseIntegerIdEntity) {
                ((BaseGenericIdEntity<Integer>) entity).setId(numberIdSource.createIntegerId(getEntityNameForIdGeneration(metaClass)));
            }
        }
    }

    protected String getEntityNameForIdGeneration(MetaClass metaClass) {
        MetaClass result = metaClass.getAncestors().stream()
                .filter(mc -> {
                    // use root of inheritance tree if the strategy is JOINED because ID is stored in the root table
                    Class<?> javaClass = mc.getJavaClass();
                    Inheritance inheritance = javaClass.getAnnotation(Inheritance.class);
                    return inheritance != null && inheritance.strategy() == InheritanceType.JOINED;
                })
                .findFirst()
                .orElse(metaClass);
        return result.getName();
    }

    protected void assignUuid(Entity entity) {
        if (entity instanceof HasUuid) {
            ((HasUuid) entity).setUuid(UuidProvider.createUuid());
        }
    }

    protected void createEmbedded(Entity entity) {
        MetaClass metaClass = getClassNN(entity.getClass());
        for (MetaProperty property : metaClass.getProperties()) {
            if (property.getRange().isClass() && tools.isEmbedded(property)) {
                EmbeddedParameters embeddedParameters = property.getAnnotatedElement().getAnnotation(EmbeddedParameters.class);
                if (embeddedParameters != null && !embeddedParameters.nullAllowed()) {
                    MetaClass embeddableMetaClass = property.getRange().asClass();
                    Entity embeddableEntity = create(embeddableMetaClass);
                    entity.setValue(property.getName(), embeddableEntity);
                }
            }
        }
    }

    protected void invokePostConstructMethods(Entity entity) throws InvocationTargetException, IllegalAccessException {
        List<Method> postConstructMethods = new ArrayList<>(4);
        List<String> methodNames = new ArrayList<>(4);
        Class clazz = entity.getClass();
        while (clazz != Object.class) {
            Method[] classMethods = clazz.getDeclaredMethods();
            for (Method method : classMethods) {
                if (method.isAnnotationPresent(PostConstruct.class) && !methodNames.contains(method.getName())) {
                    postConstructMethods.add(method);
                    methodNames.add(method.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }

        ListIterator<Method> iterator = postConstructMethods.listIterator(postConstructMethods.size());
        while (iterator.hasPrevious()) {
            Method method = iterator.previous();
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(entity);
        }
    }

    @Override
    public <T> T create(Class<T> entityClass) {
        return __create(entityClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entity create(MetaClass metaClass) {
        return (Entity) __create(metaClass.getJavaClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entity create(String entityName) {
        MetaClass metaClass = getSession().getClassNN(entityName);
        return (Entity) __create(metaClass.getJavaClass());
    }

    @Override
    public List<String> getRootPackages() {
        return Collections.unmodifiableList(rootPackages);
    }

    @Override
    public MetaModel getModel(String name) {
        return getSession().getModel(name);
    }

    @Override
    public Collection<MetaModel> getModels() {
        return getSession().getModels();
    }

    @Nullable
    @Override
    public MetaClass getClass(String name) {
        return getSession().getClass(name);
    }

    @Override
    public MetaClass getClassNN(String name) {
        return getSession().getClassNN(name);
    }

    @Nullable
    @Override
    public MetaClass getClass(Class<?> clazz) {
        return getSession().getClass(clazz);
    }

    @Override
    public MetaClass getClassNN(Class<?> clazz) {
        return getSession().getClassNN(clazz);
    }

    @Override
    public Collection<MetaClass> getClasses() {
        return getSession().getClasses();
    }
}