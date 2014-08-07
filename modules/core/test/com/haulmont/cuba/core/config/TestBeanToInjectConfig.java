/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean
public class TestBeanToInjectConfig {

    @Inject
    private TestConfig config;

    private TestConfig config2;

    @Inject
    public void setConfig2(TestConfig config2) {
        this.config2 = config2;
    }

    public TestConfig getConfig() {
        return config;
    }

    public TestConfig getConfig2() {
        return config2;
    }
}
