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

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.NumberIdSequence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Generates ids for entities with long/integer PK using database sequences.
 */
@Component(NumberIdWorker.NAME)
public class NumberIdWorker implements NumberIdSequence {

    public static final String NAME = "cuba_NumberIdWorker";

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Sequences sequences;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected GlobalConfig config;

    @Override
    public Long createLongId(String entityName, String sequenceName) {
        Sequence sequence = Sequence.withName(getSequenceName(entityName, sequenceName))
                .setStore(getDataStore(entityName))
                .setStartValue(1)
                .setIncrement(1);

        return sequences.createNextValue(sequence);
    }

    @Override
    public Long createCachedLongId(String entityName, String sequenceName) {
        Sequence sequence = Sequence.withName(getSequenceName(entityName, sequenceName))
                .setStore(getDataStore(entityName))
                .setStartValue(0)
                .setIncrement(config.getNumberIdCacheSize());

        return sequences.createNextValue(sequence);
    }

    /**
     * INTERNAL. Used by tests.
     */
    public void reset() {
        ((SequencesImpl) sequences).reset();
    }

    protected String getDataStore(String entityName) {
        if (!serverConfig.getUseEntityDataStoreForIdSequence()) {
            return Stores.MAIN;
        } else {
            return metadataTools.getStoreName(metadata.getClassNN(entityName));
        }
    }

    protected String getSequenceName(String entityName, String sequenceName) {
        if (StringUtils.isBlank(entityName))
            throw new IllegalArgumentException("entityName is blank");

        if (StringUtils.isNotBlank(sequenceName))
            return sequenceName;

        return "seq_id_" + entityName.replace("$", "_");
    }
}