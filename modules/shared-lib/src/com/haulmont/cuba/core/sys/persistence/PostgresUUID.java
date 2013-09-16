/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import java.util.UUID;
import java.sql.SQLException;

public class PostgresUUID extends org.postgresql.util.PGobject
{
    private static final long serialVersionUID = -8115115840321643248L;

    public PostgresUUID(UUID uuid) throws SQLException {
        super();
        this.setType("uuid");
        this.setValue(uuid.toString());
    }
}
