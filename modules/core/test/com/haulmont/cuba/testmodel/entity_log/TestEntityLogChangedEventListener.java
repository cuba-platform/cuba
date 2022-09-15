/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.testmodel.entity_log;

import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.global.View;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

@Component("test_TestEntityLogChangedEventListener")
public class TestEntityLogChangedEventListener {

    public static boolean enabled = false;

    @Inject
    private TransactionalDataManager dataManager;

    @EventListener
    void beforeCommit(EntityChangedEvent<EntityLogA, UUID> event) {
        if (enabled) {
            if (event.getType() == EntityChangedEvent.Type.CREATED) {
                EntityLogA entity = dataManager.load(event.getEntityId())
                        .view(View.BASE)
                        .optional()
                        .orElse(null);

                if (entity != null) {
                    entity.setDescription(entity.getName());
                    dataManager.save(entity);
                }
            }
        }
    }
}
