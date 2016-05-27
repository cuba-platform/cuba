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

package com.haulmont.cuba.web.auth;

import com.haulmont.cuba.core.global.Configuration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Component(DomainAliasesResolver.NAME)
public class DefaultDomainAliasesResolver implements DomainAliasesResolver {

    private Map<String, String> aliases = new HashMap<>();
    private Logger log = LoggerFactory.getLogger(DomainAliasesResolver.class);

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
                    } else {
                        aliases.put(aliasParts[0].toLowerCase(), aliasParts[1]);
                    }
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
        } else {
            return alias;
        }
    }
}