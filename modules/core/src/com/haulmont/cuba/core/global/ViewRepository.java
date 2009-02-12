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
        String entity = viewElem.attributeValue("entity");
        if (StringUtils.isBlank(viewName) || StringUtils.isBlank(entity))
            throw new IllegalStateException("Invalid view definition");


        Session session = MetadataProvider.getSession();
        MetaClass metaClass = session.getClass(entity);
        View v = findView(metaClass, viewName);
        if (v != null)
            return v;

        View view = new View(metaClass.getJavaClass(), viewName);
        for (Element propElem : (List<Element>) viewElem.elements("property")) {
            String propName = propElem.attributeValue("name");
            MetaProperty metaProperty = metaClass.getProperty(propName);
            if (metaProperty == null)
                throw new IllegalStateException(
                        String.format("View %s/%s definition error: property %s doesn't exists", entity, viewName, propName)
                );

            View refView = null;
            String refViewName = propElem.attributeValue("view");
            if (refViewName != null) {
                Range range = metaProperty.getRange();
                if (!range.isClass())
                    throw new IllegalStateException(
                            String.format("View %s/%s definition error: property %s is not an entity", entity, viewName, propName)
                    );

                refView = findView(range.asClass(), refViewName);
                if (refView == null) {
                    for (Element e : (List<Element>) rootElem.elements("view")) {
                        if (range.asClass().getName().equals(e.attributeValue("entity"))
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
                                        entity, viewName, range.asClass().getName(), refViewName)
                        );
                }
            }
            view.addProperty(propName, refView);
        }
        storeView(metaClass, view);
        return view;
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
