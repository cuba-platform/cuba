/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import com.haulmont.cuba.core.sys.persistence.PersistenceConfigProcessor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.persistence.annotations.TransientCompatibleAnnotations;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.Temporal;

/**
 * {@link AppContext} loader of the middleware web application.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AppContextLoader extends AbstractWebAppContextLoader {

    public static final String PERSISTENCE_CONFIG = "cuba.persistenceConfig";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void beforeInitAppContext() {
        super.beforeInitAppContext();

        log.info("DbmsType is set to " + DbmsType.getType() + DbmsType.getVersion());

        // Init persistence.xml
        String configProperty = AppContext.getProperty(PERSISTENCE_CONFIG);
        if (StringUtils.isBlank(configProperty)) {
            throw new IllegalStateException("Missing " + PERSISTENCE_CONFIG + " application property");
        }

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        PersistenceConfigProcessor processor = new PersistenceConfigProcessor();
        processor.setSourceFiles(tokenizer.getTokenList());

        String dataDir = AppContext.getProperty("cuba.dataDir");
        processor.setOutputFile(dataDir + "/persistence.xml");

        processor.create();
    }

    @Override
    protected ClassPathXmlApplicationContext createClassPathXmlApplicationContext(String[] locations) {
        return new CubaCoreApplicationContext(locations);
    }

    @Override
    protected void afterInitAppContext() {
        // Start cluster
        boolean isMaster = true;
        if (Boolean.valueOf(AppContext.getProperty("cuba.cluster.enabled"))) {
            ClusterManagerAPI clusterManager =
                    (ClusterManagerAPI) AppContext.getApplicationContext().getBean(ClusterManagerAPI.NAME);
            clusterManager.start();
            isMaster = clusterManager.isMaster();
        }
        // Init database
        if (isMaster && Boolean.valueOf(AppContext.getProperty("cuba.automaticDatabaseUpdate"))) {
            DbUpdater updater = (DbUpdater) AppContext.getApplicationContext().getBean(DbUpdater.NAME);
            updater.updateDatabase();
        }
    }
}
