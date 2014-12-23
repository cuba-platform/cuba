/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base implementation of the {@link ViewRepository}. Contains methods to store {@link View} objects and deploy
 * them from XML.
 * <p/>
 * <p/> Don't replace this class completely, because the framework uses it directly.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AbstractViewRepository implements ViewRepository {

    protected Log log = LogFactory.getLog(getClass());

    protected List<String> readFileNames = new LinkedList<>();

    protected Map<MetaClass, Map<String, View>> storage = new ConcurrentHashMap<>();

    @Inject
    protected Metadata metadata;

    @Inject
    protected Resources resources;

    private volatile boolean initialized;

    protected void checkInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    log.info("Initializing views");
                    init();
                    initialized = true;
                }
            }
        }
    }

    protected void init() {
        StopWatch initTiming = new Log4JStopWatch("ViewRepository.init." + getClass().getSimpleName());

        String configName = AppContext.getProperty("cuba.viewsConfig");
        if (!StringUtils.isBlank(configName)) {
            Element rootElem = DocumentHelper.createDocument().addElement("views");

            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                addFile(rootElem, fileName);
            }

            checkDuplicates(rootElem);

            for (Element viewElem : Dom4j.elements(rootElem, "view")) {
                deployView(rootElem, viewElem);
            }
        }

        initTiming.stop();
    }

    protected void checkDuplicates(Element rootElem) {
        Set<String> checked = new HashSet<>();
        for (Element viewElem : Dom4j.elements(rootElem, "view")) {
            String viewName = getViewName(viewElem);
            String key = getMetaClass(viewElem) + "/" + viewName;
            if (!BooleanUtils.toBoolean(viewElem.attributeValue("overwrite"))) {
                String extend = viewElem.attributeValue("extends");
                if (!StringUtils.equals(extend, viewName) && checked.contains(key)) {
                    log.warn("Duplicate view definition without 'overwrite' attribute and not extending parent view: " + key);
                }
            }
            checked.add(key);
        }
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
                doc = reader.read(new InputStreamReader(stream));
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
    public View findView(MetaClass metaClass, String name) {
        if (metaClass == null || name == null) {
            return null;
        }

        checkInitialized();

        View view = retrieveView(metaClass, name, new HashSet<ViewInfo>());
        return copyView(view);
    }

    protected View copyView(@Nullable View view) {
        if (view == null) {
            return null;
        }

        View copy = new View(view.getEntityClass(), view.getName(), view.isIncludeSystemProperties());
        for (ViewProperty property : view.getProperties()) {
            copy.addProperty(property.getName(), copyView(property.getView()), property.isLazy());
        }

        return copy;
    }

    @Override
    public Collection<String> getViewNames(MetaClass metaClass) {
        Preconditions.checkNotNullArgument(metaClass, "MetaClass is null");
        Map<String, View> viewMap = storage.get(metaClass);
        return viewMap != null ? viewMap.keySet() : Collections.<String>emptyList();
    }

    @Override
    public Collection<String> getViewNames(Class<? extends Entity> entityClass) {
        MetaClass metaClass = metadata.getClassNN(entityClass);
        return getViewNames(metaClass);
    }

    @SuppressWarnings("unchecked")
    protected View deployDefaultView(MetaClass metaClass, String name, Set<ViewInfo> visited) {
        Class<? extends Entity> javaClass = metaClass.getJavaClass();

        ViewInfo info = new ViewInfo(javaClass, name);
        if (visited.contains(info)) {
            throw new DevelopmentException(String.format("Views cannot have cyclic references. View %s for class %s",
                    name, metaClass.getName()));
        }

        View view = new View(javaClass, name, false);
        if (View.LOCAL.equals(name)) {
            for (MetaProperty property : metaClass.getProperties()) {
                if (!property.getRange().isClass()
                        && !metadata.getTools().isSystem(property)
                        && metadata.getTools().isPersistent(property)) {
                    view.addProperty(property.getName());
                }
            }
        } else if (View.MINIMAL.equals(name)) {
            Collection<MetaProperty> metaProperties = metadata.getTools().getNamePatternProperties(metaClass, true);
            for (MetaProperty metaProperty : metaProperties) {
                if (metaProperty.getRange().isClass()
                        && !metaProperty.getRange().getCardinality().isMany()) {

                    Map<String, View> views = storage.get(metaClass);
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
        } else {
            throw new UnsupportedOperationException("Unsupported default view: " + name);
        }

        storeView(metaClass, view);

        return view;
    }

    public void deployViews(String resourceUrl) {
        Element rootElem = DocumentHelper.createDocument().addElement("views");

        addFile(rootElem, resourceUrl);

        for (Element viewElem : Dom4j.elements(rootElem, "view")) {
            deployView(rootElem, viewElem);
        }
    }

    public void deployViews(InputStream xml) {
        deployViews(new InputStreamReader(xml));
    }

    public void deployViews(Reader xml) {
        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(xml);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
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

    protected View retrieveView(MetaClass metaClass, String name, Set<ViewInfo> visited) {
        Map<String, View> views = storage.get(metaClass);
        View view = (views == null ? null : views.get(name));
        if (view == null && (name.equals(View.LOCAL) || name.equals(View.MINIMAL))) {
            view = deployDefaultView(metaClass, name, visited);
        }
        return view;
    }

    public View deployView(Element rootElem, Element viewElem) {
        return deployView(rootElem, viewElem, new HashSet<ViewInfo>());
    }

    public View deployView(Element rootElem, Element viewElem, Set<ViewInfo> visited) {
        String viewName = getViewName(viewElem);
        MetaClass metaClass = getMetaClass(viewElem);

        ViewInfo info = new ViewInfo(metaClass.getJavaClass(), viewName);
        if (visited.contains(info)) {
            throw new DevelopmentException(String.format("Views cannot have cyclic references. View %s for class %s",
                    viewName, metaClass.getName()));
        }

        View v = retrieveView(metaClass, viewName, visited);
        boolean overwrite = BooleanUtils.toBoolean(viewElem.attributeValue("overwrite"));

        String ancestor = viewElem.attributeValue("extends");
        if (!overwrite) {
            overwrite = StringUtils.equals(ancestor, viewName);
        }

        if (v != null && !overwrite) {
            return v;
        }

        String systemProperties = viewElem.attributeValue("systemProperties");

        View view;
        if (ancestor != null) {
            View ancestorView = getAncestorView(metaClass, ancestor, visited);

            boolean includeSystemProperties = systemProperties == null ?
                    ancestorView.isIncludeSystemProperties() : Boolean.valueOf(systemProperties);
            view = new View(ancestorView, metaClass.getJavaClass(), viewName, includeSystemProperties);
        } else {
            view = new View(metaClass.getJavaClass(), viewName, Boolean.valueOf(systemProperties));
        }

        visited.add(info);
        loadView(rootElem, viewElem, view, visited);
        visited.remove(info);

        storeView(metaClass, view);

        if (overwrite) {
            replaceOverridden(view);
        }

        return view;
    }

    protected void replaceOverridden(View replacementView) {
        Log4JStopWatch replaceTiming = new Log4JStopWatch("ViewRepository.replaceOverridden");

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
                if (StringUtils.equals(propertyView.getName(), replacementView.getName())
                        && replacementView.getEntityClass() == propertyView.getEntityClass()) {
                    if (replacements == null) {
                        replacements = new LinkedList<>();
                    }
                    replacements.add(new ViewProperty(property.getName(), replacementView, property.isLazy()));
                } else if (propertyView.getEntityClass() != null && !checked.contains(propertyView)) {
                    replaceOverridden(propertyView, replacementView, checked);
                }
            }
        }

        if (replacements != null) {
            for (ViewProperty replacement : replacements) {
                root.addProperty(replacement.getName(), replacement.getView(), replacement.isLazy());
            }
        }
    }

    protected View getAncestorView(MetaClass metaClass, String ancestor, Set<ViewInfo> visited) {
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

    @SuppressWarnings("unchecked")
    protected void loadView(Element rootElem, Element viewElem, View view, Set<ViewInfo> visited) {
        final MetaClass metaClass = metadata.getClassNN(view.getEntityClass());
        final String viewName = view.getName();

        Set<String> propertyNames = new HashSet<>();

        for (Element propElem : (List<Element>) viewElem.elements("property")) {
            String propertyName = propElem.attributeValue("name");

            if (propertyNames.contains(propertyName)) {
                throw new DevelopmentException(String.format("View %s/%s definition error: view declared property %s twice",
                        metaClass.getName(), viewName, propertyName));
            }
            propertyNames.add(propertyName);

            MetaProperty metaProperty = metaClass.getProperty(propertyName);
            if (metaProperty == null) {
                throw new DevelopmentException(String.format("View %s/%s definition error: property %s doesn't exists",
                        metaClass.getName(), viewName, propertyName));
            }

            View refView = null;
            String refViewName = propElem.attributeValue("view");

            MetaClass refMetaClass;
            Range range = metaProperty.getRange();
            if (range == null) {
                throw new RuntimeException("cannot find range for meta property: " + metaProperty);
            }

            final List<Element> propertyElements = propElem.elements("property");
            boolean inlineView = !propertyElements.isEmpty();

            if (!range.isClass() && (refViewName != null || inlineView)) {
                throw new DevelopmentException(String.format("View %s/%s definition error: property %s is not an entity",
                        metaClass.getName(), viewName, propertyName));
            }

            if (refViewName != null) {
                refMetaClass = getMetaClass(propElem, range);

                refView = retrieveView(refMetaClass, refViewName, visited);
                if (refView == null) {
                    for (Element e : (List<Element>) rootElem.elements("view")) {
                        if (refMetaClass.equals(getMetaClass(e.attributeValue("entity"), e.attributeValue("class")))
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
                                        metaClass.getName(), viewName, range.asClass().getName(), refViewName));
                    }
                }
            }

            if (inlineView) {
                // try to import anonymous views
                Class rangeClass = range.asClass().getJavaClass();

                if (refView == null) {
                    refView = new View(rangeClass);
                } else {
                    refView = new View(refView, rangeClass, "", true);
                }
                loadView(rootElem, propElem, refView, visited);
            }

            boolean lazy = Boolean.valueOf(propElem.attributeValue("lazy"));
            if (lazy && metadata.getTools().isEmbedded(metaProperty)) {
                log.warn(String.format(
                        "Embedded property '%s' of class '%s' cannot have lazy view",
                        metaProperty.getName(), metaClass.getName()));
                lazy = false;
            }
            view.addProperty(propertyName, refView, lazy);
        }
    }

    protected String getViewName(Element viewElem) {
        String viewName = viewElem.attributeValue("name");
        if (StringUtils.isBlank(viewName))
            throw new DevelopmentException("Invalid view definition: no 'name' attribute present");
        return viewName;
    }

    protected MetaClass getMetaClass(Element viewElem) {
        MetaClass metaClass;
        String entity = viewElem.attributeValue("entity");
        if (StringUtils.isBlank(entity)) {
            String className = viewElem.attributeValue("class");
            if (StringUtils.isBlank(className))
                throw new DevelopmentException("Invalid view definition: no 'entity' or 'class' attribute present");
            Class entityClass = ReflectionHelper.getClass(className);
            metaClass = metadata.getClassNN(entityClass);
        } else {
            metaClass = metadata.getClassNN(entity);
        }
        return metaClass;
    }

    protected MetaClass getMetaClass(String entityName, String entityClass) {
        if (entityName != null) {
            return metadata.getClassNN(entityName);
        } else {
            return metadata.getClassNN(ReflectionHelper.getClass(entityClass));
        }
    }

    protected MetaClass getMetaClass(Element propElem, Range range) {
        MetaClass refMetaClass;
        String refEntityName = propElem.attributeValue("entity"); // this attribute is deprecated
        if (refEntityName == null) {
            refMetaClass = range.asClass();
        } else {
            refMetaClass = metadata.getClassNN(refEntityName);
        }
        return refMetaClass;
    }

    public void storeView(MetaClass metaClass, View view) {
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
        checkInitialized();
        List<View> list = new ArrayList<>();
        for (Map<String, View> viewMap : storage.values()) {
            list.addAll(viewMap.values());
        }
        return list;
    }

    protected static class ViewInfo {
        protected Class javaClass;
        protected String name;

        public ViewInfo(Class javaClass, String name) {
            this.javaClass = javaClass;
            this.name = name;
        }

        public Class getJavaClass() {
            return javaClass;
        }

        public void setJavaClass(Class javaClass) {
            this.javaClass = javaClass;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ViewInfo)) {
                return false;
            }

            ViewInfo that = (ViewInfo) obj;
            return this.javaClass == that.javaClass && Objects.equals(this.name, that.name);
        }

        @Override
        public int hashCode() {
            int result = javaClass.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }
}