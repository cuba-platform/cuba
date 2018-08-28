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

import com.haulmont.bali.db.DbUtils;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.NumberIdSequence;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.SequenceSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    protected SequenceAPI sequenceAPI;

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

        return sequenceAPI.createNextValue(sequence);
    }

    @Override
    public Long createCachedLongId(String entityName, String sequenceName) {
        Sequence sequence = Sequence.withName(getSequenceName(entityName, sequenceName))
                .setStore(getDataStore(entityName))
                .setStartValue(0)
                .setIncrement(config.getNumberIdCacheSize());

        return sequenceAPI.createNextValue(sequence);
    }

    /**
     * INTERNAL. Used by tests.
     */
    public void reset() {
        ((SequenceWorker) sequenceAPI).reset();
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