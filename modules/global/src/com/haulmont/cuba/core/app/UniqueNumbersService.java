/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

/**
 * Provides sequences of unique numbers based on database sequences.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface UniqueNumbersService {

    String NAME = "cuba_UniqueNumbersService";

    /**
     * Returns the next sequence value.
     *
     * @param domain    sequence identifier
     * @return          next value
     */
    long getNextNumber(String domain);
}
