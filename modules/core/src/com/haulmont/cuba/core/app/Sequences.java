/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.core.app;

/**
 * Provides access to the database sequences.
 */
public interface Sequences {
    String NAME = "cuba_Sequences";


    /**
     * Returns the next sequence value.
     * For example:
     * {@code
     *      sequences.createNextValue(Sequence.withName("seq_name").setStartValue(10).setIncrement(1))
     * }
     * @param sequence  sequence object
     * @return          next value
     */
    long createNextValue(Sequence sequence);

    /**
     * Returns the current value of the sequence. For some implementations
     * {@link #createNextValue(Sequence)} must be called at least once beforehand.
     *
     * @param sequence object {@link Sequence}
     * @return          current value
     */
    long getCurrentValue(Sequence sequence);

    /**
     * Set current value for the sequence.
     * Next {@link #getCurrentValue(Sequence)} invocation will return {@code value}
     * Next {@link #createNextValue(Sequence)} invocation will return {@code value + increment}
     *
     * @param sequence sequence object {@link Sequence}
     * @param value     value
     */
    void setCurrentValue(Sequence sequence, long value);

    /**
     * Removes sequence with specified identifier
     * @param sequence sequence object {@link Sequence}
     * @throws java.lang.IllegalStateException if sequence does not exist
     */
    void deleteSequence(Sequence sequence);
}
