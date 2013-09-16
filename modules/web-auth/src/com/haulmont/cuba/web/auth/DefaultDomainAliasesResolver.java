/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.core.global.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(DomainAliasesResolver.NAME)
public class DefaultDomainAliasesResolver implements DomainAliasesResolver {

    private Map<String, String> aliases = new HashMap<>();
    private Log log = LogFactory.getLog(DomainAliasesResolver.class);

    @Inject
    public DefaultDomainAliasesResolver(Configuration configuration) {
        WebAuthConfig webConfig = configuration.getConfig(WebAuthConfig.class);
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
        String alias_lc = alias.toLowerCase();
        if (aliases.containsKey(alias_lc)) {
            String domain = aliases.get(alias_lc);
            log.debug(String.format("Resolved domain \"%s\" from alias \"%s\"", domain, alias));
            return domain;
        } else
            return alias;
    }
}