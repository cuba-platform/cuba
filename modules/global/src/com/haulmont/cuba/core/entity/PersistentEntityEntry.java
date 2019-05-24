/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.entity;

import java.io.IOException;

import static com.haulmont.cuba.core.entity.BaseEntityInternalAccess.*;

public abstract class PersistentEntityEntry<K> implements EntityEntry<K> {
    protected byte state = BaseEntityInternalAccess.NEW;
    protected SecurityState securityState;
    protected Entity entity;

    public PersistentEntityEntry(Entity entity) {
        this.entity = entity;
    }

    public abstract void setId(K id);

    public byte getPersistenceState() {
        return state;
    }

    public void setPersistenceState(byte state) {
        this.state = state;
    }

    public SecurityState getSecurityState() {
        return securityState;
    }

    public void setSecurityState(SecurityState securityState) {
        this.securityState = securityState;
    }

    public void copySystemState(PersistentEntityEntry entityEntry) {
        setPersistenceState(entityEntry.getPersistenceState());
        setSecurityState(entityEntry.getSecurityState());
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (isManaged(entity)) {
            setManaged(entity, false);
            setDetached(entity, true);
        }
        out.defaultWriteObject();
    }
}
