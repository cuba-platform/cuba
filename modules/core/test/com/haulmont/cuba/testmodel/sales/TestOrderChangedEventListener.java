/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.testmodel.sales;

import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.global.View;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.util.UUID;

@Component
public class TestOrderChangedEventListener {

    public boolean enabled;

    @Inject
    private TransactionalDataManager tdm;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onOrderChanged(EntityChangedEvent<Order, UUID> event) {
        if (!enabled)
            return;

        switch (event.getType()) {
            case DELETED: return;
            case CREATED:
            case UPDATED: {
                Order order = tdm.load(event.getEntityId())
                        .view(View.LOCAL)
                        .one();
                order.setNumber(order.getNumber() + "changed");
                tdm.save(order);
            }
        }

    }
}
