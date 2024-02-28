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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.testsupport.TestContainer;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbUpdaterEngineTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private File dbmsDir;
    private File dbmsWebInfDir;

    private List<File> mssqlInitFiles = new ArrayList<>();
    private List<File> mssql2012InitFiles = new ArrayList<>();
    private List<File> mssqlInitAddStoreFiles = new ArrayList<>();
    private List<File> mssqlUpdateFiles = new ArrayList<>();
    private List<File> mssql2012UpdateFiles = new ArrayList<>();
    private List<File> mssqlUpdateAddStoreFiles = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeEach
    public void setUp() throws Exception {
        GlobalConfig config = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);
        dbmsDir = new File(config.getTempDir(), "db. dir");
        if (dbmsDir.exists()) {
            FileUtils.deleteDirectory(dbmsDir);
        }
        dbmsDir.mkdirs();

        dbmsWebInfDir = new File(config.getTempDir(), "WEB-INF/db");
        if (dbmsWebInfDir.exists()) {
            FileUtils.deleteDirectory(dbmsWebInfDir);
        }
        dbmsWebInfDir.mkdirs();

        File dir;
        File file;

        // Init scripts
        dir = new File(dbmsWebInfDir, "10-cuba/init/postgres");
        dir.mkdirs();
        file = new File(dir, "create-db.sql");
        file.createNewFile();


        dir = new File(dbmsDir, "10-cuba/init/mssql");
        dir.mkdirs();
        file = new File(dir, "create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);

        dir = new File(dbmsDir, "10-cuba/init/mssql-2012");
        dir.mkdirs();
        file = new File(dir, "create-db.sql");
        file.createNewFile();
        mssql2012InitFiles.add(file);

        dir = new File(dbmsDir, "10-cuba/init_addstore/mssql");
        dir.mkdirs();
        file = new File(dir, "create-db.sql");
        file.createNewFile();
        mssqlInitAddStoreFiles.add(file);

        dir = new File(dbmsDir, "50-app/init/mssql");
        dir.mkdirs();
        file = new File(dir, "10.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);
        file = new File(dir, "20.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);
        file = new File(dir, "30.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);

        dir = new File(dbmsDir, "50-app/init/mssql-2012");
        dir.mkdirs();
        file = new File(dir, "30.create-db.sql");
        file.createNewFile();
        mssql2012InitFiles.add(file);

        file = new File(dir, "40.create-db.sql");
        file.createNewFile();
        mssql2012InitFiles.add(file);

        dir = new File(dbmsDir, "50-app1/init/mssql");
        dir.mkdirs();
        file = new File(dir, "10.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);
        file = new File(dir, "20.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);
        file = new File(dir, "30.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);

        dir = new File(dbmsDir, "100-app2/init/mssql");
        dir.mkdirs();
        file = new File(dir, "10.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);
        file = new File(dir, "20.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);
        file = new File(dir, "30.create-db.sql");
        file.createNewFile();
        mssqlInitFiles.add(file);
        mssql2012InitFiles.add(file);

        // Update scripts

        dir = new File(dbmsDir, "10-cuba/update/mssql/13");
        dir.mkdirs();
        file = new File(dir, "cuba-update-1.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);
        mssql2012UpdateFiles.add(file);

        dir = new File(dbmsDir, "10-cuba/update/mssql/14");
        dir.mkdirs();
        file = new File(dir, "cuba-update-2.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);
        mssql2012UpdateFiles.add(file);

        dir = new File(dbmsDir, "50-app/update/mssql/14");
        dir.mkdirs();
        file = new File(dir, "app-update-0.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);
        mssql2012UpdateFiles.add(file);
        file = new File(dir, "app-update-1.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);

        dir = new File(dbmsDir, "50-app/update/mssql-2012/14");
        dir.mkdirs();
        file = new File(dir, "app-update-1.sql");
        file.createNewFile();
        mssql2012UpdateFiles.add(file);
        file = new File(dir, "app-update-2.sql");
        file.createNewFile();
        mssql2012UpdateFiles.add(file);

        dir = new File(dbmsDir, "50-app1/update/mssql/14");
        dir.mkdirs();
        file = new File(dir, "app1-update-0.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);
        mssql2012UpdateFiles.add(file);
        file = new File(dir, "app1-update-1.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);
        mssql2012UpdateFiles.add(file);

        dir = new File(dbmsDir, "100-app2/update/mssql/14");
        dir.mkdirs();
        file = new File(dir, "app2-update-0.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);
        mssql2012UpdateFiles.add(file);
        file = new File(dir, "app2-update-1.sql");
        file.createNewFile();
        mssqlUpdateFiles.add(file);
        mssql2012UpdateFiles.add(file);

        dir = new File(dbmsDir, "100-app2/update_addstore/mssql");
        dir.mkdirs();
        file = new File(dir, "app-update-1.sql");
        file.createNewFile();
        mssqlUpdateAddStoreFiles.add(file);
        file = new File(dir, "app-update-2.sql");
        file.createNewFile();
        mssqlUpdateAddStoreFiles.add(file);
    }

    @Test
    public void testGetInitScripts() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = dbmsDir.getAbsolutePath();
        engine.dbmsType = "mssql";

        List<ScriptResource> scripts = engine.getInitScripts();
        List<File> files = scripts.stream().map(sr -> new File(sr.getPath())).collect(Collectors.toList());
        assertEquals(mssqlInitFiles, files);

        engine.dbmsVersion = "2012";

        scripts = engine.getInitScripts();
        files = scripts.stream().map(sr -> new File(sr.getPath())).collect(Collectors.toList());
        assertEquals(mssql2012InitFiles, files);
    }

    @Test
    public void testGetInitScriptsForAdditionalStore() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = dbmsDir.getAbsolutePath();
        engine.dbmsType = "mssql";
        engine.storeName = "addStore";

        List<ScriptResource> scripts = engine.getInitScripts();
        List<File> files = scripts.stream().map(sr -> new File(sr.getPath())).collect(Collectors.toList());
        assertEquals(mssqlInitAddStoreFiles, files);
    }

    @Test
    public void testGetUpdateScripts() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = dbmsDir.getAbsolutePath();
        engine.dbmsType = "mssql";

        List<ScriptResource> scripts = engine.getUpdateScripts();
        List<File> files = scripts.stream().map(sr -> new File(sr.getPath())).collect(Collectors.toList());

        assertEquals(mssqlUpdateFiles, files);

        engine.dbmsVersion = "2012";

        scripts = engine.getUpdateScripts();
        files = scripts.stream().map(sr -> new File(sr.getPath())).collect(Collectors.toList());
        assertEquals(mssql2012UpdateFiles, files);
    }

    @Test
    public void testGetUpdateScriptsForAdditionalDatastore() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = dbmsDir.getAbsolutePath();
        engine.dbmsType = "mssql";
        engine.storeName = "addStore";

        List<ScriptResource> scripts = engine.getUpdateScripts();
        List<File> files = scripts.stream().map(sr -> new File(sr.getPath())).collect(Collectors.toList());

        assertEquals(mssqlUpdateAddStoreFiles, files);
    }

    @Test
    public void testGetModules() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = dbmsDir.getAbsolutePath();
        engine.dbmsType = "mssql";
        List<String> moduleDirs = engine.getModuleDirs();
        assertEquals("10-cuba",moduleDirs.get(0));
        assertEquals("50-app",moduleDirs.get(1));
    }

    @Test
    public void testGetScriptName() throws Exception {
        File script = new File(dbmsDir, "50-app/update/mssql-2012/14/app-update-0.sql");
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = dbmsDir.getAbsolutePath();
        String scriptName = engine.getScriptName(script.getAbsolutePath());
        assertEquals("50-app/update/mssql-2012/14/app-update-0.sql", scriptName);

        script = new File(dbmsWebInfDir, "10-cuba/init/postgres/create-db.sql");
        engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = "web-inf:db";
        scriptName = engine.getScriptName(script.getAbsolutePath());
        assertEquals("10-cuba/init/postgres/create-db.sql", scriptName);
    }

    @Test
    void deleteComments() {
        String sql = "first;^\n" +
                "second; -- comment^\n" +
                "   -- third;";

        DbUpdaterEngine engine = new DbUpdaterEngine();
        sql = engine.deleteComments(sql);
        assertEquals("first;^\nsecond;\n", sql);

        sql = "first;^\n" +
                "-- comment^\n" +
                "second;";
        sql = engine.deleteComments(sql);
        assertEquals("first;^\nsecond;\n", sql);
    }
}