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

package com.haulmont.cuba.security.app.group;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.group.AccessGroupDefinition;
import com.haulmont.cuba.security.group.BasicAccessGroupDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Builder class that helps to create access group definition.
 */
@Component(AccessGroupDefinitionBuilder.NAME)
@Scope("prototype")
public class AccessGroupDefinitionBuilder {

    public static final String NAME = "cuba_AccessGroupDefinitionBuilder";

    protected AccessConstraintsBuilder accessConstraintsBuilder;

    protected String name;
    protected Map<String, Serializable> sessionAttributes;

    public static AccessGroupDefinitionBuilder create() {
        return AppBeans.getPrototype(AccessGroupDefinitionBuilder.NAME);
    }

    @PostConstruct
    protected void init() {
        accessConstraintsBuilder = AccessConstraintsBuilder.create();
    }

    /**
     * Specifies the group name type.
     *
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Adds JPQL READ constraint to the group definition
     *
     * @param target entity class
     * @param where  JPQL where clause
     * @param join   JPQL join clause
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withJpqlConstraint(Class<? extends Entity> target, String where, String join) {
        return withConstraints(builder -> builder.withJpql(target, where, join));
    }

    /**
     * Adds JPQL READ constraint to the group definition
     *
     * @param target entity class
     * @param where  JPQL where clause
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withJpqlConstraint(Class<? extends Entity> target, String where) {
        return withConstraints(builder -> builder.withJpql(target, where));
    }

    /**
     * Adds in-memory constraint to the group definition
     *
     * @param target    entity class
     * @param operation CRUD operation
     * @param predicate in-memory predicate, returns true if entity is allowed by access constraint
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withInMemoryConstraint(Class<? extends Entity> target, EntityOp operation, Predicate<? extends Entity> predicate) {
        return withConstraints(builder -> builder.withInMemory(target, operation, predicate));
    }

    /**
     * Adds in-memory custom constraint to the group definition
     *
     * @param target         entity class
     * @param constraintCode custom constraint code
     * @param predicate      in-memory predicate, returns true if entity is allowed by access constraint
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withCustomInMemoryConstraint(Class<? extends Entity> target, String constraintCode, Predicate<? extends Entity> predicate) {
        return withConstraints(builder -> builder.withCustomInMemory(target, constraintCode, predicate));
    }

    /**
     * Adds in-memory groovy constraint to the group definition
     *
     * @param target       entity class
     * @param operation    CRUD operation
     * @param groovyScript groovy script
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withGroovyConstraint(Class<? extends Entity> target, EntityOp operation, String groovyScript) {
        return withConstraints(builder -> builder.withGroovy(target, operation, groovyScript));
    }

    /**
     * Adds in-memory groovy custom constraint to the group definition
     *
     * @param target         entity class
     * @param constraintCode custom constraint code
     * @param groovyScript   groovy script
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withCustomGroovyConstraint(Class<? extends Entity> target, String constraintCode, String groovyScript) {
        return withConstraints(builder -> builder.withCustomGroovy(target, constraintCode, groovyScript));
    }

    /**
     * Adds access constraints to the group definition using {@link AccessConstraintsBuilder}
     *
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withConstraints(Consumer<AccessConstraintsBuilder> constraintsConsumer) {
        constraintsConsumer.accept(accessConstraintsBuilder);
        return this;
    }

    /**
     * Adds new session attribute to the group definition
     *
     * @param key   session attribute key
     * @param value session attribute value
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withSessionAttribute(String key, Serializable value) {
        if (sessionAttributes == null) {
            sessionAttributes = new HashMap<>();
        }
        sessionAttributes.put(key, value);
        return this;
    }

    /**
     * Adds new session attributes to the group definition
     *
     * @param attributes map of session attributes
     * @return current instance of the builder
     */
    public AccessGroupDefinitionBuilder withSessionAttributes(Map<String, Serializable> attributes) {
        if (sessionAttributes == null) {
            sessionAttributes = new HashMap<>();
        }
        sessionAttributes.putAll(attributes);
        return this;
    }

    /**
     * Returns the built access group definition
     */
    public AccessGroupDefinition build() {
        BasicAccessGroupDefinition groupDef = new BasicAccessGroupDefinition();
        groupDef.setName(name);
        groupDef.setEntityConstraints(accessConstraintsBuilder.build());
        groupDef.setSessionAttributes(sessionAttributes);
        return groupDef;
    }
}
