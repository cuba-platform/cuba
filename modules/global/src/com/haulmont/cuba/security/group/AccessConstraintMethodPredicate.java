package com.haulmont.cuba.security.group;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;

public class AccessConstraintMethodPredicate implements ConstraintPredicate<Entity> {
    private static final long serialVersionUID = -8460977382894140101L;

    private final String className;
    private final String methodName;
    private final String argClassName;
    private final transient AccessGroupDefinition owner;
    private final transient BiPredicate<AccessGroupDefinition, Entity> ownerPredicate;

    private static final Logger log = LoggerFactory.getLogger(AccessConstraintMethodPredicate.class);

    public AccessConstraintMethodPredicate(String className,
                                           String methodName,
                                           String argClassName) {
        this.className = className;
        this.methodName = methodName;
        this.argClassName = argClassName;
        this.owner = getOwnerByClass(className);
        if (this.owner != null) {
            this.ownerPredicate = createBiPredicate(className, methodName, argClassName);
        } else {
            this.ownerPredicate = null;
        }
    }

    @Override
    public boolean test(Entity t) {
        return ownerPredicate == null || ownerPredicate.test(owner, t);
    }

    @Nullable
    private AccessGroupDefinition getOwnerByClass(String className) {
        try {
            Class<?> clazz = ReflectionHelper.loadClass(className);
            return AppBeans.getAll(AccessGroupDefinition.class).values()
                    .stream()
                    .filter(g -> Objects.equals(clazz, g.getClass()))
                    .findFirst()
                    .orElse(null);
        } catch (ClassNotFoundException e) {
            log.debug("Unable to find access group definition by class: {}", this.className);
            return null;
        }
    }

    protected Object writeReplace() {
        return new AccessConstraintMethodInfo(className, methodName, argClassName);
    }

    private static BiPredicate<AccessGroupDefinition, Entity> createBiPredicate(String className, String methodName, String argClassName) {
        Class<?> clazz = ReflectionHelper.getClass(className);
        Class<?> argumentClazz = ReflectionHelper.getClass(argClassName);

        Method method = Arrays.stream(clazz.getMethods())
                .filter(m -> Objects.equals(m.getName(), methodName))
                .filter(m -> m.getParameterCount() == 1 && Objects.equals(m.getParameterTypes()[0], argumentClazz))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Unable to find corresponding in-memory predicate method %s#%s",
                        className, methodName)));

        try {
            MethodHandles.Lookup caller = MethodHandles.lookup();
            CallSite site = LambdaMetafactory.metafactory(caller,
                    "test",
                    MethodType.methodType(BiPredicate.class),
                    MethodType.methodType(boolean.class, Object.class, Object.class),
                    caller.findVirtual(clazz, method.getName(), MethodType.methodType(method.getReturnType(), argumentClazz)),
                    MethodType.methodType(boolean.class, clazz, argumentClazz));
            //noinspection unchecked
            return (BiPredicate<AccessGroupDefinition, Entity>) site.getTarget().invoke();
        } catch (Throwable e) {
            throw new IllegalStateException(String.format("Can't create in-memory constraint predicate for method %s#%s", className, methodName), e);
        }
    }
}
