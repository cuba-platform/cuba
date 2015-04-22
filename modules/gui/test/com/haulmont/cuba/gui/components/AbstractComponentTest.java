/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class AbstractComponentTest extends CubaClientTestCase {
    protected ComponentsFactory factory;

    protected DynamicAttributesGuiTools daGuiTools = new DynamicAttributesGuiTools();

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        initExpectations();

        messages.init();
    }

    protected void initExpectations() {
        new NonStrictExpectations() {
            {
                AppBeans.get(DynamicAttributesGuiTools.NAME); result = daGuiTools;
                AppBeans.get(DynamicAttributesGuiTools.class); result = daGuiTools;
                AppBeans.get(DynamicAttributesGuiTools.NAME, DynamicAttributesGuiTools.class); result = daGuiTools;
            }
        };
    }
}