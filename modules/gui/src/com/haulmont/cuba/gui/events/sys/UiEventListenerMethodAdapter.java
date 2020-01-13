/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.events.sys;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class UiEventListenerMethodAdapter implements GenericApplicationListener {

    private static final Logger log = LoggerFactory.getLogger(UiEventListenerMethodAdapter.class);

    private WeakReference<Object> instanceRef;
    private final Method method;
    private final Method bridgedMethod;
    private final List<ResolvableType> declaredEventTypes;
    private final int order;

    private Events events;

    public UiEventListenerMethodAdapter(Object instance, Class<?> targetClass, Method method, Events events) {
        checkNotNullArgument(targetClass);
        checkNotNullArgument(method);
        checkNotNullArgument(instance);
        checkNotNullArgument(events);

        Method targetMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        EventListener ann = AnnotatedElementUtils.findMergedAnnotation(targetMethod, EventListener.class);

        if (ann == null) {
            throw new IllegalArgumentException("No @EventListener annotation for method " + method);
        }

        this.instanceRef = new WeakReference<>(instance);
        this.method = method;
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
        this.events = events;

        this.declaredEventTypes = resolveDeclaredEventTypes(method, ann);
        if (!ann.condition().isEmpty()) {
            throw new UnsupportedOperationException("@EventListener condition is not supported for UiEvents");
        }
        this.order = resolveOrder(method);
    }

    @Override
    public boolean supportsEventType(@Nonnull ResolvableType eventType) {
        for (ResolvableType declaredEventType : this.declaredEventTypes) {
            if (declaredEventType.isAssignableFrom(eventType)) {
                return true;
            } else if (eventType.getRawClass() != null
                    && PayloadApplicationEvent.class.isAssignableFrom(eventType.getRawClass())) {
                ResolvableType payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric();
                if (declaredEventType.isAssignableFrom(payloadType)) {
                    return true;
                }
            }
        }
        return eventType.hasUnresolvableGenerics();
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public void onApplicationEvent(@Nonnull ApplicationEvent event) {
        Object instance = instanceRef.get();
        if (instance != null) {
            processEvent(instance, event);
        }
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    protected List<ResolvableType> resolveDeclaredEventTypes(Method method, EventListener ann) {
        int count = method.getParameterTypes().length;
        if (count > 1) {
            throw new IllegalStateException(
                    "Maximum one parameter is allowed for event listener method: " + method);
        }
        if (ann != null && ann.classes().length > 0) {
            List<ResolvableType> types = new ArrayList<>(ann.classes().length);
            for (Class<?> eventType : ann.classes()) {
                types.add(ResolvableType.forClass(eventType));
            }
            return types;
        } else {
            if (count == 0) {
                throw new IllegalStateException(
                        "Event parameter is mandatory for event listener method: " + method);
            }
            return Collections.singletonList(ResolvableType.forMethodParameter(method, 0));
        }
    }

    protected int resolveOrder(Method method) {
        Order ann = AnnotatedElementUtils.findMergedAnnotation(method, Order.class);
        return (ann != null ? ann.value() : 0);
    }

    /**
     * Process the specified {@link ApplicationEvent}, checking if the condition match and handling non-null result, if any.
     */
    public void processEvent(Object instance, ApplicationEvent event) {
        Object[] args = resolveArguments(event);
        if (shouldHandle(event, args)) {
            Object result = doInvoke(instance, args);
            if (result != null) {
                handleResult(instance, result);
            } else {
                log.trace("No result object given - no result to handle");
            }
        }
    }

    /**
     * Resolve the method arguments to use for the specified {@link ApplicationEvent}.
     * <p>These arguments will be used to invoke the method handled by this instance. Can
     * return {@code null} to indicate that no suitable arguments could be resolved and
     * therefore the method should not be invoked at all for the specified event.
     */
    @Nullable
    protected Object[] resolveArguments(ApplicationEvent event) {
        ResolvableType declaredEventType = getResolvableType(event);
        if (declaredEventType == null || declaredEventType.getRawClass() == null) {
            return null;
        }
        if (this.method.getParameterTypes().length == 0) {
            return new Object[0];
        }
        if (!ApplicationEvent.class.isAssignableFrom(declaredEventType.getRawClass()) &&
                event instanceof PayloadApplicationEvent) {
            return new Object[]{((PayloadApplicationEvent) event).getPayload()};
        } else {
            return new Object[]{event};
        }
    }

    protected void handleResult(Object instance, Object result) {
        if (result.getClass().isArray()) {
            Object[] events = ObjectUtils.toObjectArray(result);
            for (Object event : events) {
                publishEvent(instance, event);
            }
        } else if (result instanceof Collection<?>) {
            Collection<?> events = (Collection<?>) result;
            for (Object event : events) {
                publishEvent(instance, event);
            }
        } else {
            publishEvent(instance, result);
        }
    }

    protected void publishEvent(Object instance, Object event) {
        if (event != null) {
            if (event instanceof ApplicationEvent) {
                this.events.publish((ApplicationEvent) event);
            } else {
                this.events.publish(new PayloadApplicationEvent<>(instance, event));
            }
        }
    }

    protected boolean shouldHandle(ApplicationEvent event, Object[] args) {
        if (args == null) {
            return false;
        }
        String condition = getCondition();
        if (!Strings.isNullOrEmpty(condition)) {
            throw new UnsupportedOperationException("@EventListener condition is not supported for UiEvents");
        }
        return true;
    }

    /**
     * Invoke the event listener method with the given argument values.
     */
    protected Object doInvoke(Object instance, Object... args) {
        ReflectionUtils.makeAccessible(this.bridgedMethod);
        try {
            return this.bridgedMethod.invoke(instance, args);
        } catch (IllegalArgumentException ex) {
            assertTargetBean(this.bridgedMethod, instance, args);
            throw new IllegalStateException(getInvocationErrorMessage(instance, ex.getMessage(), args), ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(getInvocationErrorMessage(instance, ex.getMessage(), args), ex);
        } catch (InvocationTargetException ex) {
            // Throw underlying exception
            Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            } else {
                String msg = getInvocationErrorMessage(instance, "Failed to invoke event listener method", args);
                throw new UndeclaredThrowableException(targetException, msg);
            }
        }
    }

    /**
     * Return the condition to use.
     * <p>Matches the {@code condition} attribute of the {@link EventListener}
     * annotation or any matching attribute on a composed annotation that
     * is meta-annotated with {@code @EventListener}.
     */
    @Nullable
    protected String getCondition() {
        return null;
    }

    /**
     * Add additional details such as the bean type and method signature to
     * the given error message.
     *
     * @param message error message to append the HandlerMethod details to
     */
    protected String getDetailedErrorMessage(Object bean, String message) {
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder sb = new StringBuilder(message).append("\n");
        sb.append("HandlerMethod details: \n");
        sb.append("Bean [").append(bean.getClass().getName()).append("]\n");
        sb.append("Method [").append(this.bridgedMethod.toGenericString()).append("]\n");
        return sb.toString();
    }

    /**
     * Assert that the target bean class is an instance of the class where the given
     * method is declared. In some cases the actual bean instance at event-
     * processing time may be a JDK dynamic proxy (lazy initialization, prototype
     * beans, and others). Event listener beans that require proxying should prefer
     * class-based proxy mechanisms.
     */
    protected void assertTargetBean(Method method, Object targetBean, Object[] args) {
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        Class<?> targetBeanClass = targetBean.getClass();
        if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
            String msg = "The event listener method class '" + methodDeclaringClass.getName() +
                    "' is not an instance of the actual bean class '" +
                    targetBeanClass.getName() + "'. If the bean requires proxying " +
                    "(e.g. due to @Transactional), please use class-based proxying.";
            throw new IllegalStateException(getInvocationErrorMessage(targetBean, msg, args));
        }
    }

    protected String getInvocationErrorMessage(Object bean, String message, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(bean, message));
        sb.append("Resolved arguments: \n");
        for (int i = 0; i < resolvedArgs.length; i++) {
            sb.append("[").append(i).append("] ");
            if (resolvedArgs[i] == null) {
                sb.append("[null] \n");
            } else {
                sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
                sb.append("[value=").append(resolvedArgs[i]).append("]\n");
            }
        }
        return sb.toString();
    }

    @Nullable
    protected ResolvableType getResolvableType(ApplicationEvent event) {
        ResolvableType payloadType = null;
        if (event instanceof PayloadApplicationEvent) {
            PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent<?>) event;
            ResolvableType resolvableType = payloadEvent.getResolvableType();
            if (resolvableType != null) {
                payloadType = resolvableType.as(PayloadApplicationEvent.class).getGeneric();
            }
        }

        for (ResolvableType declaredEventType : this.declaredEventTypes) {
            Class<?> rawClass = declaredEventType.getRawClass();

            if (rawClass != null) {
                if (!ApplicationEvent.class.isAssignableFrom(rawClass) && payloadType != null) {
                    if (declaredEventType.isAssignableFrom(payloadType)) {
                        return declaredEventType;
                    }
                }
                if (rawClass.isInstance(event)) {
                    return declaredEventType;
                }
            }
        }
        return null;
    }
}