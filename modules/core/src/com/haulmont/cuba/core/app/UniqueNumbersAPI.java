/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

/**
 * Provides unique numbers based on database sequences.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface UniqueNumbersAPI
{
    String NAME = "cuba_UniqueNumbers";
    
    /**
     * Returns the next sequence value.
     * @param domain sequence identifier
     */
    long getNextNumber(String domain);
}
