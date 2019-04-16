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

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Similar to {@link DataManager} but joins an existing transaction.
 * <p>
 * Use this bean in the middleware logic to perform some operations in one transaction, for example:
 * <pre>
 *     {@literal @}Inject
 *      private TransactionalDataManager txDataManager;
 *
 *     {@literal @}Transactional
 *      private void transfer(Id&lt;Account, UUID&gt; acc1Id, Id&lt;Account, UUID&gt; acc2Id, Long amount) {
 *          Account acc1 = txDataManager.load(acc1Id).one();
 *          Account acc2 = txDataManager.load(acc2Id).one();
 *          acc1.setBalance(acc1.getBalance() - amount);
 *          acc2.setBalance(acc2.getBalance() + amount);
 *          txDataManager.save(acc1);
 *          txDataManager.save(acc2);
 *      }
 * </pre>
 * Transactions can also be created/committed programmatically using the {@link Transactions} interface which is available
 * via {@link #transactions()} method.
 */
public interface TransactionalDataManager {

    String NAME = "cuba_TransactionalDataManager";

    /**
     * Entry point to the fluent API for loading entities.
     * <p>
     * Usage examples:
     * <pre>
     * Customer customer = txDataManager.load(Customer.class).id(someId).one();
     *
     * List&lt;Customer&gt; customers = txDataManager.load(Customer.class)
     *      .query("select c from sample$Customer c where c.name = :name")
     *      .parameter("name", "Smith")
     *      .view("customer-view")
     *      .list();
     * </pre>
     * @param entityClass   class of entity that needs to be loaded
     */
    <E extends Entity<K>, K> FluentLoader<E, K> load(Class<E> entityClass);

    /**
     * Entry point to the fluent API for loading entities.
     * <p>
     * Usage example:
     * <pre>
     * Customer customer = txDataManager.load(customerId).view("with-grade").one();
     * </pre>
     * @param entityId   {@link Id} of entity that needs to be loaded
     */
    <E extends Entity<K>, K> FluentLoader.ById<E, K> load(Id<E, K> entityId);

    /**
     * Entry point to the fluent API for loading scalar values.
     * <p>
     * Usage examples:
     * <pre>
     * List&lt;KeyValueEntity&gt; customerDataList = txDataManager.loadValues(
     *          "select c.name, c.status from sample$Customer c where c.name = :n")
     *      .properties("custName", "custStatus")
     *      .parameter("name", "Smith")
     *      .list();
     *
     * KeyValueEntity customerData = txDataManager.loadValues(
     *          "select c.name, count(c) from sample$Customer c group by c.name")
     *      .properties("custName", "custCount")
     *      .one();
     * </pre>
     * @param queryString   query string
     */
    FluentValuesLoader loadValues(String queryString);

    /**
     * Entry point to the fluent API for loading a single scalar value.
     * <p>
     * Terminal methods of this API ({@code list}, {@code one} and {@code optional}) return a single value
     * from the first column of the query result set. You should provide the expected type of this value in the second
     * parameter. Number types will be converted appropriately, so for example if the query returns Long and you
     * expected Integer, the returned value will be automatically converted from Long to Integer.
     * <p>
     * Usage examples:
     * <pre>
     * Long customerCount = txDataManager.loadValue(
     *          "select count(c) from sample$Customer c", Long.class).one();
     * </pre>
     * @param queryString   query string
     * @param valueClass    type of the returning value
     */
    <T> FluentValueLoader<T> loadValue(String queryString, Class<T> valueClass);

    /**
     * Loads a single entity instance.
     * @param context   {@link com.haulmont.cuba.core.global.LoadContext} object, defining what and how to load
     * @return          the loaded detached object, or null if not found
     */
    @Nullable
    @CheckReturnValue
    <E extends Entity> E load(LoadContext<E> context);

    /**
     * Loads collection of entity instances.
     * @param context   {@link LoadContext} object, defining what and how to load
     * @return          a list of detached instances, or empty list if nothing found
     */
    @CheckReturnValue
    <E extends Entity> List<E> loadList(LoadContext<E> context);

    /**
     * Loads list of key-value pairs.
     * @param context   defines a query for scalar values and a list of keys for returned KeyValueEntity
     * @return list of KeyValueEntity instances
     */
    @CheckReturnValue
    List<KeyValueEntity> loadValues(ValueLoadContext context);

    /**
     * Saves entity instances to the data store.
     * @param entities  entities to save
     * @return          set of saved instances
     */
    EntitySet save(Entity... entities);

    /**
     * Saves the entity to the data store.
     * @param entity    entity instance
     * @return          saved instance
     */
    <E extends Entity> E save(E entity);

    /**
     * Saves the entity to the data store.
     * @param entity    entity instance
     * @param view      view object which affects the returned instance
     * @return          saved instance fetched according to the given view
     */
    <E extends Entity> E save(E entity, @Nullable View view);

    /**
     * Saves the entity to the data store.
     * @param entity    entity instance
     * @param viewName  name of a view which affects the returned instance
     * @return          saved instance fetched according to the given view
     */
    <E extends Entity> E save(E entity, @Nullable String viewName);

    /**
     * Removes the entity instance from the data store.
     * @param entity    entity instance
     */
    void remove(Entity entity);

    /**
     * Creates a new entity instance. This is a shortcut to {@code Metadata.create()}.
     * @param entityClass   entity class
     */
    default <T> T create(Class<T> entityClass) {
        return AppBeans.get(Metadata.class).create(entityClass);
    }

    /**
     * Returns an entity instance which can be used as a reference to an object which exists in the database.
     * <p>
     * For example, if you are creating a User, you have to set a Group the user belongs to. If you know the group id,
     * you could load it from the database and set to the user. This method saves you from unneeded database round trip:
     * <pre>
     * user.setGroup(dataManager.getReference(Group.class, groupId));
     * dataManager.commit(user);
     * </pre>
     *
     * A reference can also be used to delete an existing object by id:
     * <pre>
     * dataManager.remove(dataManager.getReference(Customer.class, customerId));
     * </pre>
     *
     * @param entityClass   entity class
     * @param id            id of an existing object
     */
    default <T extends BaseGenericIdEntity<K>, K> T getReference(Class<T> entityClass, K id) {
        T entity = AppBeans.get(Metadata.class).create(entityClass);
        entity.setId(id);
        AppBeans.get(EntityStates.class).makePatch(entity);
        return entity;
    }

    /**
     * By default, DataManager does not apply security restrictions on entity operations and attributes, only row-level
     * constraints take effect.
     * <p>
     * This method returns the {@code TransactionalDataManager} implementation that applies security restrictions on entity operations.
     * Attribute permissions will be enforced only if you additionally set the {@code cuba.entityAttributePermissionChecking}
     * application property to true.
     * <p>
     * Usage example:
     * <pre>
     * txDataManager.secure().load(Customer.class).list();
     * </pre>
     */
    TransactionalDataManager secure();

    /**
     * Returns an entry point to programmatic transaction control.
     * <p>
     * Usage example:
     * <pre>
     * try (Transaction tx = txDataManager.transactions().create()) {
     *     // ...
     *     tx.commit();
     * }
     * </pre>
     */
    Transactions transactions();
}
