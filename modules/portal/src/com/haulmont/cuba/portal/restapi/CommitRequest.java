/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.*;

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

    public InstanceRef parseInstanceRefAndRegister(String fullId) throws InstantiationException, IllegalAccessException {
        boolean isNew = false;
        boolean autogenerateId = false;
        if (fullId.startsWith("NEW-")) {
            isNew = true;
            fullId = fullId.substring("NEW-".length());
            if (!fullId.contains("-"))
                autogenerateId = true;
        }

        EntityLoadInfo loadInfo;

        if (!autogenerateId) {
            InstanceRef existingRef = instanceRefs.get(fullId);
            if (existingRef != null) {
                return existingRef;
            }

            loadInfo = EntityLoadInfo.parse(fullId);
            if (loadInfo == null) {
                throw new RuntimeException("Cannot parse id: " + fullId);
            }
        } else {
            String generatedId = generateId(fullId);
            fullId = fullId + "-" + generatedId;
            loadInfo = EntityLoadInfo.parse(fullId);
            if (loadInfo == null) {
                throw new RuntimeException("Cannot parse id: " + fullId);
            }
        }

        if (isNew)
            newInstanceIds.add(fullId);

        InstanceRef result = new InstanceRef(loadInfo);
        instanceRefs.put(fullId, result);
        return result;
    }

    private String generateId(String entityName) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass metaClass = metadata.getSession().getClass(entityName);

        MetaProperty primaryKeyProp = metadata.getTools().getPrimaryKeyProperty(metaClass);
        if (primaryKeyProp == null)
            throw new UnsupportedOperationException("Cannot generate ID for " + entityName);

        if (primaryKeyProp.getJavaType().equals(UUID.class)) {
            UuidSource uuidSource = AppBeans.get(UuidSource.NAME);
            UUID uuid = uuidSource.createUuid();
            return uuid.toString();
        } else if (primaryKeyProp.getJavaType().equals(Long.class)) {
            NumberIdSource numberIdSource = AppBeans.get(NumberIdSource.NAME);
            Long longId = numberIdSource.createLongId(entityName);
            return longId.toString();
        } else if (primaryKeyProp.getJavaType().equals(Integer.class)) {
            NumberIdSource numberIdSource = AppBeans.get(NumberIdSource.NAME);
            Integer intId = numberIdSource.createIntegerId(entityName);
            return intId.toString();
        } else {
            throw new UnsupportedOperationException("Cannot generate ID for " + entityName);
        }
    }
}