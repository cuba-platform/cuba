/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.config.type.TypeFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        Log log = LogFactory.getLog(getClass());
        DataService dataService = AppBeans.get(DataService.NAME);
        String defaultConstraintViolationPattern = dataService.getDbDialect().getUniqueConstraintViolationPattern();
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
