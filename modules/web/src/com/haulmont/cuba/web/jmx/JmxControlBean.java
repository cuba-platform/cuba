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

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.jmx.JmxNodeIdentifier;
import com.haulmont.cuba.core.sys.jmx.JmxNodeIdentifierMBean;
import com.haulmont.cuba.web.jmx.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.management.*;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.UnmarshalException;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.web.jmx.JmxConnectionHelper.getObjectName;
import static com.haulmont.cuba.web.jmx.JmxConnectionHelper.withConnection;

@Component(JmxControlAPI.NAME)
public class JmxControlBean implements JmxControlAPI {

    public static final String JMX_PORT_SYSTEM_PROPERTY = "com.sun.management.jmxremote.port";

    protected static final String FIELD_RUN_ASYNC = "runAsync";
    protected static final String FIELD_TIMEOUT = "timeout";
    public static final String RMI_SERVER_HOSTNAME_SYSTEM_PROPERTY = "java.rmi.server.hostname";

    private final Logger log = LoggerFactory.getLogger(JmxControlBean.class);

    @Inject
    protected DataService dataService;
    @Inject
    protected Metadata metadata;

    /**
     * Constant identifier for the role field in a JMX {@link Descriptor}.
     */
    protected static final String FIELD_ROLE = "role";

    /**
     * Constant identifier for the getter role field value in a JMX {@link Descriptor}.
     */
    protected static final String ROLE_GETTER = "getter";

    /**
     * Constant identifier for the setter role field value in a JMX {@link Descriptor}.
     */
    protected static final String ROLE_SETTER = "setter";

    @SuppressWarnings("unchecked")
    @Override
    public List<JmxInstance> getInstances() {
        LoadContext loadContext = new LoadContext(JmxInstance.class);
        loadContext.setView(View.LOCAL);
        loadContext.setQueryString("select jmx from sys$JmxInstance jmx");

        List<JmxInstance> clusterInstances = dataService.loadList(loadContext);

        List<JmxInstance> jmxInstances = new ArrayList<>(clusterInstances.size() + 1);
        jmxInstances.add(getLocalInstance());
        jmxInstances.addAll(clusterInstances);

        return jmxInstances;
    }

    @Override
    public JmxInstance getLocalInstance() {
        JmxInstance localJmxInstance = metadata.create(JmxInstance.class);
        localJmxInstance.setId(JmxConnectionHelper.LOCAL_JMX_INSTANCE_ID);
        localJmxInstance.setNodeName(getLocalNodeName());
        return localJmxInstance;
    }

