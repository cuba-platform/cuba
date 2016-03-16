/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.security.global;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 */
public class IpMatcher {

    private static Set<String> whiteListedIPs = ImmutableSet.of("127.0.0.1", "0:0:0:0:0:0:0:1");
    private List<String[]> masks = new ArrayList<>();

    private Logger log = LoggerFactory.getLogger(IpMatcher.class);

    public IpMatcher(String source) {
        String[] parts = source.split("[,;]");
        for (String part : parts) {
            String m = part.trim();
            String[] mask = ipv4(m);
            if (mask == null) {
                mask = ipv6(m);
                if (mask == null) {
                    log.warn("Invalid IP mask: '" + m + "'");
                    continue;
                }
            }
            masks.add(mask);
        }
    }

    public boolean match(String ip) {
        if (StringUtils.isBlank(ip) || whiteListedIPs.contains(ip)) {
            return true;
        }
        if (masks.isEmpty()) {
            return true;
        }

        String[] ipv = ipv4(ip);
        if (ipv == null) {
            ipv = ipv6(ip);
            if (ipv == null) {
                log.warn("IP format not supported: '" + ip + "'");
                return true;
            }
        }

        for (String[] mask : masks) {
            if (match(mask, ipv))
                return true;
        }
        return false;
    }

    private boolean match(String[] mask, String[] ip) {
        if (mask.length != ip.length) {
            return false;
        }
        for (int j = 0; j < mask.length; j++) {
            String mp = mask[j];
            if (!mp.equals("*") && !ip[j].equals(mp)) {
                return false;
            }
        }
        return true;
    }

    private static String[] ipv4(String ip) {
        String[] ipp = ip.split("\\.");
        if (ipp.length != 4) {
            return null;
        }
        return ipp;
    }

    private static String[] ipv6(String ip) {
        String[] ipp = ip.split(":");
        if (ipp.length != 8) {
            return null;
        }
        return ipp;
    }
}
