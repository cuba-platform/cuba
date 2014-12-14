/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.junit.Before;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class AbstractComponentTest extends CubaClientTestCase {
    protected ComponentsFactory factory;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        initExpectations();

        messages.init();
    }

    protected void initExpectations() {
    }
}