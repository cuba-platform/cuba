/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys;

import org.springframework.stereotype.Service;

@Service("cuba_MethodValidationTestService")
public class MethodValidationTestServiceBean implements MethodValidationTestService {
    @Override
    public void validateParam(String param) {

    }

    @Override
    public String validateResult(String param) {
        return param;
    }
}