    @Override
    public String getLocalNodeName() {
        String hostName;

        try {
            hostName = System.getProperty(RMI_SERVER_HOSTNAME_SYSTEM_PROPERTY,
                    InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            log.warn("Unable to get local hostname", e);
            hostName = "<unknown-host>";
        }

        String jmxPort = System.getProperty(JMX_PORT_SYSTEM_PROPERTY, "<unknown-port>");

        return String.format("<local> (%s:%s)", hostName, jmxPort);
    }

    @Override
    public String getRemoteNodeName(JmxInstance instance) {
        checkNotNullArgument(instance);

        //noinspection UnnecessaryLocalVariable
        String remoteNodeName = withConnection(instance, (jmx, connection) -> {
            ObjectName nodeIdentifierBeanInfo = getObjectName(connection, JmxNodeIdentifier.class);

            if (nodeIdentifierBeanInfo != null) {
                JmxNodeIdentifierMBean identifier =
                        JmxConnectionHelper.getProxy(connection, nodeIdentifierBeanInfo, JmxNodeIdentifierMBean.class);

                Object nodeName = identifier.getNodeName();
                if (nodeName != null) {
                    return nodeName.toString();
                } else {
                    return getDefaultNodeName(jmx);
                }
            } else {
                return getDefaultNodeName(jmx);
            }
        });

        return remoteNodeName;
    }

    @Override
    public List<ManagedBeanInfo> getManagedBeans(JmxInstance instance) {
        checkNotNullArgument(instance);

        //noinspection UnnecessaryLocalVariable
        List<ManagedBeanInfo> infos = withConnection(instance, (jmx, connection) -> {
            Set<ObjectName> names = connection.queryNames(null, null);
            List<ManagedBeanInfo> infoList = new ArrayList<>(names.size());
            for (ObjectName name : names) {
                MBeanInfo info;
                try {
                    info = connection.getMBeanInfo(name);
                } catch (UnmarshalException | InstanceNotFoundException e) {
                    // unable to use this bean, may be ClassNotFoundException
                    continue;
                }

                ManagedBeanInfo mbi = createManagedBeanInfo(jmx, name, info);
                loadOperations(mbi, info);
                infoList.add(mbi);
            }

            infoList.sort(new MBeanComparator());

            return infoList;
        });

        return infos;
    }

    @Override
    public ManagedBeanInfo getManagedBean(JmxInstance instance, final String beanObjectName) {
        checkNotNullArgument(instance);
        checkNotNullArgument(beanObjectName);

        //noinspection UnnecessaryLocalVariable
        ManagedBeanInfo info = withConnection(instance, (jmx, connection) -> {
            Set<ObjectName> names = connection.queryNames(new ObjectName(beanObjectName), null);
            ManagedBeanInfo mbi = null;
            if (!names.isEmpty()) {
                ObjectName name = names.iterator().next();
                MBeanInfo info1 = connection.getMBeanInfo(name);
                mbi = createManagedBeanInfo(jmx, name, info1);
                loadOperations(mbi, info1);
            }

            return mbi;
        });

        return info;
    }

    protected ManagedBeanInfo createManagedBeanInfo(JmxInstance jmx, ObjectName name, MBeanInfo info) {
        ManagedBeanInfo mbi = metadata.create(ManagedBeanInfo.class);
        mbi.setClassName(info.getClassName());
        mbi.setDescription(info.getDescription());
        mbi.setObjectName(name.toString());
        mbi.setDomain(name.getDomain());
        mbi.setPropertyList(name.getKeyPropertyListString());
        mbi.setJmxInstance(jmx);
        return mbi;
    }

    @Override
    public void loadAttributes(final ManagedBeanInfo mbinfo) {
        checkNotNullArgument(mbinfo);
        checkNotNullArgument(mbinfo.getJmxInstance());

        withConnection(mbinfo.getJmxInstance(), (jmx, connection) -> {
            ObjectName name = new ObjectName(mbinfo.getObjectName());
            MBeanInfo info = connection.getMBeanInfo(name);
            MBeanAttributeInfo[] attributes = info.getAttributes();

            List<ManagedBeanAttribute> attrs = new ArrayList<>(attributes.length);
            for (MBeanAttributeInfo attribute : attributes) {
                ManagedBeanAttribute mba = createManagedBeanAttribute(connection, name, attribute, mbinfo);
                attrs.add(mba);
            }

            attrs.sort(new AttributeComparator());

            mbinfo.setAttributes(attrs);
            return null;
        });
    }

    @Override
    public ManagedBeanAttribute loadAttribute(final ManagedBeanInfo mbinfo, final String attributeName) {
        checkNotNullArgument(mbinfo);
        checkNotNullArgument(attributeName);

        //noinspection UnnecessaryLocalVariable
        ManagedBeanAttribute attribute = withConnection(mbinfo.getJmxInstance(), (jmx, connection) -> {
            ObjectName name = new ObjectName(mbinfo.getObjectName());
            MBeanInfo info = connection.getMBeanInfo(name);
            MBeanAttributeInfo[] attributes = info.getAttributes();
            ManagedBeanAttribute res = null;
            for (MBeanAttributeInfo attribute1 : attributes) {
                if (attribute1.getName().equals(attributeName)) {
                    res = createManagedBeanAttribute(connection, name, attribute1, mbinfo);
                    break;
                }
            }
            return res;
        });
        return attribute;
    }

    protected ManagedBeanAttribute createManagedBeanAttribute(MBeanServerConnection connection, ObjectName name,
                                                            MBeanAttributeInfo attribute, ManagedBeanInfo mbinfo) {
        ManagedBeanAttribute mba = metadata.create(ManagedBeanAttribute.class);
        mba.setMbean(mbinfo);
        mba.setName(attribute.getName());
        mba.setDescription(attribute.getDescription());
        mba.setType(cleanType(attribute.getType()));
        mba.setReadable(attribute.isReadable());
        mba.setWriteable(attribute.isWritable());

        String mask = "";
        if (attribute.isReadable()) {
            mask += "R";
        }
        if (attribute.isWritable()) {
            mask += "W";
        }
        mba.setReadableWriteable(mask);

        if (mba.getReadable()) {
            try {
                Object value = connection.getAttribute(name, mba.getName());
                setSerializableValue(mba, value);
            } catch (Exception e) {
                log.error("Error getting attribute", e);
                mba.setValue(e.getMessage());
                mba.setWriteable(false);
            }
        }
        return mba;
    }

    @Override
    public void loadAttributeValue(final ManagedBeanAttribute attribute) {
        checkNotNullArgument(attribute);
        checkNotNullArgument(attribute.getMbean());
        checkNotNullArgument(attribute.getMbean().getJmxInstance());

        withConnection(attribute.getMbean().getJmxInstance(), (jmx, connection) -> {
            ObjectName name = new ObjectName(attribute.getMbean().getObjectName());

            Object value = null;
            if (attribute.getReadable()) {
                try {
                    value = connection.getAttribute(name, attribute.getName());
                } catch (Exception e) {
                    log.error("Error getting attribute", e);
                    value = e.getMessage();
                }
            }
            setSerializableValue(attribute, value);

            return null;
        });
    }

    @Override
    public ManagedBeanOperation getOperation(ManagedBeanInfo bean, String operationName, @Nullable String[] argTypes) {
        checkNotNullArgument(bean);
        checkNotNullArgument(operationName);

        ManagedBeanOperation res=null;
        for (ManagedBeanOperation op : bean.getOperations()) {
            if (op.getName().equals(operationName)) {
                List<ManagedBeanOperationParameter> args = op.getParameters();
                if ((args.isEmpty() && argTypes == null)
                        || (argTypes != null && args.size() == argTypes.length)) {
                    boolean isFound=true;
                    for (int i = 0; i < args.size(); i++) {
                        ManagedBeanOperationParameter arg = args.get(i);
                        if (!arg.getType().equals(argTypes[i])) {
                            isFound=false;
                            break;
                        }
                    }
                    if (isFound) {
                        res = op;
                        break;
                    }
                }
            }

        }
        return res;
    }

    @Override
    public void saveAttributeValue(final ManagedBeanAttribute attribute) {
        checkNotNullArgument(attribute);
        checkNotNullArgument(attribute.getMbean());
        checkNotNullArgument(attribute.getMbean().getJmxInstance());

        withConnection(attribute.getMbean().getJmxInstance(), (jmx, connection) -> {
            try {
                ObjectName name = new ObjectName(attribute.getMbean().getObjectName());
                Attribute a = new Attribute(attribute.getName(), attribute.getValue());

                log.info(String.format("Set value '%s' to attribute '%s' in '%s' on '%s'",
                        a.getValue(), a.getName(), name.getCanonicalName(),
                        attribute.getMbean().getJmxInstance().getNodeName()));

                connection.setAttribute(name, a);
            } catch (Exception e) {
                log.info(String.format("Unable to set value '%s' to attribute '%s' in '%s' on '%s'",
                        attribute.getValue(), attribute.getName(), attribute.getMbean().getObjectName(),
                        attribute.getMbean().getJmxInstance().getNodeName()), e);

                throw e;
            }

            return null;
        });
    }

    @Override
    public Object invokeOperation(final ManagedBeanOperation operation, final Object[] parameterValues) {
        checkNotNullArgument(operation);
        checkNotNullArgument(operation.getMbean());
        checkNotNullArgument(operation.getMbean().getJmxInstance());

        //noinspection UnnecessaryLocalVariable
        Object result = withConnection(operation.getMbean().getJmxInstance(), (jmx, connection) -> {
            try {
                ObjectName name = new ObjectName(operation.getMbean().getObjectName());

                String[] types = new String[operation.getParameters().size()];
                for (int i = 0; i < operation.getParameters().size(); i++) {
                    types[i] = operation.getParameters().get(i).getJavaType();
                }

                log.debug(String.format("Invoke method '%s' from '%s' on '%s'",
                        operation.getName(), name.getCanonicalName(), operation.getMbean().getJmxInstance().getNodeName()));
                return connection.invoke(name, operation.getName(), parameterValues, types);
            } catch (Exception e) {
                log.warn(String.format("Error invoking method '%s' from '%s' on '%s'",
                        operation.getName(), operation.getMbean().getObjectName(),
                        operation.getMbean().getJmxInstance().getNodeName()), e);
                throw e;
            }
        });

        return result;
    }

    @Override
    public List<ManagedBeanDomain> getDomains(JmxInstance instance) {
        checkNotNullArgument(instance);

        //noinspection UnnecessaryLocalVariable
        List<ManagedBeanDomain> domains = withConnection(instance, (jmx, connection) -> {
            String[] domainNames = connection.getDomains();

            List<ManagedBeanDomain> domainList = new ArrayList<>(domainNames.length);
            for (String d : domainNames) {
                ManagedBeanDomain mbd = metadata.create(ManagedBeanDomain.class);
                mbd.setName(d);
                domainList.add(mbd);
            }

            domainList.sort(new DomainComparator());

            return domainList;
        });

        return domains;
    }

    protected void loadOperations(ManagedBeanInfo mbean, MBeanInfo info) {
        MBeanOperationInfo[] operations = info.getOperations();

        List<ManagedBeanOperation> opList = new ArrayList<>(operations.length);
        for (MBeanOperationInfo operation : operations) {
            Descriptor descriptor = operation.getDescriptor();
            String role = (String) descriptor.getFieldValue(FIELD_ROLE);
            if (ROLE_GETTER.equals(role) || ROLE_SETTER.equals(role)) {
                continue; // these operations do the same as reading / writing attributes
            }

            ManagedBeanOperation o = metadata.create(ManagedBeanOperation.class);
            o.setName(operation.getName());
            o.setDescription(operation.getDescription());
            o.setMbean(mbean);
            o.setReturnType(cleanType(operation.getReturnType()));

            Object runAsync = descriptor.getFieldValue(FIELD_RUN_ASYNC);
            if (runAsync != null) {
                o.setRunAsync((Boolean) runAsync);
                o.setTimeout((Long) descriptor.getFieldValue(FIELD_TIMEOUT));
            }

            List<ManagedBeanOperationParameter> paramList = new ArrayList<>();
            if (operation.getSignature() != null) {
                for (int index = 0; index < operation.getSignature().length; index++) {
                    MBeanParameterInfo pinfo = operation.getSignature()[index];
                    ManagedBeanOperationParameter p = metadata.create(ManagedBeanOperationParameter.class);
                    p.setName(pinfo.getName());
                    p.setType(cleanType(pinfo.getType()));
                    p.setJavaType(pinfo.getType());
                    p.setDescription(pinfo.getDescription());
                    p.setOperation(o);

                    // fix name if it is not set
                    if (p.getName() == null || p.getName().length() == 0 || p.getName().equals(p.getType())) {
                        p.setName("arg" + index);
                    }

                    paramList.add(p);
                }
            }
            o.setParameters(paramList);

            opList.add(o);
        }

        opList.sort(new OperationComparator());

        mbean.setOperations(opList);
    }

    protected String cleanType(String type) {
        if (type != null && type.startsWith("[L") && type.endsWith(";")) {
            return type.substring(2, type.length() - 1) + "[]";
        }
        return type;
    }

    protected void setSerializableValue(ManagedBeanAttribute mba, Object value) {
        if (value instanceof Serializable && !(value instanceof Proxy)) {
            mba.setValue(value);
        } else if (value != null) {
            mba.setValue(value.toString());
        }
    }

    protected String getDefaultNodeName(@SuppressWarnings("unused") JmxInstance instance) {
        return "Unknown JMX interface";
    }

    /**
     * Sorts domains alphabetically by name
     */
    protected static class DomainComparator implements Comparator<ManagedBeanDomain> {
        @Override
        public int compare(ManagedBeanDomain mbd1, ManagedBeanDomain mbd2) {
            return mbd1 != null && mbd1.getName() != null
                    ? mbd1.getName().compareTo(mbd2.getName())
                    : (mbd2 != null && mbd2.getName() != null ? 1 : 0);
        }
    }

    /**
     * Sorts mbeans alphabetically by name
     */
    protected static class MBeanComparator implements Comparator<ManagedBeanInfo> {
        @Override
        public int compare(ManagedBeanInfo mbd1, ManagedBeanInfo mbd2) {
            return mbd1 != null && mbd1.getPropertyList() != null
                    ? mbd1.getPropertyList().compareTo(mbd2.getPropertyList())
                    : (mbd2 != null && mbd2.getPropertyList() != null ? 1 : 0);
        }
    }

    /**
     * Sorts attributes alphabetically by name
     */
    protected static class AttributeComparator implements Comparator<ManagedBeanAttribute> {
        @Override
        public int compare(ManagedBeanAttribute mbd1, ManagedBeanAttribute mbd2) {
            return mbd1 != null && mbd1.getName() != null
                    ? mbd1.getName().compareTo(mbd2.getName())
                    : (mbd2 != null && mbd2.getName() != null ? 1 : 0);
        }
    }

    /**
     * Sorts operations alphabetically by name
     */
    protected static class OperationComparator implements Comparator<ManagedBeanOperation> {
        @Override
        public int compare(ManagedBeanOperation o1, ManagedBeanOperation o2) {
            return o1 != null && o1.getName() != null
                    ? o1.getName().compareTo(o2.getName())
                    : (o2 != null && o2.getName() != null ? 1 : 0);
        }
    }
}