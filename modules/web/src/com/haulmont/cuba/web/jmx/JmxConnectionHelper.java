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

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;

import javax.annotation.Nullable;
import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.UnmarshalException;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public final class JmxConnectionHelper {

    protected final static UUID LOCAL_JMX_INSTANCE_ID = UUID.randomUUID();

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
                } catch (InstanceNotFoundException | UnmarshalException e) {
                    return false;
                } catch (Exception e) {
                    throw new JmxControlException(e);
                }
                return StringUtils.equals(objectClass.getName(), info.getClassName());
            }
        });
    }

    protected static ObjectName getObjectName(final MBeanServerConnection connection, final String remoteContext,
                                              final Class objectClass) throws IOException {
        Set<ObjectName> names = connection.queryNames(null, null);
        return (ObjectName) CollectionUtils.find(names, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                ObjectName objectName = (ObjectName) o;

                if (!StringUtils.equals(remoteContext, objectName.getDomain())) {
                    return false;
                }

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

    protected static Collection<ObjectName> getSuitableObjectNames(final MBeanServerConnection connection,
                                                                   final Class objectClass) throws IOException {
        Set<ObjectName> names = connection.queryNames(null, null);

        // find all suitable beans
        @SuppressWarnings("unchecked")
        Collection<ObjectName> suitableNames = CollectionUtils.select(names, new Predicate() {
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

        return suitableNames;
    }

    protected static <T> T getProxy(MBeanServerConnection connection, ObjectName objectName, final Class<T> objectClass) {
        return JMX.newMBeanProxy(connection, objectName, objectClass, true);
    }

    protected static <T> T withConnection(JmxInstance instance, JmxAction<T> action) {
        try {
            if (ObjectUtils.equals(instance.getId(), LOCAL_JMX_INSTANCE_ID)) {
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