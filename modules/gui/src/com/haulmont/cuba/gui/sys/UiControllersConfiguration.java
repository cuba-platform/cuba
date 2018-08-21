package com.haulmont.cuba.gui.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.screen.UiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration that performs ClassPath scanning of {@link UiController}s and provides {@link UiControllerDefinition}.
 */
public class UiControllersConfiguration {
    public static final String DEFAULT_CLASS_RESOURCE_PATTERN = "**/*.class";

    private static final Logger log = LoggerFactory.getLogger(UiControllersConfiguration.class);

    protected ApplicationContext applicationContext;
    protected MetadataReaderFactory metadataReaderFactory;

    protected List<String> packages = Collections.emptyList();
    protected List<String> classNames = Collections.emptyList();

    public UiControllersConfiguration() {
    }

    @Inject
    protected void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.metadataReaderFactory = new CachingMetadataReaderFactory(applicationContext);
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        Preconditions.checkNotNullArgument(packages);

        this.packages = packages;
    }

    public List<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(List<String> classNames) {
        this.classNames = classNames;
    }

    public List<UiControllerDefinition> getUIControllers() {
        log.trace("Scanning packages {}", packages);

        Stream<UiControllerDefinition> scannedControllersStream = packages.stream()
                .flatMap(this::scanPackage)
                .filter(this::isCandidateUiController)
                .map(this::extractControllerDefinition);

        Stream<UiControllerDefinition> explicitControllersStream = classNames.stream()
                .map(this::loadClassMetadata)
                .map(this::extractControllerDefinition);

        return Stream.concat(scannedControllersStream, explicitControllersStream)
                .collect(Collectors.toList());
    }

    protected MetadataReader loadClassMetadata(String className) {
        Resource resource = getResourceLoader().getResource(className);
        if (!resource.isReadable()) {
            throw new RuntimeException(String.format("Resource %s is not readable for class %s", resource, className));
        }
        try {
            return getMetadataReaderFactory().getMetadataReader(resource);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read resource " + resource, e);
        }
    }

    protected UiControllerDefinition extractControllerDefinition(MetadataReader metadataReader) {
        Map<String, Object> uiControllerAnn =
                metadataReader.getAnnotationMetadata().getAnnotationAttributes(UiController.class.getName());

        String idAttr = null;
        String valueAttr = null;
        if (uiControllerAnn != null) {
            idAttr = (String) uiControllerAnn.get(UiController.ID_ATTRIBUTE);
            valueAttr = (String) uiControllerAnn.get(UiController.VALUE_ATTRIBUTE);
        }

        String className = metadataReader.getClassMetadata().getClassName();
        String controllerId = ScreenDescriptorUtils.getInferredScreenId(idAttr, valueAttr, className);

        return new UiControllerDefinition(controllerId, className);
    }

    protected Stream<MetadataReader> scanPackage(String packageName) {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(packageName) + '/' + DEFAULT_CLASS_RESOURCE_PATTERN;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException("Unable to scan package " + packageName, e);
        }

        return Arrays.stream(resources)
                .peek(resource -> log.trace("Scanning {}", resource))
                .filter(Resource::isReadable)
                .map(resource -> {
                    try {
                        return getMetadataReaderFactory().getMetadataReader(resource);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to read resource " + resource, e);
                    }
                });
    }

    protected MetadataReaderFactory getMetadataReaderFactory() {
        return metadataReaderFactory;
    }

    protected String resolveBasePackage(String basePackage) {
        Environment environment = applicationContext.getEnvironment();
        return ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(basePackage));
    }

    protected boolean isCandidateUiController(MetadataReader metadataReader) {
        return metadataReader.getClassMetadata().isConcrete()
                && metadataReader.getAnnotationMetadata().hasAnnotation(UiController.class.getName());
    }

    protected ResourceLoader getResourceLoader() {
        return applicationContext;
    }
}