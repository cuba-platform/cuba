/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys.aop;

import com.haulmont.cuba.core.global.BeanValidation;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

public class CubaMethodValidationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor
        implements InitializingBean {
    protected BeanValidation beanValidation;

    public void setBeanValidation(BeanValidation beanValidation) {
        this.beanValidation = beanValidation;
    }

    @Override
    public void afterPropertiesSet() {
        Pointcut pointcut = new AnnotationMatchingPointcut(Service.class, Validated.class, true);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, createMethodValidationAdvice(this.beanValidation));
        advisor.setOrder(2);
        this.advisor = advisor;
    }

    protected Advice createMethodValidationAdvice(BeanValidation beanValidation) {
        return new CubaMethodValidationInterceptor(beanValidation);
    }
}
