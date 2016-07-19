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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Stores;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerContextHolder {

    private ThreadLocal<EntityManagerContext> mainHolder = new ThreadLocal<>();

    private Map<String, ThreadLocal<EntityManagerContext>> additionalHolders = new HashMap<>();

    public EntityManagerContext get(String storage) {
        if (Stores.isMain(storage))
            return mainHolder.get();
        else {
            ThreadLocal<EntityManagerContext> holder = additionalHolders.get(storage);
            if (holder == null)
                return null;
            else
                return holder.get();
        }
    }

    public void set(EntityManagerContext context, String storage) {
        if (Stores.isMain(storage))
            mainHolder.set(context);
        else {
            ThreadLocal<EntityManagerContext> holder = additionalHolders.get(storage);
            if (holder == null) {
                holder = new ThreadLocal<>();
                additionalHolders.put(storage, holder);
            }
            holder.set(context);
        }
    }

    public void remove(String storage) {
        if (Stores.isMain(storage))
            mainHolder.remove();
        else {
            ThreadLocal<EntityManagerContext> holder = additionalHolders.get(storage);
            if (holder != null)
                holder.remove();
        }
    }
}
