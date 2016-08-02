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

import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import org.eclipse.persistence.core.queries.CoreAttributeGroup;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.FetchGroupManager;
import org.eclipse.persistence.internal.queries.AttributeItem;
import org.eclipse.persistence.internal.queries.EntityFetchGroup;
import org.eclipse.persistence.queries.AttributeGroup;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.eclipse.persistence.queries.LoadGroup;
import org.eclipse.persistence.sessions.CopyGroup;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CubaEntityFetchGroup extends EntityFetchGroup {

    protected FetchGroup entityFetchGroup;

    public CubaEntityFetchGroup(FetchGroup fetchGroup) {
        entityFetchGroup = fetchGroup;
    }

    public CubaEntityFetchGroup(Collection<String> attributeNames) {
        entityFetchGroup = new EntityFetchGroup(attributeNames);
    }

    @Override
    public String onUnfetchedAttribute(FetchGroupTracker entity, String attributeName) {
        String[] inaccessible = BaseEntityInternalAccess.getInaccessibleAttributes((BaseGenericIdEntity) entity);
        if (inaccessible != null) {
            for (String inaccessibleAttribute : inaccessible) {
                if (attributeName.equals(inaccessibleAttribute))
                    return null;
            }
        }

        return entityFetchGroup.onUnfetchedAttribute(entity, attributeName);
    }

    @Override
    public void addAttribute(String attributeNameOrPath, CoreAttributeGroup group) {
        entityFetchGroup.addAttribute(attributeNameOrPath, group);
    }

    @Override
    public void removeAttribute(String attributeNameOrPath) {
        entityFetchGroup.removeAttribute(attributeNameOrPath);
    }

    @Override
    public boolean isEntityFetchGroup() {
        return entityFetchGroup.isEntityFetchGroup();
    }

    @Override
    public boolean isSupersetOf(CoreAttributeGroup anotherGroup) {
        return entityFetchGroup.isSupersetOf(anotherGroup);
    }

    @Override
    public Set<String> getAttributes() {
        return entityFetchGroup.getAttributes();
    }

    @Override
    public FetchGroupTracker getRootEntity() {
        return entityFetchGroup.getRootEntity();
    }

    @Override
    public void setRootEntity(FetchGroupTracker rootEntity) {
        entityFetchGroup.setRootEntity(rootEntity);
    }

    @Override
    public void setShouldLoad(boolean shouldLoad) {
        entityFetchGroup.setShouldLoad(shouldLoad);
    }

    @Override
    public void setShouldLoadAll(boolean shouldLoad) {
        entityFetchGroup.setShouldLoadAll(shouldLoad);
    }

    @Override
    public boolean shouldLoad() {
        return entityFetchGroup.shouldLoad();
    }

    @Override
    public boolean isFetchGroup() {
        return entityFetchGroup.isFetchGroup();
    }

    @Override
    public LoadGroup toLoadGroupLoadOnly() {
        return entityFetchGroup.toLoadGroupLoadOnly();
    }

    @Override
    public FetchGroup clone() {
        return new CubaEntityFetchGroup(entityFetchGroup.clone());
    }

    @Override
    public LoadGroup toLoadGroup(Map<AttributeGroup, LoadGroup> cloneMap, boolean loadOnly) {
        return entityFetchGroup.toLoadGroup(cloneMap, loadOnly);
    }

    @Override
    public EntityFetchGroup getEntityFetchGroup(FetchGroupManager fetchGroupManager) {
        return entityFetchGroup.getEntityFetchGroup(fetchGroupManager);
    }

    @Override
    public FetchGroup getGroup(String attributeNameOrPath) {
        return entityFetchGroup.getGroup(attributeNameOrPath);
    }

    @Override
    public void addAttribute(String attributeNameOrPath, Collection<? extends CoreAttributeGroup> groups) {
        entityFetchGroup.addAttribute(attributeNameOrPath, groups);
    }

    @Override
    public void addAttributeKey(String attributeNameOrPath, CoreAttributeGroup group) {
        entityFetchGroup.addAttributeKey(attributeNameOrPath, group);
    }

    @Override
    public void addAttribute(String attributeNameOrPath, AttributeGroup group) {
        entityFetchGroup.addAttribute(attributeNameOrPath, group);
    }

    @Override
    public boolean isSupersetOf(AttributeGroup anotherGroup) {
        return entityFetchGroup.isSupersetOf(anotherGroup);
    }

    @Override
    public AttributeItem getItem(String attributeNameOrPath) {
        return entityFetchGroup.getItem(attributeNameOrPath);
    }

    @Override
    public AttributeGroup findGroup(ClassDescriptor type) {
        return entityFetchGroup.findGroup(type);
    }

    @Override
    public FetchGroup toFetchGroup() {
        return entityFetchGroup.toFetchGroup();
    }

    @Override
    public FetchGroup toFetchGroup(Map<AttributeGroup, FetchGroup> cloneMap) {
        return entityFetchGroup.toFetchGroup(cloneMap);
    }

    @Override
    public boolean isCopyGroup() {
        return entityFetchGroup.isCopyGroup();
    }

    @Override
    public CopyGroup toCopyGroup() {
        return entityFetchGroup.toCopyGroup();
    }

    @Override
    public CopyGroup toCopyGroup(Map<AttributeGroup, CopyGroup> cloneMap, Map copies) {
        return entityFetchGroup.toCopyGroup(cloneMap, copies);
    }

    @Override
    public boolean isLoadGroup() {
        return entityFetchGroup.isLoadGroup();
    }

    @Override
    public LoadGroup toLoadGroup() {
        return entityFetchGroup.toLoadGroup();
    }

    @Override
    public boolean isConcurrent() {
        return entityFetchGroup.isConcurrent();
    }

    @Override
    public void addAttribute(String attributeNameOrPath) {
        entityFetchGroup.addAttribute(attributeNameOrPath);
    }

    @Override
    public void addAttributes(Collection<String> attrOrPaths) {
        entityFetchGroup.addAttributes(attrOrPaths);
    }

    @Override
    public CoreAttributeGroup clone(Map<CoreAttributeGroup<AttributeItem, ClassDescriptor>, CoreAttributeGroup<AttributeItem, ClassDescriptor>> cloneMap) {
        return entityFetchGroup.clone(cloneMap);
    }

    @Override
    public boolean containsAttribute(String attributeNameOrPath) {
        return entityFetchGroup.containsAttribute(attributeNameOrPath);
    }

    @Override
    public boolean containsAttributeInternal(String attributeName) {
        return entityFetchGroup.containsAttributeInternal(attributeName);
    }

    @Override
    public void convertClassNamesToClasses(ClassLoader classLoader) {
        entityFetchGroup.convertClassNamesToClasses(classLoader);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CubaEntityFetchGroup that = (CubaEntityFetchGroup) o;

        return entityFetchGroup.equals(that.entityFetchGroup);
    }

    @Override
    public int hashCode() {
        return entityFetchGroup.hashCode();
    }

    @Override
    public Map<String, AttributeItem> getAllItems() {
        return entityFetchGroup.getAllItems();
    }

    @Override
    public Set<String> getAttributeNames() {
        return entityFetchGroup.getAttributeNames();
    }

    @Override
    public Map<String, AttributeItem> getItems() {
        return entityFetchGroup.getItems();
    }

    @Override
    public String getName() {
        return entityFetchGroup.getName();
    }

    @Override
    public Map<Object, CoreAttributeGroup> getSubClassGroups() {
        return entityFetchGroup.getSubClassGroups();
    }

    @Override
    public Class getType() {
        return entityFetchGroup.getType();
    }

    @Override
    public String getTypeName() {
        return entityFetchGroup.getTypeName();
    }

    @Override
    public boolean hasInheritance() {
        return entityFetchGroup.hasInheritance();
    }

    @Override
    public boolean hasItems() {
        return entityFetchGroup.hasItems();
    }

    @Override
    public void insertSubClass(CoreAttributeGroup group) {
        entityFetchGroup.insertSubClass(group);
    }

    @Override
    public boolean isValidated() {
        return entityFetchGroup.isValidated();
    }

    @Override
    public void setAllSubclasses(Map<Object, CoreAttributeGroup> subclasses) {
        entityFetchGroup.setAllSubclasses(subclasses);
    }

    @Override
    public void setAttributeNames(Set attributeNames) {
        entityFetchGroup.setAttributeNames(attributeNames);
    }

    @Override
    public void setName(String name) {
        entityFetchGroup.setName(name);
    }

    @Override
    public String toString() {
        return entityFetchGroup.toString();
    }
}