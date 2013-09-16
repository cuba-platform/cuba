/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DbDialect;
import com.haulmont.cuba.core.global.Scripting;
import groovy.lang.Binding;
import groovy.lang.Closure;
import org.apache.commons.logging.LogFactory;

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

    protected PostUpdateScripts postUpdate;

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

    @Override
    protected void executeGroovyScript(File file) {
        Binding bind = new Binding();
        bind.setProperty("ds", getDataSource());
        bind.setProperty("log", LogFactory.getLog(file.getName()));
        bind.setProperty("postUpdate", postUpdate);

        scripting.runGroovyScript(getScriptName(file), bind);
    }

    @Override
    protected void doUpdate() {
        postUpdate = new PostUpdateScripts();

        super.doUpdate();

        if (!postUpdate.getUpdates().isEmpty()) {
            log.info(String.format("Execute '%s' post update actions", postUpdate.getUpdates().size()));

            for (Closure closure : postUpdate.getUpdates()) {
                closure.call();
            }
        }
    }
}