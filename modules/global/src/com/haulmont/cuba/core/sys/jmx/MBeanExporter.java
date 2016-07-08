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

package com.haulmont.cuba.core.sys.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.support.MBeanRegistrationSupport;

import javax.management.DynamicMBean;
import javax.management.JMException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Tweaked MBean exporter.
 * <br/>
 * Difference from spring one is that it does not try to expose bean as {@link javax.management.StandardMBean}
 * if it complies to MyObject -> MyObjectMBean naming scheme.
 * <br/>
 * Instead it uses {@link AnnotationMBeanInfoAssembler} to construct MBean descriptor for every bean.
 */
public class MBeanExporter extends org.springframework.jmx.export.MBeanExporter {

    private final Logger log = LoggerFactory.getLogger(MBeanExporter.class);

    public MBeanExporter() {
        setAssembler(new AnnotationMBeanInfoAssembler());
        // hack logging
        try {
            Field loggerField = MBeanRegistrationSupport.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            loggerField.set(this, LoggerFactory.getLogger(org.springframework.jmx.export.MBeanExporter.class));
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        // hack logging
        Map beans = null;
        try {
            Field beansField = org.springframework.jmx.export.MBeanExporter.class.getDeclaredField("beans");
            beansField.setAccessible(true);
            beans = (Map) beansField.get(this);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
        if (beans != null) {
            log.info("Registering beans for JMX exposure: " + beans.keySet());
        }

        super.afterSingletonsInstantiated();
    }

    @Override
    protected boolean isMBean(Class beanClass) {
        /* Never try to adapt to StandardMBean */
        return false;
    }

    @Override
    protected DynamicMBean adaptMBeanIfPossible(Object bean) throws JMException {
        /* Never adapt */
        return null;
    }
}
