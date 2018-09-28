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

package com.haulmont.cuba.gui.components.impl;

import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Actions;
import com.haulmont.cuba.gui.sys.ActionDefinition;
import com.haulmont.cuba.gui.sys.ActionsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(Actions.NAME)
public class ActionsImpl implements Actions, ApplicationListener<ContextRefreshedEvent> {

    private final Logger log = LoggerFactory.getLogger(ActionsImpl.class);

    @Inject
    protected Scripting scripting;
    @Inject
    protected List<ActionsConfiguration> configurations;
    @Inject
    protected ApplicationContext applicationContext;

    protected Map<String, Class<? extends Action>> classes = new HashMap<>();

    @Override
    public Action create(String actionTypeId) {
        Class<? extends Action> actionClass = classes.get(actionTypeId);
        if (actionClass == null) {
            throw new IllegalArgumentException("Unable to find action type: " + actionTypeId);
        }

        return createAction(actionClass);
    }

    @Override
    public Action create(String actionTypeId, String id) {
        Class<? extends Action> actionClass = classes.get(actionTypeId);
        if (actionClass == null) {
            throw new IllegalArgumentException("Unable to find action type: " + actionTypeId);
        }

        return createAction(actionClass, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Action> T create(Class<T> actionTypeClass) {
        Class<? extends Action> actionClass = resolveActionClass(actionTypeClass);

        return (T) createAction(actionClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Action> T create(Class<T> actionTypeClass, String id) {
        Class<? extends Action> actionClass = resolveActionClass(actionTypeClass);

        return (T) createAction(actionClass, id);
    }

    protected Action createAction(Class<? extends Action> actionClass) {
        Constructor<? extends Action> constructor;
        try {
            constructor = actionClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Unable to get constructor for '%s' action", actionClass), e);
        }

        try {
            Action instance = constructor.newInstance();
            autowireContext(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Error creating the '%s' action instance", actionClass), e);
        }
    }

    protected Action createAction(Class<? extends Action> actionClass, String id) {
        Constructor<? extends Action> constructor;
        try {
            constructor = actionClass.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Unable to get constructor for '%s' action", actionClass), e);
        }

        try {
            Action instance = constructor.newInstance(id);
            autowireContext(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Error creating the '%s' action instance", actionClass), e);
        }
    }

    protected void autowireContext(Action instance) {
        AutowireCapableBeanFactory autowireBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireBeanFactory.autowireBean(instance);

        if (instance instanceof ApplicationContextAware) {
            ((ApplicationContextAware) instance).setApplicationContext(applicationContext);
        }

        if (instance instanceof InitializingBean) {
            try {
                ((InitializingBean) instance).afterPropertiesSet();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Unable to initialize UI Component - calling afterPropertiesSet for " +
                                instance.getClass(), e);
            }
        }
    }

    protected Class<? extends Action> resolveActionClass(Class<? extends Action> actionClass) {
        ActionType annotation = actionClass.getAnnotation(ActionType.class);
        if (annotation == null) {
            throw new IllegalArgumentException("No @ActionType annotation for class " + actionClass);
        }

        Class<? extends Action> resolvedClass = classes.get(annotation.value());
        if (resolvedClass == null) {
            throw new IllegalStateException("Unable to resolve Action with @ActionType " + actionClass);
        }

        if (!actionClass.isAssignableFrom(resolvedClass)) {
            throw new IllegalStateException(String.format("ActionType %s is not assignable from %s",
                    actionClass, resolvedClass));
        }

        return resolvedClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        long startTime = System.currentTimeMillis();

        Map<String, String> squashedMap = new HashMap<>();

        for (ActionsConfiguration configuration : configurations) {
            for (ActionDefinition actionDefinition : configuration.getActions()) {
                squashedMap.put(actionDefinition.getId(), actionDefinition.getControllerClass());
            }
        }

        classes.clear();

        for (Map.Entry<String, String> entry : squashedMap.entrySet()) {
            // todo load actual classes lazily because they can load a lot of dependent classes !
            Class clazz = scripting.loadClassNN(entry.getValue());
            classes.put(entry.getKey(), clazz);
        }

        log.debug("Actions initialized in {} ms", System.currentTimeMillis() - startTime);
    }
}