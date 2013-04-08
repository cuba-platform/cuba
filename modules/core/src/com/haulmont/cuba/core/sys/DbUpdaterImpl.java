/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.Scripting;
import groovy.lang.Binding;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(DbUpdater.NAME)
public class DbUpdaterImpl extends DbUpdaterEngine {

    @Inject
    protected Scripting scripting;

    @Inject
    protected Persistence persistence;

    public DbUpdaterImpl() {
        extensionHandlers.put("groovy", new FileHandler() {
            @Override
            public void run(File file) {
                executeGroovyScript(file);
            }
        });
    }

    @Inject
    public void setConfigProvider(Configuration configuration) {
        String dbDirName = configuration.getConfig(ServerConfig.class).getDbDir();
        if (dbDirName != null)
            this.dbDir = new File(dbDirName);
    }

    @Override
    public DataSource getDataSource() {
        return persistence.getDataSource();
    }

    @Override
    public DbDialect getDbDialect() {
        return persistence.getDbDialect();
    }

    protected void executeGroovyScript(File file) {
        Binding bind = new Binding();
        scripting.runGroovyScript(getScriptName(file), bind);
    }
}