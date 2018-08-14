/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.ValueLoadContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * INTERNAL.
 * Empty implementation of the {@link DataStore} interface. {@code DataManager} routes here entities that do not
 * belong to any data store.
 */
@Component(NullStore.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NullStore implements DataStore {

    public static final String NAME = "cuba_NullStore";

    public NullStore(String storeName) {
    }

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        return null;
    }

    @Override
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        return Collections.emptyList();
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        return 0;
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        Set<Entity> set = new HashSet<>();
        set.addAll(context.getCommitInstances());
        set.addAll(context.getRemoveInstances());
        return set;
    }

    @Override
    public List<KeyValueEntity> loadValues(ValueLoadContext context) {
        return Collections.emptyList();
    }
}
