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
package com.haulmont.cuba.core.app;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.LockDescriptor;
import com.haulmont.cuba.core.global.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
@Component(LockManagerAPI.NAME)
public class LockManager implements LockManagerAPI, ClusterListener<LockInfo> {

    private static class LockKey {
        
        private final String name;
        private final String id;

        private LockKey(String name, String id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LockKey key = (LockKey) o;

            if (id != null ? !id.equals(key.id) : key.id != null) return false;
            if (!name.equals(key.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + (id != null ? id.hashCode() : 0);
            return result;
        }
    }

    private Logger log = LoggerFactory.getLogger(LockManager.class);

    private volatile Map<String, LockDescriptor> config;

    private Map<LockKey, LockInfo> locks = new ConcurrentHashMap<>();

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Inject
    private UserSessionSource userSessionSource;

    private ClusterManagerAPI clusterManager;

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        this.clusterManager.addListener(LockInfo.class, this);
    }

    private Map<String, LockDescriptor> getConfig() {
        if (this.config == null) {
            synchronized (this) {
                if (this.config == null) {
                    Map<String, LockDescriptor> config = new ConcurrentHashMap<>();

                    Transaction tx = persistence.createTransaction();
                    try {
                        EntityManager em = persistence.getEntityManager();
                        TypedQuery<LockDescriptor> q = em.createQuery(
                                "select d from sys$LockDescriptor d", LockDescriptor.class);
                        List<LockDescriptor> list = q.getResultList();
                        for (LockDescriptor ld : list) {
                            config.put(ld.getName(), ld);
                        }
                        tx.commit();
                    } finally {
                        tx.end();
                    }
                    this.config = config;
                }
            }
        }
        return config;
    }

    @Override
    public LockInfo lock(String name, String id) {
        LockKey key = new LockKey(name, id);

        LockInfo lockInfo = locks.get(key);
        if (lockInfo != null) {
            log.debug("Already locked: " + lockInfo);
            return lockInfo;
        }

        LockDescriptor ld = getConfig().get(name);
        if (ld == null) {
            return new LockNotSupported();
        }

        lockInfo = new LockInfo(userSessionSource.getUserSession().getCurrentOrSubstitutedUser(), name, id);
        locks.put(key, lockInfo);
        log.debug("Locked " + name + "/" + id);

        clusterManager.send(lockInfo);

        return null;
    }

    @Nullable
    @Override
    public LockInfo lock(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");

        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);

        return lock(originalMetaClass.getName(), entity.getId().toString());
    }

    @Override
    public void unlock(String name, String id) {
        LockInfo lockInfo = locks.remove(new LockKey(name, id));
        if (lockInfo != null) {
            log.debug("Unlocked " + name + "/" + id);

            clusterManager.send(new LockInfo(null, name, id));
        }
    }

    @Override
    public void unlock(Entity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");

        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metaClass);

        unlock(originalMetaClass.getName(), entity.getId().toString());
    }

    @Override
    public LockInfo getLockInfo(String name, String id) {
        LockDescriptor ld = getConfig().get(name);
        if (ld == null) {
            return new LockNotSupported();
        }

        return locks.get(new LockKey(name, id));
    }

    @Override
    public List<LockInfo> getCurrentLocks() {
        return new ArrayList<>(locks.values());
    }

    @Override
    public void expireLocks() {
        log.debug("Expiring locks");
        ArrayList<LockKey> list = new ArrayList<>(locks.keySet());
        for (LockKey key : list) {
            LockInfo lockInfo = locks.get(key);
            if (lockInfo != null) {
                LockDescriptor ld = getConfig().get(key.name);
                if (ld == null) {
                    log.debug("Lock " + key.name + "/" + key.id + " configuration not found, remove it");
                    locks.remove(key);
                } else {
                    Integer timeoutSec = ld.getTimeoutSec();
                    if (timeoutSec != null && timeoutSec > 0) {
                        Date since = lockInfo.getSince();
                        if (since.getTime() + timeoutSec * 1000 < AppBeans.get(TimeSource.class).currentTimestamp().getTime()) {
                            log.debug("Lock " + key.name + "/" + key.id + " expired");
                            locks.remove(key);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void reloadConfiguration() {
        config = null;
    }

    @Override
    public void receive(LockInfo message) {
        LockKey key = new LockKey(message.getEntityName(), message.getEntityId());
        if (message.getUser() != null) {
            LockInfo lockInfo = locks.get(key);
            if (lockInfo == null || lockInfo.getSince().before(message.getSince())) {
                locks.put(key, message);
            }
        } else {
            locks.remove(key);
        }
    }

    @Override
    public byte[] getState() {
        List<LockInfo> list = new ArrayList<>(locks.values());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(list);
        } catch (IOException e) {
            log.error("Error serializing LockInfo list", e);
            return new byte[0];
        }
        return bos.toByteArray();
    }

    @Override
    public void setState(byte[] state) {
        if (state == null || state.length == 0)
            return;

        List<LockInfo> list;
        ByteArrayInputStream bis = new ByteArrayInputStream(state);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            list = (List<LockInfo>) ois.readObject();
        } catch (Exception e) {
            log.error("Error deserializing LockInfo list", e);
            return;
        }

        for (LockInfo lockInfo : list) {
            receive(lockInfo);
        }
    }
}