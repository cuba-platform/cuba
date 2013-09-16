/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
