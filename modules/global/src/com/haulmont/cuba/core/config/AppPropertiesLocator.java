/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.config.defaults.*;
import com.haulmont.cuba.core.config.type.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.MetadataBuildSupport;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 */
@Component(AppPropertiesLocator.NAME)
public class AppPropertiesLocator {

    public static final String NAME = "cuba_RuntimeParamsLocator";

    private Logger log = LoggerFactory.getLogger(getClass());

    protected volatile Set<String> interfacesCache;

    protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    @Inject
    protected MetadataBuildSupport metadataBuildSupport;

    @Inject
    protected Configuration configuration;

    public List<AppPropertyEntity> getAppProperties() {
        long start = System.currentTimeMillis();

        Set<Class> configInterfaces = findConfigInterfaces();
        List<AppPropertyEntity> propertyInfos = findDatabaseStoredProperties(configInterfaces);

        log.debug("Done in " + (System.currentTimeMillis() - start) + "ms");
        return propertyInfos;
    }

    protected Set<Class> findConfigInterfaces() {
        if (interfacesCache == null) {
            synchronized (this) {
                if (interfacesCache == null) {
                    Set<String> cache = new HashSet<>();
                    for (String rootPackage : metadataBuildSupport.getRootPackages()) {
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
            result.add(entity);
        }

        return result;
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
}
