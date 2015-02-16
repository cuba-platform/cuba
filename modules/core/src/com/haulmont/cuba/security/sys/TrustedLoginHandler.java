/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * @author gorelov
 * @version $Id$
 */
@ManagedBean(TrustedLoginHandler.NAME)
public class TrustedLoginHandler {
    public static final String NAME = "cuba_TrustedLoginHandler";

    @Inject
    protected Configuration configuration;

    protected Pattern permittedIpMaskPattern;

    /**
     * @param address   ip-address
     * @return          true if address in trusted list
     */
    public boolean trustedAddress(String address) {
        if (permittedIpMaskPattern == null) {
            String permittedIpList = configuration.getConfig(ServerConfig.class).getTrustedClientPermittedIpList();
            permittedIpList = convertToRegex(permittedIpList);
            if (StringUtils.isEmpty(permittedIpList)) {
                permittedIpList = configuration.getConfig(ServerConfig.class).getTrustedClientPermittedIpMask();
            }
            permittedIpMaskPattern = Pattern.compile(permittedIpList);
        }
        return permittedIpMaskPattern.matcher(address).matches();
    }

    protected String convertToRegex(String ipList) {
        String regex = null;
        if (StringUtils.isNotEmpty(ipList)) {
            regex = ipList;
            regex = regex.replace(" ", "").replace(",", "|").replace("*", "([01]?\\d\\d?|2[0-4]\\d|25[0-5])").replace(".", "\\.");
        }
        return regex;
    }

    /**
     * @param password  password to check
     * @return          true if password is trusted
     */
    public boolean trustedPassword(String password) {
        String trustedClientPassword = configuration.getConfig(ServerConfig.class).getTrustedClientPassword();
        return (StringUtils.isNotBlank(trustedClientPassword) && trustedClientPassword.equals(password));
    }
}
