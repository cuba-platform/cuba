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

package com.haulmont.cuba.testmodel.entitychangedevent;

import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@Component("test_TestProductChangeListener")
public class TestProductChangeListener {

    public boolean doLog;

    @Inject
    private TransactionalDataManager tdm;

    @Inject
    private DataManager dm;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void beforeCommit(EntityChangedEvent<EceTestProduct, UUID> event) {
        if (event.getType() == EntityChangedEvent.Type.DELETED)
            return;

        EceTestProduct product = tdm.load(event.getEntityId()).one();
        if (product.getName() == null) {
            product.setName("default name");
        }
        tdm.save(product);

        if (doLog) {
            EceTestLogEntry logEntry = dm.create(EceTestLogEntry.class);
            logEntry.setMessage("Saving product: " + product);
            dm.commit(logEntry);
        }

        EceTestStock stock;
        Optional<EceTestStock> optStock = tdm.load(EceTestStock.class)
                .query("select e from test_EceTestStock e where e.product = :product")
                .parameter("product", product)
                .optional();
        if (!optStock.isPresent()) {
            stock = tdm.create(EceTestStock.class);
            stock.setProduct(product);
        } else {
            stock = optStock.get();
        }
        stock.setQuantity(stock.getQuantity() + 1);
        tdm.save(stock);
    }

}
