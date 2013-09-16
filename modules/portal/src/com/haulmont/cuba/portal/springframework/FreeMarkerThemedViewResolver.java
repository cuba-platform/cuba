/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.springframework;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.portal.config.PortalConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * @author artamonov
 * @version $Id$
 */
public class FreeMarkerThemedViewResolver extends FreeMarkerViewResolver {

    @Override
    protected String getPrefix() {
        PortalConfig config = ConfigProvider.getConfig(PortalConfig.class);

        String prefixOriginal = super.getPrefix();
        if (StringUtils.isNotBlank(config.getTheme()))
            prefixOriginal = config.getTheme() + "/" + prefixOriginal;
        return prefixOriginal;
    }
}
