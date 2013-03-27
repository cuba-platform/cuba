/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.NodeIdentifier;
import com.haulmont.cuba.core.sys.jmx.JmxNodeIdentifier;
import com.haulmont.cuba.web.jmx.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.management.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(JmxControlAPI.NAME)
public class JmxControlBean implements JmxControlAPI {

    private final Log log = LogFactory.getLog(getClass());

    @Inject
    private DataService dataService;

    @Inject
    private NodeIdentifier nodeIdentifier;

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

    @Override
    public List<JmxInstance> getInstances() {
        LoadContext loadContext = new LoadContext(JmxInstance.class);
        loadContext.setView("_local");
        loadContext.setQueryString("select jmx from sys$JmxInstance jmx");

        List<JmxInstance> jmxInstances = new ArrayList<>();
        jmxInstances.add(getLocalInstance());

        List<JmxInstance> clusterInstances = dataService.loadList(loadContext);
        jmxInstances.addAll(clusterInstances);

        return jmxInstances;
    }

    @Override
    public JmxInstance getLocalInstance() {
        JmxInstance localJmxInstance = new JmxInstance();
        InstanceUtils.copy(JmxConnectionHelper.LOCAL_JMX_INSTANCE, localJmxInstance);
        return localJmxInstance;
    }

    @Override
    public String getLocalNodeName() {
        return nodeIdentifier.getNodeName();
    }

    @Override
    public String getRemoteNodeName(JmxInstance instance) {
        checkNotNull(instance);

        String remoteNodeName;

        final MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);
        try {
            ObjectName nodeIdentifierBeanInfo = JmxConnectionHelper.getObjectName(connection, JmxNodeIdentifier.class);

            if (nodeIdentifierBeanInfo != null) {
                JmxNodeIdentifier identifier =
                        JmxConnectionHelper.getProxy(connection, nodeIdentifierBeanInfo, JmxNodeIdentifier.class);

                Object nodeName = identifier.getNodeName();
                if (nodeName != null)
                    remoteNodeName = nodeName.toString();
                else
                    remoteNodeName = getDefaultNodeName(instance);
            } else {
                remoteNodeName = getDefaultNodeName(instance);
            }
        } catch (IOException e) {
            throw new JmxControlException(e);
        }

