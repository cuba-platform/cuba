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
package com.haulmont.cuba.core.sys.dbupdate;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.DbUpdater;
import com.haulmont.cuba.core.sys.PostUpdateScripts;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import groovy.lang.Binding;
import groovy.lang.Closure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(DbUpdater.NAME)
public class DbUpdaterImpl extends DbUpdaterEngine {

    @Inject
    protected Scripting scripting;

    @Inject
    protected Persistence persistence;

    protected PostUpdateScripts postUpdate;

    protected Map<Closure, ScriptResource> postUpdateScripts = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(DbUpdaterImpl.class);

    @Inject
    public void setConfigProvider(Configuration configuration) {
        String dbDirName = configuration.getConfig(ServerConfig.class).getDbDir();
        if (dbDirName != null)
            this.dbScriptsDirectory = dbDirName;

        dbmsType = DbmsType.getType();
        dbmsVersion = DbmsType.getVersion();
    }

    @Override
    public DataSource getDataSource() {
        return persistence.getDataSource();
    }

    @Override
    protected boolean executeGroovyScript(final ScriptResource file) {
        Binding bind = new Binding();
        bind.setProperty("ds", getDataSource());
        bind.setProperty("log", LoggerFactory.getLogger(file.getName()));
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
                ScriptResource groovyFile = postUpdateScripts.remove(closure);
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