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

package com.haulmont.cuba.core.sys.persistence.mapping.processors;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Aleksey Stukalov on 01.04.2020.
 */
@Component("cuba_FetchTypeMappingProcessor")
public class FetchTypeMappingProcessor implements MappingProcessor {

    @Inject
    private Logger log;

    @Override
    public void process(MappingProcessorContext context) {
        DatabaseMapping mapping = context.getMapping();
        String entityClassName = mapping.getDescriptor().getJavaClass().getSimpleName();

        if ((mapping.isOneToOneMapping() || mapping.isOneToManyMapping()
                || mapping.isManyToOneMapping() || mapping.isManyToManyMapping())) {
            if (!mapping.isLazy()) {
                mapping.setIsLazy(true);
                log.warn("EAGER fetch type detected for reference field {} of entity {}; Set to LAZY", mapping.getAttributeName(), entityClassName);
            }
        } else {
            if (mapping.isLazy()) {
                mapping.setIsLazy(false);
                log.warn("LAZY fetch type detected for basic field {} of entity {}; Set to EAGER", mapping.getAttributeName(), entityClassName);
            }
        }
    }
}
