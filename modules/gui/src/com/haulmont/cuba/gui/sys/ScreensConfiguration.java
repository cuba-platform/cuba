package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration that performs ClassPath scanning of {@link UiController}s and provides {@link UiControllerDefinition}.
 */
public class ScreensConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ScreensConfiguration.class);

    @Inject
    protected Scripting scripting;

    protected List<String> packages;

    protected ApplicationContext applicationContext;

    // todo add explicit exports

    public ScreensConfiguration() {
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public List<UiControllerDefinition> getUIControllers() {
        ClassPathScanningCandidateComponentProvider provider = createComponentScanner();

        log.trace("Scanning packages {}", packages);

        return packages.stream()
                .flatMap(scanPackage -> provider.findCandidateComponents(scanPackage).stream())
                .map(BeanDefinition::getBeanClassName)
                .map(className -> {
                    log.trace("Found screen controller {}", className);

                    @SuppressWarnings("unchecked")
                    Class<? extends Screen> screenClass = (Class<? extends Screen>) scripting.loadClassNN(className);

                    UiController uiController = screenClass.getAnnotation(UiController.class);
                    if (uiController == null) {
                        throw new RuntimeException("Screen class does not have @UiController annotation : " + screenClass);
                    }

                    String id = ScreenDescriptorUtils.getInferredScreenId(uiController, screenClass);

                    return new UiControllerDefinition(id, className);
                })
                .collect(Collectors.toList());
    }

    protected ClassPathScanningCandidateComponentProvider createComponentScanner() {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.setResourceLoader(getResourceLoader());
        provider.addIncludeFilter(new AnnotationTypeFilter(UiController.class));
        return provider;
    }

    protected ResourceLoader getResourceLoader() {
        return applicationContext;
    }

    public final static class UiControllerDefinition {
        private final String id;
        private final String controllerClass;

        public UiControllerDefinition(String id, String controllerClass) {
            this.id = id;
            this.controllerClass = controllerClass;
        }

        public String getId() {
            return id;
        }

        public String getControllerClass() {
            return controllerClass;
        }
    }
}