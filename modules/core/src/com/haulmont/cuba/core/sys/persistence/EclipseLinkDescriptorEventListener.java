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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventListener;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;
import java.util.Vector;

@Component(EclipseLinkDescriptorEventListener.NAME)
public class EclipseLinkDescriptorEventListener implements DescriptorEventListener {

    public static final String NAME = "cuba_EclipseLinkDescriptorEventListener";

    @Inject
    protected EntityListenerManager manager;

    @Inject
    protected Persistence persistence;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected PersistenceImplSupport support;

    protected boolean justDeleted(SoftDelete entity) {
        return entity.isDeleted() && persistence.getTools().getDirtyFields((Entity) entity).contains("deleteTs");
    }

    @Override
    public void aboutToDelete(DescriptorEvent event) {
    }

    @Override
    public void aboutToInsert(DescriptorEvent event) {
    }

    @Override
    public void aboutToUpdate(DescriptorEvent event) {
    }

    @Override
    public boolean isOverriddenEvent(DescriptorEvent event, Vector eventManagers) {
        return false;
    }

    @Override
    public void postBuild(DescriptorEvent event) {
        if (event.getObject() instanceof BaseGenericIdEntity) {
            BaseEntityInternalAccess.setNew((BaseGenericIdEntity) event.getObject(), false);
        }
        if (event.getObject() instanceof FetchGroupTracker) {
            FetchGroupTracker entity = (FetchGroupTracker) event.getObject();
            FetchGroup fetchGroup = entity._persistence_getFetchGroup();
            if (fetchGroup != null)
                entity._persistence_setFetchGroup(new CubaEntityFetchGroup(fetchGroup));
        }
    }

    @Override
    public void postClone(DescriptorEvent event) {
        // in shared cache mode, postBuild event is missed, so we repeat it here
        if (event.getObject() instanceof BaseGenericIdEntity) {
            BaseEntityInternalAccess.setNew((BaseGenericIdEntity) event.getObject(), false);
        }
        if (event.getObject() instanceof FetchGroupTracker) {
            FetchGroupTracker entity = (FetchGroupTracker) event.getObject();
            FetchGroup fetchGroup = entity._persistence_getFetchGroup();
            if (fetchGroup != null && !(fetchGroup instanceof CubaEntityFetchGroup))
                entity._persistence_setFetchGroup(new CubaEntityFetchGroup(fetchGroup));
        }

        support.registerInstance(event.getObject(), event.getSession());
    }

    @Override
    public void postDelete(DescriptorEvent event) {
        String storeName = support.getStorageName(event.getSession());
        manager.fireListener((Entity) event.getSource(), EntityListenerType.AFTER_DELETE, storeName);
    }

    @Override
    public void postInsert(DescriptorEvent event) {
        String storeName = support.getStorageName(event.getSession());
        manager.fireListener((Entity) event.getSource(), EntityListenerType.AFTER_INSERT, storeName);
    }

    @Override
    public void postMerge(DescriptorEvent event) {
    }

    @Override
    public void postRefresh(DescriptorEvent event) {
        if (event.getObject() instanceof FetchGroupTracker) {
            FetchGroupTracker entity = (FetchGroupTracker) event.getObject();
            FetchGroup fetchGroup = entity._persistence_getFetchGroup();
            if (fetchGroup != null)
                entity._persistence_setFetchGroup(new CubaEntityFetchGroup(fetchGroup));
        }
    }

    @Override
    public void postUpdate(DescriptorEvent event) {
        String storeName = support.getStorageName(event.getSession());
        manager.fireListener((Entity) event.getSource(), EntityListenerType.AFTER_UPDATE, storeName);
    }

    @Override
    public void postWrite(DescriptorEvent event) {
    }

    @Override
    public void preDelete(DescriptorEvent event) {
    }

    @Override
    public void preInsert(DescriptorEvent event) {
    }

    @Override
    public void prePersist(DescriptorEvent event) {
        Entity entity = (Entity) event.getObject();
        Date ts = timeSource.currentTimestamp();

        if (entity instanceof Creatable) {
            ((Creatable) entity).setCreatedBy(userSessionSource.getUserSession().getUser().getLogin());
            ((Creatable) entity).setCreateTs(ts);
        }
        if (entity instanceof Updatable) {
            ((Updatable) entity).setUpdateTs(ts);
        }
    }

    @Override
    public void preRemove(DescriptorEvent event) {
    }

    @Override
    public void preUpdate(DescriptorEvent event) {
        Entity entity = (Entity) event.getObject();
        if (!((entity instanceof SoftDelete) && justDeleted((SoftDelete) entity)) && (entity instanceof Updatable)) {
            Updatable updatable = (Updatable) event.getObject();
            updatable.setUpdatedBy(userSessionSource.getUserSession().getUser().getLogin());
            updatable.setUpdateTs(timeSource.currentTimestamp());
        }
    }

    @Override
    public void preUpdateWithChanges(DescriptorEvent event) {
    }

    @Override
    public void preWrite(DescriptorEvent event) {
    }
}