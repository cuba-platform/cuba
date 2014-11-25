/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;

import javax.annotation.Nullable;
import javax.management.JMX;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public final class JmxConnectionHelper {

    protected final static JmxInstance LOCAL_JMX_INSTANCE = new JmxInstance("Local");

    private JmxConnectionHelper() {
    }

    protected static MBeanServerConnection getLocalConnection() {
        return ManagementFactory.getPlatformMBeanServer();
    }

    @Nullable
    protected static ObjectName getObjectName(final MBeanServerConnection connection,
                                              final Class objectClass) throws IOException {

        Set<ObjectName> names = connection.queryNames(null, null);
        return (ObjectName) CollectionUtils.find(names, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                ObjectName objectName = (ObjectName) o;
                MBeanInfo info;
                try {
                    info = connection.getMBeanInfo(objectName);
                } catch (Exception e) {
                    throw new JmxControlException(e);
                }
                return StringUtils.equals(objectClass.getName(), info.getClassName());
            }
        });
    }

    protected static <T> T getProxy(MBeanServerConnection connection, ObjectName objectName, final Class<T> objectClass) {
        return JMX.newMBeanProxy(connection, objectName, objectClass, true);
    }

    protected static <T> T withConnection(JmxInstance instance, JmxAction<T> action) {
        try {
            if (ObjectUtils.equals(instance, LOCAL_JMX_INSTANCE)) {
                return action.perform(instance, getLocalConnection());
            } else {
                MBeanServerConnectionFactoryBean factoryBean = new MBeanServerConnectionFactoryBean();
                factoryBean.setServiceUrl("service:jmx:rmi:///jndi/rmi://" + instance.getAddress() + "/jmxrmi");

                String username = instance.getLogin();
                if (StringUtils.isNotEmpty(username)) {
                    Properties properties = new Properties();
                    properties.put("jmx.remote.credentials", new String[]{username, instance.getPassword()});
                    factoryBean.setEnvironment(properties);
                }

                factoryBean.afterPropertiesSet();

                MBeanServerConnection connection = factoryBean.getObject();
                T result;
                try {
                    result = action.perform(instance, connection);
                } finally {
                    try {
                        factoryBean.destroy();
                    } catch (Exception ignored) {
                    }
                }
                return result;
            }
        } catch (Exception e) {
            throw new JmxControlException(e);
        }
    }
}