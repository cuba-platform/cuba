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

import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManager;
import org.eclipse.persistence.internal.helper.Helper;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.internal.sessions.coordination.broadcast.BroadcastRemoteConnection;
import org.eclipse.persistence.sessions.coordination.RemoteCommandManager;
import org.eclipse.persistence.sessions.serializers.Serializer;

import java.io.Serializable;

public class EntityCacheRemoteConnection extends BroadcastRemoteConnection {

    private ClusterManager clusterManager;

    public EntityCacheRemoteConnection(RemoteCommandManager rcm, ClusterManager clusterManager) {
        super(rcm);
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
    
    public EntityCacheRemoteConnection(RemoteCommandManager rcm){
        super(rcm);
    }

    public boolean isLocal() {
        return true;
    }
    
    @Override
    protected Object executeCommandInternal(Object command) throws Exception {
        Message message = new Message(command);

        Object[] debugInfo = null;
        if(this.rcm.shouldLogDebugMessage()) {
            debugInfo = logDebugBeforePublish(null);
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

        Object object;
        try {
            Serializer serializer = this.rcm.getSerializer();
            if (serializer != null) {
                object = serializer.deserialize(message.getObject(), (AbstractSession)this.rcm.getCommandProcessor());
            } else {
                object = message.getObject();            
            }
        } catch (Exception exception) {
            failDeserializeMessage(null, exception);
            return;
        }
        
        processReceivedObject(object, "");
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
            return "Message{" +
                    "object=" + object +
                    '}';
        }
    }
}