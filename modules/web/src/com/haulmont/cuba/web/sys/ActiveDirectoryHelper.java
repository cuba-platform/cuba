/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.01.2009 13:16:42
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.WebConfig;
import jespa.ntlm.NtlmSecurityProvider;
import jespa.security.PasswordCredential;
import jespa.security.SecurityProviderException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ActiveDirectoryHelper
{
    private static class DomainInfo {
        private String bindStr;
        private String acctName;
        private String acctPassword;

        private DomainInfo(String bindStr, String acctName, String acctPassword) {
            this.acctName = acctName;
            this.acctPassword = acctPassword;
            this.bindStr = bindStr;
        }
    }

    private static volatile boolean initialized;

    private static Map<String, DomainInfo> domains = new HashMap<String, DomainInfo>();

    private static String defaultDomain;

    private static Log log = LogFactory.getLog(ActiveDirectoryHelper.class);

    public static boolean useActiveDirectory() {
        WebConfig config = ConfigProvider.getConfig(WebConfig.class);
        return config.getUseActiveDirectory();
    }

    private static void initDomains() {
        if (initialized)
            return;
        if (useActiveDirectory()) {
            WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);

            String domainsStr = webConfig.getActiveDirectoryDomains();
            if (!StringUtils.isBlank(domainsStr)) {
                String[] strings = domainsStr.split(";");
                for (int i = 0; i < strings.length; i++) {
                    String domain = strings[i];
                    domain = domain.trim();
                    if (!StringUtils.isBlank(domain)) {
                        String[] parts = domain.split("\\|");
                        if (parts.length != 4) {
                            log.error("Invalid ActiveDirectory domain definition: " + domain);
                            break;
                        } else {
                            domains.put(parts[0], new DomainInfo(parts[1], parts[2], parts[3]));
                            if (i == 0)
                                defaultDomain = parts[0];
                        }
                    }
                }
            }
        }
        initialized = true;
    }

    public static String getDefaultDomain() {
        initDomains();
        return defaultDomain != null ? defaultDomain : "";
    }

    public static String getBindStr() {
        return getBindStr(getDefaultDomain());
    }

    public static String getBindStr(String domain) {
        initDomains();
        DomainInfo domainInfo = domains.get(domain);
        return domainInfo != null ? domainInfo.bindStr : "";
    }

    public static String getAcctName() {
        return getAcctName(getDefaultDomain());
    }

    public static String getAcctName(String domain) {
        initDomains();
        DomainInfo domainInfo = domains.get(domain);
        return domainInfo != null ? domainInfo.acctName : "";
    }

    public static String getAcctPassword() {
        return getAcctPassword(getDefaultDomain());
    }

    public static String getAcctPassword(String domain) {
        initDomains();
        DomainInfo domainInfo = domains.get(domain);
        return domainInfo != null ? domainInfo.acctPassword : "";
    }

    public static void authenticate(String login, String password, Locale loc) throws LoginException {
        initDomains();
        int p = login.indexOf('\\');
        if (p <= 0)
            throw new LoginException(MessageProvider.getMessage(ActiveDirectoryHelper.class, "activeDirectory.invalidName", loc),
                    login);
        String domain = login.substring(0, p);
        String user = login.substring(p+1);

        DomainInfo domainInfo = domains.get(domain);
        if (domainInfo == null) {
            throw new LoginException(
                    MessageProvider.getMessage(ActiveDirectoryHelper.class, "activeDirectory.unknownDomain", loc),
                    domain
            );
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("bindstr", domainInfo.bindStr);
        params.put("service.acctname", domainInfo.acctName);
        params.put("service.password", domainInfo.acctPassword);
        params.put("account.canonicalForm", "3");
        fillFromSystemProperties(params);

        NtlmSecurityProvider provider = new NtlmSecurityProvider(params);
        try {
            PasswordCredential credential = new PasswordCredential(user, password.toCharArray());
            provider.authenticate(credential);
        } catch (SecurityProviderException e) {
            throw new LoginException(
                    MessageProvider.getMessage(ActiveDirectoryHelper.class, "activeDirectory.authenticationError", loc),
                    e.getMessage()
            );
        }
    }

    public static void fillFromSystemProperties(Map<String, String> params) {
        for (String name : AppContext.getPropertyNames()) {
            if (name.startsWith("jespa.")) {
                params.put(name, AppContext.getProperty(name));
            }
        }
    }
}
