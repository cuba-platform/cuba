/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class containing all views defined in XML and deployed at runtime.<br>
 * The reference to the ViewRepository can be obtained through {@link com.haulmont.cuba.core.global.MetadataProvider}
 *
 * @author krivopustov
 * @version $Id$
 */
public class ViewRepository
{
    private List<String> readFileNames = new LinkedList<String>();

    private Map<MetaClass, Map<String, View>> storage =
            new ConcurrentHashMap<MetaClass, Map<String, View>>();

    private Metadata metadata;
    private Resources resources;

    private static Log log = LogFactory.getLog(ViewRepository.class);

    public ViewRepository(Metadata metadata, Resources resources) {
        this.metadata = metadata;
        this.resources = resources;
    }

    /**
     * Get View for an entity.
     * @param entityClass   entity class
     * @param name          view name
     * @return              view instance. Throws {@link ViewNotFoundException} if not found.
     */
    public View getView(Class<? extends Entity> entityClass, String name) {
        return getView(metadata.getSession().getClassNN(entityClass), name);
    }

    /**
     * Get View for an entity.
     * @param metaClass     entity class
     * @param name          view name
     * @return              view instance. Throws {@link ViewNotFoundException} if not found.
     */
    public View getView(MetaClass metaClass, String name) {
        Objects.requireNonNull(metaClass, "MetaClass is null");

        // Replace with extended entity if such one exists
        metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(metaClass);

        View view = findView(metaClass, name);
        if (view == null) {
            MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
            if (originalMetaClass != null) {
                view = findView(originalMetaClass, name);
            }
        }

        if (view == null)
            throw new ViewNotFoundException(String.format("View %s/%s not found", metaClass.getName(), name));
        return view;
    }

    private View deployDefaultView(MetaClass metaClass, String name) {
        Class<? extends BaseEntity> javaClass = metaClass.getJavaClass();
        View view = new View(javaClass, name, false);
        if (View.LOCAL.equals(name)) {
            for (MetaProperty property : metaClass.getProperties()) {
                if (!property.getRange().isClass() && !metadata.getTools().isSystem(property)) {
                    view.addProperty(property.getName());
                }
            }
        } else if (View.MINIMAL.equals(name)) {
            NamePattern annotation = javaClass.getAnnotation(NamePattern.class);
            if (annotation != null) {
                String pattern = annotation.value();
                int pos = pattern.indexOf("|");
                if (pos >= 0) {
                    String fieldsStr = StringUtils.substring(pattern, pos + 1);
                    String[] fields = fieldsStr.split("[,;]");
                    for (String field : fields) {
                        view.addProperty(field);
                    }
                }
            }
        } else
            throw new UnsupportedOperationException("Unsupported default view: " + name);

        storeView(metaClass, view);
        return view;
    }

