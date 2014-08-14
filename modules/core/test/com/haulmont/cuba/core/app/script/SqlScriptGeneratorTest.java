/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.script;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import junit.framework.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class SqlScriptGeneratorTest extends CubaTestCase {
    public void testInsert() throws Exception {
        SqlScriptGenerator sqlScriptGenerator = AppBeans.getPrototype(SqlScriptGenerator.NAME, User.class);
        User entity = new User();
        entity.setId(UUID.fromString("bda126a4-da26-bc72-087c-079d9e25ce5c"));
        entity.setCreateTs(new Date(1));

        String script = sqlScriptGenerator.generateInsertScript(entity);
        System.out.println(script);

//        Assert.assertEquals("insert into SEC_USER \n" +
//                "(ID, CREATE_TS, CREATED_BY, VERSION, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, LOGIN, LOGIN_LC, PASSWORD, NAME, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POSITION_, EMAIL, LANGUAGE_, ACTIVE, CHANGE_PASSWORD_AT_LOGON, GROUP_ID, IP_MASK) \n" +
//                "values ('bda126a4-da26-bc72-087c-079d9e25ce5c', '1970-01-01 04:00:00', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, true, false, null, null);", script);

    }

    public void testUpdate() throws Exception {
        SqlScriptGenerator sqlScriptGenerator = AppBeans.getPrototype(SqlScriptGenerator.NAME, User.class);
        User entity = new User();
        entity.setId(UUID.fromString("bda126a4-da26-bc72-087c-079d9e25ce5c"));
        entity.setCreateTs(new Date(1));

        String script = sqlScriptGenerator.generateUpdateScript(entity);
        System.out.println(script);

//        Assert.assertEquals("update SEC_USER \n" +
//                "set CREATE_TS='1970-01-01 04:00:00', CREATED_BY=null, VERSION=null, UPDATE_TS=null, UPDATED_BY=null, DELETE_TS=null, DELETED_BY=null, LOGIN=null, LOGIN_LC=null, PASSWORD=null, NAME=null, FIRST_NAME=null, LAST_NAME=null, MIDDLE_NAME=null, POSITION_=null, EMAIL=null, LANGUAGE_=null, ACTIVE=true, CHANGE_PASSWORD_AT_LOGON=false, GROUP_ID=null, IP_MASK=null \n" +
//                "where id='bda126a4-da26-bc72-087c-079d9e25ce5c';", script);
    }

    public void testFail() throws Exception {
        try {
            SqlScriptGenerator sqlScriptGenerator = AppBeans.getPrototype(SqlScriptGenerator.NAME, UserSessionEntity.class);
            UserSessionEntity userSessionEntity = new UserSessionEntity();
            String script = sqlScriptGenerator.generateInsertScript(userSessionEntity);
        } catch (Exception e) {
            return;
        }

        Assert.fail("The test should be finished by exception");
    }
}
