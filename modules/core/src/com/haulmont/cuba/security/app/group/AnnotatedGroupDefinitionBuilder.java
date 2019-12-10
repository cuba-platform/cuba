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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.security.app.group.annotation.*;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.group.AccessGroupDefinition;
import com.haulmont.cuba.security.group.SetOfAccessConstraints;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Component(AnnotatedGroupDefinitionBuilder.NAME)
public class AnnotatedGroupDefinitionBuilder {

    public static final String NAME = "cuba_AnnotatedGroupDefinitionBuilder";

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected DatatypeRegistry datatypes;

    protected Map<Class<? extends Annotation>, AnnotationProcessor> processors = new HashMap<>();

    protected static final Set<String> FILTERED_METHOD_NAMES = ImmutableSet.of("getName", "sessionAttributes");

    protected interface AnnotationProcessor<T extends AnnotationContext> {
        void processAnnotation(T annotationContext);
    }

    protected class AnnotationContext {
        protected Annotation annotation;
        protected Method method;
        protected Class ownerClass;
        protected AccessGroupDefinition owner;

        public AnnotationContext(Annotation annotation,
                                 Method method,
                                 AccessGroupDefinition owner) {
            this.annotation = annotation;
            this.method = method;
            this.owner = owner;
            this.ownerClass = owner.getClass();
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public Method getMethod() {
            return method;
        }

        public Class getOwnerClass() {
            return ownerClass;
        }

        public AccessGroupDefinition getOwner() {
            return owner;
        }
    }

    protected class ConstraintsAnnotationContext extends AnnotationContext {
        protected AccessConstraintsBuilder constraintsBuilder;
        protected Map<Method, Predicate<Entity>> predicateCache;

        public ConstraintsAnnotationContext(Annotation annotation,
                                            Method method,
                                            AccessGroupDefinition owner,
                                            AccessConstraintsBuilder constraintsBuilder,
                                            Map<Method, Predicate<Entity>> predicateCache) {
            super(annotation, method, owner);
            this.constraintsBuilder = constraintsBuilder;
            this.predicateCache = predicateCache;
        }

        public AccessConstraintsBuilder getConstraintsBuilder() {
            return constraintsBuilder;
        }

        public Map<Method, Predicate<Entity>> getPredicateCache() {
            return predicateCache;
        }
    }

    protected class SessionAttributesContext extends AnnotationContext {
        Map<String, Serializable> sessionAttributes;

        public SessionAttributesContext(Annotation annotation,
                                        Method method,
                                        AccessGroupDefinition owner,
                                        Map<String, Serializable> sessionAttributes) {
            super(annotation, method, owner);
            this.sessionAttributes = sessionAttributes;
        }

        public Map<String, Serializable> getSessionAttributes() {
            return sessionAttributes;
        }
    }

    @PostConstruct
    protected void init() {
        registerAnnotationProcessor(JpqlConstraint.class, new JpqlAnnotationProcessor());
        registerAnnotationProcessor(Constraint.class, new ConstraintAnnotationProcessor());
        registerAnnotationProcessor(CustomConstraint.class, new CustomConstraintAnnotationProcessor());
        registerAnnotationProcessor(SessionAttribute.class, new SessionAttributesAnnotationProcessor());
    }

    public String getNameFromAnnotation(AccessGroupDefinition group) {
        return getGroupAnnotation(group.getClass()).name();
    }

    public String getParentFromAnnotation(AccessGroupDefinition group) {
        Class<? extends AccessGroupDefinition> parentClazz = getGroupAnnotation(group.getClass()).parent();
        if (AccessGroupDefinition.class.equals(parentClazz)) {
            return null;
        }
        AccessGroup ann = getGroupAnnotationOrNull(parentClazz);
        return ann == null ? null : ann.name();
    }

    public SetOfAccessConstraints buildSetOfAccessConstraints(AccessGroupDefinition group) {
        Class<? extends AccessGroupDefinition> clazz = group.getClass();

        AccessConstraintsBuilder constraintsBuilder = AccessConstraintsBuilder.create();
        Map<Method, Predicate<Entity>> predicateCache = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (isConstraintMethod(method)) {
                for (Class<? extends Annotation> annotationType : getAvailableAnnotationTypes()) {
                    for (Annotation annotation : method.getAnnotationsByType(annotationType)) {
                        AnnotationProcessor<ConstraintsAnnotationContext> processor = findAnnotationProcessor(annotationType);
                        if (processor != null) {
                            processor.processAnnotation(new ConstraintsAnnotationContext(annotation, method, group, constraintsBuilder, predicateCache));
                        }
                    }
                }
            }
        }

        return constraintsBuilder.build();
    }

    public Map<String, Serializable> buildSessionAttributes(AccessGroupDefinition group) {
        Class<? extends AccessGroupDefinition> clazz = group.getClass();

        Map<String, Serializable> sessionAttributes = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (isSessionAttributesMethod(method)) {
                for (Class<? extends Annotation> annotationType : getAvailableAnnotationTypes()) {
                    for (Annotation annotation : method.getAnnotationsByType(annotationType)) {
                        AnnotationProcessor<SessionAttributesContext> processor = findAnnotationProcessor(annotationType);
                        if (processor != null) {
                            processor.processAnnotation(new SessionAttributesContext(annotation, method, group, sessionAttributes));
                        }
                    }
                }
            }
        }

