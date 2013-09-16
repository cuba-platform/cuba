/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.EntityLoadInfo;
import com.haulmont.cuba.core.global.UuidSource;

import java.util.*;

/**
 * @author chevelev
 * @version $Id$
 */
public class CommitRequest {

    private Collection commitInstances;
    private Collection removeInstances;
    private boolean softDeletion = true;
    private HashSet<String> newInstanceIds = new HashSet<>();
    private Map<String, InstanceRef> instanceRefs = new HashMap<>();
    private Set<String> commitIds = new HashSet<>();

    public Collection getCommitInstances() {
        return commitInstances == null ? Collections.emptyList() : commitInstances;
    }

    public Collection getRemoveInstances() {
        return removeInstances == null ? Collections.emptyList() : removeInstances;
    }

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public void setCommitInstances(Collection commitInstances) {
        this.commitInstances = commitInstances;
    }

    public void setRemoveInstances(Collection removeInstances) {
        this.removeInstances = removeInstances;
    }

    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    public Collection getNewInstanceIds() {
        return Collections.unmodifiableSet(newInstanceIds);
    }

    public Set<String> getCommitIds() {
        return commitIds;
    }

    public void setCommitIds(Set<String> commitIds) {
        this.commitIds = commitIds;
    }

    public InstanceRef parseInstanceRefAndRegister(String id) throws InstantiationException, IllegalAccessException {
        boolean isNew = false;
        if (id.startsWith("NEW-")) {
            id = id.substring("NEW-".length());
            isNew = true;
        }

        InstanceRef existingRef = instanceRefs.get(id);
        if (existingRef != null) {
            return existingRef;
        }

        EntityLoadInfo loadInfo = EntityLoadInfo.parse(id);
        if (loadInfo == null) {
            if (isNew) {
                UUID uuid = AppBeans.get(UuidSource.class).createUuid();
                id = id + "-" + uuid;
                loadInfo = EntityLoadInfo.parse(id);
                if (loadInfo == null) {
                    throw new RuntimeException("Cannot parse id: " + id);
                }
            } else
                throw new RuntimeException("Cannot parse id: " + id);
        }

        if (isNew)
            newInstanceIds.add(id);

        InstanceRef result = new InstanceRef(loadInfo);
        instanceRefs.put(id, result);
        return result;
    }
}