/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Updatable;
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

/**
 * @author krivopustov
 * @version $Id$
 */
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
        return entity.isDeleted() && persistence.getTools().getDirtyFields((BaseEntity) entity).contains("deleteTs");
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
            ((BaseGenericIdEntity) event.getObject()).__new(false);
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
        support.registerInstance(event.getObject(), event.getSession());
    }

    @Override
    public void postDelete(DescriptorEvent event) {
        manager.fireListener((BaseEntity) event.getSource(), EntityListenerType.AFTER_DELETE);
    }

    @Override
    public void postInsert(DescriptorEvent event) {
        manager.fireListener((BaseEntity) event.getSource(), EntityListenerType.AFTER_INSERT);
    }

    @Override
    public void postMerge(DescriptorEvent event) {
    }

    @Override
    public void postRefresh(DescriptorEvent event) {
    }

    @Override
    public void postUpdate(DescriptorEvent event) {
        manager.fireListener((BaseEntity) event.getSource(), EntityListenerType.AFTER_UPDATE);
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
        BaseEntity entity = (BaseEntity) event.getObject();
        entity.setCreatedBy(userSessionSource.getUserSession().getUser().getLogin());
        Date ts = timeSource.currentTimestamp();
        entity.setCreateTs(ts);

        if (entity instanceof Updatable) {
            ((Updatable) entity).setUpdateTs(ts);
        }
    }

    @Override
    public void preRemove(DescriptorEvent event) {
    }

    @Override
    public void preUpdate(DescriptorEvent event) {
        BaseEntity entity = (BaseEntity) event.getObject();
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