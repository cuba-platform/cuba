/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys.aop;

import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.validation.MethodParametersValidationException;
import com.haulmont.cuba.core.global.validation.MethodResultValidationException;
import com.haulmont.cuba.core.global.validation.ServiceMethodConstraintViolation;
import com.haulmont.cuba.core.global.validation.groups.ServiceParametersChecks;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class CubaMethodValidationInterceptor implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CubaMethodValidationInterceptor.class);

    protected BeanValidation beanValidation;

    public CubaMethodValidationInterceptor(BeanValidation beanValidation) {
        this.beanValidation = beanValidation;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // Avoid Validator invocation on FactoryBean.getObjectType/isSingleton
        if (isFactoryBeanMetadataMethod(invocation.getMethod())) {
            return invocation.proceed();
        }

        log.trace("Validating service call params: {}", invocation.getStaticPart());

        Class<?>[] groups = determineValidationGroups(invocation);

        if (groups.length == 0) {
            groups = new Class[]{Default.class, ServiceParametersChecks.class};
        }

        ExecutableValidator execVal = this.beanValidation.getValidator().forExecutables();
        Method methodToValidate = invocation.getMethod();
        Set<ConstraintViolation<Object>> result;

        try {
            result = execVal.validateParameters(
                    invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
        } catch (IllegalArgumentException ex) {
            // Probably a generic type mismatch between interface and impl as reported in SPR-12237 / HV-1011
            // Let's try to find the bridged method on the implementation class...
            methodToValidate = BridgeMethodResolver.findBridgedMethod(
                    ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
            result = execVal.validateParameters(
                    invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
        }
        if (!result.isEmpty()) {
            Class serviceInterface = invocation.getMethod().getDeclaringClass();
            Set<ConstraintViolation<Object>> resultViolations = result.stream()
                    .map(violation -> new ServiceMethodConstraintViolation(serviceInterface, violation))
                    .collect(Collectors.toSet());

            throw new MethodParametersValidationException("Service method parameters validation failed", resultViolations);
        }

        Object returnValue = invocation.proceed();

        log.trace("Validating service call result: {}", invocation.getStaticPart());

        result = execVal.validateReturnValue(invocation.getThis(), methodToValidate, returnValue, groups);
        if (!result.isEmpty()) {
            Class serviceInterface = invocation.getMethod().getDeclaringClass();
            Set<ConstraintViolation<Object>> paramsViolations = result.stream()
                    .map(violation -> new ServiceMethodConstraintViolation(serviceInterface, violation))
                    .collect(Collectors.toSet());

            throw new MethodResultValidationException("Service method result validation failed", paramsViolations);
        }

        return returnValue;
    }

    protected boolean isFactoryBeanMetadataMethod(Method method) {
        Class<?> clazz = method.getDeclaringClass();

        // Call from interface-based proxy handle, allowing for an efficient check?
        if (clazz.isInterface()) {
            return ((clazz == FactoryBean.class || clazz == SmartFactoryBean.class) &&
                    !method.getName().equals("getObject"));
        }

        // Call from CGLIB proxy handle, potentially implementing a FactoryBean method?
        Class<?> factoryBeanType = null;
        if (SmartFactoryBean.class.isAssignableFrom(clazz)) {
            factoryBeanType = SmartFactoryBean.class;
        } else if (FactoryBean.class.isAssignableFrom(clazz)) {
            factoryBeanType = FactoryBean.class;
        }
        return (factoryBeanType != null && !method.getName().equals("getObject") &&
                ClassUtils.hasMethod(factoryBeanType, method.getName(), method.getParameterTypes()));
    }

    protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
        Validated validatedAnn = AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
        if (validatedAnn == null) {
            validatedAnn = AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
        }
        return (validatedAnn != null ? validatedAnn.value() : new Class<?>[0]);
    }
}
