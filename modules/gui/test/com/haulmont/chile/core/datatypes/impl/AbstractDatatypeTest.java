/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.datatypes.impl;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.sys.AppContext;
import mockit.NonStrictExpectations;
import org.junit.Ignore;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
@Ignore
public abstract class AbstractDatatypeTest extends CubaClientTestCase {

    protected Locale ruLocale;
    protected Locale enGbLocale;

    public void setUp() {
        ruLocale = Locale.forLanguageTag("ru");
        enGbLocale = Locale.forLanguageTag("en_GB");

        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        new NonStrictExpectations() {
            {
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.gui";
                globalConfig.getAvailableLocales(); result = ImmutableMap.of("ru", ruLocale, "en_GB", enGbLocale);
            }
        };
        messages.init();
    }
}