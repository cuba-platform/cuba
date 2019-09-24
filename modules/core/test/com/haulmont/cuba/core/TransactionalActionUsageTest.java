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

package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.testmodel.sales_1.Product;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionalActionUsageTest {

    private static String messageFromOnFailAction;
    private static String messageFromAfterCommitAction;

    private TransactionalActionFactory transactionalActionFactory;
    private TransactionalDataManager transactionalDataManager;
    private Metadata metadata;

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @BeforeEach
    public void setUp() throws Exception {
        transactionalActionFactory = AppBeans.get(TransactionalActionFactory.NAME);
        transactionalDataManager = AppBeans.get(TransactionalDataManager.NAME);
        metadata = AppBeans.get(Metadata.class);

        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SALES1_ORDER_LINE");
        runner.update("delete from SALES1_PRODUCT");
    }

    @Test
    public void transactionalActionsAreDifferent() {
        assertNotEquals(transactionalActionFactory.getTransactionalAction(), transactionalActionFactory.getTransactionalAction());
    }

    @Test
    public void testOnSuccessAction() {
        EntitySet entities = transactionalActionFactory
                .getTransactionalAction()
                .withCommitContext(() -> {
                    CommitContext cc = new CommitContext();
                    Product p = metadata.create(Product.class);
                    p.setName("test");
                    p.setQuantity(100);
                    cc.addInstanceToCommit(p);
                    return cc;
                })
                .onSuccess(es -> {
                    es.stream()
                            .filter(e -> Product.class.equals(e.getMetaClass().getJavaClass()))
                            .forEach(e -> ((Product) e).setName("newName"));
                })
                .perform();

        Product product = (Product) entities.stream()
                .filter(e -> Product.class.equals(e.getMetaClass().getJavaClass()))
                .findFirst().orElse(null);

        assertNotNull(product);
        assertEquals("newName", product.getName());

        Product productFromDb = transactionalDataManager.load(Id.of(product)).one();

        assertNotNull(productFromDb);
        assertEquals("test", productFromDb.getName());
    }

    @Test
    public void testOnFailAction() {
        EntitySet entities = transactionalActionFactory
                .getTransactionalAction()
                .withCommitContext(() -> {
                    CommitContext cc = new CommitContext();
                    Product p = metadata.create(Product.class);
                    p.setName("test");
                    p.setQuantity(100);
                    p.setId(null);
                    cc.addInstanceToCommit(p);
                    return cc;
                })
                .onFail((cc, t) -> {
                    messageFromOnFailAction = "[testOnFailAction] commit failed";
                })
                .perform();

        assertNull(entities);
        assertEquals("[testOnFailAction] commit failed", messageFromOnFailAction);
    }

    @Test
    public void testAfterCommitAction() {
        TransactionalAction transactionalAction = transactionalActionFactory
                .getTransactionalAction()
                .withCommitContext(() -> {
                    CommitContext cc = new CommitContext();
                    Product p = metadata.create(Product.class);
                    p.setName("test");
                    p.setQuantity(100);
                    p.setId(null);
                    cc.addInstanceToCommit(p);
                    return cc;
                })
                .afterCompletion(cc -> {
                    messageFromAfterCommitAction = "[testAfterCommitAction] transaction ended";
                });

        Throwable t = null;
        try {
            transactionalAction.perform();
        } catch (Throwable throwable) {
            t = throwable;
        }

        assertNotNull(t);
        assertEquals("[testAfterCommitAction] transaction ended", messageFromAfterCommitAction);
    }

    @Test
    public void testAllActions() {
        EntitySet entities = transactionalDataManager
                .commitAction(() -> {
                    CommitContext cc = new CommitContext();
                    Product p = transactionalDataManager.create(Product.class);
                    p.setName("allActionsTest");
                    p.setQuantity(100);
                    cc.addInstanceToCommit(p);
                    return cc;
                })
                .onSuccess(es -> {
                    es.stream()
                            .filter(e -> Product.class.equals(e.getMetaClass().getJavaClass()))
                            .forEach(e -> ((Product) e).setName("onSuccessName"));
                })
                .onFail((cc, t) -> {
                    messageFromOnFailAction = "[testAllActions] commit failed";
                })
                .afterCompletion(cc -> {
                    messageFromAfterCommitAction = "[testAllActions] transaction ended";
                })
                .perform();

        Product product = (Product) entities.stream()
                .filter(e -> Product.class.equals(e.getMetaClass().getJavaClass()))
                .findFirst().orElse(null);

        assertNotNull(product);
        assertEquals("onSuccessName", product.getName());

        Product productFromDb = transactionalDataManager.load(Id.of(product)).one();

        assertNotNull(productFromDb);
        assertEquals("allActionsTest", productFromDb.getName());

        assertNotEquals("[testAllActions] commit failed", messageFromOnFailAction);

        assertEquals("[testAllActions] transaction ended", messageFromAfterCommitAction);

    }

}
