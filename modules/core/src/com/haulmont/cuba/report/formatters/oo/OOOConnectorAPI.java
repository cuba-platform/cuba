/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report.formatters.oo;

import com.sun.star.comp.helper.BootstrapException;

import java.util.concurrent.ExecutorService;

/**
 * @author artamonov
 * @version $Id$
 */
public interface OOOConnectorAPI {
    public static final String NAME = "report_OOOConnector";

    OOOConnection createConnection(String openOfficePath) throws BootstrapException;

    void closeConnection(final OOOConnection connection);

    ExecutorService getExecutor();
}
