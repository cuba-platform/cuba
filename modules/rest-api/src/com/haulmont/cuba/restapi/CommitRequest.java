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

package com.haulmont.cuba.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class CommitRequest {

    private Collection commitInstances;
    private Collection removeInstances;
    private boolean softDeletion = true;
    private Set<String> newInstanceIds = new HashSet<>();
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
}