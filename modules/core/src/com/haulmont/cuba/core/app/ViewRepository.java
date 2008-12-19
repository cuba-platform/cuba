/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 16:03:28
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.Locator;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;

import java.io.InputStream;
import java.util.List;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class ViewRepository implements ViewRepositoryMBean
{
    public static ViewRepository getInstance() {
        ViewRepositoryMBean mbean = Locator.lookupMBean(ViewRepositoryMBean.class, ViewRepositoryMBean.OBJECT_NAME);
        return mbean.getImplementation();
    }

    public void create() {
    }

    public ViewRepository getImplementation() {
        return this;
    }

    public View getView(Class<? extends BaseEntity> entityClass, String name) {
        return null;
    }

    public View getView(MetaClass metaClass, String name) {
        return null;
    }

    private View findView(MetaClass metaClass, String name) {
        return null;
    }

    public void deployViews(InputStream xmlStream) {
        SAXReader reader = new SAXReader();
        Document doc;
        try {
            doc = reader.read(xmlStream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElem = doc.getRootElement();
        for (Element viewElem : (List<Element>) rootElem.elements("view")) {
            deployView(rootElem, viewElem);
        }
    }

    private View deployView(Element rootElem, Element viewElem) {
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
                        String.format("View %s/%s definition error: property %s doesn't exists", entity, viewName, propName));

            View refView = null;
            String refViewName = propElem.attributeValue("view");
            if (refViewName != null) {
                Range range = metaProperty.getRange();
                if (!range.isClass())
                    throw new IllegalStateException(
                            String.format("View %s/%s definition error: property %s is not an entity", entity, viewName, propName));

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
                                String.format("View %s/%s definition error: property %s is not an entity", entity, viewName, propName)
                        );
                }
            }
            view.getProperties().add(new ViewProperty(propName, refView));
        }
        return view;
    }
}
