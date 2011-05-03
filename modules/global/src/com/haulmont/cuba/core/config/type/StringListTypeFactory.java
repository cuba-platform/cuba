/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.config.type;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
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
