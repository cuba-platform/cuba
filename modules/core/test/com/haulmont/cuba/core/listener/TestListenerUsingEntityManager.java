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

package com.haulmont.cuba.core.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.Server;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.UUID;

@Component("cuba_TestListenerUsingEntityManager")
public class TestListenerUsingEntityManager
        implements BeforeInsertEntityListener<Server>, BeforeUpdateEntityListener<Server>, BeforeDeleteEntityListener<Server> {

    @Inject
    private Persistence persistence;

    @Override
    public void onBeforeInsert(Server entity) {
        EntityManager em = persistence.getEntityManager();

        FileDescriptor related = new FileDescriptor();
        related.setName("Related");
        System.out.println(">>>>> persist related: " + related.getId());
        em.persist(related);

        entity.setData(related.getId().toString());
    }

    @Override
    public void onBeforeUpdate(Server entity) {
        EntityManager em = persistence.getEntityManager();

        UUID relatedId = UUID.fromString(entity.getData());
        FileDescriptor related = em.find(FileDescriptor.class, relatedId);
        if (related != null) {
            related.setName("Related updated");
            System.out.println(">>>>> update related: " + relatedId);
        } else
            throw new RuntimeException("Related not found: " + relatedId);
    }

    @Override
    public void onBeforeDelete(Server entity) {
        EntityManager em = persistence.getEntityManager();

        UUID relatedId = UUID.fromString(entity.getData());
        FileDescriptor related = em.find(FileDescriptor.class, relatedId);
        if (related != null) {
            System.out.println(">>>>> remove related: " + relatedId);
            em.remove(related);
        } else
            throw new RuntimeException("Related entity not found" + relatedId);
    }
}