/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.bali.util;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.function.Supplier;

public final class StreamUtils {

    private StreamUtils() {
    }

    /**
     * Lazy evaluated value Supplier.
     * <p>
     * Example:
     * <pre>
     *    LazySupplier&lt;InputStream&gt; supplier = LazySupplier.of(() -&gt; {
     *        return new FileInputStream(file);
     *    });
     *
     *    ...
     *
     *    if (supplier.supplied()) {
     *        IOUtils.closeQuietly(inputStreamSupplier.get());
     *    }
     * </pre>
     *
     * @param <T> type of supplied value
     */
    @NotThreadSafe
    public static class LazySupplier<T> implements Supplier<T> {

        protected T value;
        protected final Supplier<T> supplier;

        public LazySupplier(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public static <T> LazySupplier<T> of(Supplier<T> supplier) {
            return new LazySupplier<>(supplier);
        }

        @Override
        public T get() {
            if (value == null) {
                value = supplier.get();
            }
            return value;
        }

        /**
         * @return true if value is supplied by {@link #get()} invocation
         */
        public boolean supplied() {
            return value != null;
        }
    }
}
