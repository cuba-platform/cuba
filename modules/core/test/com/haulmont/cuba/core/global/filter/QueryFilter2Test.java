/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;

import com.haulmont.bali.util.Dom4j;
import org.dom4j.Element;
import org.junit.Test;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class QueryFilter2Test {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<filter>\n" +
            "    <and>\n" +
            "        <or name=\"group\" unary=\"true\" width=\"1\">\n" +
            "            <c name=\"firstName\" class=\"java.lang.String\" operatorType=\"EQUAL\" width=\"1\" type=\"PROPERTY\">\n" +
            "                <![CDATA[u.firstName = :component$genericFilter.firstName75521]]>\n" +
            "                <param name=\"component$genericFilter.firstName75521\" javaClass=\"java.lang.String\">NULL</param>\n" +
            "            </c>\n" +
            "            <c name=\"login\" class=\"java.lang.String\" operatorType=\"EQUAL\" width=\"1\" type=\"PROPERTY\">\n" +
            "                <![CDATA[u.login = :component$genericFilter.login61430]]>\n" +
            "                <param name=\"component$genericFilter.login61430\" javaClass=\"java.lang.String\">NULL</param>\n" +
            "            </c>\n" +
            "        </or>\n" +
            "        <c name=\"group.name\" class=\"java.lang.String\" operatorType=\"EQUAL\" width=\"1\" type=\"PROPERTY\">\n" +
            "            <![CDATA[u.group.name = :component$genericFilter.group_name53787]]>\n" +
            "            <param name=\"component$genericFilter.group_name53787\" javaClass=\"java.lang.String\">GROUP</param>\n" +
            "        </c>\n" +
            "        <c name=\"createTs\" class=\"java.util.Date\" operatorType=\"EQUAL\" width=\"1\" type=\"PROPERTY\">\n" +
            "            <![CDATA[u.createTs = :component$genericFilter.createTs65126]]>\n" +
            "            <param name=\"component$genericFilter.createTs65126\" javaClass=\"java.util.Date\">2015-11-02 00:00:00.000</param>\n" +
            "        </c>\n" +
            "    </and>\n" +
            "</filter>";

    @Test
    public void testParse() throws Exception {
        Element element = Dom4j.readDocument(xml).getRootElement();
        QueryFilter queryFilter = new QueryFilter(element, "sec$User");

        Condition root = queryFilter.getRoot();
        System.out.println(new GroovyGenerator().generateGroovy(root));
        System.out.println();
        System.out.println();
        System.out.println(new JpqlGenerator().generateJpql(root));
    }
}
