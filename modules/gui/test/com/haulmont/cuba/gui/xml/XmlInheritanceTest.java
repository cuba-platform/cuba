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
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.BeanLocatorImpl;
import com.haulmont.cuba.core.sys.ResourcesImpl;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlParser;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("ReassignmentInjectVariable")
public class XmlInheritanceTest extends CubaClientTestCase {

    protected Resources resources;
    protected ScreenXmlParser screenXmlParser;
    protected BeanLocator beanLocator;

    @Before
    public void setUp() {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        resources = new ResourcesImpl(getClass().getClassLoader());
        screenXmlParser = new ScreenXmlParser();
        beanLocator = new BeanLocatorImpl() {
            @Override
            public <T> T getPrototype(String name, Object... args) {
                if (XmlInheritanceProcessor.NAME.equals(name)) {
                    XmlInheritanceProcessor processor = new XmlInheritanceProcessor((Document) args[0], (Map) args[1]) {
                        {
                            resources = XmlInheritanceTest.this.resources;
                            screenXmlParser = XmlInheritanceTest.this.screenXmlParser;
                            beanLocator = XmlInheritanceTest.this.beanLocator;
                        }
                    };
                    return (T) processor;
                }

                return super.getPrototype(name, args);
            }
        };
    }

    @Test
    public void testExtIndexNew() {
        int index = getIndexOfMovedField("com/haulmont/cuba/gui/xml/test-extends-screen-new.xml", "test");

        assertEquals(3, index);
    }

    @Test
    public void testExtIndexExtended() {
        int index = getIndexOfMovedField("com/haulmont/cuba/gui/xml/test-extends-screen-old.xml", "login");

        assertEquals(3, index);
    }

    @Test
    public void testExtIndexExtendedDown() {
        int index = getIndexOfMovedField("com/haulmont/cuba/gui/xml/test-extends-screen-old-down.xml", "name");

        assertEquals(0, index);
    }

    @Test
    public void testExtIndexExtendedUp() {
        Document document = Dom4j.readDocument(resources.getResourceAsStream("com/haulmont/cuba/gui/xml/test-extends-screen-old-up.xml"));

        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(document, emptyMap()) {
            {
                resources = XmlInheritanceTest.this.resources;
                screenXmlParser = XmlInheritanceTest.this.screenXmlParser;
                beanLocator = XmlInheritanceTest.this.beanLocator;
            }
        };
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

        assertEquals(7, index);
    }

    private int getIndexOfMovedField(String extendedXml, String fieldName) {
        Document document = Dom4j.readDocument(resources.getResourceAsStream(extendedXml));

        XmlInheritanceProcessor processor = new XmlInheritanceProcessor(document, emptyMap()) {
            {
                resources = XmlInheritanceTest.this.resources;
                screenXmlParser = XmlInheritanceTest.this.screenXmlParser;
                beanLocator = XmlInheritanceTest.this.beanLocator;
            }
        };
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