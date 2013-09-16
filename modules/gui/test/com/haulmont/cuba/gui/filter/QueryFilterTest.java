/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.GlobalConfig;
import mockit.NonStrictExpectations;
import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class QueryFilterTest extends CubaClientTestCase {

    @Before
    public void setUp() {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        new NonStrictExpectations() {
            GlobalConfig globalConfig;
            {
                configuration.getConfig(GlobalConfig.class); result = globalConfig;

                globalConfig.getUseAstBasedJpqlTransformer(); result = false;
            }
        };
    }

    private QueryFilter createFilter(String name) {
        InputStream stream = QueryFilterTest.class.getResourceAsStream("/com/haulmont/cuba/gui/filter/" + name);
        Document doc = Dom4j.readDocument(stream);
        return new QueryFilter(doc.getRootElement(), "saneco$GenDoc");
    }

    @Test
    public void test1() {
        QueryFilter filter = createFilter("filter1.xml");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("p1", "v1");
        String s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where (d.processState <> 'Version')", s);
    }

    @Test
    public void test2() {
        QueryFilter filter = createFilter("filter2.xml");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("p1", "v1");
        String s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d", s);

        params.put("custom$filter_state", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where (d.processState = :custom$filter_state)", s);
    }

    @Test
    public void test3() {
        QueryFilter filter = createFilter("filter3.xml");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("p1", "v1");
        String s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d", s);

        params.put("custom$filter_state", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where (d.processState = :custom$filter_state)", s);

        params.put("custom$filter_barCode", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where ((d.processState = :custom$filter_state and d.barCode like :custom$filter_barCode))", s);
    }

    @Test
    public void test4() {
        QueryFilter filter = createFilter("filter4.xml");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("p1", "v1");
        String s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d", s);

        params.put("custom$filter_state", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where (d.processState = :custom$filter_state)", s);

        params.put("custom$filter_barCode", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where ((d.processState = :custom$filter_state and d.barCode like :custom$filter_barCode))", s);

        params.put("custom$filter_notSigned", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where (((d.processState = :custom$filter_state and d.barCode like :custom$filter_barCode) or d.processState <> :custom$filter_notSigned))", s);
    }

    @Test
    public void test5() {
        QueryFilter filter = createFilter("filter5.xml");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("p1", "v1");
        String s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d", s);

        params.put("custom$filter_state", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where (d.processState = :custom$filter_state)", s);

        params.put("custom$filter_barCode", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d where ((d.processState = :custom$filter_state and d.barCode like :custom$filter_barCode))", s);

        params.put("custom$filter_notSigned", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d where 1=1", params);
        assertEquals("select distinct d from saneco$GenDoc d where 1=1 and (((d.processState = :custom$filter_state and d.barCode like :custom$filter_barCode) or d.processState <> :custom$filter_notSigned))", s);
    }

    @Test
    public void test6() {
        QueryFilter filter = createFilter("filter6.xml");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("p1", "v1");
        String s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d", s);

        params.put("custom$filter_state", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d , docflow$DocumentRole dr where (d.processState = :custom$filter_state)", s);

        params.put("custom$filter_barCode", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d", params);
        assertEquals("select distinct d from saneco$GenDoc d , docflow$DocumentRole dr where ((d.processState = :custom$filter_state and d.barCode like :custom$filter_barCode))", s);

        params.put("custom$filter_notSigned", "v1");
        s = filter.processQuery("select distinct d from saneco$GenDoc d where 1=1", params);
        assertEquals("select distinct d from saneco$GenDoc d , docflow$DocumentRole dr where 1=1 and (((d.processState = :custom$filter_state and d.barCode like :custom$filter_barCode) or d.processState <> :custom$filter_notSigned))", s);
    }

}