    public void deployViews(String resourceUrl) {
        if (!readFileNames.contains(resourceUrl)) {
            log.debug("Deploying views config: " + resourceUrl);

            InputStream stream = null;
            try {
                stream = resources.getResourceAsStream(resourceUrl);
                if (stream == null)
                    throw new IllegalStateException("Resource is not found: " + resourceUrl);
                deployViews(stream);
                readFileNames.add(resourceUrl);
            } finally {
                IOUtils.closeQuietly(stream);
            }
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

        for (Element includeElem : (List<Element>) rootElem.elements("include")) {
            String file = includeElem.attributeValue("file");
            if (!StringUtils.isBlank(file))
                deployViews(file);
        }

        for (Element viewElem : (List<Element>) rootElem.elements("view")) {
            deployView(rootElem, viewElem);
        }
    }

    protected View findView(MetaClass metaClass, String name) {
        Map<String, View> views = storage.get(metaClass);
        View view = (views == null ? null : views.get(name));
        if (view == null && (name.equals(View.LOCAL) || name.equals(View.MINIMAL))) {
            view = deployDefaultView(metaClass, name);
        }
        return view;
    }

    public View deployView(Element rootElem, Element viewElem) {
        String viewName = viewElem.attributeValue("name");
        if (StringUtils.isBlank(viewName))
            throw new IllegalStateException("Invalid view definition: no 'name' attribute");

        MetaClass metaClass;

        String entity = viewElem.attributeValue("entity");
        if (StringUtils.isBlank(entity)) {
            String className = viewElem.attributeValue("class");
            if (StringUtils.isBlank(className))
                throw new IllegalStateException("Invalid view definition: no 'entity' or 'class' attribute");
            Class entityClass = ReflectionHelper.getClass(className);
            metaClass = metadata.getSession().getClassNN(entityClass);
        }
        else {
            metaClass = metadata.getSession().getClassNN(entity);
        }

        View v = findView(metaClass, viewName);
        boolean overwrite = BooleanUtils.toBoolean(viewElem.attributeValue("overwrite"));
        if (v != null && !overwrite)
            return v;

        String systemProperties = viewElem.attributeValue("systemProperties");

        View view;
        String ancestor = viewElem.attributeValue("extends");
        if (ancestor != null) {
            View ancestorView = findView(metaClass, ancestor);
            if (ancestorView == null) {
                MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
                if (originalMetaClass != null)
                    ancestorView = findView(originalMetaClass, ancestor);
                if (ancestorView == null)
                    throw new IllegalStateException("No ancestor view found: " + ancestor);
            }

            boolean includeSystemProperties = systemProperties == null ?
                    ancestorView.isIncludeSystemProperties() : Boolean.valueOf(systemProperties);
            view = new View(ancestorView, metaClass.getJavaClass(), viewName, includeSystemProperties);
        } else {
            view = new View(metaClass.getJavaClass(), viewName, Boolean.valueOf(systemProperties));
        }
        loadView(rootElem, viewElem, view);
        storeView(metaClass, view);

        return view;
    }

    protected void loadView(Element rootElem, Element viewElem, View view) {
        final MetaClass metaClass = metadata.getSession().getClassNN(view.getEntityClass());
        final String viewName = view.getName();

        for (Element propElem : (List<Element>) viewElem.elements("property")) {
            String propertyName = propElem.attributeValue("name");

            MetaProperty metaProperty = metaClass.getProperty(propertyName);
            if (metaProperty == null)
                throw new IllegalStateException(
                        String.format("View %s/%s definition error: property %s doesn't exists", metaClass.getName(), viewName, propertyName)
                );

            View refView = null;
            String refViewName = propElem.attributeValue("view");

            if (refViewName != null) {
                Range range = metaProperty.getRange();
                if (range == null || !range.isClass())
                    throw new IllegalStateException(
                            String.format("View %s/%s definition error: property %s is not an entity", metaClass.getName(), viewName, propertyName)
                    );

                String refEntityName = propElem.attributeValue("entity");
                MetaClass refMetaClass;
                if (refEntityName == null) {
                    refMetaClass = range.asClass();
                } else {
                    refMetaClass = metadata.getSession().getClass(refEntityName);
                }

                refView = findView(refMetaClass, refViewName);
                if (refView == null) {
                    for (Element e : (List<Element>) rootElem.elements("view")) {
                        if ((refMetaClass.getName().equals(e.attributeValue("entity"))
                                || refMetaClass.getJavaClass().getName().equals(e.attributeValue("class")))
                                && refViewName.equals(e.attributeValue("name")))
                        {
                            refView = deployView(rootElem, e);
                            break;
                        }
                    }

                    if (refView == null)
                        throw new IllegalStateException(
                                String.format(
                                        "View %s/%s definition error: unable to find/deploy referenced view %s/%s",
                                        metaClass.getName(), viewName, range.asClass().getName(), refViewName)
                        );
                }
            }

            Range range = metaProperty.getRange();

            if (range == null) {
                throw new RuntimeException("cannot find range for meta property: " + metaProperty);
            }
            // try to import anonimous views
            if (range.isClass() && refView == null) {
                final List<Element> propertyElements = propElem.elements("property");
                if (!propertyElements.isEmpty()) {
                    refView = new View(range.asClass().getJavaClass());
                    loadView(rootElem, propElem, refView);
                }
            }

            boolean lazy = Boolean.valueOf(propElem.attributeValue("lazy"));

            view.addProperty(propertyName, refView, lazy);
        }
    }

    public void storeView(MetaClass metaClass, View view) {
        Map<String, View> views = storage.get(metaClass);
        if (views == null) {
            views = new ConcurrentHashMap<String, View>();
        }

        views.put(view.getName(), view);
        storage.put(metaClass, views);
    }

    public List<View> getAll() {
        List<View> list = new ArrayList<View>();
        for (Map<String, View> viewMap : storage.values()) {
            list.addAll(viewMap.values());
        }
        return list;
    }
}
