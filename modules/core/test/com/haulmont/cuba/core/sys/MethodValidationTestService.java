/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

public interface MethodValidationTestService {

    @Validated
    void validateParam(@Size(min = 5) String param);

    @Validated
    @Size(min = 5)
    String validateResult(String param);
}
