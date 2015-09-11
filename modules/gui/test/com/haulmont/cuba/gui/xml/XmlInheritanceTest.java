/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.ResourcesImpl;
import mockit.NonStrictExpectations;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class XmlInheritanceTest extends CubaClientTestCase {

    private Resources resources;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        resources = new ResourcesImpl(getClass().getClassLoader());

        new NonStrictExpectations() {
            {
                AppBeans.get(Resources.NAME); result = resources;
                AppBeans.get(Resources.class); result = resources;
                AppBeans.get(Resources.NAME, Resources.class); result = resources;
            }
        };
    }

    @Test
    public void testExtIndexNew() {
        int index = getIndexOfMovedField("com/haulmont/cuba/gui/xml/test-extends-screen-new.xml", "test");

        Assert.assertEquals(3, index);
    }

    @Test
    public void testExtIndexExtended() {
        int index = getIndexOfMovedField("com/haulmont/cuba/gui/xml/test-extends-screen-old.xml", "login");

        Assert.assertEquals(3, index);
    }

    @Test
    public void testExtIndexExtendedDown() {
        int index = getIndexOfMovedField("com/haulmont/cuba/gui/xml/test-extends-screen-old-down.xml", "name");

        Assert.assertEquals(0, index);
    }

    @Test
    public void testExtIndexExtendedUp() {
        Document document = Dom4j.readDocument(resources.getResourceAsStream("com/haulmont/cuba/gui/xml/test-extends-screen-old-up.xml"));

        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(document, Collections.emptyMap());
        Element resultXml = processor.getResultRoot();

        Element layoutElement = resultXml.element("layout");
        Element fieldGroupElement = layoutElement.element("fieldGroup");
        Element columnElement = fieldGroupElement.element("column");
        //noinspection unchecked
        List<Element> fieldElements = columnElement.elements("field");

        int index = 0;
        for (Element fieldElement : fieldElements) {
            if ("login".equals(fieldElement.attributeValue("id"))) {
                break;
            }
            index++;
        }

        Assert.assertEquals(7, index);
    }

    private int getIndexOfMovedField(String extendedXml, String fieldName) {
        Document document = Dom4j.readDocument(resources.getResourceAsStream(extendedXml));

        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(document, Collections.emptyMap());
        Element resultXml = processor.getResultRoot();

        Element layoutElement = resultXml.element("layout");
        Element fieldGroupElement = layoutElement.element("fieldGroup");
        Element columnElement = fieldGroupElement.element("column");
        //noinspection unchecked
        List<Element> fieldElements = columnElement.elements("field");

        int index = 0;
        for (Element fieldElement : fieldElements) {
            if (fieldName.equals(fieldElement.attributeValue("id"))) {
                break;
            }
            index++;
        }
        return index;
    }
}