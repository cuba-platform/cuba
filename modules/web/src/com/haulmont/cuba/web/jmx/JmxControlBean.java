/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.NodeIdentifier;
import com.haulmont.cuba.core.sys.jmx.JmxNodeIdentifierMBean;
import com.haulmont.cuba.web.jmx.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.management.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.web.jmx.JmxConnectionHelper.getObjectName;
import static com.haulmont.cuba.web.jmx.JmxConnectionHelper.withConnection;

/**
 * @author artamonov
 * @version $Id$
 */
@Component(JmxControlAPI.NAME)
public class JmxControlBean implements JmxControlAPI {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected DataService dataService;

    @Inject
    protected NodeIdentifier nodeIdentifier;

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
        JmxInstance localJmxInstance = metadata.create(JmxInstance.class);
        localJmxInstance.setId(JmxConnectionHelper.LOCAL_JMX_INSTANCE_ID);
        localJmxInstance.setNodeName(getLocalNodeName());
        return localJmxInstance;
    }

    @Override
    public String getLocalNodeName() {
        return nodeIdentifier.getNodeName();
    }

    @Override
    public String getRemoteNodeName(JmxInstance instance) {
        checkNotNullArgument(instance);

        //noinspection UnnecessaryLocalVariable
        String remoteNodeName = withConnection(instance, new JmxAction<String>() {
            @Override
            public String perform(JmxInstance jmx, MBeanServerConnection connection) throws IOException {
                ObjectName nodeIdentifierBeanInfo = getObjectName(connection, JmxNodeIdentifierMBean.class);

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
            }
        });

        return remoteNodeName;
    }

    @Override
    public List<ManagedBeanInfo> getManagedBeans(JmxInstance instance) {
        checkNotNullArgument(instance);

        //noinspection UnnecessaryLocalVariable
        List<ManagedBeanInfo> infos = withConnection(instance, new JmxAction<List<ManagedBeanInfo>>() {
            @Override
            public List<ManagedBeanInfo> perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                Set<ObjectName> names = connection.queryNames(null, null);
                List<ManagedBeanInfo> infoList = new ArrayList<>();
                for (ObjectName name : names) {
                    MBeanInfo info = connection.getMBeanInfo(name);
                    ManagedBeanInfo mbi = createManagedBeanInfo(jmx, name, info);
                    loadOperations(mbi, info);
                    infoList.add(mbi);
                }

                Collections.sort(infoList, new MBeanComparator());
                return infoList;
            }
        });

        return infos;
    }

    @Override
    public ManagedBeanInfo getManagedBean(JmxInstance instance, final String beanObjectName) {
        checkNotNullArgument(instance);
        checkNotNullArgument(beanObjectName);

        //noinspection UnnecessaryLocalVariable
        ManagedBeanInfo info = withConnection(instance, new JmxAction<ManagedBeanInfo>() {
            @Override
            public ManagedBeanInfo perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                Set<ObjectName> names = connection.queryNames(new ObjectName(beanObjectName), null);
                ManagedBeanInfo mbi = null;
                if (!names.isEmpty())
                {
                    ObjectName name = names.iterator().next();
                    MBeanInfo info = connection.getMBeanInfo(name);
                    mbi = createManagedBeanInfo(jmx, name, info);
                    loadOperations(mbi, info);
                }

                return mbi;
            }
        });

        return info;
    }

    private ManagedBeanInfo createManagedBeanInfo(JmxInstance jmx, ObjectName name, MBeanInfo info) {
        ManagedBeanInfo mbi = new ManagedBeanInfo();
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

        withConnection(mbinfo.getJmxInstance(), new JmxAction<Void>() {
            @Override
            public Void perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                ObjectName name = new ObjectName(mbinfo.getObjectName());
                MBeanInfo info = connection.getMBeanInfo(name);
                List<ManagedBeanAttribute> attrs = new ArrayList<>();
                MBeanAttributeInfo[] attributes = info.getAttributes();
                for (MBeanAttributeInfo attribute : attributes) {
                    ManagedBeanAttribute mba = createManagedBeanAttribute(connection, name, attribute, mbinfo);
                    attrs.add(mba);
                }
                Collections.sort(attrs, new AttributeComparator());
                mbinfo.setAttributes(attrs);
                return null;
            }
        });
    }

    @Override
    public ManagedBeanAttribute loadAttribute(final ManagedBeanInfo mbinfo, final String attributeName) {
        checkNotNullArgument(mbinfo);
        checkNotNullArgument(attributeName);

        //noinspection UnnecessaryLocalVariable
        ManagedBeanAttribute attribute = withConnection(mbinfo.getJmxInstance(), new JmxAction<ManagedBeanAttribute>() {
            @Override
            public ManagedBeanAttribute perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                ObjectName name = new ObjectName(mbinfo.getObjectName());
                MBeanInfo info = connection.getMBeanInfo(name);
                MBeanAttributeInfo[] attributes = info.getAttributes();
                ManagedBeanAttribute res = null;
                for (MBeanAttributeInfo attribute : attributes) {
                    if (attribute.getName().equals(attributeName)) {
                        res = createManagedBeanAttribute(connection, name, attribute, mbinfo);
                        break;
                    }

                }
                return res;
            }
        });
        return attribute;
    }

    private ManagedBeanAttribute createManagedBeanAttribute(MBeanServerConnection connection, ObjectName name, MBeanAttributeInfo attribute, ManagedBeanInfo mbinfo) {
        ManagedBeanAttribute mba = new ManagedBeanAttribute();
        mba.setMbean(mbinfo);
        mba.setName(attribute.getName());
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

        withConnection(attribute.getMbean().getJmxInstance(), new JmxAction<Void>() {
            @Override
            public Void perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
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
            }
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
                if ((args.isEmpty() && argTypes==null) || args.size()==argTypes.length) {
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

        withConnection(attribute.getMbean().getJmxInstance(), new JmxAction<Void>() {
            @Override
            public Void perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
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
            }
        });
    }

    @Override
    public Object invokeOperation(final ManagedBeanOperation operation, final Object[] parameterValues) {
        checkNotNullArgument(operation);
        checkNotNullArgument(operation.getMbean());
        checkNotNullArgument(operation.getMbean().getJmxInstance());

        //noinspection UnnecessaryLocalVariable
        Object result = withConnection(operation.getMbean().getJmxInstance(), new JmxAction<Object>() {
            @Override
            public Object perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                try {
                    ObjectName name = new ObjectName(operation.getMbean().getObjectName());

                    String[] types = new String[operation.getParameters().size()];
                    for (int i = 0; i < operation.getParameters().size(); i++) {
                        types[i] = operation.getParameters().get(i).getType();
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
            }
        });

        return result;
    }

    @Override
    public List<ManagedBeanDomain> getDomains(JmxInstance instance) {
        checkNotNullArgument(instance);

        //noinspection UnnecessaryLocalVariable
        List<ManagedBeanDomain> domains = withConnection(instance, new JmxAction<List<ManagedBeanDomain>>() {
            @Override
            public List<ManagedBeanDomain> perform(JmxInstance jmx, MBeanServerConnection connection) throws Exception {
                String[] domains = connection.getDomains();
                List<ManagedBeanDomain> domainList = new ArrayList<>();
                for (String d : domains) {
                    ManagedBeanDomain mbd = new ManagedBeanDomain();
                    mbd.setName(d);
                    domainList.add(mbd);
                }
                Collections.sort(domainList, new DomainComparator());
                return domainList;
            }
        });

        return domains;
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
        if (value instanceof Serializable && !(value instanceof Proxy)) {
            mba.setValue(value);
        } else if (value != null) {
            mba.setValue(value.toString());
        }
    }

    private String getDefaultNodeName(@SuppressWarnings("unused") JmxInstance instance) {
        return "Unknown JMX interface";
    }

    /**
     * Sorts domains alphabetically by name *
     */
    private static class DomainComparator implements Comparator<ManagedBeanDomain> {
        @Override
        public int compare(ManagedBeanDomain mbd1, ManagedBeanDomain mbd2) {
            return mbd1 != null && mbd1.getName() != null
                    ? mbd1.getName().compareTo(mbd2.getName())
                    : (mbd2 != null && mbd2.getName() != null ? 1 : 0);
        }
    }

    /**
     * Sorts mbeans alphabetically by name *
     */
    private static class MBeanComparator implements Comparator<ManagedBeanInfo> {
        @Override
        public int compare(ManagedBeanInfo mbd1, ManagedBeanInfo mbd2) {
            return mbd1 != null && mbd1.getPropertyList() != null
                    ? mbd1.getPropertyList().compareTo(mbd2.getPropertyList())
                    : (mbd2 != null && mbd2.getPropertyList() != null ? 1 : 0);
        }
    }

    /**
     * Sorts attributes alphabetically by name *
     */
    private static class AttributeComparator implements Comparator<ManagedBeanAttribute> {
        @Override
        public int compare(ManagedBeanAttribute mbd1, ManagedBeanAttribute mbd2) {
            return mbd1 != null && mbd1.getName() != null
                    ? mbd1.getName().compareTo(mbd2.getName())
                    : (mbd2 != null && mbd2.getName() != null ? 1 : 0);
        }
    }

    /**
     * Sorts operations alphabetically by name *
     */
    private static class OperationComparator implements Comparator<ManagedBeanOperation> {
        @Override
        public int compare(ManagedBeanOperation o1, ManagedBeanOperation o2) {
            return o1 != null && o1.getName() != null
                    ? o1.getName().compareTo(o2.getName())
                    : (o2 != null && o2.getName() != null ? 1 : 0);
        }
    }
}