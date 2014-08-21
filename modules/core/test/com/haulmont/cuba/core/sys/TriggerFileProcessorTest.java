/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.global.AppBeans;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class TriggerFileProcessorTest extends CubaTestCase {

    public void testProcessFile() throws Exception {
        TriggerFilesProcessor triggerFilesProcessor = AppBeans.get(TriggerFilesProcessor.class);
        triggerFilesProcessor.processFile("cuba_ClassLoaderManager.clearCache");
        triggerFilesProcessor.processFile("cuba_ClassLoaderManager.reloadClass('com.haulmont.cuba.core.sys.TriggerFilesProcessor')");

    }
}
