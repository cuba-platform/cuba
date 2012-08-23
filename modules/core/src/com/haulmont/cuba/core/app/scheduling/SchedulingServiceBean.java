/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Service(SchedulingService.NAME)
public class SchedulingServiceBean implements SchedulingService {

    private Log log = LogFactory.getLog(getClass());

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
    public Map<String, List<String>> getAvailableBeans() {
        Map<String, List<String>> result = new TreeMap<String, List<String>>();

        String[] beanNames = AppContext.getApplicationContext().getBeanDefinitionNames();
        for (String name : beanNames) {
            if (AppContext.getApplicationContext().isSingleton(name)
                    && name.contains("_") && !name.startsWith("base_") && !name.endsWith("Service"))
            {
                List<String> methodNames = getMethodNames(name);
                if (!methodNames.isEmpty()) {
                    result.put(name, methodNames);
                }
            }
        }

        return result;
    }

    protected List<String> getMethodNames(String beanName) {
        List<String> methodNames = new ArrayList<String>();
        try {
            Object bean = AppBeans.get(beanName);

            List<Class> classes = ClassUtils.getAllInterfaces(bean.getClass());
            for (Class aClass : classes) {
                if (aClass.getName().startsWith("org.springframework."))
                    continue;

                for (Method method : aClass.getMethods()) {
                    if (method.getParameterTypes().length == 0) {
                        if (!methodNames.contains(method.getName()))
                            methodNames.add(method.getName());
                    }
                }
            }

            if (methodNames.isEmpty()) {
                for (Method method : bean.getClass().getMethods()) {
                    if (!method.getDeclaringClass().equals(Object.class)
                            && method.getParameterTypes().length == 0) {
                        if (!methodNames.contains(method.getName()))
                            methodNames.add(method.getName());
                    }
                }
            }
        } catch (Throwable t) {
            log.debug(t.getMessage());
        }
        Collections.sort(methodNames);
        return methodNames;
    }

    @Override
    public List<String> getAvailableUsers() {
        List<String> result = new ArrayList<String>();

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
