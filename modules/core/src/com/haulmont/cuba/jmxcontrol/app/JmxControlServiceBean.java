/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 17.08.2010 10:40:00
 * $Id$
 */

package com.haulmont.cuba.jmxcontrol.app;

import com.haulmont.cuba.jmxcontrol.entity.*;
import com.haulmont.cuba.jmxcontrol.global.JmxControlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;

@Service(JmxControlService.NAME)
public class JmxControlServiceBean implements JmxControlService {

    private static Log log = LogFactory.getLog(JmxControlServiceBean.class);

    public List<ManagedBeanInfo> getManagedBeans() {
        MBeanServerConnection connection = getConnection();

        try {
            Set<ObjectName> names = connection.queryNames(null, null);
            List<ManagedBeanInfo> infoList = new ArrayList<ManagedBeanInfo>();
            for (ObjectName name : names) {
                MBeanInfo info = connection.getMBeanInfo(name);
                ManagedBeanInfo mbi = new ManagedBeanInfo();
                mbi.setClassName(info.getClassName());
                mbi.setDescription(info.getDescription());
                mbi.setObjectName(name.toString());
                mbi.setDomain(name.getDomain());
                mbi.setPropertyList(name.getKeyPropertyListString());

                loadOperations(mbi, info);

                infoList.add(mbi);
            }

            Collections.sort(infoList, new MBeanComparator());
            return infoList;
        }
        catch (IOException e) {
            throw new JmxControlException(e);
        }
        catch (IntrospectionException e) {
            throw new JmxControlException(e);
        }
        catch (ReflectionException e) {
            throw new JmxControlException(e);
        }
        catch (InstanceNotFoundException e) {
            throw new JmxControlException(e);
        }
    }

    private String cleanType(String type) {
        if (type != null && type.startsWith("[L") && type.endsWith(";")) {
            return type.substring(2, type.length() - 1) + "[]";
        }
        return type;
    }

