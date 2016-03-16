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
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import com.haulmont.cuba.core.sys.persistence.PersistenceConfigProcessor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * {@link AppContext} loader of the middleware web application.
 *
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
