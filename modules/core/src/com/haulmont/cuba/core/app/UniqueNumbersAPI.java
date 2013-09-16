/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

/**
 * Provides unique numbers based on database sequences.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface UniqueNumbersAPI {

    String NAME = "cuba_UniqueNumbers";
    
    /**
     * Returns the next sequence value.
     * @param domain sequence identifier
     */
    long getNextNumber(String domain);

    long getCurrentNumber(String domain);

    void setCurrentNumber(String domain, long value);
}
