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

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Base implementation of the {@link ViewRepository}. Contains methods to store {@link View} objects and deploy
 * them from XML. <br>
 * <br> Don't replace this class completely, because the framework uses it directly.
 */
public class AbstractViewRepository implements ViewRepository {

    private final Logger log = LoggerFactory.getLogger(AbstractViewRepository.class);

    protected List<String> readFileNames = new LinkedList<>();

    protected Map<MetaClass, Map<String, View>> storage = new ConcurrentHashMap<>();

    @Inject
    protected Metadata metadata;

    @Inject
    protected Resources resources;

    @Inject
    protected ViewLoader viewLoader;

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected void checkInitialized() {
        if (!initialized) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (!initialized) {
                    log.info("Initializing views");
                    init();
                    initialized = true;
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    protected void init() {
        StopWatch initTiming = new Slf4JStopWatch("ViewRepository.init." + getClass().getSimpleName());

        storage.clear();
        readFileNames.clear();

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            Element rootElem = DocumentHelper.createDocument().addElement("views");

            StringTokenizer tokenizer = new StringTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                addFile(rootElem, fileName);
            }

            viewLoader.checkDuplicates(rootElem);

            for (Element viewElem : Dom4j.elements(rootElem, "view")) {
                deployView(rootElem, viewElem, new HashSet<>());
            }
        }

        initTiming.stop();
    }

