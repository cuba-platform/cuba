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

package com.haulmont.cuba.core.config;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.config.defaults.*;
import com.haulmont.cuba.core.config.type.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Bean providing lookup of app properties defined in config interfaces.
 */
@Component(AppPropertiesLocator.NAME)
public class AppPropertiesLocator {

    public static final String NAME = "cuba_AppPropertiesLocator";

    private final Logger log = LoggerFactory.getLogger(AppPropertiesLocator.class);

    protected volatile Set<String> interfacesCache;

    protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    @Inject
    protected Metadata metadata;

    @Inject
    protected Configuration configuration;

    @Inject
    protected DatatypeRegistry datatypes;

    @Inject
    protected DataManager dataManager;

    public List<AppPropertyEntity> getAppProperties() {
        log.trace("Locating app properties");

        long start = System.currentTimeMillis();

        Set<Class> configInterfaces = findConfigInterfaces();
        List<AppPropertyEntity> propertyInfos = findDatabaseStoredProperties(configInterfaces);

        log.trace("Done in {} ms", (System.currentTimeMillis() - start));
        return propertyInfos;
    }

    protected Set<Class> findConfigInterfaces() {
        if (interfacesCache == null) {
            synchronized (this) {
                if (interfacesCache == null) {
                    log.trace("Locating config interfaces");
                    Set<String> cache = new HashSet<>();
                    for (String rootPackage : metadata.getRootPackages()) {
                        String packagePrefix = rootPackage.replace(".", "/") + "/**/*.class";
                        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packagePrefix;
                        Resource[] resources;
                        try {
                            resources = resourcePatternResolver.getResources(packageSearchPath);
                            for (Resource resource : resources) {
                                if (resource.isReadable()) {
                                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                                    if (classMetadata.isInterface()) {
                                        for (String intf : classMetadata.getInterfaceNames()) {
                                            if (Config.class.getName().equals(intf)) {
                                                cache.add(classMetadata.getClassName());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error searching for Config interfaces", e);
                        }
                    }
                    log.trace("Found config interfaces: {}", cache);
                    interfacesCache = cache;
                }
            }
        }
        return interfacesCache.stream()
                .map(ReflectionHelper::getClass)
                .collect(Collectors.toSet());
    }

    protected List<AppPropertyEntity> findDatabaseStoredProperties(Set<Class> configInterfaces) {
        List<AppPropertyEntity> result = new ArrayList<>();

        Map<Method, Object> propertyMethods = new HashMap<>();
        for (Class configInterface : configInterfaces) {
            if (configInterface.getClassLoader() != getClass().getClassLoader()) {
                // happens when deployed to single WAR with separate class loaders for core and web
                continue;
            }
            Config config = configuration.getConfig(configInterface);
            boolean interfaceSourceIsDb = false;
            Source sourceAnn = (Source) configInterface.getAnnotation(Source.class);
            if (sourceAnn != null && sourceAnn.type() == SourceType.DATABASE) {
                interfaceSourceIsDb = true;
            }
            Method[] declaredMethods = configInterface.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getName().startsWith("get")) {
                    Source methodSourceAnn = method.getAnnotation(Source.class);
                    if ((methodSourceAnn == null && interfaceSourceIsDb)
                            || (methodSourceAnn != null && methodSourceAnn.type() == SourceType.DATABASE)) {
                        propertyMethods.put(method, config);
                    }
                }
            }
        }

        List<com.haulmont.cuba.core.entity.Config> dbContent = loadDbContent();

        for (Map.Entry<Method, Object> entry : propertyMethods.entrySet()) {
            Method method = entry.getKey();
            Property propertyAnn = method.getAnnotation(Property.class);
            if (propertyAnn == null)
                continue;

            String name = propertyAnn.value();
            AppPropertyEntity entity = new AppPropertyEntity();
            entity.setName(name);
            entity.setDefaultValue(getDefaultValue(method));
            entity.setCurrentValue(getCurrentValue(method, entry.getValue()));
            entity.setOverridden(StringUtils.isNotEmpty(AppContext.getProperty(name)));
            entity.setSecret(method.getAnnotation(Secret.class) != null);
            if (!entity.getOverridden()) {
                assignLastUpdated(entity, dbContent);
            }
            setDataType(method, entity);
            result.add(entity);
        }

        return result;
    }

    protected List<com.haulmont.cuba.core.entity.Config> loadDbContent() {
        LoadContext<com.haulmont.cuba.core.entity.Config> loadContext =
                LoadContext.create(com.haulmont.cuba.core.entity.Config.class).setQuery(
                        LoadContext.createQuery("select c from sys$Config c")
                ).setView("appProperties");
        return dataManager.loadList(loadContext);
    }

    protected void assignLastUpdated(AppPropertyEntity entity, List<com.haulmont.cuba.core.entity.Config> dbContent) {
        dbContent.stream()
                .filter(config -> config.getName().equals(entity.getName()))
                .findFirst()
                .ifPresent(config -> {
                    entity.setUpdateTs(config.getUpdateTs() != null ? config.getUpdateTs() : config.getCreateTs());
                    entity.setUpdatedBy(config.getUpdatedBy() != null ? config.getUpdatedBy() : config.getCreatedBy());
                });
    }

    protected String getCurrentValue(Method method, Object config) {
        try {
            Object value = method.invoke(config);
            if (value == null) {
                return "";
            } else {
                TypeStringify typeStringify = getTypeStringify(method);
                return typeStringify.stringify(value);
            }
        } catch (IllegalAccessException e) {
            return "Error getting value: " + e;
        } catch (InvocationTargetException e) {
            return "Error getting value: " + e.getTargetException();
        }
    }

    protected TypeStringify getTypeStringify(Method method) {
        try {
            Stringify stringifyAnn = method.getAnnotation(Stringify.class);
            if (stringifyAnn != null) {
                if ("".equals(stringifyAnn.method())) {
                    return stringifyAnn.stringify().newInstance();
                } else {
                    String methodName = stringifyAnn.method();
                    return new MethodTypeStringify(method.getReturnType().getMethod(methodName));
                }
            }

            Factory factoryAnn = method.getAnnotation(Factory.class);
            if (factoryAnn != null) {
                TypeStringify typeStringify = getTypeStringifyForFactory(factoryAnn.factory());
                if (typeStringify != null)
                    return typeStringify;
            }

            if (Date.class.isAssignableFrom(method.getReturnType())) {
                return new DateStringify();
            }

            if (Entity.class.isAssignableFrom(method.getReturnType())) {
                return new EntityStringify();
            }

            if (EnumClass.class.isAssignableFrom(method.getReturnType())) {
                EnumStore mode = method.getAnnotation(EnumStore.class);
                if (mode != null && EnumStoreMode.ID == mode.value()) {
                    @SuppressWarnings("unchecked")
                    Class<EnumClass> enumeration = (Class<EnumClass>) method.getReturnType();
                    TypeStringify idStringify = TypeStringify.getInferred(ConfigUtil.getEnumIdType(enumeration));
                    return new EnumClassStringify(idStringify);
                }
            }

            return TypeStringify.getInferred(method.getReturnType());
        } catch (Exception e) {
            log.warn("Error getting TypeStringify: " + e);
            return new PrimitiveTypeStringify();
        }
    }

    @Nullable
    protected TypeStringify getTypeStringifyForFactory(Class<? extends TypeFactory> factoryClass) {
        if (factoryClass == StringListTypeFactory.class)
            return new StringListStringify();

        if (factoryClass == CommaSeparatedStringListTypeFactory.class)
            return new CommaSeparatedStringListStringify();

        if (factoryClass == IntegerListTypeFactory.class)
            return new IntegerListStringify();

        return null;
    }

    protected String getDefaultValue(Method method) {
        Default defaultAnn = method.getAnnotation(Default.class);
        if (defaultAnn != null) {
            return defaultAnn.value();
        }
        DefaultString defaultStringAnn = method.getAnnotation(DefaultString.class);
        if (defaultStringAnn != null) {
            return defaultStringAnn.value();
        }
        DefaultInt defaultIntAnn = method.getAnnotation(DefaultInt.class);
        if (defaultIntAnn != null) {
            return String.valueOf(defaultIntAnn.value());
        }
        DefaultInteger defaultIntegerAnn = method.getAnnotation(DefaultInteger.class);
        if (defaultIntegerAnn != null) {
            return String.valueOf(defaultIntegerAnn.value());
        }
        DefaultLong defaultLongAnn = method.getAnnotation(DefaultLong.class);
        if (defaultLongAnn != null) {
            return String.valueOf(defaultLongAnn.value());
        }
        DefaultShort defaultShortAnn = method.getAnnotation(DefaultShort.class);
        if (defaultShortAnn != null) {
            return String.valueOf(defaultShortAnn.value());
        }
        DefaultBoolean defaultBooleanAnn = method.getAnnotation(DefaultBoolean.class);
        if (defaultBooleanAnn != null) {
            return String.valueOf(defaultBooleanAnn.value());
        }
        DefaultDouble defaultDoubleAnn = method.getAnnotation(DefaultDouble.class);
        if (defaultDoubleAnn != null) {
            return String.valueOf(defaultDoubleAnn.value());
        }
        DefaultFloat defaultFloatAnn = method.getAnnotation(DefaultFloat.class);
        if (defaultFloatAnn != null) {
            return String.valueOf(defaultFloatAnn.value());
        }
        DefaultChar defaultCharAnn = method.getAnnotation(DefaultChar.class);
        if (defaultCharAnn != null) {
            return String.valueOf(defaultCharAnn.value());
        }
        return null;
    }

    private void setDataType(Method method, AppPropertyEntity entity) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class)
                entity.setDataTypeName(datatypes.getIdByJavaClass(Boolean.class));
            if (returnType == int.class)
                entity.setDataTypeName(datatypes.getIdByJavaClass(Integer.class));
            if (returnType == long.class)
                entity.setDataTypeName(datatypes.getIdByJavaClass(Long.class));
            if (returnType == double.class || returnType == float.class)
                entity.setDataTypeName(datatypes.getIdByJavaClass(Double.class));
        } else if (returnType.isEnum()) {
            entity.setDataTypeName("enum");
            EnumStore enumStoreAnn = method.getAnnotation(EnumStore.class);
            if (enumStoreAnn != null && enumStoreAnn.value() == EnumStoreMode.ID) {
                //noinspection unchecked
                Class<EnumClass> enumeration = (Class<EnumClass>) returnType;
                entity.setEnumValues(Arrays.asList(enumeration.getEnumConstants()).stream()
                        .map(ec -> String.valueOf(ec.getId()))
                        .collect(Collectors.joining(",")));
            } else {
                entity.setEnumValues(Arrays.asList(returnType.getEnumConstants()).stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")));
            }
        } else {
            Datatype<?> datatype = datatypes.get(returnType);
            if (datatype != null)
                entity.setDataTypeName(datatypes.getId(datatype));
            else
                entity.setDataTypeName(datatypes.getIdByJavaClass(String.class));
        }

        String dataTypeName = entity.getDataTypeName();
        if (!dataTypeName.equals("enum")) {
            Datatype datatype = Datatypes.get(dataTypeName);
            String v = null;
            try {
                v = entity.getDefaultValue();
                datatype.parse(v);
                v = entity.getCurrentValue();
                datatype.parse(v);
            } catch (ParseException e) {
                log.debug("Cannot parse '{}' with {} datatype, using StringDatatype for property {}", v, datatype, entity.getName());
                entity.setDataTypeName(datatypes.getIdByJavaClass(String.class));
            }
        }
    }
}