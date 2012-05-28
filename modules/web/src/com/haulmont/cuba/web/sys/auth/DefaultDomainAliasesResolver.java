/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.auth;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.web.WebConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class DefaultDomainAliasesResolver implements DomainAliasesResolver {

    private Map<String, String> aliases = new HashMap<String, String>();
    private Log log = LogFactory.getLog(DomainAliasesResolver.class);

    public void init() {
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        String aliasesConfig = webConfig.getActiveDirectoryAliases();
        if (StringUtils.isNotBlank(aliasesConfig)) {
            String[] aliasesPairs = StringUtils.split(aliasesConfig, ';');
            for (String aliasDefinition : aliasesPairs) {
                // skip blank parts
                if (StringUtils.isNotBlank(aliasDefinition)) {
                    String[] aliasParts = StringUtils.split(aliasDefinition, '|');

                    if (aliasParts == null || aliasParts.length != 2 ||
                            StringUtils.isBlank(aliasParts[0]) || StringUtils.isBlank(aliasParts[1])) {
                        log.warn("Incorrect domain alias definition: \"" + String.valueOf(aliasDefinition) + "\"");
                    } else
                        aliases.put(aliasParts[0].toLowerCase(), aliasParts[1]);
                }
            }
        }
    }

    @Override
    public String getDomainName(String alias) {
        if (aliases.containsKey(alias.toLowerCase())) {
            String domain = aliases.get(alias);
            log.debug(String.format("Resolved domain \"%s\" from alias \"%s\"", domain, alias));
            return domain;
        } else
            return alias;
    }
}
