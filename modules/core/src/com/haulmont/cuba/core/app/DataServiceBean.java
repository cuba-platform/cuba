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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 */
@Service(DataService.NAME)
public class DataServiceBean implements DataService {

    @Inject
    protected DataManager dataManager;

    @Override
    public Set<Entity> commit(CommitContext context) {
        return dataManager.secure().commit(context);
    }

    @Override
    @Nullable
    public <E extends Entity> E load(LoadContext<E> context) {
        return dataManager.secure().load(context);
    }

    @Override
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        return dataManager.secure().loadList(context);
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        return dataManager.secure().getCount(context);
    }
}