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
 */

package com.haulmont.cuba.security.app;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.haulmont.cuba.core.app.ServerConfig;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 */
@Component(BruteForceProtectionAPI.NAME)
public class BruteForceProtectionBean implements BruteForceProtectionAPI {

    @Inject
    protected ServerConfig serverConfig;

    protected LoadingCache<String, Integer> loginAttemptsCache;

    protected boolean initialized = false;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected void checkInitialized() {
        if (!initialized) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    protected void init() {
        loginAttemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(serverConfig.getBruteForceBlockIntervalSec(), TimeUnit.SECONDS)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
    }

    @Override
    public int loginAttemptsLeft(String login, String ipAddress) {
        lock.readLock().lock();
        try {
            checkInitialized();
            String cacheKey = makeCacheKey(login, ipAddress);
            Integer attemptsNumber = loginAttemptsCache.get(cacheKey);
            return serverConfig.getMaxLoginAttemptsNumber() - attemptsNumber;
        } catch (ExecutionException e) {
            throw new RuntimeException("BruteForceProtection error", e);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int registerUnsuccessfulLogin(String login, String ipAddress) {
        lock.readLock().lock();
        try {
            checkInitialized();
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            String cacheKey = makeCacheKey(login, ipAddress);
            Integer attemptsNumber = loginAttemptsCache.get(cacheKey);
            loginAttemptsCache.put(cacheKey, attemptsNumber + 1);
            return serverConfig.getMaxLoginAttemptsNumber() - (attemptsNumber + 1);
        } catch (ExecutionException e) {
            throw new RuntimeException("BruteForceProtection error", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<String> getLockedUsersInfo() {
        lock.readLock().lock();
        try {
            checkInitialized();
            int maxLoginAttemptsNumber = serverConfig.getMaxLoginAttemptsNumber();
            Set<String> result = new HashSet<>();
            for (Map.Entry<String, Integer> entry : loginAttemptsCache.asMap().entrySet()) {
                if (entry.getValue() >= maxLoginAttemptsNumber) result.add(entry.getKey());
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void unlockUser(String login, String ipAddress) {
        lock.readLock().lock();
        try {
            checkInitialized();
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            loginAttemptsCache.invalidate(makeCacheKey(login, ipAddress));
        } finally {
            lock.writeLock().unlock();
        }

    }

    @Override
    public boolean isBruteForceProtectionEnabled() {
        return serverConfig.getBruteForceProtectionEnabled();
    }

    @Override
    public int getBruteForceBlockIntervalSec() {
        return serverConfig.getBruteForceBlockIntervalSec();
    }

    protected String makeCacheKey(String login, String ipAddress) {
        return login + "|" + ipAddress;
    }
}
