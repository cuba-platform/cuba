/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.core.global.SilentException;
import com.haulmont.cuba.gui.WindowManager;
import org.springframework.core.Ordered;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;

/**
 * Handler that does nothing in respond to {@link SilentException}.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_SilentExceptionHandler")
public class SilentExceptionHandler extends AbstractGenericExceptionHandler implements Ordered {

    public SilentExceptionHandler() {
        super(SilentException.class.getName());
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        // do nothing
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 10;
    }
}
