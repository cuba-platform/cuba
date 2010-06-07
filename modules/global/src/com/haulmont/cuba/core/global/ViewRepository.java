/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 16:03:28
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class containing all views defined in XML and deployed at runtime.<br>
 * The reference to the ViewRepository can be obtained through {@link com.haulmont.cuba.core.global.MetadataProvider}
 */
public class ViewRepository
{
    public interface Listener {
        void viewStored(View view);
    }

    private List<String> readFileNames = new LinkedList<String>();

    private Map<MetaClass, Map<String, View>> storage =
            new ConcurrentHashMap<MetaClass, Map<String, View>>();

    private List<Listener> listeners = new ArrayList<Listener>();

    public View getView(Class<? extends Entity> entityClass, String name) {
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityClass);
        if (metaClass == null)
            throw new IllegalStateException("Meta class not found for " + entityClass.getName());
        return getView(metaClass, name);
    }

    public View getView(MetaClass metaClass, String name) {
        if (metaClass == null)
            throw new IllegalArgumentException("MetaClass is null");

        View view = findView(metaClass, name);
        if (view == null)
            throw new ViewNotFoundException(String.format("View %s/%s not found", metaClass.getName(), name));
        return view;
    }

    private View deployDefaultView(MetaClass metaClass, String name) {
        View view = new View(metaClass.getJavaClass(), name, false);
        if (View.LOCAL.equals(name)) {
            for (MetaProperty property : metaClass.getProperties()) {
                if (!property.getRange().isClass()) {
                    view.addProperty(property.getName());
                }
            }
        } else if (View.MINIMAL.equals(name)) {
            NamePattern annotation = (NamePattern) metaClass.getJavaClass().getAnnotation(NamePattern.class);
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

        storeView(metaClass, view, true);
        return view;
    }

    public void deployViews(String resourceUrl) {
        if (!readFileNames.contains(resourceUrl)) {
            deployViews(ScriptingProvider.getResourceAsStream(resourceUrl));
            readFileNames.add(resourceUrl);
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
        for (Element viewElem : (List<Element>) rootElem.elements("view")) {
            deployView(rootElem, viewElem);
        }
    }

    private View findView(MetaClass metaClass, String name) {
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

        Session session = MetadataProvider.getSession();
        MetaClass metaClass;

        String entity = viewElem.attributeValue("entity");
        if (StringUtils.isBlank(entity)) {
            String className = viewElem.attributeValue("class");
            if (StringUtils.isBlank(className))
                throw new IllegalStateException("Invalid view definition: no 'entity' or 'class' attribute");
            Class entityClass = ReflectionHelper.getClass(className);
            metaClass = session.getClass(entityClass);
            if (metaClass == null)
                throw new IllegalStateException("No MetaClass found for class " + className);
        }
        else {
            metaClass = session.getClass(entity);
            if (metaClass == null)
                throw new IllegalStateException("No MetaClass found for entity " + entity);
        }

        View v = findView(metaClass, viewName);
        if (v != null)
            return v;

        View view;
        String ancestor = viewElem.attributeValue("extends");
        if (ancestor != null) {
            View ancestorView = findView(metaClass, ancestor);
            if (ancestorView == null)
                throw new IllegalStateException("No ancestor view found: " + ancestor);
            view = new View(ancestorView, viewName);
        } else {
            view = new View(metaClass.getJavaClass(), viewName);
        }
        loadView(rootElem, viewElem, view);
        storeView(metaClass, view, true);

        return view;
    }

    protected void loadView(Element rootElem, Element viewElem, View view) {
        Session session = MetadataProvider.getSession();

        final MetaClass metaClass = session.getClass(view.getEntityClass());
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
                    refMetaClass = session.getClass(refEntityName);
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
            // try to import anonimus veiws
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

    public void storeView(MetaClass metaClass, View view, boolean distribute) {
        Map<String, View> views = storage.get(metaClass);
        if (views == null) {
            views = new ConcurrentHashMap<String, View>();
        }

        views.put(view.getName(), view);
        storage.put(metaClass, views);

        if (distribute) {
            for (Listener listener : listeners) {
                listener.viewStored(view);
            }
        }
    }

    public List<View> getAll() {
        List<View> list = new ArrayList<View>();
        for (Map<String, View> viewMap : storage.values()) {
            list.addAll(viewMap.values());
        }
        return list;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }
}
