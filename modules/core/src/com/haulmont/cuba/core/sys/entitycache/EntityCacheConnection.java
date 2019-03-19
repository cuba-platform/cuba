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

package com.haulmont.cuba.core.sys.entitycache;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import org.eclipse.persistence.internal.helper.Helper;
import org.eclipse.persistence.internal.sessions.UnitOfWorkChangeSet;
import org.eclipse.persistence.internal.sessions.coordination.broadcast.BroadcastRemoteConnection;
import org.eclipse.persistence.sessions.coordination.MergeChangeSetCommand;
import org.eclipse.persistence.sessions.coordination.RemoteCommandManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EntityCacheConnection extends BroadcastRemoteConnection {

    protected Metadata metadata;
    protected QueryCacheManager queryCacheManager;
    protected ClusterManagerAPI clusterManager;

    public EntityCacheConnection(RemoteCommandManager rcm, ClusterManagerAPI clusterManager) {
        super(rcm);
        this.metadata = AppBeans.get(Metadata.NAME);
        this.queryCacheManager = AppBeans.get(QueryCacheManager.NAME);
        this.clusterManager = clusterManager;
        rcm.logDebug("creating_broadcast_connection", getInfo());
        try {
            this.clusterManager.addListener(Message.class, new ClusterListenerAdapter<Message>() {
                @Override
                public void receive(Message message) {
                    onMessage(message);
                }
            });
            rcm.logDebug("broadcast_connection_created", getInfo());
        } catch (RuntimeException ex) {
            rcm.logDebug("failed_to_create_broadcast_connection", getInfo());
            close();
            throw ex;
        }
    }

    public EntityCacheConnection(RemoteCommandManager rcm) {
        super(rcm);
    }

    public boolean isLocal() {
        return true;
    }

    @Override
    protected Object executeCommandInternal(Object command) {
        Message message = new Message(command);

        Object[] debugInfo = null;
        if (this.rcm.shouldLogDebugMessage()) {
            debugInfo = logDebugBeforePublish(null);
        }

        if (queryCacheManager.isEnabled()) {
            invalidateQueryCache(command);
        }
        this.clusterManager.send(message);

        if (debugInfo != null) {
            logDebugAfterPublish(debugInfo, null);
        }

        return null;
    }

    public void onMessage(Message message) {
        if (rcm.shouldLogDebugMessage()) {
            logDebugOnReceiveMessage(null);
        }
        if (message.getObject() != null) {
            Object command = message.getObject();
            if (queryCacheManager.isEnabled()) {
                invalidateQueryCache(command);
            }
            processReceivedObject(command, "");
        }
    }

    @Override
    protected boolean areAllResourcesFreedOnClose() {
        return !isLocal();
    }

    @Override
    protected void closeInternal() {
    }

    @Override
    protected void createDisplayString() {
        this.displayString = Helper.getShortClassName(this) + "[" + serviceId.toString() + "]";
    }

    @Override
    protected boolean shouldCheckServiceId() {
        return false;
    }

    protected void invalidateQueryCache(Object command) {
        if (command instanceof MergeChangeSetCommand) {
            MergeChangeSetCommand changeSetCommand = (MergeChangeSetCommand) command;
            UnitOfWorkChangeSet changeSet = changeSetCommand.getChangeSet(null);
            if (changeSet != null && changeSet.getAllChangeSets() != null) {
                Set<String> typeNames = new HashSet<>();
                changeSet.getAllChangeSets().values().stream().filter(obj -> obj.getClassName() != null).forEach(obj -> {
                    MetaClass metaClass = metadata.getClass(ReflectionHelper.getClass(obj.getClassName()));
                    if (metaClass != null) {
                        metaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);
                        typeNames.add(metaClass.getName());
                    }
                });
                queryCacheManager.invalidate(typeNames, false);
            }
        }
    }

    public static class Message implements Serializable {

        private Object object;

        public Message(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        @Override
        public String toString() {
            return String.format("Message{object=%s}", object);
        }
    }
}