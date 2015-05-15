/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author chevelev
 * @version $Id$
 */
public class CommitRequest {

    private Collection commitInstances;
    private Collection removeInstances;
    private boolean softDeletion = true;
    private Set<String> newInstanceIds = new HashSet<>();
    private Map<String, InstanceRef> instanceRefs = new HashMap<>();
    private Set<String> commitIds = new HashSet<>();
    private boolean commitDynamicAttributes = false;

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

    public Set<String> getNewInstanceIds() {
        return Collections.unmodifiableSet(newInstanceIds);
    }

    public Set<String> getCommitIds() {
        return commitIds;
    }

    public void setCommitIds(Set<String> commitIds) {
        this.commitIds = commitIds;
    }

    public InstanceRef parseInstanceRefAndRegister(String fullId) throws InstantiationException, IllegalAccessException {
        EntityLoadInfo loadInfo;
        if (!fullId.startsWith("NEW-")) {

            loadInfo = EntityLoadInfo.parse(fullId);
            if (loadInfo == null) {
                throw new RuntimeException("Cannot parse id: " + fullId);
            }

            InstanceRef existingRef = instanceRefs.get(loadInfo.getMetaClass().getName() + "-" + loadInfo.getId().toString());
            if (existingRef != null) {
                return existingRef;
            }

        } else {
            int idDashIndex = StringUtils.ordinalIndexOf(fullId, "-", 2);
            if (idDashIndex == -1) {
                String entityName = fullId.substring("NEW-".length());
                String generatedId = generateId(entityName);
                fullId = fullId + "-" + generatedId;
            }
            loadInfo = EntityLoadInfo.parse(fullId);
            if (loadInfo == null) {
                throw new RuntimeException("Cannot parse id: " + fullId);
            }
        }

        if (loadInfo.isNewEntity())
            newInstanceIds.add(loadInfo.toString());

        InstanceRef result = new InstanceRef(loadInfo);
        instanceRefs.put(loadInfo.getMetaClass().getName() + "-" + loadInfo.getId().toString(), result);
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

    public boolean isCommitDynamicAttributes() {
        return commitDynamicAttributes;
    }

    public void setCommitDynamicAttributes(boolean commitDynamicAttributes) {
        this.commitDynamicAttributes = commitDynamicAttributes;
    }
}