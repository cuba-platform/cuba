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

package com.haulmont.cuba.security.sys;

import com.haulmont.cuba.core.app.ServerConfig;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.regex.Pattern;

@Component(TrustedLoginHandler.NAME)
public class TrustedLoginHandler {
    public static final String NAME = "cuba_TrustedLoginHandler";

    @Inject
    protected ServerConfig serverConfig;

    protected Pattern permittedIpMaskPattern;

    @Inject
    public void setServerConfig(ServerConfig serverConfig) {
        String permittedIpList = serverConfig.getTrustedClientPermittedIpList();
        permittedIpList = convertToRegex(permittedIpList);
        if (StringUtils.isEmpty(permittedIpList)) {
            permittedIpList = "127\\.0\\.0\\.1";
        }
        permittedIpMaskPattern = Pattern.compile(permittedIpList);
    }

    /**
     * @param address   ip-address
     * @return          true if address in trusted list
     */
    public boolean checkAddress(String address) {
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
    public boolean checkPassword(String password) {
        String trustedClientPassword = serverConfig.getTrustedClientPassword();
        return (StringUtils.isNotBlank(trustedClientPassword) && trustedClientPassword.equals(password));
    }
}