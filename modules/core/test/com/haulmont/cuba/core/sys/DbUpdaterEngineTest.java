/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DbUpdaterEngineTest extends CubaTestCase {

    private File dbmsDir;

    private List<File> mssqlInitFiles = new ArrayList<>();
    private List<File> mssql2012InitFiles = new ArrayList<>();
    private List<File> mssqlUpdateFiles = new ArrayList<>();
    private List<File> mssql2012UpdateFiles = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setUp() throws Exception {
        super.setUp();
        GlobalConfig config = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);
        dbmsDir = new File(config.getTempDir(), "db");
        if (dbmsDir.exists()) {
            FileUtils.deleteDirectory(dbmsDir);
        }
        dbmsDir.mkdirs();

        File dir;
        File file;

        // Init scripts

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

    public void testGetInitScripts() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbDir = dbmsDir;
        engine.dbmsType = "mssql";

        List<File> scripts = engine.getInitScripts();
        assertEquals(mssqlInitFiles, scripts);

        engine.dbmsVersion = "2012";

        scripts = engine.getInitScripts();
        assertEquals(mssql2012InitFiles, scripts);
    }

    public void testGetUpdateScripts() throws Exception {
        DbUpdaterEngine engine = new DbUpdaterEngine();
        engine.dbDir = dbmsDir;
        engine.dbmsType = "mssql";

        List<File> scripts = engine.getUpdateScripts();
        assertEquals(mssqlUpdateFiles, scripts);

        engine.dbmsVersion = "2012";

        scripts = engine.getUpdateScripts();
        assertEquals(mssql2012UpdateFiles, scripts);
    }
}