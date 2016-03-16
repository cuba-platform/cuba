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
package com.haulmont.cuba.testsupport;

import javax.transaction.*;
import javax.transaction.xa.XAResource;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Connection;

/**
 */
public class TestTransaction implements Transaction {

    private int status = Status.STATUS_ACTIVE;

    private List<Synchronization> syncs = new ArrayList<>();

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, SystemException {
        if (status != Status.STATUS_ACTIVE)
            throw new SystemException("Unable to commit: invalid tx status: " + status);

        Connection conn = null;
        for (Synchronization sync : syncs) {
            sync.beforeCompletion();
        }
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        status = Status.STATUS_COMMITTED;
        for (Synchronization sync : syncs) {
            sync.afterCompletion(Status.STATUS_COMMITTED);
        }
    }

    @Override
    public void rollback() throws IllegalStateException, SystemException {
        if (status != Status.STATUS_ACTIVE && status != Status.STATUS_MARKED_ROLLBACK)
            throw new SystemException("Unable to rollback: invalid tx status: " + status);

        status = Status.STATUS_ROLLEDBACK;
        for (Synchronization sync : syncs) {
            sync.afterCompletion(Status.STATUS_ROLLEDBACK);
        }
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        status = Status.STATUS_MARKED_ROLLBACK;
    }

    @Override
    public int getStatus() throws SystemException {
        return status;
    }

    @Override
    public boolean enlistResource(XAResource xaResource) throws RollbackException, IllegalStateException, SystemException {
        return false;
    }

    @Override
    public boolean delistResource(XAResource xaResource, int i) throws IllegalStateException, SystemException {
        return false;
    }

    @Override
    public void registerSynchronization(Synchronization synchronization) throws RollbackException, IllegalStateException, SystemException {
        syncs.add(synchronization);
    }

    @Override
    public String toString() {
        return super.toString() + ", status=" + status;
    }
}