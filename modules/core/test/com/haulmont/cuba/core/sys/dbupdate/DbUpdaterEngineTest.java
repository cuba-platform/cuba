/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.dbupdate;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.testsupport.TestContainer;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DbUpdaterEngineTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private File dbmsDir;
    private File dbmsWebInfDir;

    private List<File> mssqlInitFiles = new ArrayList<>();
    private List<File> mssql2012InitFiles = new ArrayList<>();
    private List<File> mssqlUpdateFiles = new ArrayList<>();
    private List<File> mssql2012UpdateFiles = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp() throws Exception {
        GlobalConfig config = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);
        dbmsDir = new File(config.getTempDir(), "db");
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
    public void testGetModules() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbScriptsDirectory = dbmsDir.getAbsolutePath();
        engine.dbmsType = "mssql";
        List<String> moduleDirs = engine.getModuleDirs();
        System.out.println(moduleDirs);
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
}