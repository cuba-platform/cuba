/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.app.scheduled.MethodInfo;
import com.haulmont.cuba.core.app.scheduled.MethodParameterInfo;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaDefaultListableBeanFactory;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetClassAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 */
@Service(SchedulingService.NAME)
public class SchedulingServiceBean implements SchedulingService {

    private Logger log = LoggerFactory.getLogger(getClass());

    protected List<String> beansToIgnore = Arrays.asList("cubaDataSource", "entityManagerFactory", "hibernateSessionFactory",
            "mailSendTaskExecutor", "scheduler", "sqlSession", "sqlSessionFactory", "transactionManager",
            "cuba_ServerInfoService", "cuba_LoginService", "cuba_LocalizedMessageService");

    @Inject
    protected Persistence persistence;

    @Inject
    private SchedulingAPI scheduling;

    private ClusterManagerAPI clusterManager;

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        clusterManager.addListener(SetSchedulingActiveMsg.class, new ClusterListenerAdapter<SetSchedulingActiveMsg>() {
            @Override
            public void receive(SetSchedulingActiveMsg message) {
                scheduling.setActive(message.active);
            }
        });
    }

    @Override
    public Map<String, List<MethodInfo>> getAvailableBeans() {
        Map<String, List<MethodInfo>> result = new TreeMap<>();

        String[] beanNames = AppContext.getApplicationContext().getBeanDefinitionNames();
        for (String name : beanNames) {
            if (AppContext.getApplicationContext().isSingleton(name)
                    && !name.startsWith("org.springframework.")
                    && !beansToIgnore.contains(name)) {
                List<MethodInfo> availableMethods = getAvailableMethods(name);
                if (!availableMethods.isEmpty())
                    result.put(name, availableMethods);
            }
        }

        return result;
    }

    protected List<MethodInfo> getAvailableMethods(String beanName) {
        List<MethodInfo> methods = new ArrayList<>();
        try {
            AutowireCapableBeanFactory beanFactory = AppContext.getApplicationContext().getAutowireCapableBeanFactory();
            if (beanFactory instanceof CubaDefaultListableBeanFactory) {
                BeanDefinition beanDefinition = ((CubaDefaultListableBeanFactory) beanFactory).getBeanDefinition(beanName);
                if (beanDefinition.isAbstract())
                    return methods;
            }

            Object bean = AppBeans.get(beanName);

            List<Class> classes = ClassUtils.getAllInterfaces(bean.getClass());
            for (Class aClass : classes) {
                if (aClass.getName().startsWith("org.springframework."))
                    continue;

                Class<?> targetClass = bean instanceof TargetClassAware ? ((TargetClassAware) bean).getTargetClass() : bean.getClass();

                Service serviceAnn = targetClass.getAnnotation(Service.class);
                if (serviceAnn != null)
                    return methods;

                for (Method method : aClass.getMethods()) {
                    if (isMethodAvailable(method)) {
                        Method targetClassMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
                        List<MethodParameterInfo> methodParameters = getMethodParameters(targetClassMethod);
                        MethodInfo methodInfo = new MethodInfo(method.getName(), methodParameters);
                        addMethod(methods, methodInfo);
                    }
                }

                if (methods.isEmpty()) {
                    for (Method method : bean.getClass().getMethods()) {
                        if (!method.getDeclaringClass().equals(Object.class) && isMethodAvailable(method)) {
                            List<MethodParameterInfo> methodParameters = getMethodParameters(method);
                            MethodInfo methodInfo = new MethodInfo(method.getName(), methodParameters);
                            addMethod(methods, methodInfo);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log.debug(t.getMessage());
        }
        return methods;
    }

    private void addMethod(List<MethodInfo> methods, MethodInfo methodInfo) {
        for (MethodInfo mi : methods) {
            if (mi.definitionEquals(methodInfo))
                return;
        }
        methods.add(methodInfo);
    }

    @Override
    public List<String> getAvailableUsers() {
        List<String> result = new ArrayList<>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select u from sec$User u");
            List<User> userList = query.getResultList();
            for (User user : userList) {
                result.add(user.getLogin());
            }
            tx.commit();
        } finally {
            tx.end();
        }

        return result;
    }

    @Override
    public void setActive(boolean active) {
        scheduling.setActive(active);
        clusterManager.send(new SetSchedulingActiveMsg(active));
    }

    @Override
    public void setActive(ScheduledTask task, boolean active) {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            ScheduledTask t = em.find(ScheduledTask.class, task.getId());
            t.setActive(active);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private boolean isMethodAvailable(Method method) {
        for (Class<?> aClass : method.getParameterTypes()) {
            if (!aClass.equals(String.class))
                return false;
        }
        return true;
    }

    private List<MethodParameterInfo> getMethodParameters(Method method) {
        ArrayList<MethodParameterInfo> params = new ArrayList<>();

        Class<?>[] parameterTypes = method.getParameterTypes();

        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        if (parameterTypes != null) {
            for (int i = 0; i < parameterTypes.length; i++) {
                String parameterName = parameterNames != null ? parameterNames[i] : "arg" + i;
                MethodParameterInfo parameterInfo = new MethodParameterInfo(parameterTypes[i], parameterName, null);
                params.add(parameterInfo);
            }
        }
        return params;
    }

    public static class SetSchedulingActiveMsg implements Serializable {
        private static final long serialVersionUID = 6934530919733469448L;

        public final boolean active;

        public SetSchedulingActiveMsg(boolean active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return "SetSchedulingActiveMsg{" +
                    "active=" + active +
                    '}';
        }
    }
}