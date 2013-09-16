/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.logging;

import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CubaLog4jConfigurator extends DOMConfigurator {

    @Override
    public void doConfigure(URL url, LoggerRepository repository) {
        Logger logger = Logger.getLogger(CubaLog4jConfigurator.class.getName());

        String path = url.getFile();
        for (String name : System.getProperties().stringPropertyNames()) {
            path = path.replace("${" + name + "}", System.getProperty(name)); // for backward compatibility
            path = path.replace("{" + name + "}", System.getProperty(name));
        }
        URL realUrl;
        try {
            realUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        logger.info("Configuring log4j from " + realUrl);
        super.doConfigure(realUrl, repository);
    }
}