    protected void addFile(Element commonRootElem, String fileName) {
        if (readFileNames.contains(fileName))
            return;

        log.debug("Deploying views config: " + fileName);
        readFileNames.add(fileName);

        InputStream stream = null;
        try {
            stream = resources.getResourceAsStream(fileName);
            if (stream == null) {
                throw new IllegalStateException("Resource is not found: " + fileName);
            }

            SAXReader reader = new SAXReader();
            Document doc;
            try {
                doc = reader.read(new InputStreamReader(stream, StandardCharsets.UTF_8));
            } catch (DocumentException e) {
                throw new RuntimeException("Unable to parse view file " + fileName, e);
            }
            Element rootElem = doc.getRootElement();

            for (Element includeElem : Dom4j.elements(rootElem, "include")) {
                String incFile = includeElem.attributeValue("file");
                if (!StringUtils.isBlank(incFile))
                    addFile(commonRootElem, incFile);
            }

            for (Element viewElem : Dom4j.elements(rootElem, "view")) {
                commonRootElem.add(viewElem.createCopy());
            }
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public void reset() {
        initialized = false;
    }

    /**
     * Get View for an entity.
     *
     * @param entityClass entity class
     * @param name        view name
     * @return view instance. Throws {@link com.haulmont.cuba.core.global.ViewNotFoundException} if not found.
     */
    @Override
    public View getView(Class<? extends Entity> entityClass, String name) {
        return getView(metadata.getClassNN(entityClass), name);
    }

    /**
     * Get View for an entity.
     *
     * @param metaClass entity class
     * @param name      view name
     * @return view instance. Throws {@link com.haulmont.cuba.core.global.ViewNotFoundException} if not found.
     */
    @Override
    public View getView(MetaClass metaClass, String name) {
        Preconditions.checkNotNullArgument(metaClass, "MetaClass is null");

        View view = findView(metaClass, name);

        if (view == null) {
            throw new ViewNotFoundException(String.format("View %s/%s not found", metaClass.getName(), name));
        }
        return view;
    }

    /**
     * Searches for a View for an entity
     *
     * @param metaClass entity class
     * @param name      view name
     * @return view instance or null if no view found
     */
    @Override
    @Nullable
    public View findView(MetaClass metaClass, @Nullable String name) {
        if (metaClass == null) {
            throw new IllegalArgumentException("Passed metaClass should not be null");
        }

        if (name == null) {
            return null;
        }

        lock.readLock().lock();
        try {
            checkInitialized();

            View view = retrieveView(metaClass, name, new HashSet<>());
            return copyView(view);
        } finally {
            lock.readLock().unlock();
        }
    }

    protected View copyView(@Nullable View view) {
        if (view == null) {
            return null;
        }

        View.ViewParams viewParams = new View.ViewParams()
                .entityClass(view.getEntityClass())
                .name(view.getName());
        View copy = new View(viewParams);
        for (ViewProperty property : view.getProperties()) {
            copy.addProperty(property.getName(), copyView(property.getView()), property.getFetchMode());
        }

        return copy;
    }

    @Override
    public Collection<String> getViewNames(MetaClass metaClass) {
        Preconditions.checkNotNullArgument(metaClass, "MetaClass is null");
        lock.readLock().lock();
        try {
            checkInitialized();
            Map<String, View> viewMap = storage.get(metaClass);
            if (viewMap != null && !viewMap.isEmpty()) {
                Set<String> keySet = new HashSet<>(viewMap.keySet());
                keySet.remove(View.LOCAL);
                keySet.remove(View.MINIMAL);
                keySet.remove(View.BASE);
                return keySet;
            } else {
                return Collections.emptyList();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Collection<String> getViewNames(Class<? extends Entity> entityClass) {
        Preconditions.checkNotNullArgument(entityClass, "entityClass is null");
        MetaClass metaClass = metadata.getClassNN(entityClass);
        return getViewNames(metaClass);
    }

    protected View deployDefaultView(MetaClass metaClass, String name, Set<ViewLoader.ViewInfo> visited) {
        Class<? extends Entity> javaClass = metaClass.getJavaClass();

        ViewLoader.ViewInfo info = new ViewLoader.ViewInfo(metaClass, name);
        if (visited.contains(info)) {
            throw new DevelopmentException(String.format("Views cannot have cyclic references. View %s for class %s",
                    name, metaClass.getName()));
        }

        View view;
        if (View.LOCAL.equals(name)) {
            view = new View(javaClass, name, false);
            addAttributesToLocalView(metaClass, view);
        } else if (View.MINIMAL.equals(name)) {
            view = new View(javaClass, name, false);
            addAttributesToMinimalView(metaClass, view, info, visited);
        } else if (View.BASE.equals(name)) {
            view = new View(javaClass, name, false);
            addAttributesToMinimalView(metaClass, view, info, visited);
            addAttributesToLocalView(metaClass, view);
        } else {
            throw new UnsupportedOperationException("Unsupported default view: " + name);
        }

        storeView(metaClass, view);

        return view;
    }

    protected void addAttributesToLocalView(MetaClass metaClass, View view) {
        for (MetaProperty property : metaClass.getProperties()) {
            if (!property.getRange().isClass()
                    && !metadata.getTools().isSystem(property)
                    && metadata.getTools().isPersistent(property)) {
                view.addProperty(property.getName());
            }
        }
    }

    protected void addAttributesToMinimalView(MetaClass metaClass, View view, ViewLoader.ViewInfo info, Set<ViewLoader.ViewInfo> visited) {
        Collection<MetaProperty> metaProperties = metadata.getTools().getNamePatternProperties(metaClass, true);
        for (MetaProperty metaProperty : metaProperties) {
            if (metadata.getTools().isPersistent(metaProperty)) {
                addPersistentAttributeToMinimalView(metaClass, visited, info, view, metaProperty);
            } else {
                List<String> relatedProperties = metadata.getTools().getRelatedProperties(metaProperty);
                for (String relatedPropertyName : relatedProperties) {
                    MetaProperty relatedProperty = metaClass.getPropertyNN(relatedPropertyName);
                    if (metadata.getTools().isPersistent(relatedProperty)) {
                        addPersistentAttributeToMinimalView(metaClass, visited, info, view, relatedProperty);
                    } else {
                        log.warn(
                                "Transient attribute '{}' is listed in 'related' properties of another transient attribute '{}'",
                                relatedPropertyName, metaProperty.getName());
                    }
                }
            }
        }
    }

    protected void addPersistentAttributeToMinimalView(MetaClass metaClass, Set<ViewLoader.ViewInfo> visited, ViewLoader.ViewInfo info, View view, MetaProperty metaProperty) {
        if (metaProperty.getRange().isClass()) {
            Map<String, View> views = storage.get(metaProperty.getRange().asClass());
            View refMinimalView = (views == null ? null : views.get(View.MINIMAL));

            if (refMinimalView != null) {
                view.addProperty(metaProperty.getName(), refMinimalView);
            } else {
                visited.add(info);
                View referenceMinimalView = deployDefaultView(metaProperty.getRange().asClass(), View.MINIMAL, visited);
                visited.remove(info);

                view.addProperty(metaProperty.getName(), referenceMinimalView);
            }
        } else {
            view.addProperty(metaProperty.getName());
        }
    }

    public void deployViews(String resourceUrl) {
        lock.readLock().lock();
        try {
            checkInitialized();
        } finally {
            lock.readLock().unlock();
        }

        Element rootElem = DocumentHelper.createDocument().addElement("views");

        lock.writeLock().lock();
        try {
            addFile(rootElem, resourceUrl);

            for (Element viewElem : Dom4j.elements(rootElem, "view")) {
                deployView(rootElem, viewElem, new HashSet<>());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deployViews(InputStream xml) {
        deployViews(new InputStreamReader(xml, StandardCharsets.UTF_8));
    }

    public void deployViews(Reader xml) {
        lock.readLock().lock();
        try {
            checkInitialized();
        } finally {
            lock.readLock().unlock();
        }

        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(xml);
        } catch (DocumentException e) {
            throw new RuntimeException("Unable to read views xml", e);
        }
        Element rootElem = doc.getRootElement();

        for (Element includeElem : Dom4j.elements(rootElem, "include")) {
            String file = includeElem.attributeValue("file");
            if (!StringUtils.isBlank(file))
                deployViews(file);
        }

        for (Element viewElem : Dom4j.elements(rootElem, "view")) {
            deployView(rootElem, viewElem);
        }
    }

    protected View retrieveView(MetaClass metaClass, String name, Set<ViewLoader.ViewInfo> visited) {
        Map<String, View> views = storage.get(metaClass);
        View view = (views == null ? null : views.get(name));
        if (view == null && (name.equals(View.LOCAL) || name.equals(View.MINIMAL) || name.equals(View.BASE))) {
            view = deployDefaultView(metaClass, name, visited);
        }
        return view;
    }

    public View deployView(Element rootElem, Element viewElem) {
        lock.writeLock().lock();
        try {
            return deployView(rootElem, viewElem, new HashSet<>());
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected View deployView(Element rootElem, Element viewElem, Set<ViewLoader.ViewInfo> visited) {
        ViewLoader.ViewInfo viewInfo = viewLoader.getViewInfo(viewElem);
        MetaClass metaClass = viewInfo.getMetaClass();
        String viewName = viewInfo.getName();

        if (StringUtils.isBlank(viewName)) {
            throw new DevelopmentException("Invalid view definition: no 'name' attribute present");
        }

        if (visited.contains(viewInfo)) {
            throw new DevelopmentException(String.format("Views cannot have cyclic references. View %s for class %s",
                    viewName, metaClass.getName()));
        }

        View defaultView = retrieveView(metaClass, viewName, visited);

        if (defaultView != null && !viewInfo.isOverwrite()) {
            return defaultView;
        }

        View.ViewParams viewParams = viewLoader.getViewParams(
                viewInfo,
                ancestorViewName -> getAncestorView(metaClass, ancestorViewName, visited)
        );

        View view = new View(viewParams);

        visited.add(viewInfo);
        viewLoader.loadViewProperties(viewElem, view, viewInfo.isSystemProperties(), (MetaClass refMetaClass, String refViewName) -> {
            if (refViewName == null) {
                return null;
            }
            View refView = retrieveView(refMetaClass, refViewName, visited);
            if (refView == null) {
                for (Element e : Dom4j.elements(rootElem, "view")) {
                    if (refMetaClass.equals(viewLoader.getMetaClass(e.attributeValue("entity"), e.attributeValue("class")))
                            && refViewName.equals(e.attributeValue("name"))) {
                        refView = deployView(rootElem, e, visited);
                        break;
                    }
                }

                if (refView == null) {
                    MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(refMetaClass);
                    if (originalMetaClass != null) {
                        refView = retrieveView(originalMetaClass, refViewName, visited);
                    }
                }

                if (refView == null) {
                    throw new DevelopmentException(
                            String.format("View %s/%s definition error: unable to find/deploy referenced view %s/%s",
                                    metaClass.getName(), viewName, refMetaClass, refViewName));
                }
            }
            return refView;
        });
        visited.remove(viewInfo);

        storeView(metaClass, view);

        if (viewInfo.isOverwrite()) {
            replaceOverridden(view);
        }

        return view;
    }

    protected void replaceOverridden(View replacementView) {
        StopWatch replaceTiming = new Slf4JStopWatch("ViewRepository.replaceOverridden");

        HashSet<View> checked = new HashSet<>();

        for (View view : getAllInitialized()) {
            if (!checked.contains(view)) {
                replaceOverridden(view, replacementView, checked);
            }
        }

        replaceTiming.stop();
    }

    protected void replaceOverridden(View root, View replacementView, HashSet<View> checked) {
        checked.add(root);

        List<ViewProperty> replacements = null;

        for (ViewProperty property : root.getProperties()) {
            View propertyView = property.getView();

            if (propertyView != null) {
                if (Objects.equals(propertyView.getName(), replacementView.getName())
                        && replacementView.getEntityClass() == propertyView.getEntityClass()) {
                    if (replacements == null) {
                        replacements = new LinkedList<>();
                    }
                    replacements.add(new ViewProperty(property.getName(), replacementView, property.getFetchMode()));
                } else if (propertyView.getEntityClass() != null && !checked.contains(propertyView)) {
                    replaceOverridden(propertyView, replacementView, checked);
                }
            }
        }

        if (replacements != null) {
            for (ViewProperty replacement : replacements) {
                root.addProperty(replacement.getName(), replacement.getView(), replacement.getFetchMode());
            }
        }
    }

    protected View getAncestorView(MetaClass metaClass, String ancestor, Set<ViewLoader.ViewInfo> visited) {
        View ancestorView = retrieveView(metaClass, ancestor, visited);
        if (ancestorView == null) {
            ExtendedEntities extendedEntities = metadata.getExtendedEntities();
            MetaClass originalMetaClass = extendedEntities.getOriginalMetaClass(metaClass);
            if (originalMetaClass != null) {
                ancestorView = retrieveView(originalMetaClass, ancestor, visited);
            }
            if (ancestorView == null) {
                // Last resort - search for all ancestors
                for (MetaClass ancestorMetaClass : metaClass.getAncestors()) {
                    if (ancestorMetaClass.equals(metaClass)) {
                        ancestorView = retrieveView(ancestorMetaClass, ancestor, visited);
                        if (ancestorView != null)
                            break;
                    }
                }
            }
            if (ancestorView == null) {
                throw new DevelopmentException("No ancestor view found: " + ancestor + " for " + metaClass.getName());
            }
        }
        return ancestorView;
    }

    protected void storeView(MetaClass metaClass, View view) {
        Map<String, View> views = storage.get(metaClass);
        if (views == null) {
            views = new ConcurrentHashMap<>();
        }

        views.put(view.getName(), view);
        storage.put(metaClass, views);
    }

    protected List<View> getAllInitialized() {
        List<View> list = new ArrayList<>();
        for (Map<String, View> viewMap : storage.values()) {
            list.addAll(viewMap.values());
        }
        return list;
    }

    public List<View> getAll() {
        lock.readLock().lock();
        try {
            checkInitialized();
            List<View> list = new ArrayList<>();
            for (Map<String, View> viewMap : storage.values()) {
                list.addAll(viewMap.values());
            }
            return list;
        } finally {
            lock.readLock().unlock();
        }
    }

}
