/*
 * Copyright (c) 2008-2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Alexander Budarov
 * Created: 23.08.2010 17:36:49
 * $Id$
 */

package cuba.client.web.ui.jmxcontrol.util;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.reflect.Array;

public class AttributeHelper {
    public static Object convert(String type, String str) {
        if ("java.lang.String".equals(type)) {
            return str;
        }
        else if ("int".equals(type) || "java.lang.Integer".equals(type)) {
            return Integer.valueOf(str);
        }
        else if ("long".equals(type) || "java.lang.Long".equals(type)) {
            return Long.valueOf(str);
        }
        else if ("double".equals(type) || "java.lang.Double".equals(type)) {
            return Double.valueOf(str);
        }
        else if ("float".equals(type) || "java.lang.Float".equals(type)) {
            return Float.valueOf(str);
        }
        else if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
            return Boolean.valueOf(str);
        }
        else if ("javax.management.ObjectName".equals(type)) {
            try {
                return new ObjectName(str);
            }
            catch (MalformedObjectNameException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return null;
    }

    public static boolean isBoolean(String type) {
        return "boolean".equals(type) || "java.lang.Boolean".equals(type);
    }

    public static boolean isArray(String type) {
        return type != null && (type.startsWith("[L") || type.endsWith("[]"));
    }

    public static String convertToString(Object value) {
        if (value == null) {
            return null;
        }

        if (value.getClass().isArray()) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < Array.getLength(value); i++) {
                Object o = Array.get(value, i);
                b.append(String.valueOf(o)).append("\n");
            }
            return b.toString();
        }
        return value.toString();
    }
}
