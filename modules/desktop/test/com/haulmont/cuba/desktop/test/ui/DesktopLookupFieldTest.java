/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.test.ui;

import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.gui.DesktopComponentsFactory;
import com.haulmont.cuba.gui.components.LookupFieldTest;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrictExpectations;

import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopLookupFieldTest extends LookupFieldTest {

    public DesktopLookupFieldTest() {
        factory = new DesktopComponentsFactory();
    }

    @Override
    protected void initExpectations() {
        super.initExpectations();

        new NonStrictExpectations() {
            {
                globalConfig.getAvailableLocales(); result = ImmutableMap.of("en", Locale.ENGLISH);
                AppContext.getProperty("cuba.mainMessagePack"); result = "com.haulmont.cuba.desktop";
            }
        };

        new MockUp<AutoCompleteSupport>() {
            @SuppressWarnings("UnusedDeclaration")
            @Mock
            public void checkAccessThread() {
            }
        };
    }
}