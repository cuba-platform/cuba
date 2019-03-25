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

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@NotThreadSafe
public class UnknownOperationResult implements OperationResult {
    private List<Runnable> thenListeners = new ArrayList<>(2);
    private List<Runnable> otherwiseListeners = new ArrayList<>(2);

    private Status status = Status.UNKNOWN;

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public OperationResult compose(Supplier<OperationResult> nextStep) {
        if (status == Status.SUCCESS) {
            return nextStep.get();
        }
        if (status == Status.FAIL) {
            return OperationResult.fail();
        }

        UnknownOperationResult operationResult = new UnknownOperationResult();

        then(() -> {
            OperationResult resultNested = nextStep.get();

            operationResult.resolveWith(resultNested);
        });
        otherwise(operationResult::fail);

        return operationResult;
    }

    @Override
    public OperationResult then(Runnable runnable) {
        if (status == Status.SUCCESS) {
            runnable.run();
        } else {
            thenListeners.add(runnable);
        }
        return this;
    }

    @Override
    public OperationResult otherwise(Runnable runnable) {
        if (status == Status.FAIL) {
            runnable.run();
        } else {
            otherwiseListeners.add(runnable);
        }
        return this;
    }

    public void fail() {
        for (Runnable otherwiseListener : otherwiseListeners) {
            otherwiseListener.run();
        }
        otherwiseListeners.clear();
    }

    public void success() {
        for (Runnable thenListener : thenListeners) {
            thenListener.run();
        }
        thenListeners.clear();
    }

    /**
     * Resolve this result depending on the passed result. Alias for {@link #resume(OperationResult)}.
     *
     * @param result dependency
     */
    public void resolveWith(OperationResult result) {
        resume(result);
    }

    /**
     * Resume action depending on the passed result.
     *
     * @param result dependency
     */
    public void resume(OperationResult result) {
        result.then(this::success)
                .otherwise(this::fail);
    }

    @Override
    public String toString() {
        if (status == Status.UNKNOWN) {
            return "{UNKNOWN OPERATION RESULT}";
        }

        if (status == Status.FAIL) {
            return "{OPERATION FAILED}";
        }

        return "{OPERATION SUCCESSFUL}";
    }
}