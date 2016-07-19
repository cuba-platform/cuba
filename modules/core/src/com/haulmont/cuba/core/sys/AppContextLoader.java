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

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import com.haulmont.cuba.core.sys.persistence.PersistenceConfigProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AppContext} loader of the middleware web application.
 *
 */
public class AppContextLoader extends AbstractWebAppContextLoader {

    public static final String PERSISTENCE_CONFIG = "cuba.persistenceConfig";

    private static Logger log = LoggerFactory.getLogger(AppContextLoader.class);

    public static void createPersistenceXml(String storeName) {
        String configPropertyName = AppContextLoader.PERSISTENCE_CONFIG;
        String fileName = "persistence.xml";
        if (!Stores.isMain(storeName)) {
            configPropertyName = configPropertyName + "_" + storeName;
            fileName = storeName + "-" + fileName;
        }

        String configProperty = AppContext.getProperty(configPropertyName);
        if (Strings.isNullOrEmpty(configProperty)) {
            log.debug("Property {} is not set, assuming {} is not a RdbmsStore", configPropertyName, storeName);
            return;
        }

        List<String> files = Splitter.on(AppProperties.SEPARATOR_PATTERN).omitEmptyStrings().trimResults()
                .splitToList(configProperty);
        if (!Stores.isMain(storeName) && !files.contains("base-persistence.xml")) {
            files = new ArrayList<>(files);
            files.add(0, "base-persistence.xml");
        }

        PersistenceConfigProcessor processor = new PersistenceConfigProcessor();
        processor.setStorageName(storeName);
        processor.setSourceFiles(files);

        String dataDir = AppContext.getProperty("cuba.dataDir");
        processor.setOutputFile(dataDir + "/" + fileName);

        processor.create();
    }

    @Override
    protected String getBlock() {
        return "core";
    }

    @Override
    protected void beforeInitAppContext() {
        super.beforeInitAppContext();

        log.info("DbmsType of the main database is set to " + DbmsType.getType() + DbmsType.getVersion());

        // Init persistence.xml
        Stores.getAll().forEach(AppContextLoader::createPersistenceXml);
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
