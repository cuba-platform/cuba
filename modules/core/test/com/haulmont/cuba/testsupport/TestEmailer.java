/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.core.app.Emailer;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class TestEmailer extends Emailer {
    @Override
    protected boolean applicationNotStartedYet() {
        return false;
    }
}
