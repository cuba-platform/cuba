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

package com.haulmont.cuba.gui.util;

import java.util.function.Supplier;

/**
 * Operation result object.
 */
public interface OperationResult {
    static OperationResult fail() {
        return FailedOperationResult.INSTANCE;
    }

    static OperationResult success() {
        return SuccessOperationResult.INSTANCE;
    }

    Status getStatus();

    /**
     * Creates new operation result that represents composition of two operation results. If this result is resolved as
     * successful then the second result will be obtained from the passed supplier.
     *
     * @param nextStep the next operation result supplier
     * @return new composite operation result
     */
    OperationResult compose(Supplier<OperationResult> nextStep);

    /**
     * Adds success callback to the operation result.
     *
     * @param runnable callback
     * @return this
     */
    OperationResult then(Runnable runnable);

    /**
     * Adds fail callback to the operation result.
     *
     * @param runnable callback
     * @return this
     */
    OperationResult otherwise(Runnable runnable);

    enum Status {
        UNKNOWN,
        SUCCESS,
        FAIL
    }
}