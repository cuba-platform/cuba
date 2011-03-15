/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.03.11 14:40
 *
 * $Id$
 */
package com.haulmont.cuba.security.global;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class IpMatcher {

    private List<String> masks = new ArrayList<String>();

    private Log log = LogFactory.getLog(IpMatcher.class);

    public IpMatcher(String mask) {
        String[] strings = mask.split("[,;]");
        for (String string : strings) {
            masks.add(string.trim());
        }
    }

    public boolean match(String ip) {
        if (ip == null || ip.equals("") || ip.equals("127.0.0.1"))
            return true;

        for (String mask : masks) {
            if (matchMask(mask, ip))
                return true;
        }
        return false;
    }

    private boolean matchMask(String mask, String ip) {
        if (mask.equals(ip))
            return true;

        String[] maskParts = mask.split("\\.");
        if (maskParts.length != 4) {
            log.warn("Invalid IP mask: " + mask);
            return true;
        }

        String[] ipParts = ip.split("\\.");
        if (ipParts.length != 4) {
            log.warn("IP format not supported: " + ip);
            return true;
        }

        for (int i = 0; i < maskParts.length; i++) {
            String maskPart = maskParts[i];
            if (!maskPart.equals("*") && !maskPart.equals(ipParts[i])) {
                return false;
            }
        }
        return true;
    }
}
