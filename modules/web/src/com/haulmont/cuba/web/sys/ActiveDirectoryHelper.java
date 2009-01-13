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

import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.resource.Messages;
import com.haulmont.cuba.core.global.ConfigProvider;
import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;
import org.apache.commons.lang.StringUtils;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ActiveDirectoryHelper
{
    public static boolean useActiveDirectory() {
        WebConfig config = ConfigProvider.getConfig(WebConfig.class);
        return config.getUseActiveDirectory();
    }

    public static void authenticate(String login, String password) throws LoginException {
        int p = login.indexOf('\\');
        if (p <= 0)
            throw new LoginException(Messages.getString("activeDirectory.invalidName"), login);
        String domain = login.substring(0, p);
        String user = login.substring(p+1);
        String dcIp = getActiveDirectoryDomains().get(domain);
        if (StringUtils.isBlank(dcIp))
            throw new LoginException(Messages.getString("activeDirectory.unknownDomain"), domain);
        try {
            UniAddress dc = UniAddress.getByName(dcIp);
            NtlmPasswordAuthentication cred = new NtlmPasswordAuthentication(domain, user, password);
            SmbSession.logon(dc, cred);
        } catch (UnknownHostException e) {
            throw new LoginException(Messages.getString("activeDirectory.unknownHost"), dcIp);
        } catch (SmbAuthException e) {
            throw new LoginException(Messages.getString("activeDirectory.authenticationError"), e.getMessage());
        } catch (SmbException e) {
            throw new LoginException(Messages.getString("activeDirectory.unknownError"), e.getMessage());
        }
    }

    private static Map<String, String> getActiveDirectoryDomains() {
        WebConfig config = ConfigProvider.getConfig(WebConfig.class);
        String s = config.getActiveDirectoryDomainMap();
        Map<String, String> map = new HashMap<String, String>();
        if (!StringUtils.isBlank(s)) {
            String[] strings = s.split(",");
            for (String str : strings) {
                int p = str.indexOf(':');
                if (p > 0) {
                    map.put(str.substring(0, p), str.substring(p+1));
                }
            }
        }
        return map;
    }
}
