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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.LockManagerAPI;
import com.haulmont.cuba.core.global.LockInfo;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

@Component("cuba_LockManagerMBean")
public class LockManager implements LockManagerMBean {

    @Inject
    protected LockManagerAPI lockManager;

    @Override
    public int getLockCount() {
        return lockManager.getCurrentLocks().size();
    }


    @Override
    public String showLocks() {
        StringBuilder sb = new StringBuilder();
        for (LockInfo lockInfo : lockManager.getCurrentLocks()) {
            sb.append(lockInfo).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void reloadConfiguration() {
        lockManager.reloadConfiguration();
    }
}