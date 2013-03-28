/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.jmx;

import javax.management.DynamicMBean;
import javax.management.JMException;

/**
 * Tweaked MBean exporter.
 * <br/>
 * Difference from spring one is that it does not try to expose bean as {@link javax.management.StandardMBean}
 * if it complies to MyObject -> MyObjectMBean naming scheme.
 * <br/>
 * Instead it uses {@link AnnotationMBeanInfoAssembler} to construct MBean descriptor for every bean.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public class MBeanExporter extends org.springframework.jmx.export.MBeanExporter {

    public MBeanExporter() {
        setAssembler(new AnnotationMBeanInfoAssembler());
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