        return remoteNodeName;
    }

    @Override
    public List<ManagedBeanInfo> getManagedBeans(JmxInstance instance) {
        checkNotNull(instance);

        MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);

        try {
            Set<ObjectName> names = connection.queryNames(null, null);
            List<ManagedBeanInfo> infoList = new ArrayList<>();
            for (ObjectName name : names) {
                MBeanInfo info = connection.getMBeanInfo(name);
                ManagedBeanInfo mbi = new ManagedBeanInfo();
                mbi.setClassName(info.getClassName());
                mbi.setDescription(info.getDescription());
                mbi.setObjectName(name.toString());
                mbi.setDomain(name.getDomain());
                mbi.setPropertyList(name.getKeyPropertyListString());
                mbi.setJmxInstance(instance);

                loadOperations(mbi, info);

                infoList.add(mbi);
            }

            Collections.sort(infoList, new MBeanComparator());
            return infoList;
        } catch (IOException | IntrospectionException | ReflectionException | InstanceNotFoundException e) {
            throw new JmxControlException(e);
        }
    }

    @Override
    public ManagedBeanInfo loadAttributes(ManagedBeanInfo mbinfo) {
        checkNotNull(mbinfo);
        checkNotNull(mbinfo.getJmxInstance());

        try {
            MBeanServerConnection connection = JmxConnectionHelper.getConnection(mbinfo.getJmxInstance());
            ObjectName name = new ObjectName(mbinfo.getObjectName());
            MBeanInfo info = connection.getMBeanInfo(name);
            List<ManagedBeanAttribute> attrs = new ArrayList<>();
            MBeanAttributeInfo[] attributes = info.getAttributes();
            for (MBeanAttributeInfo attribute : attributes) {
                ManagedBeanAttribute mba = new ManagedBeanAttribute();
                mba.setMbean(mbinfo);
                mba.setName(attribute.getName());
                mba.setType(cleanType(attribute.getType()));
                mba.setReadable(attribute.isReadable());
                mba.setWriteable(attribute.isWritable());

                String mask = "";
                if (attribute.isReadable()) mask += "R";
                if (attribute.isWritable()) mask += "W";
                mba.setReadableWriteable(mask);

                if (mba.getReadable())
                    try {
                        Object value = connection.getAttribute(name, mba.getName());
                        setSerializableValue(mba, value);
                    } catch (Exception e) {
                        log.error(e);
                        mba.setValue(e.getMessage());
                        mba.setWriteable(false);
                    }

                attrs.add(mba);
            }
            Collections.sort(attrs, new AttributeComparator());
            mbinfo.setAttributes(attrs);
            return mbinfo;
        } catch (Exception e) {
            throw new JmxControlException(e);
        }
    }

    @Override
    public ManagedBeanAttribute loadAttributeValue(ManagedBeanAttribute attribute) {
        checkNotNull(attribute);
        checkNotNull(attribute.getMbean());
        checkNotNull(attribute.getMbean().getJmxInstance());

        try {
            MBeanServerConnection connection = JmxConnectionHelper.getConnection(attribute.getMbean().getJmxInstance());

            ObjectName name = new ObjectName(attribute.getMbean().getObjectName());

            Object value = null;
            if (attribute.getReadable())
                try {
                    value = connection.getAttribute(name, attribute.getName());
                } catch (Exception e) {
                    log.error(e);
                    value = e.getMessage();
                }
            setSerializableValue(attribute, value);
            return attribute;
        } catch (MalformedObjectNameException e) {
            throw new JmxControlException(e);
        }
    }

    @Override
    public void saveAttributeValue(ManagedBeanAttribute attribute) {
        checkNotNull(attribute);
        checkNotNull(attribute.getMbean());
        checkNotNull(attribute.getMbean().getJmxInstance());

        try {
            MBeanServerConnection connection = JmxConnectionHelper.getConnection(attribute.getMbean().getJmxInstance());

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

            throw new JmxControlException(e);
        }
    }

    @Override
    public Object invokeOperation(ManagedBeanOperation operation, Object[] parameterValues) {
        checkNotNull(operation);
        checkNotNull(operation.getMbean());
        checkNotNull(operation.getMbean().getJmxInstance());

        try {
            MBeanServerConnection connection = JmxConnectionHelper.getConnection(operation.getMbean().getJmxInstance());
            ObjectName name = new ObjectName(operation.getMbean().getObjectName());

            String[] types = new String[operation.getParameters().size()];
            for (int i = 0; i < operation.getParameters().size(); i++) {
                types[i] = operation.getParameters().get(i).getType();
            }

            log.info(String.format("Invoke method '%s' from '%s' on '%s'",
                    operation.getName(), name.getCanonicalName(), operation.getMbean().getJmxInstance().getNodeName()));
            return connection.invoke(name, operation.getName(), parameterValues, types);
        } catch (IOException | MalformedObjectNameException | ReflectionException
                | MBeanException | InstanceNotFoundException e) {

            log.warn(String.format("Error in method invocation '%s' from '%s' on '%s'",
                    operation.getName(), operation.getMbean().getObjectName(),
                    operation.getMbean().getJmxInstance().getNodeName()), e);
            throw new JmxControlException(e);
        }
    }

    @Override
    public List<ManagedBeanDomain> getDomains(JmxInstance instance) {
        checkNotNull(instance);

        MBeanServerConnection connection = JmxConnectionHelper.getConnection(instance);

        try {
            String[] domains = connection.getDomains();
            List<ManagedBeanDomain> domainList = new ArrayList<>();
            for (String d : domains) {
                ManagedBeanDomain mbd = new ManagedBeanDomain();
                mbd.setName(d);
                domainList.add(mbd);
            }
            Collections.sort(domainList, new DomainComparator());
            return domainList;
        } catch (IOException e) {
            throw new JmxControlException(e);
        }
    }

    private void loadOperations(ManagedBeanInfo mbean, MBeanInfo info) {
        List<ManagedBeanOperation> opList = new ArrayList<>();
        MBeanOperationInfo[] operations = info.getOperations();

        for (MBeanOperationInfo operation : operations) {
            String role = (String) operation.getDescriptor().getFieldValue(FIELD_ROLE);
            if (ROLE_GETTER.equals(role) || ROLE_SETTER.equals(role)) {
                continue; // these operations do the same as reading / writing attributes
            }

            ManagedBeanOperation o = new ManagedBeanOperation();
            o.setName(operation.getName());
            o.setDescription(operation.getDescription());
            o.setMbean(mbean);
            o.setReturnType(cleanType(operation.getReturnType()));

            List<ManagedBeanOperationParameter> paramList = new ArrayList<>();
            if (operation.getSignature() != null) {
                for (int index = 0; index < operation.getSignature().length; index++) {
                    MBeanParameterInfo pinfo = operation.getSignature()[index];
                    ManagedBeanOperationParameter p = new ManagedBeanOperationParameter();
                    p.setName(pinfo.getName());
                    p.setType(cleanType(pinfo.getType()));
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
        Collections.sort(opList, new OperationComparator());
        mbean.setOperations(opList);
    }

    private String cleanType(String type) {
        if (type != null && type.startsWith("[L") && type.endsWith(";")) {
            return type.substring(2, type.length() - 1) + "[]";
        }
        return type;
    }

    private void setSerializableValue(ManagedBeanAttribute mba, Object value) {
        if (value instanceof Serializable && !(value instanceof Proxy))
            mba.setValue(value);
        else if (value != null)
            mba.setValue(value.toString());
    }

    private String getDefaultNodeName(@SuppressWarnings("unused") JmxInstance instance) {
        return "Unknown JMX interface";
    }

    /** Sorts domains alphabetically by name **/
    private static class DomainComparator implements Comparator<ManagedBeanDomain> {
        @Override
        public int compare(ManagedBeanDomain mbd1, ManagedBeanDomain mbd2) {
            return mbd1 != null && mbd1.getName() != null
                    ? mbd1.getName().compareTo(mbd2.getName())
                    : (mbd2 != null && mbd2.getName() != null ? 1 : 0);
        }
    }

    /** Sorts mbeans alphabetically by name **/
    private static class MBeanComparator implements Comparator<ManagedBeanInfo> {
        @Override
        public int compare(ManagedBeanInfo mbd1, ManagedBeanInfo mbd2) {
            return mbd1 != null && mbd1.getPropertyList() != null
                    ? mbd1.getPropertyList().compareTo(mbd2.getPropertyList())
                    : (mbd2 != null && mbd2.getPropertyList() != null ? 1 : 0);
        }
    }

    /** Sorts attributes alphabetically by name **/
    private static class AttributeComparator implements Comparator<ManagedBeanAttribute> {
        @Override
        public int compare(ManagedBeanAttribute mbd1, ManagedBeanAttribute mbd2) {
            return mbd1 != null && mbd1.getName() != null
                    ? mbd1.getName().compareTo(mbd2.getName())
                    : (mbd2 != null && mbd2.getName() != null ? 1 : 0);
        }
    }

    /** Sorts operations alphabetically by name **/
    private static class OperationComparator implements Comparator<ManagedBeanOperation> {
        @Override
        public int compare(ManagedBeanOperation o1, ManagedBeanOperation o2) {
            return o1 != null && o1.getName() != null
                    ? o1.getName().compareTo(o2.getName())
                    : (o2 != null && o2.getName() != null ? 1 : 0);
        }
    }
}