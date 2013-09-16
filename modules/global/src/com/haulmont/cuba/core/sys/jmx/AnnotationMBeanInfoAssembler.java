/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jmx;

import com.google.common.collect.Maps;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.AbstractReflectiveMBeanInfoAssembler;
import org.springframework.jmx.export.metadata.*;
import org.springframework.jmx.support.JmxUtils;
import org.springframework.jmx.support.MetricType;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * This assembler is a hybrid of {@link org.springframework.jmx.export.assembler.InterfaceBasedMBeanInfoAssembler}
 * and {@link org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler}.
 * <br/>
 * It auto-detects jmx interface either by *-MBean naming convention or by looking for @ManagedResource annotated interface.
 * Any getter, setter or operation of jmx interface become JMX-exposed.
 * Bean, operation, operation parameter and attribute descriptions can be customized by using spring annotations
 * (like for MetadataMBeanInfoAssembler).
 * <br/>
 * If getter or setter is annotated as @ManagedOperation, it is considered as heavy operation.
 * Such method is exposed as operation, not as attribute accessor.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public class AnnotationMBeanInfoAssembler extends AbstractReflectiveMBeanInfoAssembler {

    /* Map: Bean name -> jmx interface */
    private Map<String, Class> interfaceCache = Maps.newHashMap();

    /* Extracts annotation information from jmx interface */
    private JmxAttributeSource attributeSource;

    public AnnotationMBeanInfoAssembler() {
        attributeSource = new AnnotationJmxAttributeSource();
    }

    public void setAttributeSource(JmxAttributeSource attributeSource) {
        this.attributeSource = attributeSource;
    }

    private Class findJmxInterface(String beanKey, Class<?> beanClass) {
        Class cachedInterface = interfaceCache.get(beanKey);
        if (cachedInterface != null) {
            return cachedInterface;
        }

        Class mbeanInterface = JmxUtils.getMBeanInterface(beanClass);
        if (mbeanInterface != null) { // found with MBean ending
            interfaceCache.put(beanKey, mbeanInterface);
            return mbeanInterface;
        }

        Class[] ifaces = ClassUtils.getAllInterfacesForClass(beanClass);

        for (Class ifc : ifaces) {
            ManagedResource metadata = attributeSource.getManagedResource(ifc);
            if (metadata != null) { // found with @ManagedResource annotation
                interfaceCache.put(beanKey, ifc);
                return ifc;
            }
        }

        String msg = "Bean " + beanKey + " doesn't implement management interfaces. " +
                "Management interface should either follow naming scheme or be annotated by @ManagedResource";
        throw new IllegalArgumentException(msg);
    }

    /**
     * Vote on the inclusion of an attribute accessor.
     *
     * @param method  the accessor method
     * @param beanKey the key associated with the MBean in the beans map
     * @return whether the method has the appropriate metadata
     */
    @Override
    protected boolean includeReadAttribute(Method method, String beanKey) {
        Method interfaceMethod = findJmxMethod(method, beanKey);
        boolean metric = interfaceMethod != null && attributeSource.getManagedMetric(interfaceMethod) != null;
        boolean operation = interfaceMethod != null && attributeSource.getManagedOperation(interfaceMethod) != null;

        // either metric or just interface method without @Operation annotation
        boolean result = interfaceMethod != null && (metric || !operation);
        return result;
    }

    /**
     * Votes on the inclusion of an attribute mutator.
     *
     * @param method  the mutator method
     * @param beanKey the key associated with the MBean in the beans map
     * @return whether the method has the appropriate metadata
     */
    @Override
    protected boolean includeWriteAttribute(Method method, String beanKey) {
        Method interfaceMethod = findJmxMethod(method, beanKey);
        boolean operation = interfaceMethod != null && attributeSource.getManagedOperation(interfaceMethod) != null;

        // @Operation annotation means it's really an operation, not attribute setter
        boolean result = interfaceMethod != null && !operation;
        return result;
    }

    /**
     * Votes on the inclusion of an operation.
     *
     * @param method  the operation method
     * @param beanKey the key associated with the MBean in the beans map
     * @return whether the method has the appropriate metadata
     */
    @Override
    protected boolean includeOperation(Method method, String beanKey) {
        Method interfaceMethod = findJmxMethod(method, beanKey);
        return interfaceMethod != null;
    }

    /* Try to find method exposed in bean JMX interface. Return null if not found */
    private Method findJmxMethod(Method method, String beanKey) {
        if (method == null) {
            return null;
        }
        Class ifc = findJmxInterface(beanKey, method.getDeclaringClass());
        if (ifc == null) {
            return null;
        }

        for (Method ifcMethod : ifc.getMethods()) {
            if (ifcMethod.getName().equals(method.getName()) &&
                    Arrays.equals(ifcMethod.getParameterTypes(), method.getParameterTypes())) {
                return ifcMethod;
            }
        }
        return null;
    }

    /**
     * Reads managed resource description from the source level metadata.
     * Returns an empty <code>String</code> if no description can be found.
     */
    @Override
    protected String getDescription(Object managedBean, String beanKey) {
        Class ifc = findJmxInterface(beanKey, AopUtils.getTargetClass(managedBean));
        ManagedResource mr = this.attributeSource.getManagedResource(ifc);
        return (mr != null ? mr.getDescription() : null);
    }

    /**
     * Creates a description for the attribute corresponding to this property
     * descriptor. Attempts to create the description using metadata from either
     * the getter or setter attributes, otherwise uses the property name.
     */
    @Override
    protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();

        Method resolvedGetter = findJmxMethod(readMethod, beanKey);
        Method resolvedSetter = findJmxMethod(writeMethod, beanKey);

        ManagedAttribute getter =
                (resolvedGetter != null ? this.attributeSource.getManagedAttribute(resolvedGetter) : null);
        ManagedAttribute setter =
                (resolvedSetter != null ? this.attributeSource.getManagedAttribute(resolvedSetter) : null);

        if (getter != null && StringUtils.hasText(getter.getDescription())) {
            return getter.getDescription();
        } else if (setter != null && StringUtils.hasText(setter.getDescription())) {
            return setter.getDescription();
        }

        ManagedMetric metric = (resolvedGetter != null ? this.attributeSource.getManagedMetric(resolvedGetter) : null);
        if (metric != null && StringUtils.hasText(metric.getDescription())) {
            return metric.getDescription();
        }

        return null;
    }

    /**
     * Retrieves the description for the supplied <code>Method</code> from the
     * metadata. Uses the method name is no description is present in the metadata.
     */
    @Override
    protected String getOperationDescription(Method method, String beanKey) {
        PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
        Method resolvedMethod = findJmxMethod(method, beanKey);
        if (pd != null) {
            ManagedAttribute ma = this.attributeSource.getManagedAttribute(resolvedMethod);
            if (ma != null && StringUtils.hasText(ma.getDescription())) {
                return ma.getDescription();
            }
            ManagedMetric metric = this.attributeSource.getManagedMetric(resolvedMethod);
            if (metric != null && StringUtils.hasText(metric.getDescription())) {
                return metric.getDescription();
            }
        }

        ManagedOperation mo = this.attributeSource.getManagedOperation(resolvedMethod);
        if (mo != null && StringUtils.hasText(mo.getDescription())) {
            return mo.getDescription();
        }
        return null; // no operation description by default
    }

    /**
     * Reads <code>MBeanParameterInfo</code> from the <code>ManagedOperationParameter</code>
     * attributes attached to a method. Returns an empty array of <code>MBeanParameterInfo</code>
     * if no attributes are found.
     */
    @Override
    protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
        Method resolvedMethod = findJmxMethod(method, beanKey);
        ManagedOperationParameter[] params = this.attributeSource.getManagedOperationParameters(resolvedMethod);
        if (params == null || params.length == 0) {
            return new MBeanParameterInfo[0];
        }

        MBeanParameterInfo[] parameterInfo = new MBeanParameterInfo[params.length];
        Class[] methodParameters = method.getParameterTypes();

        for (int i = 0; i < params.length; i++) {
            ManagedOperationParameter param = params[i];
            parameterInfo[i] =
                    new MBeanParameterInfo(param.getName(), methodParameters[i].getName(), param.getDescription());
        }

        return parameterInfo;
    }

    /**
     * Reads the {@link ManagedNotification} metadata from the <code>Class</code> of the managed resource
     * and generates and returns the corresponding {@link javax.management.modelmbean.ModelMBeanNotificationInfo} metadata.
     */
    @Override
    protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
        Class intf = findJmxInterface(beanKey, AopUtils.getTargetClass(managedBean));
        ManagedNotification[] notificationAttributes =
                this.attributeSource.getManagedNotifications(intf);
        ModelMBeanNotificationInfo[] notificationInfos =
                new ModelMBeanNotificationInfo[notificationAttributes.length];

        for (int i = 0; i < notificationAttributes.length; i++) {
            ManagedNotification attribute = notificationAttributes[i];
            notificationInfos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(attribute);
        }

        return notificationInfos;
    }

    /**
     * Adds descriptor fields from the <code>ManagedResource</code> attribute
     * to the MBean descriptor. Specifically, adds the <code>currencyTimeLimit</code>,
     * <code>persistPolicy</code>, <code>persistPeriod</code>, <code>persistLocation</code>
     * and <code>persistName</code> descriptor fields if they are present in the metadata.
     */
    @Override
    protected void populateMBeanDescriptor(Descriptor desc, Object managedBean, String beanKey) {
        Class intf = findJmxInterface(beanKey, AopUtils.getTargetClass(managedBean));
        ManagedResource mr = this.attributeSource.getManagedResource(intf);
        if (mr == null) {
            applyDefaultCurrencyTimeLimit(desc);
            return;
        }

        applyCurrencyTimeLimit(desc, mr.getCurrencyTimeLimit());

        if (mr.isLog()) {
            desc.setField(FIELD_LOG, "true");
        }
        if (StringUtils.hasLength(mr.getLogFile())) {
            desc.setField(FIELD_LOG_FILE, mr.getLogFile());
        }

        if (StringUtils.hasLength(mr.getPersistPolicy())) {
            desc.setField(FIELD_PERSIST_POLICY, mr.getPersistPolicy());
        }
        if (mr.getPersistPeriod() >= 0) {
            desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(mr.getPersistPeriod()));
        }
        if (StringUtils.hasLength(mr.getPersistName())) {
            desc.setField(FIELD_PERSIST_NAME, mr.getPersistName());
        }
        if (StringUtils.hasLength(mr.getPersistLocation())) {
            desc.setField(FIELD_PERSIST_LOCATION, mr.getPersistLocation());
        }
    }

    /**
     * Adds descriptor fields from the <code>ManagedAttribute</code> attribute or the <code>ManagedMetric</code> attribute
     * to the attribute descriptor.
     */
    @Override
    protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey) {
        Method resolvedGetter = findJmxMethod(getter, beanKey);
        Method resolvedSetter = findJmxMethod(setter, beanKey);
        ManagedMetric metricInfo = resolvedGetter != null ? attributeSource.getManagedMetric(resolvedGetter) : null;
        if (getter != null && metricInfo != null) {
            populateMetricDescriptor(desc, metricInfo);
        } else {
            ManagedAttribute gma =
                    (resolvedGetter == null) ? null : this.attributeSource.getManagedAttribute(resolvedGetter);
            ManagedAttribute sma =
                    (resolvedSetter == null) ? null : this.attributeSource.getManagedAttribute(resolvedSetter);
            if (gma == null) {
                gma = ManagedAttribute.EMPTY;
            }
            if (sma == null) {
                sma = ManagedAttribute.EMPTY;
            }
            populateAttributeDescriptor(desc, gma, sma);
        }
    }

    private void populateAttributeDescriptor(Descriptor desc, ManagedAttribute gma, ManagedAttribute sma) {
        applyCurrencyTimeLimit(desc, resolveIntDescriptor(gma.getCurrencyTimeLimit(), sma.getCurrencyTimeLimit()));

        Object defaultValue = resolveObjectDescriptor(gma.getDefaultValue(), sma.getDefaultValue());
        desc.setField(FIELD_DEFAULT, defaultValue);

        String persistPolicy = resolveStringDescriptor(gma.getPersistPolicy(), sma.getPersistPolicy());
        if (StringUtils.hasLength(persistPolicy)) {
            desc.setField(FIELD_PERSIST_POLICY, persistPolicy);
        }
        int persistPeriod = resolveIntDescriptor(gma.getPersistPeriod(), sma.getPersistPeriod());
        if (persistPeriod >= 0) {
            desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(persistPeriod));
        }
    }

    private void populateMetricDescriptor(Descriptor desc, ManagedMetric metric) {
        applyCurrencyTimeLimit(desc, metric.getCurrencyTimeLimit());

        if (StringUtils.hasLength(metric.getPersistPolicy())) {
            desc.setField(FIELD_PERSIST_POLICY, metric.getPersistPolicy());
        }
        if (metric.getPersistPeriod() >= 0) {
            desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(metric.getPersistPeriod()));
        }

        if (StringUtils.hasLength(metric.getDisplayName())) {
            desc.setField(FIELD_DISPLAY_NAME, metric.getDisplayName());
        }

        if (StringUtils.hasLength(metric.getUnit())) {
            desc.setField(FIELD_UNITS, metric.getUnit());
        }

        if (StringUtils.hasLength(metric.getCategory())) {
            desc.setField(FIELD_METRIC_CATEGORY, metric.getCategory());
        }

        String metricType = (metric.getMetricType() == null) ? MetricType.GAUGE.toString() : metric.getMetricType().toString();
        desc.setField(FIELD_METRIC_TYPE, metricType);
    }

    /**
     * Adds descriptor fields from the <code>ManagedAttribute</code> attribute
     * to the attribute descriptor. Specifically, adds the <code>currencyTimeLimit</code>
     * descriptor field if it is present in the metadata.
     */
    @Override
    protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey) {
        Method resolvedOperation = findJmxMethod(method, beanKey);
        ManagedOperation mo = this.attributeSource.getManagedOperation(resolvedOperation);
        if (mo != null) {
            applyCurrencyTimeLimit(desc, mo.getCurrencyTimeLimit());
        }
    }

    /**
     * Determines which of two <code>int</code> values should be used as the value
     * for an attribute descriptor. In general, only the getter or the setter will
     * be have a non-negative value so we use that value. In the event that both values
     * are non-negative, we use the greater of the two. This method can be used to
     * resolve any <code>int</code> valued descriptor where there are two possible values.
     *
     * @param getter the int value associated with the getter for this attribute
     * @param setter the int associated with the setter for this attribute
     */
    private int resolveIntDescriptor(int getter, int setter) {
        return (getter >= setter ? getter : setter);
    }

    /**
     * Locates the value of a descriptor based on values attached
     * to both the getter and setter methods. If both have values
     * supplied then the value attached to the getter is preferred.
     *
     * @param getter the Object value associated with the get method
     * @param setter the Object value associated with the set method
     * @return the appropriate Object to use as the value for the descriptor
     */
    private Object resolveObjectDescriptor(Object getter, Object setter) {
        return (getter != null ? getter : setter);
    }

    /**
     * Locates the value of a descriptor based on values attached
     * to both the getter and setter methods. If both have values
     * supplied then the value attached to the getter is preferred.
     * The supplied default value is used to check to see if the value
     * associated with the getter has changed from the default.
     *
     * @param getter the String value associated with the get method
     * @param setter the String value associated with the set method
     * @return the appropriate String to use as the value for the descriptor
     */
    private String resolveStringDescriptor(String getter, String setter) {
        return (StringUtils.hasLength(getter) ? getter : setter);
    }
}