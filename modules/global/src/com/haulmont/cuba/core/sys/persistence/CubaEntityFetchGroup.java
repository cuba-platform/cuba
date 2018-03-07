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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.View;
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

    protected FetchGroup wrappedFetchGroup;

    protected static ThreadLocal<Boolean> accessLocalUnfetched = new ThreadLocal<>();

    public CubaEntityFetchGroup(FetchGroup fetchGroup) {
        wrappedFetchGroup = fetchGroup;
    }

    public CubaEntityFetchGroup(Collection<String> attributeNames) {
        wrappedFetchGroup = new EntityFetchGroup(attributeNames);
    }

    public static void setAccessLocalUnfetched(boolean value) {
        accessLocalUnfetched.set(value);
    }

    @Override
    public String onUnfetchedAttribute(FetchGroupTracker entity, String attributeName) {
        if (entity instanceof BaseGenericIdEntity) {
            String[] inaccessible = BaseEntityInternalAccess.getInaccessibleAttributes((BaseGenericIdEntity) entity);
            if (inaccessible != null) {
                for (String inaccessibleAttribute : inaccessible) {
                    if (attributeName.equals(inaccessibleAttribute))
                        return null;
                }
            }
        }

        if (cannotAccessUnfetched(entity))
            return "Cannot get unfetched attribute [" + attributeName + "] from object " + entity;

        return wrappedFetchGroup.onUnfetchedAttribute(entity, attributeName);
    }

    protected boolean cannotAccessUnfetched(FetchGroupTracker entity) {
        return Boolean.FALSE.equals(accessLocalUnfetched.get())
                && entity instanceof Entity
                && !AppBeans.get(EntityStates.class).isLoadedWithView((Entity) entity, View.LOCAL);
    }

    @Override
    public void addAttribute(String attributeNameOrPath, CoreAttributeGroup group) {
        wrappedFetchGroup.addAttribute(attributeNameOrPath, group);
    }

    @Override
    public void removeAttribute(String attributeNameOrPath) {
        wrappedFetchGroup.removeAttribute(attributeNameOrPath);
    }

    @Override
    public boolean isEntityFetchGroup() {
        return wrappedFetchGroup.isEntityFetchGroup();
    }

    @Override
    public boolean isSupersetOf(CoreAttributeGroup anotherGroup) {
        return wrappedFetchGroup.isSupersetOf(anotherGroup);
    }

    @Override
    public Set<String> getAttributes() {
        return wrappedFetchGroup.getAttributes();
    }

    @Override
    public FetchGroupTracker getRootEntity() {
        return wrappedFetchGroup.getRootEntity();
    }

    @Override
    public void setRootEntity(FetchGroupTracker rootEntity) {
        wrappedFetchGroup.setRootEntity(rootEntity);
    }

    @Override
    public void setShouldLoad(boolean shouldLoad) {
        wrappedFetchGroup.setShouldLoad(shouldLoad);
    }

    @Override
    public void setShouldLoadAll(boolean shouldLoad) {
        wrappedFetchGroup.setShouldLoadAll(shouldLoad);
    }

    @Override
    public boolean shouldLoad() {
        return wrappedFetchGroup.shouldLoad();
    }

    @Override
    public boolean isFetchGroup() {
        return wrappedFetchGroup.isFetchGroup();
    }

    @Override
    public LoadGroup toLoadGroupLoadOnly() {
        return wrappedFetchGroup.toLoadGroupLoadOnly();
    }

    @Override
    public FetchGroup clone() {
        return new CubaEntityFetchGroup(wrappedFetchGroup.clone());
    }

    @Override
    public CoreAttributeGroup cloneWithSameAttributes(Map<CoreAttributeGroup<AttributeItem, ClassDescriptor>, CoreAttributeGroup<AttributeItem, ClassDescriptor>> cloneMap) {
        return new CubaEntityFetchGroup(wrappedFetchGroup.cloneWithSameAttributes());
    }

    @Override
    public LoadGroup toLoadGroup(Map<AttributeGroup, LoadGroup> cloneMap, boolean loadOnly) {
        return wrappedFetchGroup.toLoadGroup(cloneMap, loadOnly);
    }

    @Override
    public EntityFetchGroup getEntityFetchGroup(FetchGroupManager fetchGroupManager) {
        return wrappedFetchGroup.getEntityFetchGroup(fetchGroupManager);
    }

    @Override
    public FetchGroup getGroup(String attributeNameOrPath) {
        return wrappedFetchGroup.getGroup(attributeNameOrPath);
    }

    @Override
    public void addAttribute(String attributeNameOrPath, Collection<? extends CoreAttributeGroup> groups) {
        wrappedFetchGroup.addAttribute(attributeNameOrPath, groups);
    }

    @Override
    public void addAttributeKey(String attributeNameOrPath, CoreAttributeGroup group) {
        wrappedFetchGroup.addAttributeKey(attributeNameOrPath, group);
    }

    @Override
    public void addAttribute(String attributeNameOrPath, AttributeGroup group) {
        wrappedFetchGroup.addAttribute(attributeNameOrPath, group);
    }

    @Override
    public boolean isSupersetOf(AttributeGroup anotherGroup) {
        return wrappedFetchGroup.isSupersetOf(anotherGroup);
    }

    @Override
    public AttributeItem getItem(String attributeNameOrPath) {
        return wrappedFetchGroup.getItem(attributeNameOrPath);
    }

    @Override
    public AttributeGroup findGroup(ClassDescriptor type) {
        return wrappedFetchGroup.findGroup(type);
    }

    @Override
    public FetchGroup toFetchGroup() {
        return wrappedFetchGroup.toFetchGroup();
    }

    @Override
    public FetchGroup toFetchGroup(Map<AttributeGroup, FetchGroup> cloneMap) {
        return wrappedFetchGroup.toFetchGroup(cloneMap);
    }

    @Override
    public boolean isCopyGroup() {
        return wrappedFetchGroup.isCopyGroup();
    }

    @Override
    public CopyGroup toCopyGroup() {
        return wrappedFetchGroup.toCopyGroup();
    }

    @Override
    public CopyGroup toCopyGroup(Map<AttributeGroup, CopyGroup> cloneMap, Map copies) {
        return wrappedFetchGroup.toCopyGroup(cloneMap, copies);
    }

    @Override
    public boolean isLoadGroup() {
        return wrappedFetchGroup.isLoadGroup();
    }

    @Override
    public LoadGroup toLoadGroup() {
        return wrappedFetchGroup.toLoadGroup();
    }

    @Override
    public boolean isConcurrent() {
        return wrappedFetchGroup.isConcurrent();
    }

    @Override
    public void addAttribute(String attributeNameOrPath) {
        wrappedFetchGroup.addAttribute(attributeNameOrPath);
    }

    @Override
    public void addAttributes(Collection<String> attrOrPaths) {
        wrappedFetchGroup.addAttributes(attrOrPaths);
    }

    @Override
    public CoreAttributeGroup clone(Map<CoreAttributeGroup<AttributeItem, ClassDescriptor>, CoreAttributeGroup<AttributeItem, ClassDescriptor>> cloneMap) {
        return wrappedFetchGroup.clone(cloneMap);
    }

    @Override
    public boolean containsAttribute(String attributeNameOrPath) {
        return wrappedFetchGroup.containsAttribute(attributeNameOrPath);
    }

    @Override
    public boolean containsAttributeInternal(String attributeName) {
        return wrappedFetchGroup.containsAttributeInternal(attributeName);
    }

    @Override
    public void convertClassNamesToClasses(ClassLoader classLoader) {
        wrappedFetchGroup.convertClassNamesToClasses(classLoader);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CubaEntityFetchGroup that = (CubaEntityFetchGroup) o;

        return wrappedFetchGroup.equals(that.wrappedFetchGroup);
    }

    @Override
    public int hashCode() {
        return wrappedFetchGroup.hashCode();
    }

    @Override
    public Map<String, AttributeItem> getAllItems() {
        return wrappedFetchGroup.getAllItems();
    }

    @Override
    public Set<String> getAttributeNames() {
        return wrappedFetchGroup.getAttributeNames();
    }

    @Override
    public Map<String, AttributeItem> getItems() {
        return wrappedFetchGroup.getItems();
    }

    @Override
    public String getName() {
        return wrappedFetchGroup.getName();
    }

    @Override
    public Map<Object, CoreAttributeGroup> getSubClassGroups() {
        return wrappedFetchGroup.getSubClassGroups();
    }

    @Override
    public Class getType() {
        return wrappedFetchGroup.getType();
    }

    @Override
    public String getTypeName() {
        return wrappedFetchGroup.getTypeName();
    }

    @Override
    public boolean hasInheritance() {
        return wrappedFetchGroup.hasInheritance();
    }

    @Override
    public boolean hasItems() {
        return wrappedFetchGroup.hasItems();
    }

    @Override
    public void insertSubClass(CoreAttributeGroup group) {
        wrappedFetchGroup.insertSubClass(group);
    }

    @Override
    public boolean isValidated() {
        return wrappedFetchGroup.isValidated();
    }

    @Override
    public void setAllSubclasses(Map<Object, CoreAttributeGroup> subclasses) {
        wrappedFetchGroup.setAllSubclasses(subclasses);
    }

    @Override
    public void setAttributeNames(Set attributeNames) {
        wrappedFetchGroup.setAttributeNames(attributeNames);
    }

    @Override
    public void setName(String name) {
        wrappedFetchGroup.setName(name);
    }

    @Override
    public String toString() {
        return wrappedFetchGroup.toString();
    }
}