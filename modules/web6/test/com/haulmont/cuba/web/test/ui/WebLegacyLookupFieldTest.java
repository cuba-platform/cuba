/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.test.ui;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.LookupFieldTest;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import mockit.NonStrictExpectations;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebLegacyLookupFieldTest extends LookupFieldTest {

    public WebLegacyLookupFieldTest() {
        factory = new WebComponentsFactory();
    }

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new NonStrictExpectations() {
            {
                globalConfig.getAvailableLocales(); result = ImmutableMap.of("en", Locale.ENGLISH);
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.web";
            }
        };
    }
}