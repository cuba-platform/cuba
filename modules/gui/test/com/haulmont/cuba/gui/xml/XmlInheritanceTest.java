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