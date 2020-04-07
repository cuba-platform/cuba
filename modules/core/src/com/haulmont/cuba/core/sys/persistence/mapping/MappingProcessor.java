/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.core.sys.persistence.mapping;

/**
 * Updates mapping properties according to requirements. Processors application
 * order is not guaranteed. Every mapping processor should be a Spring @{@link org.springframework.stereotype.Component},
 * so you can inject other beans to it.
 */
public interface MappingProcessor {

    /**
     * Updates mapping according to the processor's aim. Can be used to enable lazy fetch, add join expression, etc.
     * @param context Context data contains objects that cannot be injected.
     */
    void process(MappingProcessorContext context);

}
