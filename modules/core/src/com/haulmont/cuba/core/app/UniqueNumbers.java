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

import com.haulmont.cuba.core.global.Stores;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Provides unique numbers based on database sequences.
 */
@Component(UniqueNumbersAPI.NAME)
public class UniqueNumbers implements UniqueNumbersAPI {

    @Inject
    protected Sequences sequences;

    @Override
    public long getNextNumber(String domain) {
        Sequence sequence = Sequence.withName(getSequenceName(domain))
                .setStore(getDataStore(domain))
                .setStartValue(1)
                .setIncrement(1);
        return sequences.createNextValue(sequence);
    }

    @Override
    public long getCurrentNumber(String domain) {
        Sequence sequence = Sequence.withName(getSequenceName(domain))
                .setStore(getDataStore(domain));
        return sequences.getCurrentValue(sequence);
    }

    @Override
    public void setCurrentNumber(String domain, long value) {
        Sequence sequence = Sequence.withName(getSequenceName(domain))
                .setStore(getDataStore(domain));
        sequences.setCurrentValue(sequence, value);
    }

    @Override
    public void deleteSequence(String domain) {
        Sequence sequence = Sequence.withName(getSequenceName(domain))
                .setStore(getDataStore(domain));
        sequences.deleteSequence(sequence);
    }

    /**
     * Override this method if you want to control in what datastore a sequence is created for a particular domain
     *
     * @param domain    sequence identifier passed to the interface methods
     * @return          datastore id (by default, the main datastore)
     */
    protected String getDataStore(String domain) {
        return Stores.MAIN;
    }

    protected String getSequenceName(String domain) {
        if (StringUtils.isBlank(domain))
            throw new IllegalArgumentException("Domain name can not be blank");
        return "seq_un_" + domain;
    }
}