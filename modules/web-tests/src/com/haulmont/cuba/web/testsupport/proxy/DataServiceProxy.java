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

package com.haulmont.cuba.web.testsupport.proxy;

import com.haulmont.bali.util.Numbers;
import com.haulmont.cuba.client.testsupport.TestSupport;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.web.testsupport.TestContainer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataServiceProxy implements DataService {

    private TestContainer container;

    public DataServiceProxy(TestContainer container) {
        this.container = container;
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        EntityStates entityStates = container.getBean(EntityStates.class);
        UserSessionSource userSessionSource = container.getBean(UserSessionSource.class);
        TimeSource timeSource = container.getBean(TimeSource.class);

        Set<Entity> result = new HashSet<>();

        for (Entity entity : context.getCommitInstances()) {
            if (entity == null) {
                throw new RuntimeException("Null instance in collection of commit entities");
            }

            BaseGenericIdEntity e = TestSupport.reserialize(entity);
            entityStates.makeDetached(e);

            if (e instanceof Versioned) {
                Versioned versioned = (Versioned) e;
                versioned.setVersion(Numbers.nullToZero(versioned.getVersion()) + 1);
            }

            if (e instanceof Creatable) {
                Creatable creatable = (Creatable) e;
                creatable.setCreateTs(timeSource.currentTimestamp());
                creatable.setCreatedBy(userSessionSource.getUserSession().getUser().getLogin());
            }

            if (e instanceof Updatable) {
                Updatable updatable = (Updatable) e;
                updatable.setUpdateTs(timeSource.currentTimestamp());
                if (!entityStates.isNew(entity)) {
                    updatable.setUpdatedBy(userSessionSource.getUserSession().getUser().getLogin());
                }
            }

            result.add(e);
        }

        for (Entity entity : context.getRemoveInstances()) {
            if (entity == null) {
                throw new RuntimeException("Null instance in collection of removed entities");
            }

            BaseGenericIdEntity e = TestSupport.reserialize(entity);
            entityStates.makeDetached(e);

            if (e instanceof Versioned) {
                Versioned versioned = (Versioned) e;
                versioned.setVersion(Numbers.nullToZero(versioned.getVersion()) + 1);
            }

            if (e instanceof SoftDelete) {
                SoftDelete softDelete = (SoftDelete) e;
                softDelete.setDeleteTs(timeSource.currentTimestamp());
                softDelete.setDeletedBy(userSessionSource.getUserSession().getUser().getLogin());
            } else {
                BaseEntityInternalAccess.setRemoved(e, true);
            }

            result.add(e);
        }

        return result;
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
    public List<KeyValueEntity> loadValues(ValueLoadContext context) {
        return Collections.emptyList();
    }
}
