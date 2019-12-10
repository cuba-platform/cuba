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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.group.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Predicate;

/**
 * Builder class that helps to create access constraints set.
 */
@Component(AccessConstraintsBuilder.NAME)
@Scope("prototype")
public class AccessConstraintsBuilder {

    public static final String NAME = "cuba_AccessConstraintsBuilder";

    @Inject
    protected Metadata metadata;

    @Inject
    protected PersistenceSecurity security;

    protected List<SetOfAccessConstraints> joinSets = new ArrayList<>();
    protected Map<String, List<AccessConstraint>> builderConstraints = new HashMap<>();

    /**
     * @return new constraints builder
     */
    public static AccessConstraintsBuilder create() {
        return AppBeans.getPrototype(AccessConstraintsBuilder.NAME);
    }

    /**
     * Adds all existing entity constraints to the new constructed constraints set.
     *
     * @return current instance of the builder
     */
    public AccessConstraintsBuilder join(SetOfAccessConstraints constraints) {
        joinSets.add(constraints);
        return this;
    }

    /**
     * Adds JPQL READ constraint to the constraints set
     *
     * @param target entity class
     * @param where  JPQL where clause
     * @param join   JPQL join clause
     * @return current instance of the builder
     */
    public AccessConstraintsBuilder withJpql(Class<? extends Entity> target, String where, String join) {
        MetaClass metaClass = metadata.getClassNN(target);

        BasicJpqlAccessConstraint constraint = new BasicJpqlAccessConstraint();
        constraint.setOperation(EntityOp.READ);
        constraint.setEntityType(metaClass.getName());
        constraint.setWhere(where);
        constraint.setJoin(join);

        addConstraint(metaClass, constraint);

        return this;
    }

    /**
     * Adds JPQL READ constraint to the constraints set
     *
     * @param target entity class
     * @param where  JPQL where clause
     * @return current instance of the builder
     */
    public AccessConstraintsBuilder withJpql(Class<? extends Entity> target, String where) {
        return withJpql(target, where, null);
    }

    /**
     * Adds in-memory constraint to the constraints set
     *
     * @param target    entity class
     * @param operation CRUD operation
     * @param predicate in-memory predicate, returns true if entity is allowed by access constraint
     * @return current instance of the builder
     */
    public AccessConstraintsBuilder withInMemory(Class<? extends Entity> target, EntityOp operation, Predicate<? extends Entity> predicate) {
        MetaClass metaClass = metadata.getClassNN(target);

        BasicAccessConstraint constraint = new BasicAccessConstraint();
        constraint.setEntityType(metaClass.getName());
        constraint.setOperation(operation);
        constraint.setPredicate(predicate);

        addConstraint(metaClass, constraint);

        return this;
    }

    /**
     * Adds in-memory custom constraint to the constraints set
     *
     * @param target         entity class
     * @param constraintCode custom constraint code
     * @param predicate      in-memory predicate, returns true if entity is allowed by access constraint
     * @return current instance of the builder
     */
    public AccessConstraintsBuilder withCustomInMemory(Class<? extends Entity> target, String constraintCode, Predicate<? extends Entity> predicate) {
        MetaClass metaClass = metadata.getClassNN(target);

        BasicAccessConstraint constraint = new BasicAccessConstraint();
        constraint.setEntityType(metaClass.getName());
        constraint.setCode(constraintCode);
        constraint.setPredicate(predicate);

        addConstraint(metaClass, constraint);

        return this;
    }

    /**
     * Adds in-memory groovy constraint to the constraints set
     *
     * @param target       entity class
     * @param operation    CRUD operation
     * @param groovyScript groovy script
     * @return current instance of the builder
     */
    public AccessConstraintsBuilder withGroovy(Class<? extends Entity> target, EntityOp operation, String groovyScript) {
        MetaClass metaClass = metadata.getClassNN(target);

        BasicAccessConstraint constraint = new BasicAccessConstraint();
        constraint.setEntityType(metaClass.getName());
        constraint.setOperation(operation);
        constraint.setPredicate((Predicate<? extends Entity>) o -> (boolean) security.evaluateConstraintScript(o, groovyScript));

        addConstraint(metaClass, constraint);

        return this;
    }

    /**
     * Adds in-memory groovy custom constraint to the constraints set
     *
     * @param target         entity class
     * @param constraintCode custom constraint code
     * @param groovyScript   groovy script
     * @return current instance of the builder
     */
    public AccessConstraintsBuilder withCustomGroovy(Class<? extends Entity> target, String constraintCode, String groovyScript) {
        MetaClass metaClass = metadata.getClassNN(target);

        BasicAccessConstraint constraint = new BasicAccessConstraint();
        constraint.setEntityType(metaClass.getName());
        constraint.setCode(constraintCode);
        constraint.setPredicate((Predicate<? extends Entity>) o -> (boolean) security.evaluateConstraintScript(o, groovyScript));

        addConstraint(metaClass, constraint);

        return this;
    }

    /**
     * Returns the built set of entity constraints
     */
    public SetOfAccessConstraints build() {
        BasicSetOfAccessConstraints setOfEntityConstraints = new BasicSetOfAccessConstraints();

        Map<String, List<AccessConstraint>> resultConstraints = new HashMap<>();
        for (SetOfAccessConstraints joinSet : joinSets) {
            if (joinSet instanceof BasicSetOfAccessConstraints) {
                Map<String, List<AccessConstraint>> constraints = ((BasicSetOfAccessConstraints) joinSet).getConstraints();
                for (Map.Entry<String, List<AccessConstraint>> entry : constraints.entrySet()) {
                    resultConstraints.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
                }
            }
        }

        for (Map.Entry<String, List<AccessConstraint>> entry : builderConstraints.entrySet()) {
            resultConstraints.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
        }

        setOfEntityConstraints.setConstraints(resultConstraints);

        return setOfEntityConstraints;
    }

    protected void addConstraint(MetaClass metaClass, AccessConstraint constraint) {
        List<AccessConstraint> constraints = builderConstraints.computeIfAbsent(metaClass.getName(), k -> new ArrayList<>());

        if (constraint.isCustom()) {
            constraints.add(constraint);
        } else {
            AccessConstraint existingConstraint = constraints.stream()
                    .filter(c -> Objects.equals(c.getOperation(), constraint.getOperation()))
                    .findFirst()
                    .orElse(null);
            if (existingConstraint != null) {
                if (constraint instanceof JpqlAccessConstraint) {
                    if (existingConstraint instanceof JpqlAccessConstraint) {
                        constraints.add(constraint);
                    } else {
                        constraints.remove(existingConstraint);
                        constraints.add(constraint);

                        ((BasicJpqlAccessConstraint) constraint).setPredicate(existingConstraint.getPredicate());
                    }
                } else {
                    if (existingConstraint instanceof JpqlAccessConstraint && existingConstraint.getPredicate() == null) {
                        ((BasicJpqlAccessConstraint) existingConstraint).setPredicate(constraint.getPredicate());
                    } else {
                        constraints.add(constraint);
                    }
                }
            } else {
                constraints.add(constraint);
            }
        }
    }
}
