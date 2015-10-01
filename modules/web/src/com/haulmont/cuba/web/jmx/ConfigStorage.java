/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.config.ConfigStorageCommon;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_ConfigStorageMBean")
public class ConfigStorage implements ConfigStorageMBean {

    @Inject
    protected ConfigStorageCommon configStorageCommon;

    @Override
    public String printAppProperties() {
        return printAppProperties(null);
    }

    @Override
    public String printAppProperties(String prefix) {
        return configStorageCommon.printAppProperties(prefix);
    }

    @Override
    public String getAppProperty(String name) {
        return configStorageCommon.getAppProperty(name);
    }

    @Override
    public String setAppProperty(String name, String value) {
        return configStorageCommon.setAppProperty(name, value);
    }

    @Override
    public String getConfigValue(String classFQN, String methodName) {
        return configStorageCommon.getConfigValue(classFQN, methodName);
    }

}
