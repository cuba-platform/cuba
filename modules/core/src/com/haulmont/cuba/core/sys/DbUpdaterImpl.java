/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import groovy.lang.Binding;
import groovy.lang.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected Map<Closure, File> postUpdateScripts = new HashMap<>();

    private static final Log log = LogFactory.getLog(DbUpdaterImpl.class);

    @Inject
    public void setConfigProvider(Configuration configuration) {
        String dbDirName = configuration.getConfig(ServerConfig.class).getDbDir();
        if (dbDirName != null)
            this.dbDir = new File(dbDirName);

        dbmsType = DbmsType.getType();
        dbmsVersion = DbmsType.getVersion();
    }

    @Override
    public DataSource getDataSource() {
        return persistence.getDataSource();
    }

    @Override
    protected boolean executeGroovyScript(final File file) {
        Binding bind = new Binding();
        bind.setProperty("ds", getDataSource());
        bind.setProperty("log", LogFactory.getLog(file.getName()));
        bind.setProperty("postUpdate", new PostUpdateScripts() {
            @Override
            public void add(Closure closure) {
                postUpdateScripts.put(closure, file);

                postUpdate.add(closure);
            }

            @Override
            public List<Closure> getUpdates() {
                return postUpdate.getUpdates();
            }
        });

        scripting.runGroovyScript(getScriptName(file), bind);
        return !postUpdateScripts.containsValue(file);
    }

    @Override
    protected void doUpdate() {
        postUpdate = new PostUpdateScripts();

        try {
            super.doUpdate();
        } catch (RuntimeException e) {
            postUpdateScripts.clear();
            throw new RuntimeException(e);
        }

        if (!postUpdate.getUpdates().isEmpty()) {
            log.info(String.format("Execute '%s' post update actions", postUpdate.getUpdates().size()));

            for (Closure closure : postUpdate.getUpdates()) {
                File groovyFile = postUpdateScripts.remove(closure);
                if (groovyFile != null) {
                    log.info("Execute post update from " + getScriptName(groovyFile));
                }

                closure.call();

                if (groovyFile != null && !postUpdateScripts.containsValue(groovyFile)) {
                    log.info("All post update actions completed for " + getScriptName(groovyFile));

                    markScript(getScriptName(groovyFile), false);
                }
            }
        }
    }
}