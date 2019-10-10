/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.dbupdate;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ScriptExecutionPolicy;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.DbUpdater;
import com.haulmont.cuba.core.sys.PostUpdateScripts;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import groovy.lang.Binding;
import groovy.lang.Closure;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(DbUpdater.NAME)
@Scope("prototype")
public class ServerDbUpdater extends DbUpdaterEngine {
    @Inject
    protected Scripting scripting;

    @Inject
    protected Persistence persistence;

    @Inject
    protected ServerConfig serverConfig;

    protected PostUpdateScripts postUpdate;

    protected Map<Closure, ScriptResource> postUpdateScripts = new HashMap<>();

    public ServerDbUpdater(String storeName) {
        this.storeName = storeName;
    }

    @PostConstruct
    protected void init() {
        String dbDirName = serverConfig.getDbDir();
        if (dbDirName != null) {
            this.dbScriptsDirectory = dbDirName;
        }

        //dbms type is not null for RDBMS stores
        dbmsType = DbmsType.getTypeOrNull(storeName);
        dbmsVersion = DbmsType.getVersion(storeName);
    }

    @Override
    public DataSource getDataSource() {
        return dbmsType != null ? persistence.getDataSource(storeName) : null;
    }

    @Override
    protected void doUpdate() {
        postUpdate = new PostUpdateScripts();

        try {
            super.doUpdate();
        } catch (RuntimeException e) {
            postUpdateScripts.clear();
            throw e;
        }

        if (!postUpdate.getUpdates().isEmpty()) {
            log.info(String.format("Execute '%s' post update actions", postUpdate.getUpdates().size()));

            for (Closure closure : postUpdate.getUpdates()) {
                ScriptResource groovyFile = postUpdateScripts.remove(closure);
                if (groovyFile != null) {
                    log.info("Execute post update from {}", getScriptName(groovyFile));
                }

                closure.call();

                if (groovyFile != null && !postUpdateScripts.containsValue(groovyFile)) {
                    log.info("All post update actions completed for {}", getScriptName(groovyFile));

                    markScript(getScriptName(groovyFile), false);
                }
            }
        }
    }

    @Override
    protected boolean executeGroovyScript(final ScriptResource file) {
        Binding bind = new Binding();
        bind.setProperty("ds", getDataSource());
        bind.setProperty("log", LoggerFactory.getLogger(String.format("%s$%s", DbUpdaterEngine.class.getName(),
                StringUtils.removeEndIgnoreCase(file.getName(), ".groovy"))));
        if (!StringUtils.endsWithIgnoreCase(file.getName(), "." + UPGRADE_GROOVY_EXTENSION)) {
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
        }

        try {
            scripting.evaluateGroovy(file.getContent(), bind, ScriptExecutionPolicy.DO_NOT_USE_COMPILE_CACHE);
        } catch (Exception e) {
            throw new RuntimeException(ERROR + "Error executing Groovy script " + file.name + "\n" + e.getMessage(), e);
        }
        return !postUpdateScripts.containsValue(file);
    }
}
