/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class IntegerListTypeFactory extends TypeFactory {

    private Log log = LogFactory.getLog(IntegerListTypeFactory.class);

    @Override
    public Object build(String string) {
        List<Integer> integerList = new ArrayList<Integer>();
        if (StringUtils.isNotEmpty(string)) {
            String[] elements = string.split(" ");
            for (String element : elements) {
                if (StringUtils.isNotEmpty(element)) {
                    try {
                        Integer value = Integer.parseInt(element);
                        integerList.add(value);
                    } catch (NumberFormatException e) {
                        log.debug("Invalid integer list property: " + string);
                        return Collections.emptyList();
                    }
                }
            }
        }
        return integerList;
    }
}
