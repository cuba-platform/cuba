/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.sys.remoting.discovery;

import java.util.List;
import java.util.function.Consumer;

/**
 * Strategy class for work with {@link StaticServerSelector}.
 * Does not sorts the list of servers - this is a legacy behavior for platform version &lt; 6.5.
 */
public class NoopServerSorter implements Consumer<List<String>> {

    @Override
    public void accept(List<String> strings) {
        // no sorting
    }
}