    public void loadAttributes(ManagedBeanInfo mbinfo) {
        try {
            MBeanServerConnection connection = getConnection();
            ObjectName name = new ObjectName(mbinfo.getObjectName());
            MBeanInfo info = connection.getMBeanInfo(name);
            List<ManagedBeanAttribute> attrs = new ArrayList<ManagedBeanAttribute>();
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

                try {
                    Object value = connection.getAttribute(name, mba.getName());
                    mba.setValue(value);
                }
                catch (Exception e) {
                    log.error(e);
                    mba.setValue(e.getMessage());
                    mba.setWriteable(false);
                }

                attrs.add(mba);
            }
            mbinfo.setAttributes(attrs);

        }
        catch (IOException e) {
            throw new JmxControlException(e);
        }
        catch (IntrospectionException e) {
            throw new JmxControlException(e);
        }
        catch (ReflectionException e) {
            throw new JmxControlException(e);
        }
        catch (InstanceNotFoundException e) {
            throw new JmxControlException(e);
        }
        catch (MalformedObjectNameException e) {
            throw new JmxControlException(e);
        }
    }

    public void loadAttributeValue(ManagedBeanAttribute attr) {
        try {
            MBeanServerConnection connection = getConnection();

            ObjectName name = new ObjectName(attr.getMbean().getObjectName());

            Object value;
            try {
                value = connection.getAttribute(name, attr.getName());
            }
            catch (Exception e) {
                log.error(e);
                value = e.getMessage();
            }
            attr.setValue(value);
        }
        catch (MalformedObjectNameException e) {
            throw new JmxControlException(e);
        }
    }

    public void saveAttributeValue(ManagedBeanAttribute attr) {
        try {
            MBeanServerConnection connection = getConnection();

            ObjectName name = new ObjectName(attr.getMbean().getObjectName());

            Attribute a = new Attribute(attr.getName(), attr.getValue());
            connection.setAttribute(name, a);
        }
        catch (IOException e) {
            throw new JmxControlException(e);
        }
        catch (ReflectionException e) {
            throw new JmxControlException(e);
        }
        catch (InstanceNotFoundException e) {
            throw new JmxControlException(e);
        }
        catch (MalformedObjectNameException e) {
            throw new JmxControlException(e);
        }
        catch (AttributeNotFoundException e) {
            throw new JmxControlException(e);
        }
        catch (MBeanException e) {
            throw new JmxControlException(e);
        }
        catch (InvalidAttributeValueException e) {
            throw new JmxControlException(e);
        }
    }

    private void loadOperations(ManagedBeanInfo mbean, MBeanInfo info) {
        List<ManagedBeanOperation> opList = new ArrayList<ManagedBeanOperation>();
        MBeanOperationInfo[] operations = info.getOperations();

        for (MBeanOperationInfo operation: operations) {
            ManagedBeanOperation o = new ManagedBeanOperation();
            o.setName(operation.getName());
            o.setDescription(operation.getDescription());
            o.setMbean(mbean);
            o.setReturnType(cleanType(operation.getReturnType()));

            List<ManagedBeanOperationParameter> paramList = new ArrayList<ManagedBeanOperationParameter>();
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
        mbean.setOperations(opList);
    }

    public Object invokeOperation(ManagedBeanOperation operation, Object[] parameterValues) {
        try {
            MBeanServerConnection connection = getConnection();
            ObjectName name = new ObjectName(operation.getMbean().getObjectName());

            String[] types = new String[operation.getParameters().size()];
            for (int i = 0; i < operation.getParameters().size(); i++) {
                types[i] = operation.getParameters().get(i).getType();
            }

            return connection.invoke(name, operation.getName(), parameterValues, types);
        }
        catch (IOException e) {
            throw new JmxControlException(e);
        }
        catch (MalformedObjectNameException e) {
            throw new JmxControlException(e);
        }
        catch (ReflectionException e) {
            throw new JmxControlException(e);
        }
        catch (MBeanException e) {
            throw new JmxControlException(e);
        }
        catch (InstanceNotFoundException e) {
            throw new JmxControlException(e);
        }
    }

    public List<ManagedBeanDomain> getDomains() {
        MBeanServerConnection connection = getConnection();

        try {
            String[] domains = connection.getDomains();
            List<ManagedBeanDomain> domainList = new ArrayList<ManagedBeanDomain>();
            for (String d : domains) {
                ManagedBeanDomain mbd = new ManagedBeanDomain();
                mbd.setName(d);
                domainList.add(mbd);
            }
            Collections.sort(domainList, new DomainComparator());
            return domainList;
        }
        catch (IOException e) {
            throw new JmxControlException(e);
        }
    }

    private MBeanServerConnection getConnection() {
        return ManagementFactory.getPlatformMBeanServer();
    }

    /*private MBeanServerConnection getRemoteConnection() {
        MBeanServerConnectionFactoryBean factoryBean = new MBeanServerConnectionFactoryBean();
        try {
            JmxControlConfig configuration = ConfigProvider.getConfig(JmxControlConfig.class);

            int port = configuration.getPort();
            factoryBean.setServiceUrl("service:jmx:rmi:///jndi/rmi://localhost:" + String.valueOf(port) + "/jmxrmi");

            String username = configuration.getUsername();
            if (StringUtils.isNotEmpty(username)) {
                Properties properties = new Properties();
                properties.put("jmx.remote.credentials", new String[] {username, configuration.getPassword()});
                factoryBean.setEnvironment(properties);
            }

            factoryBean.afterPropertiesSet();

            return factoryBean.getObject();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    public static class DomainComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            ManagedBeanDomain mbd1 = (ManagedBeanDomain) o1;
            ManagedBeanDomain mbd2 = (ManagedBeanDomain) o2;
            return mbd1 != null && mbd1.getName() != null
                    ? mbd1.getName().compareTo(mbd2.getName())
                    : (mbd2 != null && mbd2.getName() != null ? 1 : 0);
        }
    }

    public static class MBeanComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            ManagedBeanInfo mbd1 = (ManagedBeanInfo) o1;
            ManagedBeanInfo mbd2 = (ManagedBeanInfo) o2;
            return mbd1 != null && mbd1.getPropertyList() != null
                    ? mbd1.getPropertyList().compareTo(mbd2.getPropertyList())
                    : (mbd2 != null && mbd2.getPropertyList() != null ? 1 : 0);
        }
    }

}
