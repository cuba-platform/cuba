/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class StringListTypeFactory extends TypeFactory{
    @Override
    public Object build(String string) {
        List<String> stringList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(string)){
            String[] elements = string.split("\\|");
            for (String element : elements) {
                if (StringUtils.isNotEmpty(element))
                    stringList.add(element);
            }
        }
        return stringList;
    }
}
