/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.testsupport;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public class TestNamePrinter extends TestWatcher {

    protected void starting(Description description) {
        System.out.println(">>>\n>>> Starting " + description.getMethodName() + "\n>>>");
    }

    @Override
    protected void finished(Description description) {
        System.out.println(">>>>\n>>> Finished " + description.getMethodName() + "\n>>>");
    }
}
