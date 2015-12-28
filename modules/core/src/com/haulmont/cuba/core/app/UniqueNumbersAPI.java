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
public interface UniqueNumbersAPI {

    String NAME = "cuba_UniqueNumbers";

    /**
     * Returns the next sequence value.
     *
     * @param domain    sequence identifier
     * @return          next value
     */
    long getNextNumber(String domain);

    /**
     * Returns the current value of the sequence. For some implementations
     * {@link #getNextNumber(String)} must be called at least once beforehand.
     *
     * @param domain    sequence identifier
     * @return          current value
     */
    long getCurrentNumber(String domain);

    /**
     * Set current value for the sequence.
     * Next {@link #getCurrentNumber(String)} invocation will return {@code value}
     * Next {@link #getNextNumber(String)} invocation will return {@code value + increment}
     *
     * @param domain    sequence identifier
     * @param value     value
     */
    void setCurrentNumber(String domain, long value);

    /**
     * Removes sequence with specified identifier
     * @param domain sequence identifier
     * @throws java.lang.IllegalStateException if sequence does not exist
     */
    void deleteSequence(String domain);
}
