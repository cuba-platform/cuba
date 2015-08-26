/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.config.type.TypeFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author zlatoverov
 * @version $Id$
 */
public class UniqueConstraintViolationPatternFactory extends TypeFactory {

    @Override
    public Object build(String value) {
        Messages messages = AppBeans.get(Messages.NAME);
        Logger log = LoggerFactory.getLogger(getClass());
        PersistenceManagerService pmService = AppBeans.get(PersistenceManagerService.NAME);
        String defaultConstraintViolationPattern = pmService.getUniqueConstraintViolationPattern();
        Pattern constraintViolationPattern;

        if (StringUtils.isBlank(value)) {
            constraintViolationPattern = Pattern.compile(defaultConstraintViolationPattern);
        } else {
            try {
                constraintViolationPattern = Pattern.compile(value);
            } catch (PatternSyntaxException e) {
                constraintViolationPattern = Pattern.compile(defaultConstraintViolationPattern);
                log.warn(String.format(messages.getMainMessage("incorrectRegexp"),
                        "cuba.uniqueConstraintViolationPattern"), e);
            }
        }

        return constraintViolationPattern;
    }
}
