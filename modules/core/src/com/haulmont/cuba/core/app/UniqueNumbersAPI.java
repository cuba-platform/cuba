/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.app;

/**
 * Provides sequences of unique numbers based on database sequences.
 *
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
