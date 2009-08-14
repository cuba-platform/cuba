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
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.bali.util.ReflectionHelper;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ViewRepository
{
    private Map<MetaClass, Map<String, View>> storage =
            new ConcurrentHashMap<MetaClass, Map<String, View>>();

    public View getView(Class<? extends BaseEntity> entityClass, String name) {
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

    public void deployViews(String resourceUrl) {
        deployViews(getClass().getResourceAsStream(resourceUrl));
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
        if (views == null)
            return null;
        else
            return views.get(name);
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
        if (v != null) return v;

        View view = new View(metaClass.getJavaClass(), viewName);
        loadView(rootElem, viewElem, view);
        storeView(metaClass, view);

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
                if (!range.isClass())
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
            // try to import anonimus veiws
            if (range.isClass() && refView == null) {
                final List<Element> propertyElements = propElem.elements("property");
                if (!propertyElements.isEmpty()) {
                    refView = new View(range.asClass().getJavaClass());
                    loadView(rootElem, propElem, refView);
                }
            }

            view.addProperty(propertyName, refView);
        }
    }

    private void storeView(MetaClass metaClass, View view) {
        Map<String, View> views = storage.get(metaClass);
        if (views == null) {
            views = new ConcurrentHashMap<String, View>();
        }

        views.put(view.getName(), view);
        storage.put(metaClass, views);
    }
}
