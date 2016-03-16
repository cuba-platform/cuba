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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LockInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

/**
 */
@Service(LockService.NAME)
public class LockServiceBean implements LockService {

    @Inject
    private LockManagerAPI lockManager;

    @Override
    public LockInfo lock(String name, String id) {
        return lockManager.lock(name, id);
    }

    @Nullable
    @Override
    public LockInfo lock(Entity entity) {
        return lockManager.lock(entity);
    }

    @Override
    public void unlock(String name, String id) {
        lockManager.unlock(name, id);
    }

    @Override
    public void unlock(Entity entity) {
        lockManager.unlock(entity);
    }

    @Override
    public LockInfo getLockInfo(String name, String id) {
        return lockManager.getLockInfo(name, id);
    }

    @Override
    public List<LockInfo> getCurrentLocks() {
        return lockManager.getCurrentLocks();
    }

    @Override
    public void reloadConfiguration(){
        lockManager.reloadConfiguration();
    }
}