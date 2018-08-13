/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.sys.persistence;

import org.eclipse.persistence.descriptors.*;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;

import java.io.Serializable;
import java.util.List;

/**
 * INTERNAL.
 * EclipseLink's DescriptorEventManager that doesn't invoke listeners for base classes.
 */
@SuppressWarnings("unused")
public class DescriptorEventManagerWrapper extends DescriptorEventManager
        implements Cloneable, Serializable {

    protected final DescriptorEventManager delegate;

    public DescriptorEventManagerWrapper(DescriptorEventManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public void notifyListeners(DescriptorEvent event) {
        if (hasAnyListeners()) {
            for (int index = 0; index < getEventListeners().size(); index++) {
                DescriptorEventListener listener = (DescriptorEventListener) getEventListeners().get(index);
                notifyListener(listener, event);
            }
        }
    }

    // boilerplate

    public void remoteInitialization(AbstractSession session) {
        delegate.remoteInitialization(session);
    }

    public void removeListener(DescriptorEventListener listener) {
        delegate.removeListener(listener);
    }

    public void setAboutToDeleteSelector(String aboutToDeleteSelector) {
        delegate.setAboutToDeleteSelector(aboutToDeleteSelector);
    }

    public void setAboutToInsertSelector(String aboutToInsertSelector) {
        delegate.setAboutToInsertSelector(aboutToInsertSelector);
    }

    public void setAboutToUpdateSelector(String aboutToUpdateSelector) {
        delegate.setAboutToUpdateSelector(aboutToUpdateSelector);
    }

    public void setDescriptor(ClassDescriptor descriptor) {
        delegate.setDescriptor(descriptor);
    }

    public void setEntityEventListener(DescriptorEventListener listener) {
        delegate.setEntityEventListener(listener);
    }

    public void setExcludeDefaultListeners(boolean excludeDefaultListeners) {
        delegate.setExcludeDefaultListeners(excludeDefaultListeners);
    }

    public void setExcludeSuperclassListeners(boolean excludeSuperclassListeners) {
        delegate.setExcludeSuperclassListeners(excludeSuperclassListeners);
    }

    public void setPostBuildSelector(String postBuildSelector) {
        delegate.setPostBuildSelector(postBuildSelector);
    }

    public void setPostCloneSelector(String postCloneSelector) {
        delegate.setPostCloneSelector(postCloneSelector);
    }

    public void setPostDeleteSelector(String postDeleteSelector) {
        delegate.setPostDeleteSelector(postDeleteSelector);
    }

    public void setPostInsertSelector(String postInsertSelector) {
        delegate.setPostInsertSelector(postInsertSelector);
    }

    public void setPostMergeSelector(String postMergeSelector) {
        delegate.setPostMergeSelector(postMergeSelector);
    }

    public void setPostRefreshSelector(String postRefreshSelector) {
        delegate.setPostRefreshSelector(postRefreshSelector);
    }

    public void setPostUpdateSelector(String postUpdateSelector) {
        delegate.setPostUpdateSelector(postUpdateSelector);
    }

    public void setPostWriteSelector(String postWriteSelector) {
        delegate.setPostWriteSelector(postWriteSelector);
    }

    public void setPreDeleteSelector(String preDeleteSelector) {
        delegate.setPreDeleteSelector(preDeleteSelector);
    }

    public void setPreInsertSelector(String preInsertSelector) {
        delegate.setPreInsertSelector(preInsertSelector);
    }

    public void setPrePersistSelector(String prePersistSelector) {
        delegate.setPrePersistSelector(prePersistSelector);
    }

    public void setPreRemoveSelector(String preRemoveSelector) {
        delegate.setPreRemoveSelector(preRemoveSelector);
    }

    public void setPreUpdateSelector(String preUpdateSelector) {
        delegate.setPreUpdateSelector(preUpdateSelector);
    }

    public void setPreWriteSelector(String preWriteSelector) {
        delegate.setPreWriteSelector(preWriteSelector);
    }

    public void addDefaultEventListener(DescriptorEventListener listener) {
        delegate.addDefaultEventListener(listener);
    }

    public void addEntityListenerEventListener(DescriptorEventListener listener) {
        delegate.addEntityListenerEventListener(listener);
    }

    public void addListener(DescriptorEventListener listener) {
        delegate.addListener(listener);
    }

    public void addInternalListener(DescriptorEventListener listener) {
        delegate.addInternalListener(listener);
    }

    public void addEntityListenerHolder(SerializableDescriptorEventHolder holder) {
        delegate.addEntityListenerHolder(holder);
    }

    @Override
    public Object clone() {
        return delegate.clone();
    }

    public void processDescriptorEventHolders(AbstractSession session, ClassLoader classLoader) {
        delegate.processDescriptorEventHolders(session, classLoader);
    }

    public boolean excludeDefaultListeners() {
        return delegate.excludeDefaultListeners();
    }

    public boolean excludeSuperclassListeners() {
        return delegate.excludeSuperclassListeners();
    }

    @Override
    public void executeEvent(DescriptorEvent event) throws DescriptorException {
        delegate.executeEvent(event);
    }

    public String getAboutToDeleteSelector() {
        return delegate.getAboutToDeleteSelector();
    }

    public String getAboutToInsertSelector() {
        return delegate.getAboutToInsertSelector();
    }

    public String getAboutToUpdateSelector() {
        return delegate.getAboutToUpdateSelector();
    }

    public List<DescriptorEventListener> getDefaultEventListeners() {
        return delegate.getDefaultEventListeners();
    }

    public List<SerializableDescriptorEventHolder> getDescriptorEventHolders() {
        return delegate.getDescriptorEventHolders();
    }

    public void setDescriptorEventHolders(List<SerializableDescriptorEventHolder> descriptorEventHolders) {
        delegate.setDescriptorEventHolders(descriptorEventHolders);
    }

    public DescriptorEventListener getEntityEventListener() {
        return delegate.getEntityEventListener();
    }

    public List<DescriptorEventListener> getEntityListenerEventListeners() {
        return delegate.getEntityListenerEventListeners();
    }

    public List<DescriptorEventListener> getEventListeners() {
        return delegate.getEventListeners();
    }

    public String getPostBuildSelector() {
        return delegate.getPostBuildSelector();
    }

    public String getPostCloneSelector() {
        return delegate.getPostCloneSelector();
    }

    public String getPostDeleteSelector() {
        return delegate.getPostDeleteSelector();
    }

    public String getPostInsertSelector() {
        return delegate.getPostInsertSelector();
    }

    public String getPostMergeSelector() {
        return delegate.getPostMergeSelector();
    }

    public String getPostRefreshSelector() {
        return delegate.getPostRefreshSelector();
    }

    public String getPostUpdateSelector() {
        return delegate.getPostUpdateSelector();
    }

    public String getPostWriteSelector() {
        return delegate.getPostWriteSelector();
    }

    public String getPrePersistSelector() {
        return delegate.getPrePersistSelector();
    }

    public String getPreDeleteSelector() {
        return delegate.getPreDeleteSelector();
    }

    public String getPreInsertSelector() {
        return delegate.getPreInsertSelector();
    }

    public String getPreRemoveSelector() {
        return delegate.getPreRemoveSelector();
    }

    public String getPreUpdateSelector() {
        return delegate.getPreUpdateSelector();
    }

    public String getPreWriteSelector() {
        return delegate.getPreWriteSelector();
    }

    @Override
    public boolean hasAnyEventListeners() {
        return delegate.hasAnyEventListeners();
    }

    public boolean hasDefaultEventListeners() {
        return delegate.hasDefaultEventListeners();
    }

    public boolean hasEntityEventListener() {
        return delegate.hasEntityEventListener();
    }

    public boolean hasInternalEventListeners() {
        return delegate.hasInternalEventListeners();
    }

    public boolean hasEntityListenerEventListeners() {
        return delegate.hasEntityListenerEventListeners();
    }

    public void initialize(AbstractSession session) {
        delegate.initialize(session);
    }
}
