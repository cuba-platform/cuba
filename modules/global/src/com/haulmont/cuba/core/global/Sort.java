/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;

import java.io.Serializable;
import java.util.*;

/**
 * Defines sorting of queries.
 * <p>
 * Based on the {@code Sort} class from Spring Data project.
 */
public class Sort implements Serializable {

    /**
     * Undefined sort order.
     */
    public static final Sort UNSORTED = Sort.by(new Order[0]);

    private final List<Order> orders;

    /**
     * Creates new {@code Sort} for the given list of orders.
     */
    public static Sort by(List<Order> orders) {
        Preconditions.checkNotNullArgument(orders, "orders list is null");
        return orders.isEmpty() ? Sort.UNSORTED : new Sort(orders);
    }

    /**
     * Creates new {@code Sort} for the given orders.
     */
    public static Sort by(Order... orders) {
        Preconditions.checkNotNullArgument(orders, "orders list is null");
        return new Sort(Arrays.asList(orders));
    }

    /**
     * Creates new {@code Sort} for the given properties with ASC direction.
     */
    public static Sort by(String... properties) {
        Preconditions.checkNotNullArgument(properties, "properties list is null");
        return new Sort(Direction.ASC, Arrays.asList(properties));
    }

    /**
     * Creates new {@code Sort} for the given properties with the given direction.
     */
    public static Sort by(Direction direction, String... properties) {
        Preconditions.checkNotNullArgument(properties, "properties list is null");
        return new Sort(direction, Arrays.asList(properties));
    }

    protected Sort(List<Order> orders) {
        this.orders = Collections.unmodifiableList(orders);
    }

    protected Sort(Direction direction, List<String> properties) {
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("properties list is empty");
        }
        orders = new ArrayList<>(properties.size());
        for (String property : properties) {
            orders.add(new Order(direction, property));
        }
    }

    /**
     * Returns unmodifiable list of orders.
     */
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return orders.toString();
    }

    /**
     * Sort order defines a property and corresponding {@link Direction}.
     */
    public static class Order implements Serializable {

        private final Direction direction;
        private final String property;

        /**
         * Creates new order for the given property with ASC direction.
         */
        public static Order asc(String property) {
            return new Order(Direction.ASC, property);
        }

        /**
         * Creates new order for the given property with DESC direction.
         */
        public static Order desc(String property) {
            return new Order(Direction.DESC, property);
        }

        protected Order(Direction direction, String property) {
            this.direction = direction;
            this.property = property;
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }

        @Override
        public String toString() {
            return property + ": " + direction;
        }
    }

    /**
     * Sort direction.
     */
    public enum Direction {
        ASC,
        DESC
    }
}