        return sessionAttributes;
    }

    protected <T extends AnnotationContext> AnnotationProcessor<T> findAnnotationProcessor(Class<? extends Annotation> annotationType) {
        //noinspection unchecked
        return (AnnotationProcessor<T>) processors.get(annotationType);
    }

    protected Set<Class<? extends Annotation>> getAvailableAnnotationTypes() {
        return processors.keySet();
    }

    protected void registerAnnotationProcessor(Class<? extends Annotation> annotation, AnnotationProcessor processor) {
        processors.put(annotation, processor);
    }

    protected boolean isConstraintMethod(Method method) {
        return !FILTERED_METHOD_NAMES.contains(method.getName());
    }

    protected boolean isSessionAttributesMethod(Method method) {
        return "sessionAttributes".equals(method.getName());
    }

    protected AccessGroup getGroupAnnotation(Class<? extends AccessGroupDefinition> clazz) {
        AccessGroup annotation = getGroupAnnotationOrNull(clazz);
        if (annotation == null) {
            throw new IllegalStateException("The class must have @Group annotation.");
        }
        return annotation;
    }

    protected @Nullable
    AccessGroup getGroupAnnotationOrNull(Class<? extends AccessGroupDefinition> clazz) {
        return clazz.getAnnotation(AccessGroup.class);
    }

    protected class JpqlAnnotationProcessor implements AnnotationProcessor<ConstraintsAnnotationContext> {
        @Override
        public void processAnnotation(ConstraintsAnnotationContext context) {
            JpqlConstraint constraint = (JpqlConstraint) context.getAnnotation();
            Class<? extends Entity> targetClass = !Entity.class.equals(constraint.target()) ? constraint.target() : resolveTargetClass(context.getMethod());
            if (!Entity.class.equals(targetClass)) {
                String where = Strings.emptyToNull(constraint.value());
                if (where == null) {
                    where = Strings.emptyToNull(constraint.where());
                }
                context.getConstraintsBuilder().withJpql(targetClass, where, Strings.emptyToNull(constraint.join()));
            }
        }
    }

    protected class ConstraintAnnotationProcessor implements AnnotationProcessor<ConstraintsAnnotationContext> {
        @Override
        public void processAnnotation(ConstraintsAnnotationContext context) {
            Constraint constraint = (Constraint) context.getAnnotation();
            Class<? extends Entity> targetClass = resolveTargetClass(context.getMethod());
            if (!Entity.class.equals(targetClass)) {
                for (EntityOp operation : constraint.operations()) {
                    context.getConstraintsBuilder().withInMemory(targetClass, operation, createConstraintPredicate(context));
                }
            }
        }
    }

    protected class CustomConstraintAnnotationProcessor implements AnnotationProcessor<ConstraintsAnnotationContext> {
        @Override
        public void processAnnotation(ConstraintsAnnotationContext context) {
            CustomConstraint constraint = (CustomConstraint) context.getAnnotation();
            Class<? extends Entity> targetClass = resolveTargetClass(context.getMethod());
            if (!Entity.class.equals(targetClass)) {
                context.getConstraintsBuilder().withCustomInMemory(targetClass, constraint.value(), createConstraintPredicate(context));
            }
        }
    }

    protected class SessionAttributesAnnotationProcessor implements AnnotationProcessor<SessionAttributesContext> {
        @Override
        public void processAnnotation(SessionAttributesContext context) {
            SessionAttribute attribute = (SessionAttribute) context.getAnnotation();
            Map<String, Serializable> sessionAttributes = context.getSessionAttributes();
            Datatype datatype = datatypes.getNN(attribute.javaClass());
            try {
                sessionAttributes.put(attribute.name(), (Serializable) datatype.parse(attribute.value()));
            } catch (ParseException e) {
                throw new RuntimeException(String.format("Unable to load session attribute %s for group %s",
                        attribute.name(), context.getOwnerClass().getSimpleName()), e);
            }
        }
    }

    protected Class<? extends Entity> resolveTargetClass(Method method) {
        if (method.getParameterTypes().length == 1) {
            Class<?> parameterType = method.getParameterTypes()[0];
            if (Entity.class.isAssignableFrom(parameterType)) {
                //noinspection unchecked
                return (Class<? extends Entity>) parameterType;
            }
        }
        throw new IllegalStateException(
                String.format("Method [%s] must have only one parameter with Entity argument", method.getName()));
    }

    protected Predicate<Entity> createConstraintPredicate(ConstraintsAnnotationContext context) {
        return context.getPredicateCache().computeIfAbsent(context.getMethod(), method -> {
            try {
                Class argType = method.getParameterTypes()[0];
                MethodHandles.Lookup caller = MethodHandles.lookup();
                CallSite site = LambdaMetafactory.metafactory(caller,
                        "test",
                        MethodType.methodType(BiPredicate.class),
                        MethodType.methodType(boolean.class, Object.class, Object.class),
                        caller.findVirtual(context.getOwnerClass(), method.getName(), MethodType.methodType(method.getReturnType(), argType)),
                        MethodType.methodType(boolean.class, context.getOwnerClass(), argType));
                MethodHandle factory = site.getTarget();
                //noinspection unchecked
                BiPredicate<AccessGroupDefinition, Entity> result = (BiPredicate<AccessGroupDefinition, Entity>) factory.invoke();
                return entity -> result.test(context.getOwner(), entity);
            } catch (Throwable e) {
                throw new IllegalStateException("Can't create in-memory constraint predicate", e);
            }
        });
    }
}
